package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.network.message.ThrowBaoziMessage;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerInteractionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import static io.github.fabricators_of_create.porting_lib.entity.events.PlayerInteractionEvents.LEFT_CLICK_EMPTY;

public class LeftClickEvent {
    public static void register() {
        LEFT_CLICK_EMPTY.register(LeftClickEvent::onHandle);
    }

    //肉包打狗，AUV，地道！
    private static void onHandle(PlayerInteractionEvents.LeftClickEmpty event) {
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        if (player == null) return;
        if (player.isSecondaryUseActive()
                && hand == InteractionHand.MAIN_HAND
                && player.getMainHandItem().is(ModItems.BAOZI)
                && ClientPlayNetworking.canSend(ThrowBaoziMessage.TYPE)
        ) {
            ClientPlayNetworking.send(new ThrowBaoziMessage());
        }
    }
}