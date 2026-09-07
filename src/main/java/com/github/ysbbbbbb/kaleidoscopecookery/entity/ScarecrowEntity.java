package com.github.ysbbbbbb.kaleidoscopecookery.entity;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class ScarecrowEntity extends LivingEntity {
    public static final EntityType<ScarecrowEntity> TYPE = EntityType.Builder
            .<ScarecrowEntity>of(ScarecrowEntity::new, MobCategory.MISC)
            .sized(0.5F, 2.375f)
            .clientTrackingRange(10)
            .build("scarecrow");

    private static final EntityDataAccessor<CompoundTag> DATA_SHOULDER = SynchedEntityData.defineId(ScarecrowEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final Predicate<Entity> RIDABLE_MINECARTS = e -> e instanceof AbstractMinecart minecart && minecart.canBeRidden();
    private static final Predicate<Entity> SHOULDER_RIDING_ENTITY = e -> e instanceof ShoulderRidingEntity entity && !entity.isOrderedToSit() && entity.canSitOnShoulder();
    private static final String HAND_ITEMS_TAG = "HandItems";
    private static final String ARMOR_ITEMS_TAG = "ArmorItems";
    private static final String SHOULDER_ENTITY_TAG = "ShoulderEntity";

    private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);
    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);

    public long lastHit;
    private int cooldown;
    private long timeEntitySatOnShoulder;

    public ScarecrowEntity(EntityType<ScarecrowEntity> type, Level level) {
        super(type, level);
        this.setMaxUpStep(0);
    }

    public ScarecrowEntity(Level level, double pX, double pY, double pZ) {
        this(TYPE, level);
        this.setPos(pX, pY, pZ);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SHOULDER, new CompoundTag());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.cooldown > 0) {
            this.cooldown--;
        }
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec3, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.is(Items.NAME_TAG)) {
            return InteractionResult.PASS;
        }
        if (player.isSpectator()) {
            return InteractionResult.SUCCESS;
        }
        if (player.level().isClientSide) {
            return InteractionResult.CONSUME;
        }
        if (hand == InteractionHand.OFF_HAND) {
            return InteractionResult.PASS;
        }
        if (this.cooldown > 0) {
            return InteractionResult.PASS;
        }
        if (isClickHand(vec3)) {
            return handleHandItems(player, itemInHand);
        }
        if (isClickHead(vec3)) {
            return handleHeadItems(player, itemInHand);
        }
        return InteractionResult.PASS;
    }

    private InteractionResult handleHeadItems(Player player, ItemStack itemInHand) {
        this.cooldown = 5;
        ItemStack headItem = this.getItemBySlot(EquipmentSlot.HEAD);
        if (itemInHand.isEmpty() && !headItem.isEmpty()) {
            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
            ItemHandlerHelper.giveItemToPlayer(player, headItem);
            return InteractionResult.SUCCESS;
        }

        if (!(itemInHand.getItem() instanceof BlockItem blockItem) || !(blockItem.getBlock() instanceof SkullBlock)) {
            return InteractionResult.PASS;
        }

        if (player.getAbilities().instabuild && headItem.isEmpty()) {
            this.setItemSlot(EquipmentSlot.HEAD, itemInHand.copyWithCount(1));
            this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_FRAME_ADD_ITEM, this.getSoundSource());
            ModTrigger.EVENT.trigger(player, ModEventTriggerType.PLACE_HEAD_ON_SCARECROW);
            return InteractionResult.SUCCESS;
        }
        if (!itemInHand.isEmpty() && itemInHand.getCount() > 1) {
            if (headItem.isEmpty()) {
                this.setItemSlot(EquipmentSlot.HEAD, itemInHand.split(1));
                this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_FRAME_ADD_ITEM, this.getSoundSource());
                ModTrigger.EVENT.trigger(player, ModEventTriggerType.PLACE_HEAD_ON_SCARECROW);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        this.setItemSlot(EquipmentSlot.HEAD, itemInHand);
        this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_FRAME_ADD_ITEM, this.getSoundSource());
        player.setItemInHand(InteractionHand.MAIN_HAND, headItem);
        ModTrigger.EVENT.trigger(player, ModEventTriggerType.PLACE_HEAD_ON_SCARECROW);
        return InteractionResult.SUCCESS;
    }

    private InteractionResult handleHandItems(Player player, ItemStack itemInHand) {
        this.cooldown = 5;
        if (itemInHand.isEmpty()) {
            ItemStack mainhand = this.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack offhand = this.getItemInHand(InteractionHand.OFF_HAND);
            if (!mainhand.isEmpty()) {
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                ItemHandlerHelper.giveItemToPlayer(player, mainhand);
                return InteractionResult.SUCCESS;
            }
            if (!offhand.isEmpty()) {
                this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                ItemHandlerHelper.giveItemToPlayer(player, offhand);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        if (itemInHand.getItem().canBeDepleted()) {
            if (swapHand(InteractionHand.MAIN_HAND, player, itemInHand)) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_FRAME_ADD_ITEM, this.getSoundSource());
                return InteractionResult.SUCCESS;
            }
        }
        if (itemInHand.getItem().canBeDepleted()) {
            if (swapHand(InteractionHand.OFF_HAND, player, itemInHand)) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_FRAME_ADD_ITEM, this.getSoundSource());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private boolean swapHand(InteractionHand hand, Player player, ItemStack itemInHand) {
        ItemStack scarecrowStack = this.getItemInHand(hand);
        if (!scarecrowStack.isEmpty()) {
            return false;
        }
        if (!itemInHand.isEmpty()) {
            if (player.getAbilities().instabuild) {
                this.setItemInHand(hand, itemInHand.copyWithCount(1));
            } else {
                this.setItemInHand(hand, itemInHand.split(1));
            }
            return true;
        }
        return false;
    }

    private boolean isClickHand(Vec3 vector) {
        return 17 / 16.0 <= vector.y && vector.y <= 27 / 17.0;
    }

    private boolean isClickHead(Vec3 vector) {
        return 27 / 17.0 < vector.y;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide || this.isRemoved()) {
            return false;
        }

        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            this.kill();
            return false;
        }

        if (this.isInvulnerableTo(source)) {
            return false;
        }

        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            this.brokenByAnything(source);
            this.kill();
            return false;
        }

        Entity entity = source.getEntity();
        if (entity instanceof Player player) {
            if (!player.getAbilities().mayBuild) {
                return false;
            }
        }

        if (source.isCreativePlayer()) {
            this.playBrokenSound();
            this.showBreakingParticles();
            this.kill();
            return false;
        }

        long gameTime = this.level().getGameTime();
        if (gameTime - this.lastHit > 5) {
            this.level().playSound(null, this.blockPosition(), SoundEvents.ARMOR_STAND_HIT, this.getSoundSource(), 0.3F, 1.0F);
            this.level().broadcastEntityEvent(this, (byte) 32);
            this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
            this.lastHit = gameTime;
            if (!this.getShoulderEntity().isEmpty()) {
                this.removeEntitiesOnShoulder();
            }
        } else {
            this.brokenByPlayer(source);
            this.showBreakingParticles();
            this.kill();
        }

        return true;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 32) {
            if (this.level().isClientSide) {
                this.lastHit = this.level().getGameTime();
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void brokenByPlayer(DamageSource source) {
        ItemStack stack = new ItemStack(ModItems.SCARECROW.get());
        if (this.hasCustomName()) {
            stack.setHoverName(this.getCustomName());
        }
        Block.popResource(this.level(), this.blockPosition(), stack);
        this.brokenByAnything(source);
    }

    private void brokenByAnything(DamageSource source) {
        this.playBrokenSound();
        this.dropAllDeathLoot(source);
        for (int i = 0; i < this.handItems.size(); ++i) {
            ItemStack stack = this.handItems.get(i);
            if (!stack.isEmpty()) {
                Block.popResource(this.level(), this.blockPosition().above(), stack);
                this.handItems.set(i, ItemStack.EMPTY);
            }
        }

        for (int i = 0; i < this.armorItems.size(); ++i) {
            ItemStack stack = this.armorItems.get(i);
            if (!stack.isEmpty()) {
                Block.popResource(this.level(), this.blockPosition().above(), stack);
                this.armorItems.set(i, ItemStack.EMPTY);
            }
        }
    }

    private void playBrokenSound() {
        this.level().playSound(null, this.blockPosition(), SoundEvents.ARMOR_STAND_BREAK, this.getSoundSource(), 1.0F, 1.0F);
    }

    private void showBreakingParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            BlockParticleOption particleOption = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.defaultBlockState());
            serverLevel.sendParticles(particleOption,
                    this.getX(), this.getY(2 / 3.0),
                    this.getZ(), 10,
                    this.getBbWidth() / 4f,
                    this.getBbHeight() / 4f,
                    this.getBbWidth() / 4f,
                    0.05);
        }
    }

    private boolean setEntityOnShoulder(CompoundTag tag) {
        if (this.canEntityOnShoulder()) {
            this.setShoulderEntity(tag);
            this.timeEntitySatOnShoulder = this.level().getGameTime();
            return true;
        }
        return false;
    }

    private void removeEntitiesOnShoulder() {
        if (this.timeEntitySatOnShoulder + 20 < this.level().getGameTime()) {
            this.respawnEntityOnShoulder(this.getShoulderEntity());
            this.setShoulderEntity(new CompoundTag());
        }
    }

    private void respawnEntityOnShoulder(CompoundTag tag) {
        if (this.level() instanceof ServerLevel serverLevel && !tag.isEmpty()) {
            EntityType.create(tag, this.level()).ifPresent(entity -> {
                entity.setPos(this.getX(), this.getY() + 1.675, this.getZ());
                serverLevel.addWithUUID(entity);
            });
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.put(HAND_ITEMS_TAG, (new ItemStackHandler(this.handItems)).serializeNBT());
        tag.put(ARMOR_ITEMS_TAG, (new ItemStackHandler(this.armorItems)).serializeNBT());
        if (!this.getShoulderEntity().isEmpty()) {
            tag.put(SHOULDER_ENTITY_TAG, this.getShoulderEntity());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(HAND_ITEMS_TAG)) {
            ItemStackHandler handler = new ItemStackHandler();
            handler.deserializeNBT(tag.getCompound(HAND_ITEMS_TAG));
            for (int i = 0; i < this.handItems.size(); ++i) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    this.handItems.set(i, stack);
                }
            }
        }
        if (tag.contains(ARMOR_ITEMS_TAG)) {
            ItemStackHandler handler = new ItemStackHandler();
            handler.deserializeNBT(tag.getCompound(ARMOR_ITEMS_TAG));
            for (int i = 0; i < this.armorItems.size(); ++i) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    this.armorItems.set(i, stack);
                }
            }
        }

        if (tag.contains(SHOULDER_ENTITY_TAG, Tag.TAG_COMPOUND)) {
            this.setShoulderEntity(tag.getCompound(SHOULDER_ENTITY_TAG));
        }
    }

    @Override
    protected float tickHeadTurn(float yRot, float animStep) {
        this.yBodyRotO = this.yRotO;
        this.yBodyRot = this.getYRot();
        return 0.0F;
    }

    @Override
    public void setYBodyRot(float offset) {
        this.yBodyRotO = this.yRotO = offset;
        this.yHeadRotO = this.yHeadRot = offset;
    }

    @Override
    public void setYHeadRot(float rotation) {
        this.yBodyRotO = this.yRotO = rotation;
        this.yHeadRotO = this.yHeadRot = rotation;
    }

    @Override
    public void kill() {
        if (!this.getShoulderEntity().isEmpty()) {
            this.removeEntitiesOnShoulder();
        }
        this.remove(Entity.RemovalReason.KILLED);
        this.gameEvent(GameEvent.ENTITY_DIE);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    protected void pushEntities() {
        List<Entity> list = this.level().getEntities(this, this.getBoundingBox(), RIDABLE_MINECARTS);
        for (Entity entity : list) {
            if (this.distanceToSqr(entity) <= 0.2) {
                entity.push(this);
                return;
            }
        }

        if (this.canEntityOnShoulder()) {
            list = this.level().getEntities(this, this.getBoundingBox().inflate(2), SHOULDER_RIDING_ENTITY);
            for (Entity entity : list) {
                if (this.distanceToSqr(entity) <= 1.5 && entity instanceof ShoulderRidingEntity shoulderEntity
                    && setEntityOnShoulder(shoulderEntity)) {
                    return;
                }
            }
        }
    }

    private boolean setEntityOnShoulder(ShoulderRidingEntity entity) {
        CompoundTag tag = new CompoundTag();
        String id = entity.getEncodeId();
        if (id == null) {
            return false;
        }
        tag.putString("id", id);
        entity.saveWithoutId(tag);
        if (this.setEntityOnShoulder(tag)) {
            entity.discard();
            return true;
        }
        return false;
    }

    private boolean canEntityOnShoulder() {
        return !this.isPassenger() && this.onGround() && !this.isInWater() && !this.isInPowderSnow && this.getShoulderEntity().isEmpty();
    }

    @Override
    public Iterable<ItemStack> getHandSlots() {
        return this.handItems;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return this.armorItems;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return switch (slot.getType()) {
            case HAND -> this.handItems.get(slot.getIndex());
            case ARMOR -> this.armorItems.get(slot.getIndex());
        };
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        this.verifyEquippedItem(stack);
        switch (slot.getType()) {
            case HAND:
                this.onEquipItem(slot, this.handItems.set(slot.getIndex(), stack), stack);
                break;
            case ARMOR:
                this.onEquipItem(slot, this.armorItems.set(slot.getIndex(), stack), stack);
        }
    }

    @Override
    public boolean skipAttackInteraction(Entity entity) {
        return entity instanceof Player player && !this.level().mayInteract(player, this.blockPosition());
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public LivingEntity.Fallsounds getFallSounds() {
        return new LivingEntity.Fallsounds(SoundEvents.ARMOR_STAND_FALL, SoundEvents.ARMOR_STAND_FALL);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ARMOR_STAND_HIT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ARMOR_STAND_BREAK;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean attackable() {
        return false;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.SCARECROW.get());
    }

    public CompoundTag getShoulderEntity() {
        return this.entityData.get(DATA_SHOULDER);
    }

    public void setShoulderEntity(CompoundTag tag) {
        this.entityData.set(DATA_SHOULDER, tag);
    }
}
