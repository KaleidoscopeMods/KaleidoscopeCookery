package com.github.ysbbbbbb.kaleidoscopecookery.client.init;

import com.github.ysbbbbbb.kaleidoscopecookery.client.event.FlatulenceEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.client.event.PotOverlayEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.MillstoneModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.*;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.item.StrawHatArmorRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.PackType;

public class ClientRegistry {
    public static void init() {
        // 注册盔甲渲染器
        ArmorRenderer.register(new StrawHatArmorRenderer(), ModItems.STRAW_HAT, ModItems.STRAW_HAT_FLOWER);

        registerItemProperties();
        registerClientEvents();
        registerBlockEntityRenderers();
        registerResourceReloadListeners();
    }

    private static void registerItemProperties() {
        ItemProperties.register(ModItems.KITCHEN_SHOVEL, KitchenShovelItem.HAS_OIL_PROPERTY, KitchenShovelItem::getTexture);
        ItemProperties.register(ModItems.STOCKPOT_LID, StockpotLidItem.USING_PROPERTY, StockpotLidItem::getTexture);
        ItemProperties.register(ModItems.STEAMER, SteamerItem.HAS_ITEMS, SteamerItem::getTexture);
        ItemProperties.register(ModItems.RECIPE_ITEM, RecipeItem.HAS_RECIPE_PROPERTY, RecipeItem::getTexture);
        ItemProperties.register(ModItems.RAW_DOUGH, RawDoughItem.PULL_PROPERTY, RecipeItem::getTexture);
        ItemProperties.register(ModItems.OIL_POT, OilPotItem.HAS_OIL_PROPERTY, OilPotItem::getTexture);
        ItemProperties.register(ModItems.TRANSMUTATION_LUNCH_BAG, TransmutationLunchBagItem.HAS_ITEMS_PROPERTY, TransmutationLunchBagItem::getTexture);
    }

    private static void registerClientEvents() {
        FlatulenceEvent.register();
        PotOverlayEvent.register();
    }

    private static void registerBlockEntityRenderers() {
        BlockEntityRenderers.register(ModBlocks.POT_BE, PotBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.FRUIT_BASKET_BE, FruitBasketBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.CHOPPING_BOARD_BE, ChoppingBoardBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.STOCKPOT_BE, StockpotBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.KITCHENWARE_RACKS_BE, KitchenwareRacksBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.CHAIR_BE, ChairBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.TABLE_BE, TableBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.SHAWARMA_SPIT_BE, ShawarmaSpitBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.MILLSTONE_BE, MillstoneBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.RECIPE_BLOCK_BE, RecipeBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.STEAMER_BE, SteamerBlockEntityRender::new);

        EntityModelLayerRegistry.registerModelLayer(MillstoneModel.LAYER_LOCATION, MillstoneModel::createBodyLayer);
    }

    private static void registerResourceReloadListeners() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new ItemRenderReplacerReloadListener());
    }
}
