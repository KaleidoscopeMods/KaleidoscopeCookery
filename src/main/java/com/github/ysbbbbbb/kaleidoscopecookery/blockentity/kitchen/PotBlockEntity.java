package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IPot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenShovelItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.OilPotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

import static com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock.HAS_OIL;
import static com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock.SHOW_OIL;
import static com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry.*;

public class PotBlockEntity extends BaseBlockEntity implements IPot {
    private static final int PUT_INGREDIENT_TIME = 60 * 20;
    private static final int TAKEOUT_TIME = 40 * 20;
    private static final int BURNT_TIME = 20 * 20;

    private static final String INPUTS = "Inputs";
    private static final String CARRIER = "Carrier";
    private static final String RESULT = "Result";
    private static final String STATUS = "Status";
    private static final String CURRENT_TICK = "CurrentTick";
    private static final String STIR_FRY_COUNT = "StirFryCount";
    private static final String SEED = "Seed";

    private NonNullList<ItemStack> inputs = NonNullList.withSize(PotRecipe.RECIPES_SIZE, ItemStack.EMPTY);
    private Ingredient carrier = Ingredient.EMPTY;
    private ItemStack result = ItemStack.EMPTY;
    private int status = PUT_INGREDIENT;
    private int currentTick = 0;
    private int stirFryCount = 0;

    /**
     * 用于渲染动画时数据
     */
    public long seed;
    public StirFryAnimationData animationData = new StirFryAnimationData();

