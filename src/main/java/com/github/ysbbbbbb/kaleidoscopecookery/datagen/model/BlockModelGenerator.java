package com.github.ysbbbbbb.kaleidoscopecookery.datagen.model;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.stream.IntStream;

public class BlockModelGenerator extends BlockModelProvider {
    public BlockModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, KaleidoscopeCookery.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ResourceLocation stoveSide = modLoc("block/stove_side");
        ResourceLocation stoveTop = modLoc("block/stove_top");
        ResourceLocation stoveTopLit = modLoc("block/stove_top_lit");

        cube("stove", stoveTop, stoveTop, modLoc("block/stove_front"), stoveSide, stoveSide, stoveSide).texture("particle", stoveSide);
        cube("stove_lit", stoveTopLit, stoveTopLit, modLoc("block/stove_front_lit"), stoveSide, stoveSide, stoveSide).texture("particle", stoveSide);

        cross("block/chili_ristra/head", modLoc("block/chili_ristra/head")).renderType("cutout");
        cross("block/chili_ristra/body", modLoc("block/chili_ristra/body")).renderType("cutout");
        cross("block/chili_ristra/head_sheared", modLoc("block/chili_ristra/head_sheared")).renderType("cutout");
        cross("block/chili_ristra/body_sheared", modLoc("block/chili_ristra/body_sheared")).renderType("cutout");

        cross("block/strung_mushrooms/head", modLoc("block/strung_mushrooms/head")).renderType("cutout");
        cross("block/strung_mushrooms/body", modLoc("block/strung_mushrooms/body")).renderType("cutout");
        cross("block/strung_mushrooms/head_sheared", modLoc("block/strung_mushrooms/head_sheared")).renderType("cutout");
        cross("block/strung_mushrooms/body_sheared", modLoc("block/strung_mushrooms/body_sheared")).renderType("cutout");

        cubeColumn("straw_block", modLoc("block/straw_block_side"), modLoc("block/straw_block_end"));

        wood("oak");
        wood("spruce");
        wood("acacia");
        wood("bamboo");
        wood("birch");
        wood("cherry");
        wood("crimson");
        wood("dark_oak");
        wood("jungle");
        wood("mangrove");
        wood("warped");

        crop("tomato", 8);
        crop("chili", 8);
        crop("lettuce", 8);

        riceCrop();
        carpet();
    }

    public void wood(String name) {
        String cookStool = "block/cook_stool/" + name;
        withExistingParent(cookStool, modLoc("block/cook_stool/cook_stool")).texture("particle", modLoc(cookStool));

        String chair = "block/chair/" + name;
        withExistingParent(chair, modLoc("block/chair/chair")).texture("particle", modLoc(chair));

        String tableSingle = "block/table/" + name + "_single";
        ResourceLocation tableTexture = modLoc("block/table/" + name);
        withExistingParent(tableSingle, modLoc("block/table/single")).texture("particle", tableTexture);

        String tableLeft = "block/table/" + name + "_left";
        withExistingParent(tableLeft, modLoc("block/table/left")).texture("particle", tableTexture);
        String tableRight = "block/table/" + name + "_right";
        withExistingParent(tableRight, modLoc("block/table/right")).texture("particle", tableTexture);
        String tableMiddle = "block/table/" + name + "_middle";
        withExistingParent(tableMiddle, modLoc("block/table/middle")).texture("particle", tableTexture);

        String tableLeftRot = "block/table/" + name + "_left_rot";
        withExistingParent(tableLeftRot, modLoc("block/table/left_rot")).texture("particle", tableTexture);
        String tableRightRot = "block/table/" + name + "_right_rot";
        withExistingParent(tableRightRot, modLoc("block/table/right_rot")).texture("particle", tableTexture);
        String tableMiddleRot = "block/table/" + name + "_middle_rot";
        withExistingParent(tableMiddleRot, modLoc("block/table/middle_rot")).texture("particle", tableTexture);
    }

    public void carpet() {
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();

            String chairCarpet = "block/carpet/chair/" + name;
            withExistingParent(chairCarpet, modLoc("block/carpet/chair/chair_carpet")).texture("particle", modLoc(chairCarpet));

            ResourceLocation tableCarpetTexture = modLoc("block/carpet/table/" + name);
            String tableCarpetSingle = "block/carpet/table/" + name + "_single";
            withExistingParent(tableCarpetSingle, modLoc("block/carpet/table/single")).texture("particle", tableCarpetTexture);
            String tableCarpetLeft = "block/carpet/table/" + name + "_left";
            withExistingParent(tableCarpetLeft, modLoc("block/carpet/table/left")).texture("particle", tableCarpetTexture);
            String tableCarpetRight = "block/carpet/table/" + name + "_right";
            withExistingParent(tableCarpetRight, modLoc("block/carpet/table/right")).texture("particle", tableCarpetTexture);
            String tableCarpetMiddle = "block/carpet/table/" + name + "_middle";
            withExistingParent(tableCarpetMiddle, modLoc("block/carpet/table/middle")).texture("particle", tableCarpetTexture);
        }
    }

    public void crop(String name, int stage) {
        IntStream.range(0, stage).forEach(i -> {
            String id = "block/crop/%s/stage%d".formatted(name, i);
            singleTexture(id, modLoc("block/cross"), "cross", modLoc(id))
                    .renderType("cutout");
        });
    }

    public void riceCrop() {
        IntStream.range(0, 8).forEach(i -> {
            String down = "block/crop/rice/stage%d_down".formatted(i);
            String middle = "block/crop/rice/stage%d_middle".formatted(i);
            String up = "block/crop/rice/stage%d_up".formatted(i);
            cross(down, modLoc(down)).renderType("cutout");
            cross(middle, modLoc(middle)).renderType("cutout");
            cross(up, modLoc(up)).renderType("cutout");
        });
    }
}
