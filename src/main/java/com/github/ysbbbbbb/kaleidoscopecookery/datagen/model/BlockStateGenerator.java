package com.github.ysbbbbbb.kaleidoscopecookery.datagen.model;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.TableBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.*;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.ChiliRistraBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;

public class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, KaleidoscopeCookery.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        horizontalBlock(ModBlocks.STOVE.get(), blockState -> {
            if (blockState.getValue(StoveBlock.LIT)) {
                return new ModelFile.UncheckedModelFile(modLoc("block/stove_lit"));
            } else {
                return new ModelFile.UncheckedModelFile(modLoc("block/stove"));
            }
        });

        horizontalBlock(ModBlocks.POT.get(), blockState -> {
            if (blockState.getValue(PotBlock.HAS_OIL) && blockState.getValue(PotBlock.SHOW_OIL)) {
                if (blockState.getValue(PotBlock.HAS_BASE)) {
                    return new ModelFile.UncheckedModelFile(modLoc("block/pot_base_has_oil"));
                }
                return new ModelFile.UncheckedModelFile(modLoc("block/pot_has_oil"));
            } else {
                if (blockState.getValue(PotBlock.HAS_BASE)) {
                    return new ModelFile.UncheckedModelFile(modLoc("block/pot_base"));
                }
                return new ModelFile.UncheckedModelFile(modLoc("block/pot"));
            }
        });

        horizontalBlock(ModBlocks.STOCKPOT.get(), blockState -> {
            if (blockState.getValue(StockpotBlock.HAS_LID)) {
                if (blockState.getValue(StockpotBlock.HAS_BASE)) {
                    return new ModelFile.UncheckedModelFile(modLoc("block/stockpot_base_has_lid"));
                }
                if (blockState.getValue(StockpotBlock.HAS_CHAINS)) {
                    return new ModelFile.UncheckedModelFile(modLoc("block/stockpot_chains_has_lid"));
                }
                return new ModelFile.UncheckedModelFile(modLoc("block/stockpot_has_lid"));
            } else {
                if (blockState.getValue(StockpotBlock.HAS_BASE)) {
                    return new ModelFile.UncheckedModelFile(modLoc("block/stockpot_base"));
                }
                if (blockState.getValue(StockpotBlock.HAS_CHAINS)) {
                    return new ModelFile.UncheckedModelFile(modLoc("block/stockpot_chains"));
                }
                return new ModelFile.UncheckedModelFile(modLoc("block/stockpot"));
            }
        });

        horizontalBlock(ModBlocks.SHAWARMA_SPIT.get(), blockState -> {
            if (blockState.getValue(ShawarmaSpitBlock.POWERED)) {
                if (blockState.getValue(ShawarmaSpitBlock.HALF) == DoubleBlockHalf.LOWER) {
                    return new ModelFile.UncheckedModelFile(modLoc("block/shawarma_spit_powered_lower"));
                }
                return new ModelFile.UncheckedModelFile(modLoc("block/shawarma_spit_powered_upper"));
            } else {
                if (blockState.getValue(ShawarmaSpitBlock.HALF) == DoubleBlockHalf.LOWER) {
                    return new ModelFile.UncheckedModelFile(modLoc("block/shawarma_spit_lower"));
                }
                return new ModelFile.UncheckedModelFile(modLoc("block/shawarma_spit_upper"));
            }
        });

        horizontalBlock(ModBlocks.OIL_POT.get(), blockState -> {
            if (blockState.getValue(OilPotBlock.HAS_OIL)) {
                return new ModelFile.UncheckedModelFile(modLoc("block/oil_pot_with_oil"));
            } else {
                return new ModelFile.UncheckedModelFile(modLoc("block/oil_pot"));
            }
        });

        horizontalBlock(ModBlocks.STEAMER.get(), blockState -> {
            boolean hasLid = blockState.getValue(SteamerBlock.HAS_LID);
            boolean hasBase = blockState.getValue(SteamerBlock.HAS_BASE);
            boolean half = blockState.getValue(SteamerBlock.HALF);

            if (hasLid && hasBase && half) {
                return new ModelFile.UncheckedModelFile(modLoc("block/steamer_half_lid_base"));
            } else if (hasLid && hasBase) {
                return new ModelFile.UncheckedModelFile(modLoc("block/steamer_full_lid_base"));
            } else if (hasLid && half) {
                return new ModelFile.UncheckedModelFile(modLoc("block/steamer_half_lid"));
            } else if (hasLid) {
                return new ModelFile.UncheckedModelFile(modLoc("block/steamer_full_lid"));
            } else if (hasBase && half) {
                return new ModelFile.UncheckedModelFile(modLoc("block/steamer_half_base"));
            } else if (hasBase) {
                return new ModelFile.UncheckedModelFile(modLoc("block/steamer_full_base"));
            } else if (half) {
                return new ModelFile.UncheckedModelFile(modLoc("block/steamer_half"));
            } else {
                return new ModelFile.UncheckedModelFile(modLoc("block/steamer_full"));
            }
        });

        FoodBiteRegistry.FOOD_DATA_MAP.forEach((key, value) -> {
            Block block = BuiltInRegistries.BLOCK.get(key);
            addFoodBiteBlock(block, key);
        });

        horizontalBlock(ModBlocks.FRUIT_BASKET.get(), new ModelFile.UncheckedModelFile(modLoc("block/fruit_basket")));
        horizontalBlock(ModBlocks.CHOPPING_BOARD.get(), new ModelFile.UncheckedModelFile(modLoc("block/chopping_board")));
        horizontalBlock(ModBlocks.KITCHENWARE_RACKS.get(), new ModelFile.UncheckedModelFile(modLoc("block/kitchenware_racks")));

        cookStool(ModBlocks.COOK_STOOL_OAK, "oak");
        cookStool(ModBlocks.COOK_STOOL_SPRUCE, "spruce");
        cookStool(ModBlocks.COOK_STOOL_ACACIA, "acacia");
        cookStool(ModBlocks.COOK_STOOL_BAMBOO, "bamboo");
        cookStool(ModBlocks.COOK_STOOL_BIRCH, "birch");
        cookStool(ModBlocks.COOK_STOOL_CHERRY, "cherry");
        cookStool(ModBlocks.COOK_STOOL_CRIMSON, "crimson");
        cookStool(ModBlocks.COOK_STOOL_DARK_OAK, "dark_oak");
        cookStool(ModBlocks.COOK_STOOL_JUNGLE, "jungle");
        cookStool(ModBlocks.COOK_STOOL_MANGROVE, "mangrove");
        cookStool(ModBlocks.COOK_STOOL_WARPED, "warped");

        chair(ModBlocks.CHAIR_OAK, "oak");
        chair(ModBlocks.CHAIR_SPRUCE, "spruce");
        chair(ModBlocks.CHAIR_ACACIA, "acacia");
        chair(ModBlocks.CHAIR_BAMBOO, "bamboo");
        chair(ModBlocks.CHAIR_BIRCH, "birch");
        chair(ModBlocks.CHAIR_CHERRY, "cherry");
        chair(ModBlocks.CHAIR_CRIMSON, "crimson");
        chair(ModBlocks.CHAIR_DARK_OAK, "dark_oak");
        chair(ModBlocks.CHAIR_JUNGLE, "jungle");
        chair(ModBlocks.CHAIR_MANGROVE, "mangrove");
        chair(ModBlocks.CHAIR_WARPED, "warped");

        table(ModBlocks.TABLE_OAK, "oak");
        table(ModBlocks.TABLE_SPRUCE, "spruce");
        table(ModBlocks.TABLE_ACACIA, "acacia");
        table(ModBlocks.TABLE_BAMBOO, "bamboo");
        table(ModBlocks.TABLE_BIRCH, "birch");
        table(ModBlocks.TABLE_CHERRY, "cherry");
        table(ModBlocks.TABLE_CRIMSON, "crimson");
        table(ModBlocks.TABLE_DARK_OAK, "dark_oak");
        table(ModBlocks.TABLE_JUNGLE, "jungle");
        table(ModBlocks.TABLE_MANGROVE, "mangrove");
        table(ModBlocks.TABLE_WARPED, "warped");

        simpleBlock(ModBlocks.OIL_BLOCK.get());

        crop(ModBlocks.TOMATO_CROP, "tomato");
        crop(ModBlocks.CHILI_CROP, "chili");
        crop(ModBlocks.LETTUCE_CROP, "lettuce");

        axisBlock((RotatedPillarBlock) ModBlocks.STRAW_BLOCK.get());

        horizontalFaceBlock(ModBlocks.RECIPE_BLOCK.get(), new ModelFile.UncheckedModelFile(modLoc("block/recipe_block")));

        riceCrop();

        variantBlock(ModBlocks.ENAMEL_BASIN.get(), blockState -> {
            if (blockState.getValue(EnamelBasinBlock.HAS_LID)) {
                return new ModelFile.UncheckedModelFile(modLoc("block/enamel_basin/base"));
            }
            int oilCount = blockState.getValue(EnamelBasinBlock.OIL_COUNT);
            if (oilCount <= 0) {
                return new ModelFile.UncheckedModelFile(modLoc("block/enamel_basin/empty"));
            }
            if (oilCount <= EnamelBasinBlock.MAX_OIL_COUNT / 3) {
                return new ModelFile.UncheckedModelFile(modLoc("block/enamel_basin/low"));
            }
            if (oilCount <= EnamelBasinBlock.MAX_OIL_COUNT / 3 * 2) {
                return new ModelFile.UncheckedModelFile(modLoc("block/enamel_basin/middle"));
            }
            return new ModelFile.UncheckedModelFile(modLoc("block/enamel_basin/high"));
        });

        variantBlock(ModBlocks.CHILI_RISTRA.get(), blockState -> {
            if (blockState.getValue(ChiliRistraBlock.IS_HEAD)) {
                if (blockState.getValue(ChiliRistraBlock.SHEARED)) {
                    return new ModelFile.UncheckedModelFile(modLoc("block/chili_ristra/head_sheared"));
                }
                return new ModelFile.UncheckedModelFile(modLoc("block/chili_ristra/head"));
            } else {
                if (blockState.getValue(ChiliRistraBlock.SHEARED)) {
                    return new ModelFile.UncheckedModelFile(modLoc("block/chili_ristra/body_sheared"));
                }
                return new ModelFile.UncheckedModelFile(modLoc("block/chili_ristra/body"));
            }
        });
    }

    public void variantBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(modelFunc.apply(state)).build());
    }

    public void crop(DeferredBlock<Block> block, String name) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            int age = state.getValue(CropBlock.AGE);
            ResourceLocation file = modLoc("block/crop/%s/stage%d".formatted(name, age));
            return ConfiguredModel.builder()
                    .modelFile(new ModelFile.UncheckedModelFile(file))
                    .build();
        });
    }

    public void riceCrop() {
        getVariantBuilder(ModBlocks.RICE_CROP.get()).forAllStates(state -> {
            int age = state.getValue(CropBlock.AGE);
            int location = state.getValue(RiceCropBlock.LOCATION);
            ResourceLocation file;
            if (location == RiceCropBlock.DOWN) {
                file = modLoc("block/crop/rice/stage%d_down".formatted(age));
            } else if (location == RiceCropBlock.MIDDLE) {
                file = modLoc("block/crop/rice/stage%d_middle".formatted(age));
            } else {
                file = modLoc("block/crop/rice/stage%d_up".formatted(age));
            }
            return ConfiguredModel.builder()
                    .modelFile(new ModelFile.UncheckedModelFile(file))
                    .build();
        });
    }

    public void cookStool(DeferredBlock<Block> block, String name) {
        horizontalBlock(block.get(), new ModelFile.UncheckedModelFile(modLoc("block/cook_stool/" + name)));
    }

    public void chair(DeferredBlock<Block> block, String name) {
        horizontalBlock(block.get(), new ModelFile.UncheckedModelFile(modLoc("block/chair/" + name)));
    }

    private void table(DeferredBlock<Block> block, String name) {
        ModelFile.UncheckedModelFile leftModel = new ModelFile.UncheckedModelFile(modLoc("block/table/%s_left".formatted(name)));
        ModelFile.UncheckedModelFile rightModel = new ModelFile.UncheckedModelFile(modLoc("block/table/%s_right".formatted(name)));
        ModelFile.UncheckedModelFile middleModel = new ModelFile.UncheckedModelFile(modLoc("block/table/%s_middle".formatted(name)));

        ModelFile.UncheckedModelFile leftModelRot = new ModelFile.UncheckedModelFile(modLoc("block/table/%s_left_rot".formatted(name)));
        ModelFile.UncheckedModelFile rightModelRot = new ModelFile.UncheckedModelFile(modLoc("block/table/%s_right_rot".formatted(name)));
        ModelFile.UncheckedModelFile middleModelRot = new ModelFile.UncheckedModelFile(modLoc("block/table/%s_middle_rot".formatted(name)));


        getVariantBuilder(block.get()).forAllStates(blockState -> {
            int position = blockState.getValue(TableBlock.POSITION);
            if (position == TableBlock.SINGLE) {
                return ConfiguredModel.builder()
                        .modelFile(new ModelFile.UncheckedModelFile(modLoc("block/table/%s_single".formatted(name))))
                        .build();
            }
            boolean isRotation = blockState.getValue(TableBlock.AXIS) == Direction.Axis.Z;
            if (position == TableBlock.LEFT) {
                if (isRotation) {
                    return ConfiguredModel.builder().modelFile(rightModelRot).build();
                } else {
                    return ConfiguredModel.builder().modelFile(rightModel).build();
                }
            }
            if (position == TableBlock.RIGHT) {
                if (isRotation) {
                    return ConfiguredModel.builder().modelFile(leftModelRot).build();
                } else {
                    return ConfiguredModel.builder().modelFile(leftModel).build();
                }
            }
            if (isRotation) {
                return ConfiguredModel.builder().modelFile(middleModelRot).build();
            } else {
                return ConfiguredModel.builder().modelFile(middleModel).build();
            }
        });
    }

    public void addFoodBiteBlock(Block block, ResourceLocation id) {
        horizontalBlock(block, blockState -> {
            if (!(blockState.getBlock() instanceof FoodBiteBlock foodBiteBlock)) {
                throw new IllegalArgumentException("Block must be an instance of FoodBiteBlock");
            }
            int bites = blockState.getValue(foodBiteBlock.getBites());
            ResourceLocation model = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/food/%s/%s_%d".formatted(id.getPath(), id.getPath(), bites));
            return new ModelFile.UncheckedModelFile(model);
        });
    }
}
