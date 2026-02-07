package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.scenes;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.EnamelBasinBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StoveBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenShovelItem;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class EnamelBasinScenes {
    public static void introduction(SceneBuilder scene, SceneBuildingUtil util) {
        VectorUtil vector = util.vector();
        SelectionUtil select = util.select();
        PositionUtil grid = util.grid();

        scene.title("enamel_basin", "");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        BlockPos basePos = grid.at(2, 1, 2);
        Selection baseSel = select.position(basePos);
        BlockPos funcPos = grid.at(2, 2, 2);
        Selection funcSel = select.position(funcPos);

        scene.idle(20);
        scene.world().showSection(baseSel, Direction.EAST);
        scene.idle(3);
        scene.world().showSection(funcSel, Direction.EAST);
        scene.idle(20);

        scene.overlay().showText(60).text("")
                .pointAt(vector.blockSurface(funcPos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(funcPos, Direction.UP), Pointing.DOWN, 20)
                .rightClick();
        scene.idle(7);
        scene.world().modifyBlock(funcPos, (s) -> ModBlocks.ENAMEL_BASIN.get().defaultBlockState()
                .setValue(EnamelBasinBlock.HAS_LID, false)
                .setValue(EnamelBasinBlock.OIL_COUNT, 0), false);
        scene.idle(28);

        scene.overlay().showControls(vector.blockSurface(funcPos, Direction.UP), Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(ModItems.OIL.get()));
        scene.idle(7);
        scene.world().modifyBlock(funcPos, (s) -> ModBlocks.ENAMEL_BASIN.get().defaultBlockState()
                .setValue(EnamelBasinBlock.HAS_LID, false)
                .setValue(EnamelBasinBlock.OIL_COUNT, 32), false);
        scene.idle(33);

        scene.addKeyframe();
        scene.idle(20);

        scene.overlay().showText(60).text("")
                .pointAt(vector.blockSurface(funcPos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(funcPos, Direction.UP), Pointing.DOWN, 35)
                .rightClick()
                .withItem(new ItemStack(ModItems.KITCHEN_SHOVEL.get()));
        scene.idle(7);
        scene.world().modifyBlock(funcPos, (s) -> ModBlocks.ENAMEL_BASIN.get().defaultBlockState()
                .setValue(EnamelBasinBlock.HAS_LID, false)
                .setValue(EnamelBasinBlock.OIL_COUNT, 31), false);
        scene.idle(43);

        ItemStack shovel = new ItemStack(ModItems.KITCHEN_SHOVEL.get());
        KitchenShovelItem.setHasOil(shovel, true);
        scene.overlay().showControls(vector.blockSurface(funcPos, Direction.UP), Pointing.DOWN, 35)
                .withItem(shovel);
        scene.idle(55);

        scene.addKeyframe();
        scene.idle(20);

        scene.world().hideSection(baseSel, Direction.EAST);
        scene.idle(3);
        scene.world().hideSection(funcSel, Direction.EAST);
        scene.idle(20);

        scene.world().setBlock(basePos, ModBlocks.STOVE.get().defaultBlockState()
                .setValue(StoveBlock.FACING, Direction.NORTH)
                .setValue(StoveBlock.LIT, true), false);
        scene.world().setBlock(funcPos, ModBlocks.POT.get().defaultBlockState()
                .setValue(PotBlock.FACING, Direction.NORTH)
                .setValue(PotBlock.HAS_OIL, false)
                .setValue(PotBlock.SHOW_OIL, false), false);

        scene.world().showSection(baseSel, Direction.EAST);
        scene.idle(3);
        scene.world().showSection(funcSel, Direction.EAST);
        scene.idle(20);

        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(funcPos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(funcPos, Direction.UP), Pointing.DOWN, 35)
                .rightClick()
                .withItem(shovel);
        scene.idle(7);
        scene.world().modifyBlock(funcPos, (s) -> ModBlocks.POT.get().defaultBlockState()
                .setValue(PotBlock.FACING, Direction.NORTH)
                .setValue(PotBlock.HAS_OIL, true)
                .setValue(PotBlock.SHOW_OIL, true), false);
        scene.idle(43);

        scene.overlay().showControls(vector.blockSurface(funcPos, Direction.UP), Pointing.DOWN, 35)
                .withItem(new ItemStack(ModItems.KITCHEN_SHOVEL.get()));
        scene.idle(55);
    }
}
