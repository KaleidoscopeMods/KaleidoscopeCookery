package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.*;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.RecipeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TableBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenwareRacksBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.*;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class ModPlugin implements IWailaPlugin {
    public static final ResourceLocation SHAWARMA_SPIT = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "shawarma_spit");
    public static final ResourceLocation POT = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "pot");
    public static final ResourceLocation STOCKPOT = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot");
    public static final ResourceLocation CHOPPING_BOARD = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "chopping_board");
    public static final ResourceLocation MILLSTONE = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "millstone");
    public static final ResourceLocation ENAMEL_BASIN = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "enamel_basin");
    public static final ResourceLocation TABLE = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "table");
    public static final ResourceLocation FRUIT_BASKET = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "fruit_basket");
    public static final ResourceLocation KITCHENWARE_RACK = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "kitchenware_rack");
    public static final ResourceLocation OIL_POT = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "oil_pot");
    public static final ResourceLocation RECIPE_BLOCK = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "recipe_block");
    public static final ResourceLocation STEAMER = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "steamer");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerItemStorage(FruitBasketComponentProvider.INSTANCE, FruitBasketBlockEntity.class);
        registration.registerItemStorage(KitchenwareRackComponentProvider.INSTANCE, KitchenwareRacksBlockEntity.class);
        registration.registerItemStorage(TableComponentProvider.INSTANCE, TableBlockEntity.class);
        registration.registerItemStorage(PotComponentProvider.INSTANCE, PotBlockEntity.class);
        registration.registerItemStorage(StockpotComponentProvider.INSTANCE, StockpotBlockEntity.class);
        registration.registerItemStorage(SteamerComponentProvider.INSTANCE, SteamerBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(ShawarmaSpitComponentProvider.INSTANCE, ShawarmaSpitBlock.class);
        registration.registerBlockComponent(ChoppingBoardComponentProvider.INSTANCE, ChoppingBoardBlock.class);
        registration.registerBlockComponent(EnamelBasinComponentProvider.INSTANCE, EnamelBasinBlock.class);
        registration.registerBlockComponent(OilPotComponentProvider.INSTANCE, OilPotBlock.class);

        registration.registerItemStorageClient(FruitBasketComponentProvider.INSTANCE);
        registration.registerItemStorageClient(KitchenwareRackComponentProvider.INSTANCE);
        registration.registerItemStorageClient(TableComponentProvider.INSTANCE);
        registration.registerItemStorageClient(PotComponentProvider.INSTANCE);
        registration.registerItemStorageClient(StockpotComponentProvider.INSTANCE);
        registration.registerItemStorageClient(SteamerComponentProvider.INSTANCE);

        registration.registerBlockComponent(MillstoneComponentProvider.INSTANCE, MillstoneBlock.class);
        registration.registerBlockComponent(RecipeBlockComponentProvider.INSTANCE, RecipeBlock.class);
    }
}
