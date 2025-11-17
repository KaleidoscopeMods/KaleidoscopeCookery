package com.github.ysbbbbbb.kaleidoscopecookery.network.message;

import com.github.ysbbbbbb.kaleidoscopecookery.entity.ThrowableBaoziEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.network.NetworkHandler;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThrowBaoziMessage implements FabricPacket, ServerPlayNetworking.PlayPacketHandler<ThrowBaoziMessage> {
    public static final PacketType<ThrowBaoziMessage> TYPE = PacketType.create(NetworkHandler.THROWING_BAOZI_PACKET, ThrowBaoziMessage::new);

    public ThrowBaoziMessage(FriendlyByteBuf buf) {
    }

    public ThrowBaoziMessage() {
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    @Override
    public void receive(ThrowBaoziMessage throwBaoziMessage, ServerPlayer player, PacketSender packetSender) {
        if (!player.getMainHandItem().is(ModItems.BAOZI) || !player.isSecondaryUseActive()) {
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
