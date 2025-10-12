package com.github.ysbbbbbb.kaleidoscopecookery.network.message;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopecookery.network.NetworkHandler;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

public class FlatulenceMessage implements FabricPacket, ServerPlayNetworking.PlayPacketHandler<FlatulenceMessage> {
    public static final PacketType<FlatulenceMessage> TYPE = PacketType.create(NetworkHandler.FLATULENCE_PACKET, FlatulenceMessage::new);

    public FlatulenceMessage(FriendlyByteBuf buf) {
    }

    public FlatulenceMessage() {
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    @Override
    public void receive(FlatulenceMessage packet, ServerPlayer player, PacketSender responseSender) {
        if (player != null && player.hasEffect(ModEffects.FLATULENCE)) {
            ServerLevel serverLevel = player.serverLevel();
            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    player.getX(), player.getY() + 0.25, player.getZ(),
                    10, 0.25, 0.25, 0.25, 0.1);
            serverLevel.playSound(null, player.blockPosition(),
                    ModSounds.ENTITY_FART, SoundSource.PLAYERS,
                    1.0F, 0.8F + (float) Math.random() * 0.4F);
        }
    }
}