package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeapotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.network.message.SimpleC2SModMessage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class LeftClickEvent {
    @SubscribeEvent
    public static void onLeftClickItem(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        if (player.isSecondaryUseActive() && event.getHand() == InteractionHand.MAIN_HAND) {
            ItemStack mainHandItem = player.getMainHandItem();
            if (mainHandItem.is(ModItems.BAOZI.get())) {
                PacketDistributor.sendToServer(new SimpleC2SModMessage(SimpleC2SModMessage.THROW_BAOZI));
            }
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (player.isSecondaryUseActive() && event.getHand() == InteractionHand.MAIN_HAND) {
            ItemStack mainHandItem = player.getMainHandItem();
            if (mainHandItem.is(ModItems.TEAPOT.get())) {
                TeapotItem.clearAll(mainHandItem, event.getEntity());
                event.setCanceled(true);
            }
        }
    }
}
