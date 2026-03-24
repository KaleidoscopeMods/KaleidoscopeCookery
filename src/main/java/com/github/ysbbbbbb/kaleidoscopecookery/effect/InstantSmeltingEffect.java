package com.github.ysbbbbbb.kaleidoscopecookery.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

public class InstantSmeltingEffect {
    public static void onGetDrops(BlockState state, ServerLevel level, Entity entity, CallbackInfoReturnable<List<ItemStack>> cir) {
        // 只允许冶炼矿物
        if (!state.is(Tags.Blocks.ORES) || !(entity instanceof LivingEntity living)) {
            return;
        }

        MobEffectInstance effect = living.getEffect(ModEffects.INSTANT_SMELTING.get());
        if (effect == null) {
            return;
        }

        List<ItemStack> orgDrops = cir.getReturnValue();
        if (orgDrops.isEmpty()) {
            return;
        }

        // 因为 lambda 只能访问 final 或 effectively final 的变量，所以用数组来存储剩余的可冶炼数量
        final int[] count = new int[]{effect.getAmplifier() + 1};
        List<ItemStack> newDrops = Lists.newArrayList();

        for (ItemStack stack : orgDrops) {
            // 如果没有剩余的可冶炼数量了，就直接跳出循环，保留剩余的原版掉落
            if (count[0] <= 0) {
                break;
            }
            SimpleContainer container = new SimpleContainer(stack);
            level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, container, level).ifPresent(recipe -> {
                // 使用 assemble 更为标准
                ItemStack result = recipe.assemble(container, level.registryAccess());
                // 扣除原始物品的数量，增加冶炼结果的数量
                int splitCount = stack.split(count[0]).getCount();
                // 有可能烧炼的结果数量不为 1，故需要重新计算总量
                newDrops.add(result.copyWithCount(result.getCount() * splitCount));
                // 扣除已经处理的数量
                count[0] -= splitCount;
            });
        }

        // 没有可烧的，不处理
        if (newDrops.isEmpty()) {
            return;
        }

        // 最后合并
        orgDrops.stream().filter(stack -> !stack.isEmpty()).forEach(newDrops::add);
        cir.setReturnValue(newDrops);
    }
}
