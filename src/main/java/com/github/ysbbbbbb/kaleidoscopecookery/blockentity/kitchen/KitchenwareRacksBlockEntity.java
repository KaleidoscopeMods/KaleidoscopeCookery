package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IKitchenwareRacks;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class KitchenwareRacksBlockEntity extends BaseBlockEntity implements IKitchenwareRacks {
    private static final String LEFT_ITEM = "LeftItem";
    private static final String RIGHT_ITEM = "RightItem";

    private ItemStack itemLeft = ItemStack.EMPTY;
    private ItemStack itemRight = ItemStack.EMPTY;

    public KitchenwareRacksBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.KITCHENWARE_RACKS_BE, pPos, pBlockState);
    }

    @Override
    public boolean onClick(LivingEntity user, ItemStack stack, boolean isLeft) {
        ItemStack stackInRacks = isLeft ? itemLeft : itemRight;
        // 取出物品
        if (stack.isEmpty() && !stackInRacks.isEmpty()) {
            ItemUtils.getItemToLivingEntity(user, stackInRacks);
            if (isLeft) {
                itemLeft = ItemStack.EMPTY;
            } else {
                itemRight = ItemStack.EMPTY;
            }
            user.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
            this.refresh();
            return true;
        }
        // 放入物品
        if (stack.isDamageableItem() && stackInRacks.isEmpty()) {
            if (isLeft) {
                itemLeft = stack.split(1);
            } else {
                itemRight = stack.split(1);
            }
            user.playSound(SoundEvents.ITEM_FRAME_ADD_ITEM, 1.0F, 1.0F);
            this.refresh();
            return true;
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(LEFT_ITEM, itemLeft.save(new CompoundTag()));
        tag.put(RIGHT_ITEM, itemRight.save(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.itemLeft = ItemStack.of(tag.getCompound(LEFT_ITEM));
        this.itemRight = ItemStack.of(tag.getCompound(RIGHT_ITEM));
    }

//    @Override
//    public AABB getRenderBoundingBox() {
//        return new AABB(this.worldPosition);
//    }

    @Override
    public ItemStack getItemLeft() {
        return itemLeft;
    }

    @Override
    public ItemStack getItemRight() {
        return itemRight;
    }

    public void setItemLeft(ItemStack itemLeft) {
        this.itemLeft = itemLeft;
    }

    public void setItemRight(ItemStack itemRight) {
        this.itemRight = itemRight;
    }
}
