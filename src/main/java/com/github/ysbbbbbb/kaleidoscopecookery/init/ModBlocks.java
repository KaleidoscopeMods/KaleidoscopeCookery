package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.BaseCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.ChiliCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.LettuceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.ChairBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.CookStoolBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.FruitBasketBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.TableBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.*;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.ChiliRistraBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.OilBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.RecipeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.StrawBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.*;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlocks {
    // Kitchen blocks
    public static final Block STOVE = new StoveBlock();
    public static final Block POT = new PotBlock();
    public static final Block STOCKPOT = new StockpotBlock();
    public static final Block FRUIT_BASKET = new FruitBasketBlock();
    public static final Block CHOPPING_BOARD = new ChoppingBoardBlock();
    public static final Block OIL_BLOCK = new OilBlock();
    public static final Block ENAMEL_BASIN = new EnamelBasinBlock();
    public static final Block KITCHENWARE_RACKS = new KitchenwareRacksBlock();
    public static final Block CHILI_RISTRA = new ChiliRistraBlock();
    public static final Block STRAW_BLOCK = new StrawBlocks();
    public static final Block SHAWARMA_SPIT = new ShawarmaSpitBlock();

    // Crop blocks
    public static final Block TOMATO_CROP = new BaseCropBlock(() -> ModItems.TOMATO, () -> ModItems.TOMATO_SEED);
    public static final Block CHILI_CROP = new ChiliCropBlock();
    public static final Block LETTUCE_CROP = new LettuceCropBlock();
    public static final Block RICE_CROP = new RiceCropBlock();

    // Cook stools
    public static final Block COOK_STOOL_OAK = new CookStoolBlock();
    public static final Block COOK_STOOL_SPRUCE = new CookStoolBlock();
    public static final Block COOK_STOOL_ACACIA = new CookStoolBlock();
    public static final Block COOK_STOOL_BAMBOO = new CookStoolBlock();
    public static final Block COOK_STOOL_BIRCH = new CookStoolBlock();
    public static final Block COOK_STOOL_CHERRY = new CookStoolBlock();
    public static final Block COOK_STOOL_CRIMSON = new CookStoolBlock();
    public static final Block COOK_STOOL_DARK_OAK = new CookStoolBlock();
    public static final Block COOK_STOOL_JUNGLE = new CookStoolBlock();
    public static final Block COOK_STOOL_MANGROVE = new CookStoolBlock();
    public static final Block COOK_STOOL_WARPED = new CookStoolBlock();


    public static final Block MILLSTONE = new MillstoneBlock();
    public static final Block STEAMER = new SteamerBlock();
    public static final Block OIL_POT = new OilPotBlock();
    public static final Block RECIPE_BLOCK = new RecipeBlock();



//    public static RegistryObject<Block> MILLSTONE = BLOCKS.register("millstone", MillstoneBlock::new);
//    public static RegistryObject<Block> STEAMER = BLOCKS.register("steamer", SteamerBlock::new);
//
//    public static RegistryObject<Block> OIL_POT = BLOCKS.register("oil_pot", OilPotBlock::new);
//    public static RegistryObject<Block> RECIPE_BLOCK = BLOCKS.register("recipe_block", RecipeBlock::new);

    // Chairs
    public static final Block CHAIR_OAK = new ChairBlock();
    public static final Block CHAIR_SPRUCE = new ChairBlock();
    public static final Block CHAIR_ACACIA = new ChairBlock();
    public static final Block CHAIR_BAMBOO = new ChairBlock();
    public static final Block CHAIR_BIRCH = new ChairBlock();
    public static final Block CHAIR_CHERRY = new ChairBlock();
    public static final Block CHAIR_CRIMSON = new ChairBlock();
    public static final Block CHAIR_DARK_OAK = new ChairBlock();
    public static final Block CHAIR_JUNGLE = new ChairBlock();
    public static final Block CHAIR_MANGROVE = new ChairBlock();
    public static final Block CHAIR_WARPED = new ChairBlock();

    // Tables
    public static final Block TABLE_OAK = new TableBlock();
    public static final Block TABLE_SPRUCE = new TableBlock();
    public static final Block TABLE_ACACIA = new TableBlock();
    public static final Block TABLE_BAMBOO = new TableBlock();
    public static final Block TABLE_BIRCH = new TableBlock();
    public static final Block TABLE_CHERRY = new TableBlock();
    public static final Block TABLE_CRIMSON = new TableBlock();
    public static final Block TABLE_DARK_OAK = new TableBlock();
    public static final Block TABLE_JUNGLE = new TableBlock();
    public static final Block TABLE_MANGROVE = new TableBlock();
    public static final Block TABLE_WARPED = new TableBlock();

    // Block entities
    public static final BlockEntityType<PotBlockEntity> POT_BE = BlockEntityType.Builder.of(PotBlockEntity::new, POT).build(null);
    public static final BlockEntityType<StockpotBlockEntity> STOCKPOT_BE = BlockEntityType.Builder.of(StockpotBlockEntity::new, STOCKPOT).build(null);
    public static final BlockEntityType<FruitBasketBlockEntity> FRUIT_BASKET_BE = BlockEntityType.Builder.of(FruitBasketBlockEntity::new, FRUIT_BASKET).build(null);
    public static final BlockEntityType<ChoppingBoardBlockEntity> CHOPPING_BOARD_BE = BlockEntityType.Builder.of(ChoppingBoardBlockEntity::new, CHOPPING_BOARD).build(null);
    public static final BlockEntityType<KitchenwareRacksBlockEntity> KITCHENWARE_RACKS_BE = BlockEntityType.Builder.of(KitchenwareRacksBlockEntity::new, KITCHENWARE_RACKS).build(null);
    public static final BlockEntityType<ShawarmaSpitBlockEntity> SHAWARMA_SPIT_BE = BlockEntityType.Builder.of(ShawarmaSpitBlockEntity::new, SHAWARMA_SPIT).build(null);

    public static final BlockEntityType<ChairBlockEntity> CHAIR_BE = BlockEntityType.Builder.of(ChairBlockEntity::new,
            CHAIR_OAK, CHAIR_SPRUCE, CHAIR_ACACIA, CHAIR_BAMBOO,
            CHAIR_BIRCH, CHAIR_CHERRY, CHAIR_CRIMSON, CHAIR_DARK_OAK,
            CHAIR_JUNGLE, CHAIR_MANGROVE, CHAIR_WARPED
    ).build(null);

    public static final BlockEntityType<TableBlockEntity> TABLE_BE = BlockEntityType.Builder.of(TableBlockEntity::new,

    TABLE_OAK, TABLE_SPRUCE, TABLE_ACACIA, TABLE_BAMBOO,
            TABLE_BIRCH, TABLE_CHERRY, TABLE_CRIMSON, TABLE_DARK_OAK,

    TABLE_JUNGLE, TABLE_MANGROVE, TABLE_WARPED
    ).build(null);

    public static final BlockEntityType<MillstoneBlockEntity> MILLSTONE_BE = BlockEntityType.Builder.of(MillstoneBlockEntity::new, MILLSTONE).build(null);
    public static final BlockEntityType<RecipeBlockEntity> RECIPE_BLOCK_BE = BlockEntityType.Builder.of(RecipeBlockEntity::new, RECIPE_BLOCK).build(null);
    public static final BlockEntityType<SteamerBlockEntity> STEAMER_BE = BlockEntityType.Builder.of(SteamerBlockEntity::new, STEAMER).build(null);



//    public static RegistryObject<BlockEntityType<MillstoneBlockEntity>> MILLSTONE_BE = BLOCK_ENTITIES.register("millstone",
//            () -> BlockEntityType.Builder.of(MillstoneBlockEntity::new, MILLSTONE.get()).build(null));
//
//    public static RegistryObject<BlockEntityType<RecipeBlockEntity>> RECIPE_BLOCK_BE = BLOCK_ENTITIES.register("recipe_block",
//            () -> BlockEntityType.Builder.of(RecipeBlockEntity::new, RECIPE_BLOCK.get()).build(null));
//
//    public static RegistryObject<BlockEntityType<SteamerBlockEntity>> STEAMER_BE = BLOCK_ENTITIES.register("steamer",
//            () -> BlockEntityType.Builder.of(SteamerBlockEntity::new, STEAMER.get()).build(null));

    public static void registerBlocks() {
        // Kitchen blocks
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stove"), STOVE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "pot"), POT);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot"), STOCKPOT);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "fruit_basket"), FRUIT_BASKET);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chopping_board"), CHOPPING_BOARD);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "oil_block"), OIL_BLOCK);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "enamel_basin"), ENAMEL_BASIN);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "kitchenware_racks"), KITCHENWARE_RACKS);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chili_ristra"), CHILI_RISTRA);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "straw_block"), STRAW_BLOCK);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "shawarma_spit"), SHAWARMA_SPIT);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "oil_pot"), OIL_POT);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "recipe_block"), RECIPE_BLOCK);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "millstone"), MILLSTONE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "steamer"), STEAMER);


        // Crop blocks
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "tomato_crop"), TOMATO_CROP);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chili_crop"), CHILI_CROP);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "lettuce_crop"), LETTUCE_CROP);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "rice_crop"), RICE_CROP);

        // Cook stools
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_oak"), COOK_STOOL_OAK);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_spruce"), COOK_STOOL_SPRUCE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_acacia"), COOK_STOOL_ACACIA);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_bamboo"), COOK_STOOL_BAMBOO);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_birch"), COOK_STOOL_BIRCH);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_cherry"), COOK_STOOL_CHERRY);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_crimson"), COOK_STOOL_CRIMSON);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_dark_oak"), COOK_STOOL_DARK_OAK);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_jungle"), COOK_STOOL_JUNGLE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_mangrove"), COOK_STOOL_MANGROVE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "cook_stool_warped"), COOK_STOOL_WARPED);

        // Chairs
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_oak"), CHAIR_OAK);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_spruce"), CHAIR_SPRUCE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_acacia"), CHAIR_ACACIA);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_bamboo"), CHAIR_BAMBOO);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_birch"), CHAIR_BIRCH);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_cherry"), CHAIR_CHERRY);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_crimson"), CHAIR_CRIMSON);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_dark_oak"), CHAIR_DARK_OAK);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_jungle"), CHAIR_JUNGLE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_mangrove"), CHAIR_MANGROVE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair_warped"), CHAIR_WARPED);

        // Tables
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_oak"), TABLE_OAK);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_spruce"), TABLE_SPRUCE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_acacia"), TABLE_ACACIA);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_bamboo"), TABLE_BAMBOO);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_birch"), TABLE_BIRCH);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_cherry"), TABLE_CHERRY);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_crimson"), TABLE_CRIMSON);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_dark_oak"), TABLE_DARK_OAK);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_jungle"), TABLE_JUNGLE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_mangrove"), TABLE_MANGROVE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table_warped"), TABLE_WARPED);

        // Block entities
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "pot"), POT_BE);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot"), STOCKPOT_BE);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "fruit_basket"), FRUIT_BASKET_BE);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chopping_board"), CHOPPING_BOARD_BE);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "kitchenware_racks"), KITCHENWARE_RACKS_BE);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "shawarma_spit"), SHAWARMA_SPIT_BE);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chair"), CHAIR_BE);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table"), TABLE_BE);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "recipe_block"), RECIPE_BLOCK_BE);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "millstone"), MILLSTONE_BE);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, "steamer"), STEAMER_BE);
    }
}
