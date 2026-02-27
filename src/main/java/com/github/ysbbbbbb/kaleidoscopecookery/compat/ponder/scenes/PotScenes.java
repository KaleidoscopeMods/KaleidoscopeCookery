package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.scenes;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StoveBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class PotScenes {
    public static void introduction(SceneBuilder scene, SceneBuildingUtil util) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        VectorUtil vector = util.vector();
        SelectionUtil select = util.select();
        PositionUtil grid = util.grid();

        scene.title("pot", "");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        BlockPos litPos = grid.at(2, 1, 2);
        Selection litSel = select.position(litPos);
        BlockPos potPos = grid.at(2, 2, 2);
        Selection potSel = select.position(potPos);
        LivingEntity pig = new Pig(EntityType.PIG, level);

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
        scene.world().modifyBlock(litPos, (s) ->
                s.setValue(StoveBlock.FACING, Direction.NORTH)
                .setValue(StoveBlock.LIT, true), false);
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(potPos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 35)
                .rightClick()
                .withItem(new ItemStack(ModItems.OIL.get()));
        scene.idle(7);
        scene.world().modifyBlock(potPos, (s) ->
                s.setValue(PotBlock.HAS_OIL, true)
                .setValue(PotBlock.SHOW_OIL, true), false);
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(52).text("")
                .pointAt(vector.blockSurface(potPos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(ModItems.RED_CHILI.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class, (e) -> {
            for (int i = 0; i < 3; i++) {
                e.addIngredient(level, pig, new ItemStack(ModItems.RED_CHILI.get()));
            }
        });
        scene.idle(28);
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(Items.CHICKEN));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class, (e) -> {
            for (int i = 0; i < 4; i++) {
                e.addIngredient(level, pig, new ItemStack(Items.CHICKEN));
            }
        });
        scene.idle(33);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(79).text("")
                .pointAt(vector.blockSurface(potPos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(ModItems.KITCHEN_SHOVEL.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class, (e) -> {
            e.seed = System.currentTimeMillis();
            e.refresh();
        });
        scene.idle(20);
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 20)
                .rightClick().withItem(new ItemStack(ModItems.KITCHEN_SHOVEL.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class, (e) -> {
            e.seed = System.currentTimeMillis();
            e.refresh();
        });
        scene.idle(20);
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 20)
                .rightClick().withItem(new ItemStack(ModItems.KITCHEN_SHOVEL.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class, (e) -> {
            e.seed = System.currentTimeMillis();
            e.refresh();
        });
        scene.idle(20);
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 20)
                .rightClick().withItem(new ItemStack(ModItems.KITCHEN_SHOVEL.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class, (e) -> {
            e.seed = System.currentTimeMillis();
            e.refresh();
        });
        scene.idle(20);
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 20)
                .rightClick().withItem(new ItemStack(ModItems.KITCHEN_SHOVEL.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class, (e) -> {
            e.seed = System.currentTimeMillis();
            e.refresh();
        });
        scene.idle(33);

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
                .rightClick()
                .withItem(new ItemStack(Items.BOWL));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class, PotBlockEntity::reset);
        scene.world().modifyBlock(potPos, (s) ->
                s.setValue(PotBlock.HAS_OIL, false)
                .setValue(PotBlock.SHOW_OIL, false), false);
        scene.idle(43);
        scene.overlay().showControls(vector.blockSurface(potPos, Direction.UP), Pointing.DOWN, 35)
                .withItem(new ItemStack(BuiltInRegistries.ITEM.get(
                        new ResourceLocation(KaleidoscopeCookery.MOD_ID, "spicy_chicken"))
                ));
        scene.idle(40);
    }
}
