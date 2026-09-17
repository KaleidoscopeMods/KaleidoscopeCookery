package com.github.ysbbbbbb.kaleidoscopecookery.block.dispenser;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

public class BambooTrayDispenseBehavior extends OptionalDispenseItemBehavior {
    @Override
    protected @NotNull ItemStack execute(BlockSource source, ItemStack stack) {
        this.setSuccess(false);
        ServerLevel level = source.level();
        Direction facing = source.state().getValue(DispenserBlock.FACING);
        BlockPos placePos = source.pos().relative(facing);
        if (stack.is(ModItems.BAMBOO_TRAY.get()) && stack.getItem() instanceof BlockItem blockItem) {
            try {
                DirectionalPlaceContext context = new DirectionalPlaceContext(level, placePos, facing, stack, Direction.UP);
                this.setSuccess(blockItem.place(context).consumesAction());
            } catch (Exception exception) {
                KaleidoscopeCookery.LOGGER.error("Error trying to place bamboo tray at {}", placePos, exception);
            }
        }
        return stack;
    }
}
