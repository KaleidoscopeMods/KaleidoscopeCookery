package com.github.ysbbbbbb.kaleidoscopecookery.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.SteamerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {
    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
            ),
            cancellable = true
    )
    private void onSpawnAtLocation(CallbackInfo ci) {
        FallingBlockEntity self = (FallingBlockEntity) (Object) this;
        BlockState blockState = self.getBlockState();
        // 如果是蒸笼
        if (blockState.is(ModBlocks.STEAMER.get())) {
            ci.cancel();
            // 换成自己的掉落物
            List<ItemStack> drops = dropAsItem(blockState, self.blockData);
            for (ItemStack drop : drops) {
                self.spawnAtLocation(drop);
            }
        }
    }

    public List<ItemStack> dropAsItem(BlockState blockState, @Nullable CompoundTag steamerTag) {
        NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);
        int[] cookingProgress = new int[8];
        int[] cookingTime = new int[8];
        if (steamerTag != null) {
            if (steamerTag.contains(SteamerBlockEntity.ITEMS_TAG, Tag.TAG_LIST)) {
                ContainerHelper.loadAllItems(steamerTag, items);
            }
            if (steamerTag.contains(SteamerBlockEntity.COOKING_PROGRESS_TAG, Tag.TAG_INT_ARRAY)) {
                cookingProgress = steamerTag.getIntArray(SteamerBlockEntity.COOKING_PROGRESS_TAG);
            }
            if (steamerTag.contains(SteamerBlockEntity.COOKING_TIME_TAG, Tag.TAG_INT_ARRAY)) {
                cookingTime = steamerTag.getIntArray(SteamerBlockEntity.COOKING_TIME_TAG);
            }
        }

        List<ItemStack> drops = Lists.newArrayList();
        // 先看看是单层还是双层
        boolean half = blockState.getValue(SteamerBlock.HALF);
        // 全为空？那么直接返回
        ItemStack first = ModItems.STEAMER.get().getDefaultInstance();
        if (items.stream().allMatch(ItemStack::isEmpty)) {
            drops.add(first);
            if (!half) {
                drops.add(ModItems.STEAMER.get().getDefaultInstance());
            }
            return drops;
        }

        // 只需要保存物品和进度即可
        CompoundTag tag1 = new CompoundTag();
        CompoundTag tag2 = new CompoundTag();
        SteamerBlockEntity.saveSplit(tag1, tag2, items, cookingProgress, cookingTime);

        BlockItem.setBlockEntityData(first, ModBlocks.STEAMER_BE.get(), tag1);
        drops.add(first);

        if (!half) {
            ItemStack second = ModItems.STEAMER.get().getDefaultInstance();
            BlockItem.setBlockEntityData(second, ModBlocks.STEAMER_BE.get(), tag2);
            drops.add(second);
        }
        return drops;
    }
}
