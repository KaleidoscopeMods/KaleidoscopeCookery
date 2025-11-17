package com.github.ysbbbbbb.kaleidoscopecookery.network.message;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ThrowableBaoziEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record ThrowBaoziMessage() implements CustomPacketPayload, ServerPlayNetworking.PlayPayloadHandler<ThrowBaoziMessage> {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "throwing_baozi");
    public static final Type<ThrowBaoziMessage> TYPE = new Type<>(ID);
    public static ThrowBaoziMessage INSTANCE = new ThrowBaoziMessage();
    public static final StreamCodec<RegistryFriendlyByteBuf, ThrowBaoziMessage> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public void receive(ThrowBaoziMessage payload, ServerPlayNetworking.Context context) {
        if (!(context.player() instanceof ServerPlayer player) || !player.getMainHandItem().is(ModItems.BAOZI) || !player.isSecondaryUseActive()) {
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
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
