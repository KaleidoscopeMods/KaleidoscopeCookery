package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IMillstone;
import com.github.ysbbbbbb.kaleidoscopecookery.api.event.MillstoneFinishEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.api.event.MillstoneTakeItemEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.datamap.MillstoneBindableData;
import com.github.ysbbbbbb.kaleidoscopecookery.datamap.resources.MillstoneBindableDataReloadListener;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler.MillstoneOutputHandler;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MillstoneBlockEntity extends BaseBlockEntity implements IMillstone {
    public static final int MAX_INPUT_COUNT = 8;
    private static final String ENTITY_ID_KEY = "EntityId";
    private static final String CACHE_ROT_KEY = "CacheRot";
    private static final String ROT_SPEED_TICK_KEY = "RotSpeedTick";
    private static final String LIFT_ANGLE_KEY = "LiftAngle";
    private static final String INPUT_ITEM_KEY = "InputItem";
    private static final String OUTPUT_ITEM_KEY = "OutputItem";
    private static final String CARRIER_INGREDIENT_KEY = "CarrierIngredient";
    private static final String PROGRESS_KEY = "Progress";

    private final RecipeManager.CachedCheck<Container, MillstoneRecipe> quickCheck = RecipeManager.createCheck(ModRecipes.MILLSTONE_RECIPE);

    private UUID entityId = Util.NIL_UUID;
    // 缓存的角度，避免动画突兀的跳动变化
    private float cacheRot = 0f;
    private float rotSpeedTick = 200f;
    private float liftAngle = 5f;
    private ItemStack input = ItemStack.EMPTY;
    private ItemStack output = ItemStack.EMPTY;
    private Ingredient carrier = Ingredient.EMPTY;
    private int progress = 0;

    private @Nullable Mob bindEntity;
    private Vec3 offset = Vec3.ZERO;
    private LazyOptional<MillstoneOutputHandler> outputHandler;

    public MillstoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.MILLSTONE_BE.get(), pos, state);
    }

    public float getRotation(Level level, float partialTick) {
        float degPerTick = 360f / Math.max(this.rotSpeedTick, 1);
        float gameTime = level.getGameTime() + partialTick;
        return (this.cacheRot + gameTime * degPerTick) % 360;
    }

    public void tick(Level level) {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (Util.NIL_UUID.equals(this.entityId)) {
            return;
        }

        // 每秒额外检查一次输出，强制触发 MillstoneFinishEvent 事件
        if (serverLevel.getGameTime() % 20 == 0 && this.input.isEmpty() && !this.output.isEmpty()) {
            MinecraftForge.EVENT_BUS.post(new MillstoneFinishEvent(this, this.bindEntity));
        }

        // 旋转一圈的时间 (ticks)
        float rot = this.getRotation(level, 0);
        Vec3 center = Vec3.atBottomCenterOf(this.getBlockPos());
        double maxDistanceSqr = 5 * 5;
        // 服务器端检查实体是否还存在
        if (bindEntity == null) {
            // 必须距离磨盘足够近才可以（5 格）
            if (serverLevel.getEntity(entityId) instanceof Mob mob
                && mob.isAlive() && mob.distanceToSqr(center) < maxDistanceSqr
                && this.canBindEntity(mob)) {
                this.bindEntity(mob);
            } else {
                this.entityId = Util.NIL_UUID;
                this.cacheRot = 0f;
                this.liftAngle = 0f;
                this.refresh();
                return;
            }
        } else if (!bindEntity.isAlive()
                   || bindEntity.distanceToSqr(center) >= maxDistanceSqr
                   || bindEntity.fallDistance > 0.5f
                   || bindEntity.isInWall()
                   || this.saddleEntityIsControlling(bindEntity)) {
            this.entityId = Util.NIL_UUID;
            this.bindEntity = null;
            this.cacheRot = rot;
            this.liftAngle = 0f;
            this.refresh();
            return;
        }

        // 如果实体存在，检查是否需要更新位置
        Vec3 pos = new Vec3(0, 0, 2)
                .add(this.offset)
                .yRot(rot * Mth.DEG_TO_RAD)
                .add(center);
        this.bindEntity.moveTo(pos.x, pos.y, pos.z, -rot - 90, 0);

        // 如果实体带有库存，那么可以尝试往磨盘里放物品
        if (this.bindEntity.tickCount % 10 == 0 && this.output.isEmpty() && this.input.isEmpty() && this.progress <= 0) {
            LazyOptional<IItemHandler> capability = this.bindEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
            capability.ifPresent(handler -> {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stackInSlot = handler.getStackInSlot(i);
                    if (stackInSlot.isEmpty()) {
                        continue;
                    }
                    ItemStack stack = handler.extractItem(i, MAX_INPUT_COUNT, true);
                    if (this.onPutItem(level, stack)) {
                        handler.extractItem(i, MAX_INPUT_COUNT, false);
                        return;
                    }
                }
            });
        }

        // 释放粒子效果
        if (serverLevel.getGameTime() % 5 == 2) {
            Item item = !this.output.isEmpty() ? this.output.getItem() : (!this.input.isEmpty() ? this.input.getItem() : Items.AIR);
            if (item != Items.AIR) {
                Vec3 particlePos = new Vec3(0, 1, 1)
                        .yRot(rot * Mth.DEG_TO_RAD)
                        .add(center);
                if (item instanceof BlockItem blockItem) {
                    BlockState block = blockItem.getBlock().defaultBlockState();
                    BlockParticleOption option = new BlockParticleOption(ParticleTypes.BLOCK, block);
                    serverLevel.sendParticles(option,
                            particlePos.x, particlePos.y, particlePos.z,
                            5, 0.1, 0.1, 0.1,
                            0.05);
                } else {
                    ItemParticleOption option = new ItemParticleOption(ParticleTypes.ITEM, item.getDefaultInstance());
                    serverLevel.sendParticles(option,
                            particlePos.x, particlePos.y, particlePos.z,
                            5, 0.1, 0.1, 0.1,
                            0.05);
                }
            }
        }

        // 播放音频
        if (serverLevel.getGameTime() % 25 == 0) {
            float pitch = level.random.nextFloat() * 0.2f + 0.9f;
            serverLevel.playSound(null, this.worldPosition,
                    ModSounds.BLOCK_MILLSTONE.get(), SoundSource.BLOCKS, 0.5f, pitch);
        }

        // 输出栏为空才能进行研磨
        if (this.progress > 0 && this.output.isEmpty()) {
            this.progress--;
            // 每 10 tick 保存一次
            if (this.progress % 10 == 0) {
                this.refresh();
            }
        }

        // 当进度为 0 时，检查输入输出
        if (this.progress <= 0 && !this.input.isEmpty() && this.output.isEmpty()) {
            SimpleContainer container = new SimpleContainer(this.input);
            this.quickCheck.getRecipeFor(container, level).ifPresentOrElse(recipe -> {
                this.output = recipe.assemble(container, level.registryAccess());
                // 依据输入数量决定输出数量
                this.output.setCount(this.output.getCount() * this.input.getCount());
                this.input = ItemStack.EMPTY;
                this.carrier = recipe.getCarrier();
                this.refresh();
            }, () -> {
                // 几乎不太可能，但是此时把输入转向输出
                this.output = this.input.copyAndClear();
                this.input = ItemStack.EMPTY;
                this.carrier = Ingredient.EMPTY;
                this.refresh();
            });

            // 触发完成事件，用于特殊情况判断（比如油壶自动化）
            MinecraftForge.EVENT_BUS.post(new MillstoneFinishEvent(this, this.bindEntity));
        }
    }

    @Override
    public boolean onPutItem(Level level, ItemStack putOnItem) {
        // 先清空输出槽才可以
        if (!this.output.isEmpty()) {
            return false;
        }
        // 正在工作中，不能放入
        if (this.progress > 0 && !this.input.isEmpty()) {
            return false;
        }
        SimpleContainer container = new SimpleContainer(putOnItem);
        return this.quickCheck.getRecipeFor(container, level).map(recipe -> {
            this.input = putOnItem.split(MAX_INPUT_COUNT);
            this.progress = Math.max(Math.round(this.rotSpeedTick), 1);
            this.refresh();
            level.playSound(null, this.worldPosition,
                    SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.8f,
                    level.random.nextFloat() * 0.2f + 0.9f);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean onTakeItem(LivingEntity user, ItemStack heldItem) {
        // 先尝试取出输出槽
        if (!this.output.isEmpty()) {
            // 事件系统处理特殊情况
            var event = new MillstoneTakeItemEvent(user, heldItem, this);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return event.isSuccess();
            }
            // 兼容容器是否正确
            if (!carrier.isEmpty() && !carrier.test(heldItem)) {
                Component carrierName = carrier.getItems()[0].getHoverName();
                this.sendActionBarMessage(user, "tip.kaleidoscope_cookery.pot.need_carrier", carrierName);
                return false;
            }
            int consumeCount = this.output.getCount();
            // 返回容器
            if (!carrier.isEmpty()) {
                // 依据容器数量消耗
                consumeCount = Math.min(consumeCount, heldItem.getCount());
                Item containerItem = ItemUtils.getContainerItem(heldItem.split(consumeCount));
                if (containerItem != Items.AIR) {
                    ItemUtils.getItemToLivingEntity(user, containerItem.getDefaultInstance());
                }
            }
            ItemUtils.getItemToLivingEntity(user, this.output.split(consumeCount));
            if (this.output.isEmpty()) {
                this.resetWhenTakeout();
            }
            return true;
        }
        // 如果没有输出，则尝试取出输入槽
        if (!this.input.isEmpty()) {
            ItemUtils.getItemToLivingEntity(user, this.input.copyAndClear());
            this.input = ItemStack.EMPTY;
            this.progress = 0;
            this.refresh();
            return true;
        }
        return false;
    }

    public void resetWhenTakeout() {
        this.output = ItemStack.EMPTY;
        this.carrier = Ingredient.EMPTY;
        this.progress = 0;
        this.refresh();
    }

    public boolean saddleEntityIsControlling(Mob mob) {
        if (!(mob instanceof Saddleable saddleable)) {
            return false;
        }
        // 骑乘的生物不能被绑定
        return saddleable.isSaddled() && mob.getControllingPassenger() != null;
    }

    public boolean canBindEntity(Mob mob) {
        if (!mob.getType().is(TagMod.MILLSTONE_BINDABLE)) {
            return false;
        }
        if (mob.getVehicle() != null) {
            // 骑乘的生物不能被绑定
            return false;
        }
        // 禁止童工！
        if (mob.isBaby()) {
            return false;
        }
        // 已经被骑乘的生物不能被绑定
        if (this.saddleEntityIsControlling(mob)) {
            return false;
        }
        // 如果是可驯服生物，必须已经被驯服才行
        if (mob instanceof AbstractHorse horse) {
            return horse.isTamed();
        }
        if (mob instanceof TamableAnimal tamableAnimal) {
            return tamableAnimal.isTame();
        }
        if (mob instanceof OwnableEntity ownable) {
            return ownable.getOwnerUUID() != null;
        }
        return true;
    }

    public void bindEntity(Mob mob) {
        if (this.level == null || this.level.isClientSide) {
            // 仅在服务器端绑定实体
            return;
        }
        if (!mob.isAlive()) {
            return;
        }
        this.entityId = mob.getUUID();
        this.bindEntity = mob;
        // 缓存角度纠正
        float rot = this.getRotation(this.level, 0);
        this.cacheRot = this.cacheRot - (rot - this.cacheRot);

        // 读取数据地图，获取抬升角度
        MillstoneBindableData data = MillstoneBindableDataReloadListener.INSTANCE.getOrDefault(mob.getType(), MillstoneBindableData.DEFAULT);
        this.rotSpeedTick = data.rotSpeedTick();
        this.liftAngle = data.liftAngle();
        this.offset = data.offset();
        this.refresh();
    }

    public void sendActionBarMessage(LivingEntity user, String key, Object... args) {
        if (user instanceof ServerPlayer serverPlayer) {
            MutableComponent message = Component.translatable(key, args);
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(message));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putUUID(ENTITY_ID_KEY, entityId);
        tag.putFloat(CACHE_ROT_KEY, cacheRot);
        tag.putFloat(ROT_SPEED_TICK_KEY, rotSpeedTick);
        tag.putFloat(LIFT_ANGLE_KEY, liftAngle);
        if (!input.isEmpty()) {
            tag.put(INPUT_ITEM_KEY, input.save(new CompoundTag()));
        } else {
            tag.put(INPUT_ITEM_KEY, new CompoundTag());
        }
        if (!output.isEmpty()) {
            tag.put(OUTPUT_ITEM_KEY, output.save(new CompoundTag()));
        } else {
            tag.put(OUTPUT_ITEM_KEY, new CompoundTag());
        }
        tag.putString(CARRIER_INGREDIENT_KEY, this.carrier.toJson().toString());
        tag.putInt(PROGRESS_KEY, this.progress);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.entityId = tag.getUUID(ENTITY_ID_KEY);
        this.cacheRot = tag.getFloat(CACHE_ROT_KEY);
        this.rotSpeedTick = tag.getFloat(ROT_SPEED_TICK_KEY);
        this.liftAngle = tag.getFloat(LIFT_ANGLE_KEY);
        this.input = tag.getCompound(INPUT_ITEM_KEY).isEmpty() ? ItemStack.EMPTY : ItemStack.of(tag.getCompound(INPUT_ITEM_KEY));
        this.output = tag.getCompound(OUTPUT_ITEM_KEY).isEmpty() ? ItemStack.EMPTY : ItemStack.of(tag.getCompound(OUTPUT_ITEM_KEY));
        if (tag.contains(CARRIER_INGREDIENT_KEY, Tag.TAG_STRING)) {
            JsonElement element = JsonParser.parseString(tag.getString(CARRIER_INGREDIENT_KEY));
            this.carrier = Ingredient.fromJson(element);
        } else {
            this.carrier = Ingredient.EMPTY;
        }
        this.progress = tag.getInt(PROGRESS_KEY);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition.offset(-3, 0, -3), worldPosition.offset(3, 2, 3));
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER && side == Direction.DOWN && !this.remove) {
            if (this.outputHandler == null) {
                this.outputHandler = LazyOptional.of(() -> new MillstoneOutputHandler(this));
            }
            return this.outputHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setBlockState(BlockState blockState) {
        super.setBlockState(blockState);
        if (this.outputHandler != null) {
            LazyOptional<?> oldHandler = this.outputHandler;
            this.outputHandler = null;
            oldHandler.invalidate();
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (outputHandler != null) {
            outputHandler.invalidate();
            outputHandler = null;
        }
    }


    public boolean hasEntity() {
        return !Util.NIL_UUID.equals(this.entityId);
    }

    public float getCacheRot() {
        return this.cacheRot;
    }

    public float getLiftAngle() {
        return this.liftAngle;
    }

    public ItemStack getInput() {
        return this.input;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public Ingredient getCarrier() {
        return this.carrier;
    }

    public float getProgressPercent() {
        float total = Math.max(this.rotSpeedTick, 1);
        return (total - this.progress) / total;
    }
}