    public PotBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.POT_BE.get(), pPos, pBlockState);
        this.seed = System.currentTimeMillis();
    }

    @Override
    public boolean hasHeatSource(Level level) {
        BlockState belowState = level.getBlockState(worldPosition.below());
        if (belowState.hasProperty(BlockStateProperties.LIT) && belowState.getValue(BlockStateProperties.LIT)) {
            return true;
        }
        return belowState.is(TagMod.HEAT_SOURCE_BLOCKS_WITHOUT_LIT);
    }

    public void tick(Level level) {
        if (!this.hasHeatSource(level)) {
            return;
        }
        RandomSource random = level.random;
        if (currentTick > 0) {
            this.currentTick--;
            // 每 5tick 刷新一次
            if (this.currentTick % 5 == 0) {
                this.refresh();
            }
            // 模拟油炸声音
            if (this.currentTick % 20 == 0) {
                level.playSound(null, this.worldPosition,
                        SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS,
                        0.5f + random.nextFloat() / 0.5f,
                        0.8f + random.nextFloat() / 0.5f);
            }
        }

        // 放素材阶段
        if (this.status == PUT_INGREDIENT) {
            tickPutIngredient(level, random);
            return;
        }
        // 炒菜阶段
        if (this.status == COOKING) {
            tickCooking(level, random);
            return;
        }
        // 出结果阶段
        if (this.status == FINISHED) {
            tickFinished(random);
            return;
        }
        // 炒糊
        if (this.status == BURNT) {
            tickBurnt(level, random);
        }
    }

    private void tickBurnt(Level level, RandomSource random) {
        int particleCount = 10 - this.currentTick / 5;
        // 依据过头情况，粒子数量逐渐增加
        if (this.currentTick % 2 == 0 && this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE,
                    worldPosition.getX() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    worldPosition.getY() + 0.25 + random.nextDouble() / 3,
                    worldPosition.getZ() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    particleCount, 0, 0, 0, 0.05);
        }
        if (currentTick == 0) {
            this.reset();
            level.playSound(null, worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1F,
                    (random.nextFloat() - random.nextFloat()) * 0.8F);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SMOKE,
                        worldPosition.getX() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                        worldPosition.getY() + 0.25 + random.nextDouble() / 3,
                        worldPosition.getZ() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                        8, 0, 0, 0, 0.05);
                int count = 1 + random.nextInt(3);
                Block.popResource(level, worldPosition, new ItemStack(Items.CHARCOAL, count));
            }
        }
    }

    private void tickFinished(RandomSource random) {
        if (this.currentTick % 10 == 0 && this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ModParticles.COOKING.get(),
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 0.1 + random.nextDouble() / 2,
                    worldPosition.getZ() + 0.5,
                    1, 0, 0, 0, 0);
        }
        if (this.currentTick == 0) {
            // 炒菜完成，进入出锅阶段
            this.status = BURNT;
            this.currentTick = BURNT_TIME;
            this.setChanged();
        }
    }

    private void tickCooking(Level level, RandomSource random) {
        if (this.currentTick == 0) {
            level.playSound(null, worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1F,
                    (random.nextFloat() - random.nextFloat()) * 0.8F);
            this.status = FINISHED;
            // 检查翻炒次数
            if (this.stirFryCount > 0) {
                this.result = getItem(SUSPICIOUS_STIR_FRY).getDefaultInstance();
                this.carrier = Ingredient.of(Items.BOWL);
            }
            this.currentTick = TAKEOUT_TIME;
            this.setChanged();
            BlockState state = level.getBlockState(worldPosition);
            level.setBlockAndUpdate(worldPosition, state.setValue(SHOW_OIL, false));
        }
    }

    private void tickPutIngredient(Level level, RandomSource random) {
        if (this.currentTick % 10 == 0 && level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ModParticles.COOKING.get(),
                    worldPosition.getX() + 0.5 + random.nextDouble() / 5 * (random.nextBoolean() ? 1 : -1),
                    worldPosition.getY() + 0.1 + random.nextDouble() / 3,
                    worldPosition.getZ() + 0.5 + random.nextDouble() / 5 * (random.nextBoolean() ? 1 : -1),
                    1, 0, 0, 0, 0);
        }
        // 有菜，且炒菜时间超过了
        if (currentTick == 0) {
            if (this.isEmpty()) {
                // 清空
                this.reset();
                level.playSound(null, worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1F,
                        (random.nextFloat() - random.nextFloat()) * 0.8F);
                if (this.level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ModParticles.COOKING.get(),
                            worldPosition.getX() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                            worldPosition.getY() + 0.1 + random.nextDouble() / 3,
                            worldPosition.getZ() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                            8, 0, 0, 0, 0.05);
                }
            } else {
                // 自动切炒菜阶段
                this.startCooking(level);
            }
        }
    }

    @Override
    public boolean onPlaceOil(Level level, LivingEntity user, ItemStack stack) {
        if (stack.is(TagMod.OIL)) {
            // 普通情况油脂
            placeOil(level, user, level.random);
            stack.shrink(1);
            ModTrigger.EVENT.trigger(user, ModEventTriggerType.PUT_OIL_IN_POT);
            return true;
        } else if (stack.is(ModItems.KITCHEN_SHOVEL.get()) && KitchenShovelItem.hasOil(stack)) {
            // 带油锅铲特判
            placeOil(level, user, level.random);
            KitchenShovelItem.setHasOil(stack, false);
            ModTrigger.EVENT.trigger(user, ModEventTriggerType.PUT_OIL_IN_POT);
            return true;
        } else if (stack.is(ModItems.OIL_POT.get()) && OilPotItem.hasOil(stack)) {
            // 油壶特判
            placeOil(level, user, level.random);
            OilPotItem.shrinkOilCount(stack);
            ModTrigger.EVENT.trigger(user, ModEventTriggerType.PUT_OIL_IN_POT);
            return true;
        }
        return false;
    }

    private void placeOil(Level level, LivingEntity user, RandomSource random) {
        this.currentTick = PUT_INGREDIENT_TIME;
        BlockState state = level.getBlockState(worldPosition);
        level.setBlockAndUpdate(worldPosition, state.setValue(HAS_OIL, true).setValue(SHOW_OIL, true));
        level.playSound(user, worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1F,
                (random.nextFloat() - random.nextFloat()) * 0.8F);
        for (int i = 0; i < 10; i++) {
            level.addParticle(ParticleTypes.SMOKE,
                    worldPosition.getX() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    worldPosition.getY() + 0.25 + random.nextDouble() / 3,
                    worldPosition.getZ() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    0, 0.05, 0);
        }
    }

    @Override
    public void onShovelHit(Level level, LivingEntity user, ItemStack shovel) {
        if (!level.isClientSide) {
            this.seed = System.currentTimeMillis();
            this.refresh();
        }

        // 每次翻炒给点粒子效果
        if (this.level instanceof ServerLevel serverLevel) {
            RandomSource random = serverLevel.random;
            serverLevel.sendParticles(ModParticles.COOKING.get(),
                    worldPosition.getX() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    worldPosition.getY() + 0.1 + random.nextDouble() / 3,
                    worldPosition.getZ() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    1, 0, 0, 0, 0.05);
        }

        // 起锅烧油，放入食材阶段
        if (this.status == PUT_INGREDIENT) {
            if (!this.isEmpty()) {
                this.startCooking(level);
                ModTrigger.EVENT.trigger(user, ModEventTriggerType.STIR_FRY_IN_POT);
            }
        }

        // 炒菜阶段
        if (this.status == COOKING) {
            if (this.stirFryCount > 0) {
                this.stirFryCount--;
                ModTrigger.EVENT.trigger(user, ModEventTriggerType.STIR_FRY_IN_POT);
            }
        }
    }

    private void startCooking(Level level) {
        SimpleContainer container = this.getContainer();
        level.getRecipeManager().getRecipeFor(ModRecipes.POT_RECIPE, container, level).ifPresentOrElse(recipe -> {
            // 如果合成表符合，那么进入炒菜阶段
            this.carrier = recipe.carrier();
            this.result = recipe.assemble(container, level.registryAccess());
            this.currentTick = recipe.time();
            this.stirFryCount = recipe.stirFryCount();
        }, () -> {
            // 不符合，进入迷之炒菜阶段
            this.carrier = Ingredient.of(Items.BOWL);
            this.result = getItem(SUSPICIOUS_STIR_FRY).getDefaultInstance();
            this.currentTick = 10 * 20; // 迷之炒菜时间
            this.stirFryCount = 0; // 迷之炒菜不计翻炒次数
        });
        this.status = COOKING;
        this.refresh();
    }

    @Override
    public boolean takeOutProduct(Level level, LivingEntity user, ItemStack stack) {
        // 仅在炒菜完成或炒糊阶段可以取出
        if (this.status != FINISHED && this.status != BURNT) {
            return false;
        }
        // 烧焦时取出的是黑暗料理
        ItemStack finallyResult = this.status == FINISHED ? this.result : getItem(DARK_CUISINE).getDefaultInstance();

        // 迷之炒菜盖饭特判逻辑
        if (finallyResult.is(getItem(SUSPICIOUS_STIR_FRY)) && stack.is(TagCommon.COOKED_RICE)) {
            stack.shrink(1);
            ItemUtils.getItemToLivingEntity(user, ModItems.SUSPICIOUS_STIR_FRY_RICE_BOWL.get().getDefaultInstance());
            this.reset();
            return true;
        }

        if (!this.carrier.isEmpty()) {
            return this.takeOutWithCarrier(level, user, stack, finallyResult);
        } else {
            return this.takeOutWithoutCarrier(level, user, stack, finallyResult);
        }
    }

    private boolean takeOutWithoutCarrier(Level level, LivingEntity user, ItemStack stack, ItemStack finallyResult) {
        if (stack.is(TagMod.KITCHEN_SHOVEL)) {
            // 如果是玩家，则需要判断是否潜行才能取出
            if (user instanceof Player player && !player.isSecondaryUseActive()) {
                return false;
            }
            ItemUtils.getItemToLivingEntity(user, finallyResult);
            this.reset();
            return true;
        } else {
            if (this.hasHeatSource(level)) {
                user.hurt(level.damageSources().inFire(), 1);
                ModTrigger.EVENT.trigger(user, ModEventTriggerType.HURT_WHEN_TAKEOUT_FROM_POT);
            }
            this.sendActionBarMessage(user, "need_kitchen_shovel");
            return false;
        }
    }

    private boolean takeOutWithCarrier(Level level, LivingEntity user, ItemStack mainHandItem, ItemStack finallyResult) {
        Component carrierName = carrier.getItems()[0].getHoverName();
        if (this.carrier.test(mainHandItem)) {
            if (mainHandItem.getCount() < finallyResult.getCount()) {
                this.sendActionBarMessage(user, "carrier_count_not_enough", finallyResult.getCount(), carrierName);
                return false;
            } else {
                mainHandItem.shrink(finallyResult.getCount());
                ItemUtils.getItemToLivingEntity(user, finallyResult);
                this.reset();
                return true;
            }
        }
        // 没有锅铲时才会触发提示和伤害
        if (!mainHandItem.is(TagMod.KITCHEN_SHOVEL)) {
            if (this.hasHeatSource(level)) {
                user.hurt(level.damageSources().inFire(), 1);
                ModTrigger.EVENT.trigger(user, ModEventTriggerType.HURT_WHEN_TAKEOUT_FROM_POT);
            }
            this.sendActionBarMessage(user, "need_carrier", carrierName);
        }
        return false;
    }

    private void sendActionBarMessage(LivingEntity user, String type, Object... args) {
        if (user instanceof ServerPlayer serverPlayer) {
            String key = "tip.kaleidoscope_cookery.pot." + type;
            MutableComponent message = Component.translatable(key, args);
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(message));
        }
    }

    public void addAllIngredients(List<ItemStack> ingredients, LivingEntity user) {
        if (this.level == null) {
            return;
        }
        if (this.status != PUT_INGREDIENT) {
            return;
        }
        for (int i = 0; i < Math.min(ingredients.size(), this.inputs.size()); i++) {
            ItemStack stack = ingredients.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            // 如果带有容器，此时返还容器
            Item containerItem = ItemUtils.getContainerItem(stack);
            if (containerItem != Items.AIR) {
                ItemUtils.getItemToLivingEntity(user, containerItem.getDefaultInstance());
            }
            this.inputs.set(i, stack.copyWithCount(1));
        }
        level.playSound(null, this.worldPosition, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 1.0F, 0.5F);
        this.refresh();
    }

    @Override
    public boolean addIngredient(Level level, LivingEntity user, ItemStack itemStack) {
        if (this.status != PUT_INGREDIENT) {
            return false;
        }
        // 只允许食物和特定 tag 的东西放入
        if (!itemStack.isEdible() && !itemStack.is(TagMod.POT_INGREDIENT)) {
            return false;
        }
        for (int i = 0; i < this.inputs.size(); i++) {
            ItemStack item = this.inputs.get(i);
            if (item.isEmpty()) {
                // 如果带有容器，此时返还容器
                Item containerItem = ItemUtils.getContainerItem(itemStack);
                if (containerItem != Items.AIR) {
                    ItemUtils.getItemToLivingEntity(user, containerItem.getDefaultInstance());
                }
                this.inputs.set(i, itemStack.split(1));
                level.playSound(null, this.worldPosition, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 1.0F, 0.5F);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeIngredient(Level level, LivingEntity user) {
        if (this.status != PUT_INGREDIENT) {
            return false;
        }
        for (int i = this.inputs.size() - 1; i >= 0; i--) {
            ItemStack stack = this.inputs.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            // 检查容器是否符合取出条件
            if (!containerIsMatch(user, stack)) {
                return false;
            }
            this.inputs.set(i, ItemStack.EMPTY);
            ItemUtils.getItemToLivingEntity(user, stack);
            if (this.hasHeatSource(level)) {
                user.hurt(level.damageSources().inFire(), 1);
                ModTrigger.EVENT.trigger(user, ModEventTriggerType.HURT_WHEN_TAKEOUT_FROM_POT);
            }
            return true;
        }
        return false;
    }

    private boolean containerIsMatch(LivingEntity user, ItemStack stack) {
        Item containerItem = ItemUtils.getContainerItem(stack);
        if (containerItem == Items.AIR) {
            return true;
        }
        if (user.getMainHandItem().is(containerItem)) {
            user.getMainHandItem().shrink(1);
            return true;
        }
        if (user instanceof ServerPlayer player) {
            player.sendSystemMessage(Component.translatable("tip.kaleidoscope_cookery.kitchen.remove_ingredient.need_container",
                    containerItem.getDefaultInstance().getHoverName()));
        }
        return false;
    }

    public void reset() {
        this.inputs.clear();
        this.carrier = Ingredient.EMPTY;
        this.result = ItemStack.EMPTY;
        this.status = PUT_INGREDIENT;
        this.currentTick = 0;
        this.stirFryCount = 0;
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.setBlockAndUpdate(worldPosition, state.setValue(HAS_OIL, false).setValue(SHOW_OIL, false));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(INPUTS, ContainerHelper.saveAllItems(new CompoundTag(), this.inputs));
        tag.putString(CARRIER, this.carrier.toJson().toString());
        tag.put(RESULT, this.result.serializeNBT());
        tag.putInt(STATUS, this.status);
        tag.putInt(CURRENT_TICK, this.currentTick);
        tag.putInt(STIR_FRY_COUNT, this.stirFryCount);
        tag.putLong(SEED, this.seed);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.inputs = NonNullList.withSize(PotRecipe.RECIPES_SIZE, ItemStack.EMPTY);
        if (tag.contains(INPUTS, Tag.TAG_COMPOUND)) {
            ContainerHelper.loadAllItems(tag.getCompound(INPUTS), this.inputs);
        }
        if (tag.contains(CARRIER, Tag.TAG_STRING)) {
            JsonElement element = JsonParser.parseString(tag.getString(CARRIER));
            this.carrier = Ingredient.fromJson(element);
        }
        if (tag.contains(RESULT, Tag.TAG_COMPOUND)) {
            this.result = ItemStack.of(tag.getCompound(RESULT));
        }
        this.status = tag.getInt(STATUS);
        this.currentTick = tag.getInt(CURRENT_TICK);
        this.stirFryCount = tag.getInt(STIR_FRY_COUNT);
        this.seed = tag.getLong(SEED);
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public SimpleContainer getContainer() {
        SimpleContainer container = new SimpleContainer(PotRecipe.RECIPES_SIZE);
        for (int i = 0; i < this.inputs.size(); i++) {
            ItemStack stack = this.inputs.get(i);
            if (!stack.isEmpty()) {
                container.setItem(i, stack);
            }
        }
        return container;
    }

    public boolean isEmpty() {
        for (ItemStack stack : this.inputs) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public boolean hasCarrier() {
        return !carrier.isEmpty();
    }

    public ItemStack getResult() {
        return result;
    }

    public long getSeed() {
        return seed;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public static class StirFryAnimationData {
        public long preSeed = -1L;
        public long timestamp = -1L;
        public float[] randomHeights = new float[]{};
    }
}
