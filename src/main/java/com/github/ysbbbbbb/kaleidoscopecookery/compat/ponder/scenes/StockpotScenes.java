package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.scenes;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StockpotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StoveBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;

public class StockpotScenes {
    public static void introduction(SceneBuilder scene, SceneBuildingUtil util) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        VectorUtil vector = util.vector();
        SelectionUtil select = util.select();
        PositionUtil grid = util.grid();

        scene.title("stockpot", "");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        BlockPos litPos = grid.at(2, 1, 2);
        Selection litSel = select.position(litPos);
        BlockPos potPos = grid.at(2, 2, 2);
        Selection potSel = select.position(potPos);
        LivingEntity pig = new Pig(EntityType.PIG, level);

        scene.world().modifyBlock(litPos, (s) -> ModBlocks.STOVE.get().defaultBlockState()
                .setValue(StoveBlock.FACING, Direction.NORTH)
                .setValue(StoveBlock.LIT, false), false);
        scene.idle(20);

        scene.world().showSection(litSel, Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(potSel, Direction.DOWN);
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
                .pointAt(vector.blockSurface(potPos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 35)
                .rightClick()
                .withItem(new ItemStack(Items.WATER_BUCKET));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, StockpotBlockEntity.class, (e) -> {
            e.addSoupBase(level, pig, new ItemStack(Items.WATER_BUCKET));
            e.refresh();
        });
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(potPos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 35)
                .rightClick()
                .withItem(new ItemStack(ModItems.RICE_SEED.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, StockpotBlockEntity.class, (e) -> {
            e.addAllIngredients(List.of(new ItemStack(ModItems.RICE_SEED.get()),
                    new ItemStack(ModItems.RICE_SEED.get()),
                    new ItemStack(ModItems.RICE_SEED.get()),
                    new ItemStack(ModItems.RICE_SEED.get()),
                    new ItemStack(ModItems.RICE_SEED.get())), pig);
        });
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(potPos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 35)
                .rightClick()
                .withItem(new ItemStack(ModItems.STOCKPOT_LID.get()));
        scene.idle(7);
        scene.world().modifyBlock(potPos, (s) -> ModBlocks.STOCKPOT.get().defaultBlockState()
                .setValue(StockpotBlock.HAS_LID, true), false);
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("").placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(grid.at(2, 3, 2), Direction.UP), Pointing.DOWN, 35)
                .withItem(new ItemStack(Items.CLOCK));
        scene.idle(55);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(potPos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 35)
                .rightClick();
        scene.idle(7);
        scene.world().modifyBlock(potPos, (s) -> ModBlocks.STOCKPOT.get().defaultBlockState()
                .setValue(StockpotBlock.HAS_LID, false), false);
        scene.world().modifyBlockEntity(potPos, StockpotBlockEntity.class, (e) -> {
            e.setRecipe(level);
            e.setStatus(3);
            e.getInputs().clear();
            e.refresh();
        });
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(potPos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 35)
                .rightClick()
                .withItem(new ItemStack(Items.BOWL));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, StockpotBlockEntity.class, (e) -> {
            e.takeOutProduct(level, pig, new ItemStack(Items.BOWL, 5));
        });
        scene.idle(43);
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 35)
                .withItem(new ItemStack(ModItems.COOKED_RICE.get()));
        scene.idle(40);
    }
}
