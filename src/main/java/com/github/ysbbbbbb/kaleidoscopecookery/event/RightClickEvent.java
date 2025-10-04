package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class RightClickEvent {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack itemInHand = player.getItemInHand(hand);
        Item item = itemInHand.getItem();
        Item maidItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation("touhou_little_maid", "smart_slab_has_maid"));
        if (player.isSecondaryUseActive() && hand == InteractionHand.MAIN_HAND
            && !itemInHand.is(Items.DEBUG_STICK)
            && !itemInHand.is(Items.FIREWORK_ROCKET)
            && (maidItem == null || !itemInHand.is(maidItem))
            && level.getBlockEntity(pos) instanceof FruitBasketBlockEntity fruitBasketBlock) {
            if (item instanceof BlockItem) {
                Direction clickedFace = event.getFace();
                if (!(clickedFace == Direction.UP)) {
                   return;
                }
            }
            fruitBasketBlock.takeOut(player);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        Level level = event.getLevel();
        if (target instanceof Chicken chicken && chicken.isBaby()
            && player.getMainHandItem().is(ModItems.CATERPILLAR.get())) {
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
        }
    }
}
