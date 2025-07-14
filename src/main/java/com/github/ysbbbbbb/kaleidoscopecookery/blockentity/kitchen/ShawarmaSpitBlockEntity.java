package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ShawarmaSpitBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class ShawarmaSpitBlockEntity extends BlockEntity {
    private static final int MAX_ITEMS = 8;

    public static final String COOKING_ITEM = "CookingItem";
    public static final String COOKED_ITEM = "CookedItem";
    public static final String COOK_TIME = "CookTime";

    private final RecipeManager.CachedCheck<Container, CampfireCookingRecipe> quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
    public ItemStack cookingItem = ItemStack.EMPTY;
    public ItemStack cookedItem = ItemStack.EMPTY;
    public int cookTime;

    public ShawarmaSpitBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.SHAWARMA_SPIT_BE.get(), pPos, pBlockState);
    }

    public boolean onPutCookingItem(Level level, ItemStack itemStack) {
        // 先判断能否放入物品
        if (!this.cookingItem.isEmpty() || !this.cookedItem.isEmpty()) {
            return false;
        }
        // 尝试通过输入的物品寻找营火配方
        SimpleContainer container = new SimpleContainer(itemStack);
        return this.quickCheck.getRecipeFor(container, level).map(recipe -> {
            // 如果找到了配方，则设置正在烹饪的物品和烹饪时间
            this.cookingItem = itemStack.split(MAX_ITEMS);
            this.cookedItem = recipe.assemble(container, level.registryAccess());
            this.cookedItem.setCount(this.cookingItem.getCount());
            this.cookTime = recipe.getCookingTime();
            this.refresh();
            if (level instanceof ServerLevel) {
                level.playSound(null,
                        worldPosition.getX() + 0.5,
                        worldPosition.getY() + 0.5,
                        worldPosition.getZ() + 0.5,
                        SoundEvents.ITEM_FRAME_ADD_ITEM,
                        SoundSource.BLOCKS,
                        0.5F + level.random.nextFloat(),
                        level.random.nextFloat() * 0.7F + 0.6F);
            }
            return true;
        }).orElse(false);
    }

    public boolean onTakeCookedItem(Level level, LivingEntity entity) {
        ItemStack mainHandItem = entity.getMainHandItem();

        // 如果有烹饪完成的物品，则将其取出
        if (this.cookTime <= 0 && !this.cookedItem.isEmpty()) {
            giveItem(level, entity, mainHandItem, this.cookedItem.copy());
            return true;
        }

        // 如果没有烹饪完成，返回原材料并重置
        if (this.cookTime > 0 && !this.cookingItem.isEmpty()) {
            giveItem(level, entity, mainHandItem, this.cookingItem.copy());
            return true;
        }

        return false;
    }

    private void giveItem(Level level, LivingEntity entity, ItemStack mainHandItem, ItemStack copy) {
        this.cookingItem = ItemStack.EMPTY;
        this.cookedItem = ItemStack.EMPTY;
        this.cookTime = 0;
        this.refresh();

        if (mainHandItem.isEmpty()) {
            entity.setItemInHand(InteractionHand.MAIN_HAND, copy);
        } else {
            if (entity instanceof Player player) {
                ItemHandlerHelper.giveItemToPlayer(player, copy);
            } else {
                Block.popResource(level, this.worldPosition, copy);
            }
        }
        if (!mainHandItem.is(TagMod.KITCHEN_KNIFE) && this.getBlockState().getValue(ShawarmaSpitBlock.POWERED)) {
            entity.hurt(level.damageSources().inFire(), 1);
        }

        if (level instanceof ServerLevel) {
            level.playSound(null,
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 0.5,
                    worldPosition.getZ() + 0.5,
                    SoundEvents.ITEM_FRAME_REMOVE_ITEM,
                    SoundSource.BLOCKS,
                    0.5F + level.random.nextFloat(),
                    level.random.nextFloat() * 0.7F + 0.6F);
        }
    }

    public void tick() {
        if (cookingItem.isEmpty()) {
            if (!cookedItem.isEmpty()) {
                this.spawnParticles();
            }
            return;
        }
        this.spawnParticles();
        if (cookTime > 0) {
            cookTime--;
        } else {
            if (level instanceof ServerLevel) {
                level.playSound(null,
                        worldPosition.getX() + 0.5,
                        worldPosition.getY() + 0.5,
                        worldPosition.getZ() + 0.5,
                        SoundEvents.FIRE_EXTINGUISH,
                        SoundSource.BLOCKS,
                        0.5F + level.random.nextFloat(),
                        level.random.nextFloat() * 0.7F + 0.6F);
            }
            this.cookingItem = ItemStack.EMPTY;
            this.refresh();
        }
    }

    private void spawnParticles() {
        if (level instanceof ServerLevel serverLevel) {
            if (level.random.nextFloat() < 0.25f) {
                serverLevel.sendParticles(ModParticles.COOKING.get(),
                        worldPosition.getX() + 0.5,
                        worldPosition.getY() + 0.5,
                        worldPosition.getZ() + 0.5,
                        1,
                        0.25, 0.2, 0.25,
                        0.1f);
            }
            if (level.random.nextInt(20) == 0) {
                serverLevel.playSound(null,
                        worldPosition.getX() + 0.5,
                        worldPosition.getY() + 0.5,
                        worldPosition.getZ() + 0.5,
                        SoundEvents.CAMPFIRE_CRACKLE,
                        SoundSource.BLOCKS,
                        0.5F + level.random.nextFloat(),
                        level.random.nextFloat() * 0.7F + 0.6F);
            }
        }
    }

    public void refresh() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(COOKING_ITEM, this.cookingItem.save(new CompoundTag()));
        tag.put(COOKED_ITEM, this.cookedItem.save(new CompoundTag()));
        tag.putInt(COOK_TIME, this.cookTime);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(COOKING_ITEM)) {
            this.cookingItem = ItemStack.of(tag.getCompound(COOKING_ITEM));
        }
        if (tag.contains(COOKED_ITEM)) {
            this.cookedItem = ItemStack.of(tag.getCompound(COOKED_ITEM));
        }
        this.cookTime = tag.getInt(COOK_TIME);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
