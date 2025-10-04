package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.network.NetworkHandler;
import com.github.ysbbbbbb.kaleidoscopecookery.network.message.SimpleC2SModMessage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class LeftClickEvent {
    @SubscribeEvent
    public static void onLeftClickItem(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        if (player.isSecondaryUseActive() && event.getHand() == InteractionHand.MAIN_HAND && player.getMainHandItem().is(ModItems.BAOZI.get())) {
            NetworkHandler.CHANNEL.sendToServer(new SimpleC2SModMessage(SimpleC2SModMessage.THROW_BAOZI));
        }
    }
}
