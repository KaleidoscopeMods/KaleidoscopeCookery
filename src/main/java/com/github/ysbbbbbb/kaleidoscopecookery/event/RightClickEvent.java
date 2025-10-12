package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class RightClickEvent {
    public static void register() {
        UseBlockCallback.EVENT.register(RightClickEvent::onUseBlock);
        UseEntityCallback.EVENT.register(RightClickEvent::onUseEntity);
    }

    private static InteractionResult onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        ItemStack itemInHand = player.getItemInHand(hand);
        if (player.isSecondaryUseActive() && hand == InteractionHand.MAIN_HAND
            // FIXME: 目前仅排除调试棒，这导致其他方块无法潜行右击果篮使用
            && !itemInHand.is(Items.DEBUG_STICK)
            && level.getBlockEntity(pos) instanceof FruitBasketBlockEntity fruitBasketBlock) {
            fruitBasketBlock.takeOut(player);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult onUseEntity(Player player, Level level, InteractionHand hand, Entity target, EntityHitResult hitResult) {
        if (target instanceof Chicken chicken && chicken.isBaby()
            && player.getMainHandItem().is(ModItems.CATERPILLAR)) {
            // 让鸡瞬间成年
            chicken.setAge(0);
            // 加一些特性和音效
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.HEART,
                        chicken.getX(),
                        chicken.getY() + 0.25,
                        chicken.getZ(),
                        5,
                        0.2, 0.1, 0.2, 0.1);
                serverLevel.playSound(null, chicken.getX(), chicken.getY(), chicken.getZ(),
                        SoundEvents.PARROT_EAT, chicken.getSoundSource(),
                        1.0F, 1.0F + (serverLevel.random.nextFloat() - serverLevel.random.nextFloat()) * 0.2F);
            }
            player.getMainHandItem().shrink(1);
            ModTrigger.EVENT.trigger(player, ModEventTriggerType.USE_CATERPILLAR_FEED_CHICKEN);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
