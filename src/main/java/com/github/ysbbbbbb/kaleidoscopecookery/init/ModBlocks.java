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
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.ChairBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.RecipeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TableBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, KaleidoscopeCookery.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, KaleidoscopeCookery.MOD_ID);

    public static RegistryObject<Block> STOVE = BLOCKS.register("stove", StoveBlock::new);
    public static RegistryObject<Block> POT = BLOCKS.register("pot", PotBlock::new);
    public static RegistryObject<Block> STOCKPOT = BLOCKS.register("stockpot", StockpotBlock::new);
    public static RegistryObject<Block> FRUIT_BASKET = BLOCKS.register("fruit_basket", FruitBasketBlock::new);
    public static RegistryObject<Block> CHOPPING_BOARD = BLOCKS.register("chopping_board", ChoppingBoardBlock::new);
    public static RegistryObject<Block> OIL_BLOCK = BLOCKS.register("oil_block", OilBlock::new);
    public static RegistryObject<Block> ENAMEL_BASIN = BLOCKS.register("enamel_basin", EnamelBasinBlock::new);
    public static RegistryObject<Block> KITCHENWARE_RACKS = BLOCKS.register("kitchenware_racks", KitchenwareRacksBlock::new);
    public static RegistryObject<Block> CHILI_RISTRA = BLOCKS.register("chili_ristra", ChiliRistraBlock::new);
    public static RegistryObject<Block> STRAW_BLOCK = BLOCKS.register("straw_block", StrawBlocks::new);
    public static RegistryObject<Block> SHAWARMA_SPIT = BLOCKS.register("shawarma_spit", ShawarmaSpitBlock::new);
    public static RegistryObject<Block> MILLSTONE = BLOCKS.register("millstone", MillstoneBlock::new);
    public static RegistryObject<Block> STEAMER = BLOCKS.register("steamer", SteamerBlock::new);

    public static RegistryObject<Block> OIL_POT = BLOCKS.register("oil_pot", OilPotBlock::new);
    public static RegistryObject<Block> RECIPE_BLOCK = BLOCKS.register("recipe_block", RecipeBlock::new);

    public static RegistryObject<Block> TOMATO_CROP = BLOCKS.register("tomato_crop", () -> new BaseCropBlock(ModItems.TOMATO, ModItems.TOMATO_SEED));
    public static RegistryObject<Block> CHILI_CROP = BLOCKS.register("chili_crop", ChiliCropBlock::new);
    public static RegistryObject<Block> LETTUCE_CROP = BLOCKS.register("lettuce_crop", LettuceCropBlock::new);
    public static RegistryObject<Block> RICE_CROP = BLOCKS.register("rice_crop", RiceCropBlock::new);

    public static RegistryObject<Block> COOK_STOOL_OAK = BLOCKS.register("cook_stool_oak", CookStoolBlock::new);
    public static RegistryObject<Block> COOK_STOOL_SPRUCE = BLOCKS.register("cook_stool_spruce", CookStoolBlock::new);
    public static RegistryObject<Block> COOK_STOOL_ACACIA = BLOCKS.register("cook_stool_acacia", CookStoolBlock::new);
    public static RegistryObject<Block> COOK_STOOL_BAMBOO = BLOCKS.register("cook_stool_bamboo", CookStoolBlock::new);
    public static RegistryObject<Block> COOK_STOOL_BIRCH = BLOCKS.register("cook_stool_birch", CookStoolBlock::new);
    public static RegistryObject<Block> COOK_STOOL_CHERRY = BLOCKS.register("cook_stool_cherry", CookStoolBlock::new);
    public static RegistryObject<Block> COOK_STOOL_CRIMSON = BLOCKS.register("cook_stool_crimson", CookStoolBlock::new);
    public static RegistryObject<Block> COOK_STOOL_DARK_OAK = BLOCKS.register("cook_stool_dark_oak", CookStoolBlock::new);
    public static RegistryObject<Block> COOK_STOOL_JUNGLE = BLOCKS.register("cook_stool_jungle", CookStoolBlock::new);
    public static RegistryObject<Block> COOK_STOOL_MANGROVE = BLOCKS.register("cook_stool_mangrove", CookStoolBlock::new);
    public static RegistryObject<Block> COOK_STOOL_WARPED = BLOCKS.register("cook_stool_warped", CookStoolBlock::new);

    public static RegistryObject<Block> CHAIR_OAK = BLOCKS.register("chair_oak", ChairBlock::new);
    public static RegistryObject<Block> CHAIR_SPRUCE = BLOCKS.register("chair_spruce", ChairBlock::new);
    public static RegistryObject<Block> CHAIR_ACACIA = BLOCKS.register("chair_acacia", ChairBlock::new);
    public static RegistryObject<Block> CHAIR_BAMBOO = BLOCKS.register("chair_bamboo", ChairBlock::new);
    public static RegistryObject<Block> CHAIR_BIRCH = BLOCKS.register("chair_birch", ChairBlock::new);
    public static RegistryObject<Block> CHAIR_CHERRY = BLOCKS.register("chair_cherry", ChairBlock::new);
    public static RegistryObject<Block> CHAIR_CRIMSON = BLOCKS.register("chair_crimson", ChairBlock::new);
    public static RegistryObject<Block> CHAIR_DARK_OAK = BLOCKS.register("chair_dark_oak", ChairBlock::new);
    public static RegistryObject<Block> CHAIR_JUNGLE = BLOCKS.register("chair_jungle", ChairBlock::new);
    public static RegistryObject<Block> CHAIR_MANGROVE = BLOCKS.register("chair_mangrove", ChairBlock::new);
    public static RegistryObject<Block> CHAIR_WARPED = BLOCKS.register("chair_warped", ChairBlock::new);

    public static RegistryObject<Block> TABLE_OAK = BLOCKS.register("table_oak", TableBlock::new);
    public static RegistryObject<Block> TABLE_SPRUCE = BLOCKS.register("table_spruce", TableBlock::new);
    public static RegistryObject<Block> TABLE_ACACIA = BLOCKS.register("table_acacia", TableBlock::new);
    public static RegistryObject<Block> TABLE_BAMBOO = BLOCKS.register("table_bamboo", TableBlock::new);
    public static RegistryObject<Block> TABLE_BIRCH = BLOCKS.register("table_birch", TableBlock::new);
    public static RegistryObject<Block> TABLE_CHERRY = BLOCKS.register("table_cherry", TableBlock::new);
    public static RegistryObject<Block> TABLE_CRIMSON = BLOCKS.register("table_crimson", TableBlock::new);
    public static RegistryObject<Block> TABLE_DARK_OAK = BLOCKS.register("table_dark_oak", TableBlock::new);
    public static RegistryObject<Block> TABLE_JUNGLE = BLOCKS.register("table_jungle", TableBlock::new);
    public static RegistryObject<Block> TABLE_MANGROVE = BLOCKS.register("table_mangrove", TableBlock::new);
    public static RegistryObject<Block> TABLE_WARPED = BLOCKS.register("table_warped", TableBlock::new);

    public static RegistryObject<BlockEntityType<PotBlockEntity>> POT_BE = BLOCK_ENTITIES.register("pot",
            () -> BlockEntityType.Builder.of(PotBlockEntity::new, POT.get()).build(null));

    public static RegistryObject<BlockEntityType<StockpotBlockEntity>> STOCKPOT_BE = BLOCK_ENTITIES.register("stockpot",
            () -> BlockEntityType.Builder.of(StockpotBlockEntity::new, STOCKPOT.get()).build(null));

    public static RegistryObject<BlockEntityType<FruitBasketBlockEntity>> FRUIT_BASKET_BE = BLOCK_ENTITIES.register("fruit_basket",
            () -> BlockEntityType.Builder.of(FruitBasketBlockEntity::new, FRUIT_BASKET.get()).build(null));

    public static RegistryObject<BlockEntityType<ChoppingBoardBlockEntity>> CHOPPING_BOARD_BE = BLOCK_ENTITIES.register("chopping_board",
            () -> BlockEntityType.Builder.of(ChoppingBoardBlockEntity::new, CHOPPING_BOARD.get()).build(null));

    public static RegistryObject<BlockEntityType<KitchenwareRacksBlockEntity>> KITCHENWARE_RACKS_BE = BLOCK_ENTITIES.register("kitchenware_racks",
            () -> BlockEntityType.Builder.of(KitchenwareRacksBlockEntity::new, KITCHENWARE_RACKS.get()).build(null));

    public static RegistryObject<BlockEntityType<ShawarmaSpitBlockEntity>> SHAWARMA_SPIT_BE = BLOCK_ENTITIES.register("shawarma_spit",
            () -> BlockEntityType.Builder.of(ShawarmaSpitBlockEntity::new, SHAWARMA_SPIT.get()).build(null));

    public static RegistryObject<BlockEntityType<MillstoneBlockEntity>> MILLSTONE_BE = BLOCK_ENTITIES.register("millstone",
            () -> BlockEntityType.Builder.of(MillstoneBlockEntity::new, MILLSTONE.get()).build(null));

    public static RegistryObject<BlockEntityType<RecipeBlockEntity>> RECIPE_BLOCK_BE = BLOCK_ENTITIES.register("recipe_block",
            () -> BlockEntityType.Builder.of(RecipeBlockEntity::new, RECIPE_BLOCK.get()).build(null));

    public static RegistryObject<BlockEntityType<SteamerBlockEntity>> STEAMER_BE = BLOCK_ENTITIES.register("steamer",
            () -> BlockEntityType.Builder.of(SteamerBlockEntity::new, STEAMER.get()).build(null));

    public static RegistryObject<BlockEntityType<ChairBlockEntity>> CHAIR_BE = BLOCK_ENTITIES.register("chair", () -> BlockEntityType.Builder.of(ChairBlockEntity::new,
            CHAIR_OAK.get(), CHAIR_SPRUCE.get(), CHAIR_ACACIA.get(), CHAIR_BAMBOO.get(),
            CHAIR_BIRCH.get(), CHAIR_CHERRY.get(), CHAIR_CRIMSON.get(), CHAIR_DARK_OAK.get(),
            CHAIR_JUNGLE.get(), CHAIR_MANGROVE.get(), CHAIR_WARPED.get()
    ).build(null));

    public static RegistryObject<BlockEntityType<TableBlockEntity>> TABLE_BE = BLOCK_ENTITIES.register("table", () -> BlockEntityType.Builder.of(TableBlockEntity::new,
            TABLE_OAK.get(), TABLE_SPRUCE.get(), TABLE_ACACIA.get(), TABLE_BAMBOO.get(),
            TABLE_BIRCH.get(), TABLE_CHERRY.get(), TABLE_CRIMSON.get(), TABLE_DARK_OAK.get(),
            TABLE_JUNGLE.get(), TABLE_MANGROVE.get(), TABLE_WARPED.get()
    ).build(null));
}
