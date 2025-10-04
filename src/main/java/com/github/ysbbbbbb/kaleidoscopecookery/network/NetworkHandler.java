package com.github.ysbbbbbb.kaleidoscopecookery.network;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.network.message.SimpleC2SModMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class NetworkHandler {
    private static final String VERSION = "1.0.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "network"),
            () -> VERSION, it -> it.equals(VERSION), it -> it.equals(VERSION));

    public static void init() {
        CHANNEL.registerMessage(0, SimpleC2SModMessage.class, SimpleC2SModMessage::encode, SimpleC2SModMessage::decode, SimpleC2SModMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}
