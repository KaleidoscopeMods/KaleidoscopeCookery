package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.scenes;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.SteamerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StoveBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SteamerScenes {
    public static void introduction(SceneBuilder scene, SceneBuildingUtil util) {
        VectorUtil vector = util.vector();
        SelectionUtil select = util.select();
        PositionUtil grid = util.grid();

        scene.title("steamer", "");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        BlockPos litPos = grid.at(2, 1, 2);
        Selection litSel = select.position(litPos);
        BlockPos steamerPos1 = grid.at(2, 2, 2);
        Selection steamerSel1 = select.position(steamerPos1);

        scene.world().modifyBlock(steamerPos1, (s) -> ModBlocks.STEAMER.get().defaultBlockState()
                .setValue(SteamerBlock.FACING, Direction.NORTH)
                .setValue(SteamerBlock.HALF, true)
                .setValue(SteamerBlock.HAS_LID, false), false);

        scene.idle(20);
        scene.world().showSection(litSel, Direction.EAST);
        scene.idle(3);
        scene.world().showSection(steamerSel1, Direction.EAST);
        scene.idle(20);

        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(litPos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(litPos, Direction.NORTH), Pointing.RIGHT, 35)
                .rightClick()
                .withItem(new ItemStack(Items.FLINT_AND_STEEL));
        scene.idle(7);
        scene.world().modifyBlock(litPos, (s) -> ModBlocks.STOVE.get().defaultBlockState()
                .setValue(StoveBlock.FACING, Direction.NORTH)
                .setValue(StoveBlock.LIT, true), false);
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(steamerPos1, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(steamerPos1, Direction.UP), Pointing.DOWN, 35)
                .rightClick()
                .withItem(new ItemStack(ModItems.STEAMER.get()));
        scene.idle(7);
        scene.world().modifyBlock(steamerPos1, (s) -> ModBlocks.STEAMER.get().defaultBlockState()
                .setValue(SteamerBlock.FACING, Direction.NORTH)
                .setValue(SteamerBlock.HAS_LID, false)
                .setValue(SteamerBlock.HALF, false), false);
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        for (int i = 0; i < 5; i++) {
            if (i == 1) {
                scene.overlay().showText(40).text("").placeNearTarget();
            }
            scene.idle(3);
            scene.world().showSection(select.position(grid.at(2, 3 + i, 2)), Direction.EAST);
        }
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.RED, select.fromTo(2, 2, 2, 2, 7, 2),
                select.fromTo(2, 2, 2, 2, 7, 2), 45);
        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(grid.at(2, 4, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(65);

        for (int i = 0; i < 2; i++) {
            scene.idle(3);
            scene.world().hideSection(select.position(grid.at(2, 7 - i, 2)), Direction.EAST);
        }
        scene.idle(20);

        scene.overlay().showOutline(PonderPalette.GREEN, select.fromTo(2, 2, 2, 2, 5, 2),
                select.fromTo(2, 2, 2, 2, 5, 2), 45);
        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(grid.at(2, 4, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(55);
    }

    public static void cooking(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("steamer_1", "");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        BlockPos litPos = util.grid().at(2, 1, 2);
        Selection litSel = util.select().position(litPos);
        BlockPos steamerPos1 = util.grid().at(2, 2, 2);
        Selection steamerSel1 = util.select().position(steamerPos1);
        ItemStack steamer = new ItemStack(ModItems.STEAMER.get());
        CompoundTag tag = new CompoundTag();
        NonNullList<ItemStack> first = NonNullList.withSize(4, new ItemStack(ModItems.RAW_DOUGH.get()));
        ContainerHelper.saveAllItems(tag, first);
        BlockItem.setBlockEntityData(steamer, ModBlocks.STEAMER_BE.get(), tag);

        scene.world().modifyBlock(litPos, (s) -> ModBlocks.STOVE.get().defaultBlockState()
                .setValue(StoveBlock.FACING, Direction.NORTH)
                .setValue(StoveBlock.LIT, true), false);
        scene.world().modifyBlock(steamerPos1, (s) -> ModBlocks.STEAMER.get().defaultBlockState()
                .setValue(SteamerBlock.FACING, Direction.NORTH)
                .setValue(SteamerBlock.HALF, true)
                .setValue(SteamerBlock.HAS_LID, false), false);

        scene.idle(20);
        scene.world().showSection(litSel, Direction.EAST);
        scene.idle(3);
        scene.world().showSection(steamerSel1, Direction.EAST);
        scene.idle(20);

        scene.overlay().showText(40).text("")
                .pointAt(util.vector().blockSurface(steamerPos1, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(steamerPos1, Direction.UP), Pointing.DOWN, 35)
                .rightClick()
                .withItem(new ItemStack(ModItems.RAW_DOUGH.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(steamerPos1, SteamerBlockEntity.class, (e) -> {
            ItemStack dough = new ItemStack(ModItems.RAW_DOUGH.get(), 4);
            for (int i = 0; i < 4; i++) {
                e.getItems().set(i, dough.split(1));
            }
            e.setChanged();
            e.refresh();
        });
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("")
                .pointAt(util.vector().blockSurface(steamerPos1, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(steamerPos1, Direction.UP), Pointing.DOWN, 35)
                .rightClick()
                .withItem(steamer);
        scene.idle(7);
        scene.world().modifyBlock(steamerPos1, (s) -> ModBlocks.STEAMER.get().defaultBlockState()
                .setValue(SteamerBlock.FACING, Direction.NORTH)
                .setValue(SteamerBlock.HALF, false)
                .setValue(SteamerBlock.HAS_LID, false), false);
        scene.world().modifyBlockEntity(steamerPos1, SteamerBlockEntity.class, (e) -> {
            ItemStack dough = new ItemStack(ModItems.RAW_DOUGH.get(), 4);
            for (int i = 4; i < 8; i++) {
                e.getItems().set(i, dough.split(1));
            }
            e.setChanged();
            e.refresh();
        });
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("")
                .pointAt(util.vector().blockSurface(steamerPos1, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(steamerPos1, Direction.EAST), Pointing.RIGHT, 35)
                .rightClick()
                .whileSneaking();
        scene.idle(7);
        scene.world().modifyBlock(steamerPos1, (s) -> ModBlocks.STEAMER.get().defaultBlockState()
                .setValue(SteamerBlock.FACING, Direction.NORTH)
                .setValue(SteamerBlock.HALF, false)
                .setValue(SteamerBlock.HAS_LID, true), false);
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("").placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(2, 3, 2), Direction.UP), Pointing.DOWN, 35)
                .withItem(new ItemStack(Items.CLOCK));
        scene.idle(55);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("")
                .pointAt(util.vector().blockSurface(steamerPos1, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(steamerPos1, Direction.EAST), Pointing.RIGHT, 35)
                .rightClick()
                .whileSneaking();
        scene.idle(7);
        scene.world().modifyBlock(steamerPos1, (s) -> ModBlocks.STEAMER.get().defaultBlockState()
                .setValue(SteamerBlock.FACING, Direction.NORTH)
                .setValue(SteamerBlock.HALF, false)
                .setValue(SteamerBlock.HAS_LID, false), false);
        scene.world().modifyBlockEntity(steamerPos1, SteamerBlockEntity.class, (e) -> {
            ItemStack dough = new ItemStack(ModItems.MANTOU.get(), 8);
            for (int i = 0; i < 8; i++) {
                e.getItems().set(i, dough.split(1));
            }
            e.setChanged();
            e.refresh();
        });
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(120).text("")
                .pointAt(util.vector().blockSurface(steamerPos1, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(steamerPos1, Direction.UP), Pointing.DOWN, 20)
                .rightClick();
        scene.idle(7);
        scene.world().modifyBlockEntity(steamerPos1, SteamerBlockEntity.class, (e) -> {
            for (int i = 4; i < 8; i++) {
                e.getItems().set(i, ItemStack.EMPTY);
            }
            e.setChanged();
            e.refresh();
        });
        scene.idle(28);
        scene.overlay().showControls(util.vector().blockSurface(steamerPos1, Direction.UP), Pointing.DOWN, 20)
                .withItem(new ItemStack(ModItems.MANTOU.get()));
        scene.idle(30);
        scene.overlay().showControls(util.vector().blockSurface(steamerPos1, Direction.EAST), Pointing.RIGHT, 20)
                .rightClick();
        scene.idle(7);
        scene.world().modifyBlock(steamerPos1, (s) -> ModBlocks.STEAMER.get().defaultBlockState()
                .setValue(SteamerBlock.FACING, Direction.NORTH)
                .setValue(SteamerBlock.HALF, true)
                .setValue(SteamerBlock.HAS_LID, false), false);
        scene.idle(28);
        scene.overlay().showControls(util.vector().blockSurface(steamerPos1, Direction.EAST), Pointing.RIGHT, 20)
                .withItem(new ItemStack(ModItems.STEAMER.get()));
        scene.idle(35);
    }
}
