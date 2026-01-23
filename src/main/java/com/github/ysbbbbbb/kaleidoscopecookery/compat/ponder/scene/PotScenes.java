package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.scene;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StoveBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
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
import net.minecraft.world.level.block.state.BlockState;

public class PotScenes {
    public static void introduction(SceneBuilder scene, SceneBuildingUtil util){
        scene.title("pot", "使用炒锅");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        BlockPos litPos = util.grid().at(2,1,2);
        Selection litSel = util.select().position(litPos);
        BlockPos potPos = util.grid().at(2,2,2);
        Selection potSel = util.select().position(potPos);
        Level level = Minecraft.getInstance().level;
        LivingEntity pig = new Pig(EntityType.PIG,level);

        scene.idle(20);
        scene.world().showSection(litSel, Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(potSel,Direction.DOWN);
        scene.idle(20);

        scene.overlay().showText(40).text("想要使用炒锅烹饪，需要将炒锅置于有效热源之上").pointAt(util.vector().blockSurface(litPos,Direction.WEST)).placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(litPos,Direction.NORTH), Pointing.RIGHT, 35).rightClick().withItem(new ItemStack(Items.FLINT_AND_STEEL));
        scene.idle(7);
        scene.world().modifyBlock(litPos,(s)-> (BlockState) ModBlocks.STOVE.get().defaultBlockState().setValue(StoveBlock.FACING,Direction.NORTH).setValue(StoveBlock.LIT,true),false);
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("首先，起锅烧油！").pointAt(util.vector().blockSurface(potPos,Direction.WEST)).placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 35).rightClick().withItem(new ItemStack(ModItems.OIL.get()));
        scene.idle(7);
        scene.world().modifyBlock(potPos,(s)-> ModBlocks.POT.get().defaultBlockState().setValue(PotBlock.HAS_OIL,true).setValue(PotBlock.SHOW_OIL,true).setValue(PotBlock.FACING,Direction.NORTH),false);
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(52).text("接下来，添加烹饪所需的食材").pointAt(util.vector().blockSurface(potPos,Direction.WEST)).placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(ModItems.RED_CHILI.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class,(e)-> {
            for (int i = 0;i < 3;i++)
                e.addIngredient(level,pig,new ItemStack(ModItems.RED_CHILI.get()));
            //e.addAllIngredients(List.of(new ItemStack(ModItems.RED_CHILI.get()),new ItemStack(ModItems.RED_CHILI.get()),new ItemStack(ModItems.RED_CHILI.get())),pig);
        });
        scene.idle(28);
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(Items.CHICKEN));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class,(e)-> {
            for (int i = 0;i < 4;i++)
                e.addIngredient(level,pig,new ItemStack(Items.CHICKEN));
            //e.addAllIngredients(List.of(new ItemStack(Items.CHICKEN),new ItemStack(Items.CHICKEN),new ItemStack(Items.CHICKEN),new ItemStack(Items.CHICKEN)),pig);
        });
        scene.idle(33);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(79).text("确保食谱无误后，开始翻炒！").pointAt(util.vector().blockSurface(potPos,Direction.WEST)).placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(ModItems.KITCHEN_SHOVEL.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class,(e)-> {
            e.seed = System.currentTimeMillis();
            e.refresh();
        });
        scene.idle(20);
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(ModItems.KITCHEN_SHOVEL.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class,(e)-> {
            e.seed = System.currentTimeMillis();
            e.refresh();
        });
        scene.idle(20);
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(ModItems.KITCHEN_SHOVEL.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class,(e)-> {
            e.seed = System.currentTimeMillis();
            e.refresh();
        });
        scene.idle(20);
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(ModItems.KITCHEN_SHOVEL.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class,(e)-> {
            e.seed = System.currentTimeMillis();
            e.refresh();
        });
        scene.idle(20);
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(ModItems.KITCHEN_SHOVEL.get()));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class,(e)-> {
            e.seed = System.currentTimeMillis();
            e.refresh();
        });
        scene.idle(33);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("接下来只需等待片刻...").placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(2,3,2),Direction.UP), Pointing.DOWN, 35).withItem(new ItemStack(Items.CLOCK));
        scene.idle(55);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("...料理就完成啦！").pointAt(util.vector().blockSurface(potPos,Direction.WEST)).placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 35).rightClick().withItem(new ItemStack(Items.BOWL));
        scene.idle(7);
        scene.world().modifyBlockEntity(potPos, PotBlockEntity.class, PotBlockEntity::reset);
        scene.world().modifyBlock(potPos, (s)-> ModBlocks.POT.get().defaultBlockState().setValue(PotBlock.FACING,Direction.NORTH).setValue(PotBlock.HAS_OIL,false).setValue(PotBlock.SHOW_OIL,false),false);
        scene.idle(43);
        scene.overlay().showControls(util.vector().blockSurface(potPos,Direction.UP), Pointing.DOWN, 35).withItem(new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID,"spicy_chicken"))));
        scene.idle(40);
    }
}
