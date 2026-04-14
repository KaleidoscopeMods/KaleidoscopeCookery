package com.github.ysbbbbbb.kaleidoscopecookery.client.gui.overlay;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.items.ItemStackHandler;

public class TrashCanOverlay implements IGuiOverlay {
    private static final ResourceLocation TRASH_CAN_OVERLAY = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/gui/trash_can_overlay.png");

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = gui.getMinecraft();
        if (minecraft.gameMode == null || minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
            return;
        }
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }

        // 如果在垃圾桶里，那么渲染遮罩
        if (player.getVehicle() instanceof SitEntity sitEntity && sitEntity.getSitType() == SitEntity.TRASH_CAN) {
            if (minecraft.options.getCameraType().isFirstPerson()) {
                gui.renderTextureOverlay(guiGraphics, TRASH_CAN_OVERLAY, 1.0f);
            }
        } else {
            this.renderTrashCanTip(guiGraphics, screenWidth, screenHeight, minecraft, player);
        }
    }

    private void renderTrashCanTip(GuiGraphics guiGraphics, int screenWidth, int screenHeight, Minecraft minecraft, LocalPlayer player) {
        HitResult hitResult = minecraft.hitResult;
        if (!(hitResult instanceof BlockHitResult result)) {
            return;
        }
        Level level = player.level();
        BlockPos blockPos = result.getBlockPos();
        BlockState blockState = player.level().getBlockState(blockPos);
        if (!blockState.is(ModBlocks.TRASH_CAN.get())) {
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!(blockEntity instanceof TrashCanBlockEntity trashCan)) {
            return;
        }

        Font font = Minecraft.getInstance().font;
        int x = screenWidth / 2 - 28;
        int y = screenHeight / 2 + 4;

        ItemStackHandler storage = trashCan.getStorage();
        for (int i = 0; i < storage.getSlots(); i++) {
            ItemStack stack = storage.getStackInSlot(i);
            if (!stack.isEmpty()) {
                guiGraphics.renderFakeItem(stack, x, y);
                guiGraphics.renderItemDecorations(font, stack, x, y);
                x = x + 20;
            }
        }
    }
}
