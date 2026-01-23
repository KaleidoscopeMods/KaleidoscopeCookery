package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.scene;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MillstoneScenes {
    public static void introduction(SceneBuilder scene, SceneBuildingUtil util){
        scene.title("millstone", "使用石磨");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        BlockPos millstonePos = util.grid().at(2,1,2);
        Selection millstoneSel = util.select().position(millstonePos);
        Level level = Minecraft.getInstance().level;
        scene.world().modifyBlock(millstonePos,(s)-> ModBlocks.MILLSTONE.get().defaultBlockState().setValue(MillstoneBlock.FACING, Direction.WEST),false);

        scene.idle(20);
        scene.world().showSection(millstoneSel, Direction.DOWN);
        scene.idle(20);

        scene.overlay().showText(40).text("手持待加工的物品右键磨盘可以将其放入石磨").pointAt(util.vector().blockSurface(millstonePos,Direction.WEST)).placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(millstonePos,Direction.UP), Pointing.DOWN, 35).rightClick().withItem(new ItemStack(Items.FLINT));
        scene.idle(7);
        scene.world().modifyBlockEntity(millstonePos, MillstoneBlockEntity.class, (e)->{
            e.onPutItem(level,new ItemStack(Items.FLINT,4));
        });
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("磨盘上方的掉落物也会被自动置入石磨中").pointAt(util.vector().blockSurface(millstonePos,Direction.WEST)).placeNearTarget();
        ElementLink<EntityElement> itemDrop = scene.world().createItemEntity(util.vector().topOf(2,3,2),new Vec3(0,0,0),new ItemStack(Items.FLINT));
        scene.idle(10);
        scene.world().modifyEntity(itemDrop,(e)->{
            ((ItemEntity)e).discard();
        });
        scene.world().modifyBlockEntity(millstonePos, MillstoneBlockEntity.class, (e)->{
            e.resetWhenTakeout();
            e.onPutItem(level,new ItemStack(Items.FLINT,8));
        });
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("石磨会自动绑定周围的生物...").pointAt(util.vector().blockSurface(millstonePos,Direction.WEST)).placeNearTarget();
        scene.idle(25);
        ElementLink<EntityElement> donkey = scene.world().createEntity((lvl)-> new Donkey(EntityType.DONKEY,lvl));
        scene.world().modifyEntity(donkey,(e)->{
            e.setPos(util.grid().at(2,5,2).getCenter());
            e.setDeltaMovement(0,-0.2f,0);
            ((Donkey)e).setTamed(true);
            ((Donkey)e).setOwnerUUID(Minecraft.getInstance().player.getUUID());
            ((Donkey)e).equipSaddle(new ItemStack(Items.SADDLE),null);
        });
        for (int i = 0;i < 15;i++) {
            scene.world().modifyEntity(donkey, (e) -> {
                e.move(MoverType.SELF, e.getDeltaMovement());
            });
            scene.idle(1);
        }

        for (int i = 0;i < 200;i++){
            if (i == 20){
                scene.overlay().showText(130).text("绑定了生物后，石磨开始工作...").placeNearTarget();
            }
            if (i == 170){
                scene.overlay().showText(50).text("...走过一圈后，材料转化为成品，从开口喷出").placeNearTarget();
            }

            scene.world().modifyEntity(donkey,(e)->{
                float rot = getCacheRot();
                Vec3 center = millstonePos.getBottomCenter();
                Vec3 pos = (new Vec3((double)0.0F, (double)0.0F, (double)2.0F)).yRot(rot * ((float)Math.PI / 180F)).add(center);
                e.moveTo(pos.x,pos.y,pos.z);
                ((LivingEntity)e).setYBodyRot(-rot-90);
                ((LivingEntity)e).setYHeadRot(-rot-90);
            });
            scene.world().modifyBlockEntity(millstonePos, MillstoneBlockEntity.class, (e)->{
                e.setCacheRot(cacheRot);
            });
            scene.idle(1);
        }

        scene.world().modifyBlockEntity(millstonePos, MillstoneBlockEntity.class, (e)->{
            e.getInput().copyAndClear();
            e.resetWhenTakeout();
            e.setChanged();
            e.refresh();
        });
        scene.world().createItemEntity(util.vector().blockSurface(millstonePos.west(),Direction.UP),new Vec3(-0.1f,0,0),new ItemStack(Items.GUNPOWDER));
        scene.world().createItemEntity(util.vector().blockSurface(millstonePos.west(),Direction.UP),new Vec3(-0.1f,0,0.05f),new ItemStack(Items.GUNPOWDER));
        scene.world().createItemEntity(util.vector().blockSurface(millstonePos.west(),Direction.UP),new Vec3(-0.1f,0,-0.05f),new ItemStack(Items.GUNPOWDER));
        scene.idle(20);
        scene.idle(20);
    }

    public static float getCacheRot(){
        cacheRot += 1.8f;
        return (cacheRot % 360);
    }

    public static float cacheRot;
}
