package com.github.ysbbbbbb.kaleidoscopecookery.client.gui.overlay;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class PotOverlay implements IGuiOverlay {
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = gui.getMinecraft();
        if (minecraft.gameMode == null || minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
            return;
        }
        HitResult hitResult = minecraft.hitResult;
        if (!(hitResult instanceof BlockHitResult result)) {
            return;
        }
        if (result.getType() != HitResult.Type.BLOCK) {
            return;
        }
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        Level level = player.level();
        BlockPos blockPos = result.getBlockPos();
        BlockState blockState = player.level().getBlockState(blockPos);
        if (!blockState.is(ModBlocks.POT.get())) {
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!(blockEntity instanceof PotBlockEntity pot)) {
            return;
        }
        Font font = Minecraft.getInstance().font;
        int x = screenWidth / 2;
        int y = screenHeight - 72;
        if (minecraft.gui.overlayMessageTime > 0) {
            y = y - 12;
        }

        if (blockState.getValue(PotBlock.HAS_OIL) && pot.hasHeatSource(level)) {
            int status = pot.getStatus();
            if (status == PotBlockEntity.PUT_INGREDIENT) {
                drawWordWrap(guiGraphics, font, Component.translatable("tip.kaleidoscope_cookery.pot.add_ingredient"), x, y, 0xFFFFFF);
                return;
            }
            if (status == PotBlockEntity.COOKING) {
                drawWordWrap(guiGraphics, font, Component.translatable("tip.kaleidoscope_cookery.pot.need_stir_fry"), x, y, 0xFFFFFF);
                return;
            }
            if (status == PotBlockEntity.FINISHED) {
                drawWordWrap(guiGraphics, font, Component.translatable("tip.kaleidoscope_cookery.pot.done"), x, y, ChatFormatting.RED.getColor());
            }
        }
    }

    private static void drawWordWrap(GuiGraphics graphics, Font font, MutableComponent text, int pX, int pY, int color) {
        for (FormattedCharSequence sequence : font.split(text, 100)) {
            graphics.drawString(font, sequence, pX - font.width(sequence) / 2, pY, color);
            pY += font.lineHeight;
        }
    }
}
