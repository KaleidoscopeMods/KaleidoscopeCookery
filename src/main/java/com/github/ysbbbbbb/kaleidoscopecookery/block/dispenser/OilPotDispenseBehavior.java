package com.github.ysbbbbbb.kaleidoscopecookery.block.dispenser;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

public class OilPotDispenseBehavior extends OptionalDispenseItemBehavior {
    @Override
    protected @NotNull ItemStack execute(BlockSource source, ItemStack stack) {
        this.setSuccess(false);
        Item item = stack.getItem();
        ServerLevel level = source.getLevel();
        if (stack.is(ModItems.OIL_POT) && item instanceof BlockItem blockItem) {
            Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
            BlockPos placePos = source.getPos().relative(facing);
            Direction placeDirection = facing.getAxis() == Direction.Axis.Y ? Direction.NORTH : facing;
            try {
                DirectionalPlaceContext context = new DirectionalPlaceContext(level, placePos, facing, stack, placeDirection);
                this.setSuccess(blockItem.place(context).consumesAction());
            } catch (Exception exception) {
                KaleidoscopeCookery.LOGGER.error("Error trying to place oil pot at {}", placePos, exception);
            }
        }
        return stack;
    }
}
