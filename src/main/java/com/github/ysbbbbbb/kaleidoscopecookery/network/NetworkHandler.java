package com.github.ysbbbbbb.kaleidoscopecookery.network;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.network.message.FlatulenceMessage;
//import com.github.ysbbbbbb.kaleidoscopecookery.network.message.SimpleC2SModMessage;
import com.github.ysbbbbbb.kaleidoscopecookery.network.message.ThrowBaoziMessage;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class NetworkHandler {
    public static final ResourceLocation FLATULENCE_PACKET = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "flatulence");
    public static final ResourceLocation THROWING_BAOZI_PACKET = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "throwing_baozi");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ThrowBaoziMessage.TYPE, new ThrowBaoziMessage());
        ServerPlayNetworking.registerGlobalReceiver(FlatulenceMessage.TYPE, new FlatulenceMessage());
    }
}
