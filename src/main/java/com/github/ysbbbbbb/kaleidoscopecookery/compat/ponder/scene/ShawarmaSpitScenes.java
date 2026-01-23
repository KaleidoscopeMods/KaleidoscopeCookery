package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.scene;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ShawarmaSpitBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ShawarmaSpitBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.RedstoneSide;

public class ShawarmaSpitScenes {
    public static void introduction(SceneBuilder scene, SceneBuildingUtil util){
        scene.title("shawarma_spit", "使用旋风烤肉塔");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        BlockPos spitPos = util.grid().at(2,1,2);
        Selection spitSel = util.select().position(spitPos);
        Selection spitSel1 = util.select().position(spitPos.above());
        Selection selection = util.select().fromTo(2,1,2,2,2,2);
        BlockPos redPos = util.grid().at(1,1,2);
        Selection redSel = util.select().position(redPos);
        BlockPos triggerPos = util.grid().at(0,1,2);
        Selection triggerSel = util.select().position(triggerPos);
        Level level = Minecraft.getInstance().level;
        scene.world().setBlock(redPos, Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.EAST, RedstoneSide.SIDE).setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE).setValue(RedStoneWireBlock.POWER,0),false);
        scene.world().setBlock(triggerPos,Blocks.LEVER.defaultBlockState().setValue(LeverBlock.FACING, Direction.EAST).setValue(LeverBlock.POWERED,false).setValue(LeverBlock.FACE, AttachFace.FLOOR),false);

        scene.idle(20);
        scene.world().showSection(spitSel, Direction.DOWN);
        scene.world().showSection(spitSel1, Direction.DOWN);
        scene.idle(20);

        scene.overlay().showText(60).text("首先将食材放入旋风烤肉塔的上下两层").pointAt(util.vector().blockSurface(spitPos,Direction.WEST)).placeNearTarget();
        scene.overlay().showText(60).text("每层最多能够放入 8 个相同的可烧炼食物").placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(spitPos,Direction.EAST), Pointing.RIGHT, 20).rightClick().withItem(new ItemStack(Items.BEEF));
        scene.idle(7);
        scene.world().modifyBlockEntity(spitPos, ShawarmaSpitBlockEntity.class, (e)->{
            e.onPutCookingItem(level,new ItemStack(Items.BEEF,8));
        });
        scene.idle(28);
        scene.overlay().showControls(util.vector().blockSurface(spitPos.above(),Direction.EAST), Pointing.RIGHT, 20).rightClick().withItem(new ItemStack(Items.BEEF));
        scene.idle(7);
        scene.world().modifyBlockEntity(spitPos.above(), ShawarmaSpitBlockEntity.class, (e)->{
            e.onPutCookingItem(level,new ItemStack(Items.BEEF,8));
        });
        scene.idle(33);

        scene.addKeyframe();
        scene.idle(20);
        scene.world().showSection(redSel, Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(triggerSel, Direction.DOWN);
        scene.idle(20);

        scene.overlay().showText(40).text("通入红石信号以激活烤肉塔，开始烹饪").placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(triggerPos,Direction.UP), Pointing.DOWN, 35).rightClick();
        scene.idle(7);
        scene.world().modifyBlock(triggerPos, (s)-> Blocks.LEVER.defaultBlockState().setValue(LeverBlock.FACING,Direction.EAST).setValue(LeverBlock.POWERED,true).setValue(LeverBlock.FACE, AttachFace.FLOOR),false);
        scene.world().modifyBlock(redPos, (s)-> Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.EAST, RedstoneSide.SIDE).setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE).setValue(RedStoneWireBlock.POWER,15),false);
        scene.world().modifyBlock(spitPos, (s)-> ModBlocks.SHAWARMA_SPIT.get().defaultBlockState().setValue(ShawarmaSpitBlock.FACING,Direction.NORTH).setValue(ShawarmaSpitBlock.HALF, DoubleBlockHalf.LOWER).setValue(ShawarmaSpitBlock.POWERED,true),false);
        scene.world().modifyBlock(spitPos.above(), (s)-> ModBlocks.SHAWARMA_SPIT.get().defaultBlockState().setValue(ShawarmaSpitBlock.FACING,Direction.NORTH).setValue(ShawarmaSpitBlock.HALF, DoubleBlockHalf.UPPER).setValue(ShawarmaSpitBlock.POWERED,true),false);
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("接下来只需等待一段时间...").placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(2,3,2),Direction.UP), Pointing.DOWN, 35).withItem(new ItemStack(Items.CLOCK));
        scene.idle(55);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("...烹饪就完成啦！").pointAt(util.vector().blockSurface(spitPos,Direction.WEST)).placeNearTarget();
        scene.idle(20);
        scene.world().modifyBlockEntity(spitPos, ShawarmaSpitBlockEntity.class, (e)->{
            e.cookingItem = ItemStack.EMPTY;
            e.cookedItem = new ItemStack(Items.COOKED_BEEF,8);
            e.refresh();
        });scene.world().modifyBlockEntity(spitPos.above(), ShawarmaSpitBlockEntity.class, (e)->{
            e.cookingItem = ItemStack.EMPTY;
            e.cookedItem = new ItemStack(Items.COOKED_BEEF,8);
            e.refresh();
        });
        scene.idle(35);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showOutline(PonderPalette.RED,selection,selection,65);
        scene.overlay().showText(60).text("然而，直接从激活的旋风烤肉塔上空手取下食物会被烧伤").pointAt(util.vector().blockSurface(spitPos,Direction.WEST)).colored(PonderPalette.RED).placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(spitPos,Direction.EAST), Pointing.RIGHT, 20).rightClick();
        scene.idle(35);
        scene.overlay().showControls(util.vector().blockSurface(spitPos,Direction.EAST), Pointing.RIGHT, 20).withItem(new ItemStack(ModItems.RED_CHILI.get()));
        scene.idle(35);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(60).text("正确的做法是使用菜刀取下食物...").pointAt(util.vector().blockSurface(spitPos,Direction.WEST)).placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(spitPos,Direction.EAST), Pointing.RIGHT, 20).rightClick().withItem(new ItemStack(ModItems.IRON_KITCHEN_KNIFE.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(spitPos, ShawarmaSpitBlockEntity.class, (e)->{
            e.cookedItem = ItemStack.EMPTY;
            e.refresh();
        });
        scene.idle(28);
        scene.overlay().showControls(util.vector().blockSurface(spitPos,Direction.EAST), Pointing.RIGHT, 20).withItem(new ItemStack(Items.COOKED_BEEF));
        scene.idle(40);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(60).text("...或是先关闭激活烤肉塔的红石信号再尝试取下食物").placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(triggerPos,Direction.UP), Pointing.DOWN, 20).rightClick();
        scene.idle(7);
        scene.world().modifyBlock(triggerPos, (s)-> Blocks.LEVER.defaultBlockState().setValue(LeverBlock.FACING,Direction.EAST).setValue(LeverBlock.POWERED,false).setValue(LeverBlock.FACE, AttachFace.FLOOR),false);
        scene.world().modifyBlock(redPos, (s)-> Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.EAST, RedstoneSide.SIDE).setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE).setValue(RedStoneWireBlock.POWER,0),false);
        scene.world().modifyBlock(spitPos, (s)-> ModBlocks.SHAWARMA_SPIT.get().defaultBlockState().setValue(ShawarmaSpitBlock.FACING,Direction.NORTH).setValue(ShawarmaSpitBlock.HALF, DoubleBlockHalf.LOWER).setValue(ShawarmaSpitBlock.POWERED,false),false);
        scene.world().modifyBlock(spitPos.above(), (s)-> ModBlocks.SHAWARMA_SPIT.get().defaultBlockState().setValue(ShawarmaSpitBlock.FACING,Direction.NORTH).setValue(ShawarmaSpitBlock.HALF, DoubleBlockHalf.UPPER).setValue(ShawarmaSpitBlock.POWERED,false),false);
        scene.idle(28);
        scene.overlay().showControls(util.vector().blockSurface(spitPos.above(),Direction.EAST), Pointing.RIGHT, 20).rightClick();
        scene.idle(7);
        scene.world().modifyBlockEntity(spitPos.above(), ShawarmaSpitBlockEntity.class, (e)->{
            e.cookedItem = ItemStack.EMPTY;
            e.refresh();
        });
        scene.idle(28);
        scene.overlay().showControls(util.vector().blockSurface(spitPos.above(),Direction.EAST), Pointing.RIGHT, 20).withItem(new ItemStack(Items.COOKED_BEEF));
        scene.idle(40);
    }
}
