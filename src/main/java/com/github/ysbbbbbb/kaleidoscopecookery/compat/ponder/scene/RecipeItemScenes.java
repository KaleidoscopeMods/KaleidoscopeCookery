package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.scene;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StockpotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StoveBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
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

public class RecipeItemScenes {
    public static void introduction(SceneBuilder scene, SceneBuildingUtil util){
        scene.title("recipe_item", "使用菜谱");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        BlockPos litPos = util.grid().at(2,1,2);
        Selection litSel = util.select().position(litPos);
        BlockPos potPos = util.grid().at(2,2,2);
        Selection potSel = util.select().position(potPos);
        Level level = Minecraft.getInstance().level;
        LivingEntity pig = new Pig(EntityType.PIG,level);

        scene.idle(20);
        scene.world().showSection(litSel, Direction.EAST);
        scene.idle(3);
        scene.world().showSection(potSel,Direction.EAST);
        scene.idle(20);

        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(Items.WATER_BUCKET));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, StockpotBlockEntity.class,(e)-> {
            e.addSoupBase(level,pig,new ItemStack(Items.WATER_BUCKET));
            e.refresh();
        });
        scene.idle(28);
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(Items.POTATO));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, StockpotBlockEntity.class,(e)-> {
            for (int i = 0;i < 3;i++)
                e.addIngredient(level,pig,new ItemStack(Items.POTATO));
        });
        scene.idle(28);
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(Items.BEEF));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, StockpotBlockEntity.class,(e)-> {
            for (int i = 0;i < 2;i++)
                e.addIngredient(level,pig,new ItemStack(Items.BEEF));
        });
        scene.idle(33);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(90).text("手持空白的菜谱右键已有物品放入的炒锅或汤锅，会将当前已加入的材料和烹饪结果记录到菜谱中").pointAt(util.vector().blockSurface(potPos,Direction.WEST)).placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 35).rightClick().withItem(new ItemStack(ModItems.RECIPE_ITEM.get()));
        scene.idle(50);
        ItemStack recipe = new ItemStack(ModItems.RECIPE_ITEM.get());
        RecipeItem.setRecipe(recipe, new RecipeItem.RecipeRecord(List.of(new ItemStack(Items.POTATO),new ItemStack(Items.POTATO),new ItemStack(Items.POTATO),new ItemStack(Items.BEEF),new ItemStack(Items.BEEF)),new ItemStack(ModItems.BRAISED_BEEF_WITH_POTATOES.get()),RecipeItem.STOCKPOT));
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 35).withItem(recipe);
        scene.idle(50);

        scene.addKeyframe();
        scene.idle(20);
        scene.world().hideSection(litSel, Direction.EAST);
        scene.idle(3);
        scene.world().hideSection(potSel, Direction.EAST);
        scene.idle(20);
        scene.world().setBlock(litPos, ModBlocks.STOVE.get().defaultBlockState().setValue(StoveBlock.FACING,Direction.NORTH).setValue(StoveBlock.LIT,true),false);
        scene.world().setBlock(potPos, ModBlocks.STOCKPOT.get().defaultBlockState().setValue(StockpotBlock.FACING,Direction.NORTH).setValue(StockpotBlock.HAS_LID,false),false);
        scene.world().modifyBlockEntity(potPos, StockpotBlockEntity.class,(e)-> {
            e.addSoupBase(level,pig,new ItemStack(Items.WATER_BUCKET));
            e.getInputs().clear();
            e.setChanged();
            e.refresh();
        });
        scene.world().showSection(litSel, Direction.EAST);
        scene.idle(3);
        scene.world().showSection(potSel, Direction.EAST);
        scene.idle(20);

        scene.overlay().showText(60).text("手持已记录食物配方的菜谱右键炒锅或汤锅，会将背包中携带有的记录在菜谱中的材料自动加入锅中").pointAt(util.vector().blockSurface(potPos,Direction.WEST)).placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 35).rightClick().withItem(recipe);
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, StockpotBlockEntity.class,(e)-> {
            for (int i = 0;i < 3;i++)
                e.addIngredient(level,pig,new ItemStack(Items.POTATO));
            for (int i = 0;i < 2;i++)
                e.addIngredient(level,pig,new ItemStack(Items.BEEF));
        });
        scene.idle(68);
    }
}
