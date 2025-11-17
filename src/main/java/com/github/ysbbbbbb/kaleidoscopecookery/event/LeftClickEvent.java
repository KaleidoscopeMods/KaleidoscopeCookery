package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEvents;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.network.message.ThrowBaoziMessage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class LeftClickEvent {
    public static void register() {
        ModEvents.PLAYER_LEFT_CLICK.register(LeftClickEvent::onHandle);
    }

    //肉包打狗，AUV，地道！
    private static void onHandle(@Nullable Player player, InteractionHand hand) {
        if (player == null) return;
        if (player.isSecondaryUseActive()
                && hand == InteractionHand.MAIN_HAND
                && player.getMainHandItem().is(ModItems.BAOZI)
                && ClientPlayNetworking.canSend(ThrowBaoziMessage.TYPE)
        ) {
            ClientPlayNetworking.send(ThrowBaoziMessage.INSTANCE);
        }
    }
}
