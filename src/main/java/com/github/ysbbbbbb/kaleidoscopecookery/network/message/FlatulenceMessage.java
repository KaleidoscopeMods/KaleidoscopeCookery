package com.github.ysbbbbbb.kaleidoscopecookery.network.message;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public record FlatulenceMessage() implements CustomPacketPayload, ServerPlayNetworking.PlayPayloadHandler<FlatulenceMessage> {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "flatulence");
    public static final Type<FlatulenceMessage> TYPE = new Type<>(ID);
    public static final FlatulenceMessage INSTANCE = new FlatulenceMessage();
    public static final StreamCodec<RegistryFriendlyByteBuf, FlatulenceMessage> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public void receive(FlatulenceMessage payload, ServerPlayNetworking.Context context) {
        Player player = context.player();
        if (player instanceof ServerPlayer serverPlayer && player.hasEffect(ModEffects.FLATULENCE)) {
            ServerLevel serverLevel = serverPlayer.serverLevel();
            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    player.getX(), player.getY() + 0.25, player.getZ(),
                    10, 0.25, 0.25, 0.25, 0.1);
            serverLevel.playSound(null, player.blockPosition(), ModSounds.ENTITY_FART,
                    SoundSource.PLAYERS, 1.0F, 0.8F + (float) Math.random() * 0.4F);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
