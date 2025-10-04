package com.github.ysbbbbbb.kaleidoscopecookery.network.message;

import com.github.ysbbbbbb.kaleidoscopecookery.entity.ThrowableBaoziEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SimpleC2SModMessage(int index) {
    public static final int FLATULENCE = 0;
    public static final int THROW_BAOZI = 1;

    public static void encode(SimpleC2SModMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.index);
    }

    public static SimpleC2SModMessage decode(FriendlyByteBuf buf) {
        return new SimpleC2SModMessage(buf.readVarInt());
    }

    public static void handle(SimpleC2SModMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> onHandle(context, message.index));
        }
        context.setPacketHandled(true);
    }

    private static void onHandle(NetworkEvent.Context context, int index) {
        if (index == FLATULENCE) {
            handleFlatulenceEffect(context);
        }
        if (index == THROW_BAOZI) {
            handleBaozi(context);
        }
    }

    private static void handleFlatulenceEffect(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player != null && player.hasEffect(ModEffects.FLATULENCE.get())) {
            ServerLevel serverLevel = player.serverLevel();
            serverLevel.sendParticles(ParticleTypes.CLOUD, player.getX(),
                    player.getY() + 0.25, player.getZ(),
                    10, 0.25, 0.25, 0.25, 0.1);
            serverLevel.playSound(null, player.blockPosition(),
                    ModSounds.ENTITY_FART.get(), SoundSource.PLAYERS,
                    1.0F, 0.8F + (float) Math.random() * 0.4F);
        }
    }

    private static void handleBaozi(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player == null || !player.getMainHandItem().is(ModItems.BAOZI.get()) || !player.isSecondaryUseActive()) {
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
}
