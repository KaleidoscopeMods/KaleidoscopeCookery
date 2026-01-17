package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.client.animation.CustomArmPose;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

/**
 * 带有举起姿势的方块物品
 */
public class LiftBlockItem extends WithTooltipsBlockItem {
    public LiftBlockItem(Block block, Properties properties, String name) {
        super(block, properties, name);
    }

    public LiftBlockItem(Block block, String name) {
        super(block, name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
                if (!stack.isEmpty()) {
                    return CustomArmPose.LIFT_POSE;
                }
                return HumanoidModel.ArmPose.EMPTY;
            }
        });
    }
}
