package com.github.ysbbbbbb.kaleidoscopecookery.network;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.network.message.FlatulenceMessage;
//import com.github.ysbbbbbb.kaleidoscopecookery.network.message.SimpleC2SModMessage;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class NetworkHandler {
    public static final ResourceLocation FLATULENCE_PACKET = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "flatulence");

    public static void init() {
//        CHANNEL.registerMessage(0, SimpleC2SModMessage.class, SimpleC2SModMessage::encode, SimpleC2SModMessage::decode, SimpleC2SModMessage::handle,
//                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        ServerPlayNetworking.registerGlobalReceiver(FlatulenceMessage.TYPE, new FlatulenceMessage());
    }
}
