package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.scenes;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class MillstoneScenes {
    public static void introduction(SceneBuilder scene, SceneBuildingUtil util) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        VectorUtil vector = util.vector();
        SelectionUtil select = util.select();
        PositionUtil grid = util.grid();

        scene.title("millstone", "");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        BlockPos millstonePos = grid.at(2, 1, 2);
        Selection millstoneSel = select.position(millstonePos);
        scene.world().modifyBlock(millstonePos, (s) -> ModBlocks.MILLSTONE.get().defaultBlockState()
                .setValue(MillstoneBlock.FACING, Direction.WEST), false);

        scene.idle(20);
        scene.world().showSection(millstoneSel, Direction.DOWN);
        scene.idle(20);

        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(millstonePos, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showControls(vector.blockSurface(millstonePos, Direction.UP), Pointing.DOWN, 35)
                .rightClick()
                .withItem(new ItemStack(Items.FLINT));
        scene.idle(7);
        scene.world().modifyBlockEntity(millstonePos, MillstoneBlockEntity.class, (e) ->
                e.onPutItem(level, new ItemStack(Items.FLINT, 4)));
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(millstonePos, Direction.WEST))
                .placeNearTarget();
        ElementLink<EntityElement> itemDrop = scene.world().createItemEntity(
                vector.topOf(2, 3, 2), new Vec3(0, 0, 0), new ItemStack(Items.FLINT));
        scene.idle(10);
        scene.world().modifyEntity(itemDrop, Entity::discard);
        scene.world().modifyBlockEntity(millstonePos, MillstoneBlockEntity.class, (e) -> {
            e.resetWhenTakeout();
            e.onPutItem(level, new ItemStack(Items.FLINT, 8));
        });
        scene.idle(48);

        scene.addKeyframe();
        scene.idle(20);
        scene.overlay().showText(40).text("")
                .pointAt(vector.blockSurface(millstonePos, Direction.WEST))
                .placeNearTarget();
        scene.idle(25);
        ElementLink<EntityElement> donkey = scene.world().createEntity(
                (lvl) -> new Donkey(EntityType.DONKEY, lvl));
        scene.world().modifyEntity(donkey, (e) -> {
            e.setPos(grid.at(2, 5, 2).getCenter());
        });
        for (int i = 0;i < 10;i++) {
            int j = i;
            scene.world().modifyEntity(donkey, (e) -> {
                e.setDeltaMovement(0, fallingSpeed(j,0.08D,0.98D), 0);
                e.move(MoverType.SELF, e.getDeltaMovement());
            });
            scene.idle(1);
        }

        AtomicReference<Vec3> pre = new AtomicReference<>();

        scene.world().modifyEntity(donkey,(e)->{
            Vec3 pos = (new Vec3(0.0F, 0.0F, 2.5F))
                    .yRot(0)
                    .add(millstonePos.getCenter().relative(Direction.DOWN, 0.5f));
            e.moveTo(pos.x, pos.y, pos.z);
            (e).setYBodyRot(-90);
            (e).setYHeadRot(-90);
            pre.set(pos);
        });

        for (int i = 0; i < 200; i++) {
            if (i == 20) {
                scene.overlay().showText(130).text("")
                        .placeNearTarget();
            }
            if (i == 170) {
                scene.overlay().showText(50).text("")
                        .placeNearTarget();
            }

            int j = i;
            scene.world().modifyEntity(donkey,(e)->{
                float rot = (j * 1.8f) % 360;
                Vec3 pos = (new Vec3(0.0F, 0.0F, 2.5F))
                        .yRot(rot * ((float)Math.PI / 180F))
                        .add(millstonePos.getCenter().relative(Direction.DOWN, 0.5f));
                Vec3 vec = new Vec3(pos.x() - pre.get().x(),
                        0,
                        pos.z() - pre.get().z());
                e.setDeltaMovement(vec);
                e.move(MoverType.SELF, e.getDeltaMovement());
                (e).setYBodyRot(-rot - 90);
                (e).setYHeadRot(-rot - 90);
                pre.set(pos);
            });
            scene.world().modifyBlockEntity(millstonePos, MillstoneBlockEntity.class, (e)->{
                e.setCacheRot((j * 1.8f) % 360);
            });
            scene.idle(1);
        }

        scene.world().modifyBlockEntity(millstonePos, MillstoneBlockEntity.class, (e) -> {
            e.getInput().copyAndClear();
            e.resetWhenTakeout();
            e.setChanged();
            e.refresh();
        });
        scene.world().createItemEntity(
                vector.blockSurface(millstonePos.west(), Direction.UP),
                new Vec3(-0.1f, 0, 0), new ItemStack(Items.GUNPOWDER));
        scene.world().createItemEntity(
                vector.blockSurface(millstonePos.west(), Direction.UP),
                new Vec3(-0.1f, 0, 0.05f), new ItemStack(Items.GUNPOWDER));
        scene.world().createItemEntity(
                vector.blockSurface(millstonePos.west(), Direction.UP),
                new Vec3(-0.1f, 0, -0.05f), new ItemStack(Items.GUNPOWDER));
        for (int i = 0;i < 40;i++) {
            scene.world().modifyEntity(donkey,(e)->{
                (e).setYBodyRot(-90);
                (e).setYHeadRot(-90);
            });
            scene.idle(1);
        }
        scene.world().modifyEntity(donkey,(e)->{
            e.setInvisible(true);
        });
    }

    private static double fallingSpeed(int tick, double gravity, double drag) {
        return (Math.pow(drag, tick) - 1) * (gravity / (1 - drag));
    }
}
