package com.github.ysbbbbbb.kaleidoscopecookery.client.gui.overlay;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
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

public class SteamerOverlay implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || !(minecraft.hitResult instanceof BlockHitResult hit)
            || hit.getType() != HitResult.Type.BLOCK
            || !player.level().getBlockState(hit.getBlockPos()).is(ModBlocks.STEAMER.get())) {
            return;
        }
        BlockPos pos = hit.getBlockPos();
        BlockEntity entity = player.level().getBlockEntity(pos);
        if (!(entity instanceof SteamerBlockEntity steamer)) {
            return;
        }

        int x = graphics.guiWidth() / 2 - 38;
        int y = graphics.guiHeight() / 2 + 4;
        int count = 0;

        for (ItemStack stack : steamer.getItems()) {
            if (!stack.isEmpty()) {
                graphics.renderFakeItem(stack, x, y);
                graphics.renderItemDecorations(minecraft.font, stack, x, y);

                count++;
                x += 20;

                if (count > 3) {
                    x = graphics.guiWidth() / 2 - 38;
                    y += 20;
                    count = 0;
                }
            }
        }
    }
}
