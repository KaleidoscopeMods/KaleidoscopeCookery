package com.github.ysbbbbbb.kaleidoscopecookery.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(
            method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void onGetDrops(BlockState pState, ServerLevel pLevel, BlockPos pPos, BlockEntity pBlockEntity,
                                   Entity pEntity, ItemStack pTool, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (pEntity instanceof LivingEntity living && living.hasEffect(ModEffects.INSTANT_SMELTING.get())) {
            List<ItemStack> original = new ArrayList<>(cir.getReturnValue());

            // 构建熔炼结果缓存
            List<ItemStack> smeltedList = original.stream().map(stack -> pLevel.getRecipeManager().getRecipeFor(
                    RecipeType.SMELTING,
                    new SimpleContainer(stack),
                    pLevel
            ).map(recipe -> recipe.getResultItem(pLevel.registryAccess()).copy()).orElse(ItemStack.EMPTY)).toList();

            // 构建索引列表并打乱
            List<Integer> indexList = new ArrayList<>();
            for (int i = 0; i < original.size(); i++) {
                for (int j = 0; j < original.get(i).getCount(); j++) {
                    indexList.add(i);
                }
            }
            Collections.shuffle(indexList);

            // 可熔炼数量为 效果等级+2
            int count = Objects.requireNonNull(living.getEffect(ModEffects.INSTANT_SMELTING.get())).getAmplifier() + 2;
            Map<Integer, Integer> resultMap = new HashMap<>();

            // 统计对应索引的物品需熔炼的数量
            for (int index : indexList) {
                if (count <= 0) break;
                if (!smeltedList.get(index).isEmpty()) {
                    resultMap.merge(index, 1, Integer::sum);
                    count--;
                }
            }

            // 生成结果列表
            List<ItemStack> ans = new ArrayList<>();
            for (int i = 0; i < original.size(); i++) {
                ItemStack stack = original.get(i);
                if (resultMap.containsKey(i)) {
                    int toSmelt = resultMap.get(i);
                    int originalCount = stack.getCount() - toSmelt;
                    if (originalCount != 0) {
                        ans.add(stack.copyWithCount(originalCount));
                    }
                    ans.add(smeltedList.get(i).copyWithCount(toSmelt));
                } else {
                    ans.add(stack);
                }
            }

            cir.setReturnValue(ans);
        }
    }
}
