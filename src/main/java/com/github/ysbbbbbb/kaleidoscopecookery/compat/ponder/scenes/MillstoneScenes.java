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
            e.setDeltaMovement(0, -0.2f, 0);
            Donkey d = (Donkey) e;
            d.setTamed(true);
            d.setOwnerUUID(UUID.randomUUID());
            d.equipSaddle(null);
        });
        for (int i = 0; i < 15; i++) {
            scene.world().modifyEntity(donkey, (e) -> e.move(MoverType.SELF, e.getDeltaMovement()));
            scene.idle(1);
        }

        for (int i = 0; i < 200; i++) {
            if (i == 20) {
                scene.overlay().showText(130).text("")
                        .placeNearTarget();
            }
            if (i == 170) {
                scene.overlay().showText(50).text("")
                        .placeNearTarget();
            }

            scene.world().modifyEntity(donkey, (e) -> {
                float rot = getCacheRot();
                Vec3 center = millstonePos.getCenter();
                center = new Vec3(center.x, center.y - 0.5f, center.z);
                Vec3 pos = new Vec3(0.0F, 0.0F, 2.0F)
                        .yRot(rot * ((float) Math.PI / 180F))
                        .add(center);
                e.moveTo(pos.x, pos.y, pos.z);
                LivingEntity living = (LivingEntity) e;
                living.setYBodyRot(-rot - 90);
                living.setYHeadRot(-rot - 90);
            });
            scene.world().modifyBlockEntity(millstonePos, MillstoneBlockEntity.class, (e) -> {
                e.setCacheRot(cacheRot);
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
        scene.idle(20);
        scene.idle(20);
    }

    public static float getCacheRot() {
        cacheRot += 1.8f;
        return (cacheRot % 360);
    }

    public static float cacheRot;
}
