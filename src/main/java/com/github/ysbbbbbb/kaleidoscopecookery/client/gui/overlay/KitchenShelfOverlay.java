package com.github.ysbbbbbb.kaleidoscopecookery.client.gui.overlay;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenShelfBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class KitchenShelfOverlay implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || !(minecraft.hitResult instanceof BlockHitResult hit)
            || hit.getType() != HitResult.Type.BLOCK
            || !player.level().getBlockState(hit.getBlockPos()).is(ModBlocks.KITCHEN_SHELF.get())) {
            return;
        }

        BlockPos pos = hit.getBlockPos();
        BlockEntity entity = player.level().getBlockEntity(pos);
        if (!(entity instanceof KitchenShelfBlockEntity shelf)) {
            return;
        }

        ItemStack stored = shelf.getStoredStack();
        if (!stored.isEmpty()) {
            int x = graphics.guiWidth() / 2 - 8;
            int y = graphics.guiHeight() / 2 + 8;
            graphics.renderFakeItem(stored, x, y);
            graphics.renderItemDecorations(minecraft.font, stored, x, y);
        }
    }
}
