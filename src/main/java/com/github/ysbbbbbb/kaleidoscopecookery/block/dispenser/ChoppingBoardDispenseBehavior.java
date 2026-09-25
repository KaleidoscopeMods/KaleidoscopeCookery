package com.github.ysbbbbbb.kaleidoscopecookery.block.dispenser;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IChoppingBoard;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class ChoppingBoardDispenseBehavior extends OptionalDispenseItemBehavior {
    private final boolean knife;

    public ChoppingBoardDispenseBehavior(boolean knife) {
        this.knife = knife;
    }

    @Override
    protected @NotNull ItemStack execute(BlockSource source, ItemStack stack) {
        this.setSuccess(false);
        Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
        BlockPos boardPos = source.getPos().relative(facing);
        BlockEntity blockEntity = source.getLevel().getBlockEntity(boardPos);
        if (blockEntity instanceof IChoppingBoard board) {
            boolean success = knife
                    ? board.onCutItem(source.getLevel(), null, stack)
                    : board.onPutItem(source.getLevel(), null, stack);
            if (success) {
                this.setSuccess(true);
                if (!knife) {
                    stack.shrink(1);
                }
            }
        }
        return stack;
    }

    public static void register() {
        DispenserBlock.registerBehavior(ModItems.IRON_KITCHEN_KNIFE.get(), new ChoppingBoardDispenseBehavior(true));
        DispenserBlock.registerBehavior(ModItems.GOLD_KITCHEN_KNIFE.get(), new ChoppingBoardDispenseBehavior(true));
        DispenserBlock.registerBehavior(ModItems.DIAMOND_KITCHEN_KNIFE.get(), new ChoppingBoardDispenseBehavior(true));
        DispenserBlock.registerBehavior(ModItems.NETHERITE_KITCHEN_KNIFE.get(), new ChoppingBoardDispenseBehavior(true));

        // 因为原版的 shit 设计，必须手动枚举所有能够摆放的食物
        ChoppingBoardDispenseBehavior foodBehavior = new ChoppingBoardDispenseBehavior(false);
        Item[] choppingIngredients = {
                Items.TROPICAL_FISH,
                Items.COD, Items.COOKED_COD,
                Items.SALMON, Items.COOKED_SALMON,
                Items.MUTTON, Items.COOKED_MUTTON,
                Items.BEEF, Items.COOKED_BEEF,
                Items.PORKCHOP, Items.COOKED_PORKCHOP,
                Items.CHICKEN, Items.COOKED_CHICKEN,
                Items.RABBIT, Items.COOKED_RABBIT,
                ModItems.RAW_DOUGH.get()
        };
        for (Item item : choppingIngredients) {
            DispenserBlock.registerBehavior(item, foodBehavior);
        }
    }
}
