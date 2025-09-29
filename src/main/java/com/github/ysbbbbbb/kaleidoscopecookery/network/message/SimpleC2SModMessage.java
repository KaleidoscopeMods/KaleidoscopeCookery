package com.github.ysbbbbbb.kaleidoscopecookery.network.message;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ThrowableBaoziEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SimpleC2SModMessage(int index) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "simple_c2s");
    public static final CustomPacketPayload.Type<SimpleC2SModMessage> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<ByteBuf, SimpleC2SModMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SimpleC2SModMessage::index,
            SimpleC2SModMessage::new);

    public static final int FLATULENCE = 0;
    public static final int THROW_BAOZI = 1;


    public static void handle(SimpleC2SModMessage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> onHandle(context, message.index));
        }
    }

    private static void onHandle(IPayloadContext context, int index) {
        if (index == FLATULENCE) {
            handleFlatulenceEffect(context);
        }
        if (index == THROW_BAOZI) {
            handleBaozi(context);
        }
    }

    private static void handleFlatulenceEffect(IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player && player.hasEffect(ModEffects.FLATULENCE)) {
            ServerLevel serverLevel = player.serverLevel();
            serverLevel.sendParticles(ParticleTypes.CLOUD, player.getX(),
                    player.getY() + 0.25, player.getZ(),
                    10, 0.25, 0.25, 0.25, 0.1);
            serverLevel.playSound(null, player.blockPosition(),
                    ModSounds.ENTITY_FART.get(), SoundSource.PLAYERS,
                    1.0F, 0.8F + (float) Math.random() * 0.4F);
        }
    }

    private static void handleBaozi(IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player) || !player.getMainHandItem().is(ModItems.BAOZI.get()) || !player.isSecondaryUseActive()) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        Level level = player.level();
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL,
                0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        ThrowableBaoziEntity baozi = new ThrowableBaoziEntity(level, player);
        baozi.setItem(stack.copyWithCount(1));
        baozi.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 1);
        level.addFreshEntity(baozi);
        stack.shrink(1);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
