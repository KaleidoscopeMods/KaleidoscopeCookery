package com.github.ysbbbbbb.kaleidoscopecookery.client.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.client.animation.CustomArmPose;
import com.github.ysbbbbbb.kaleidoscopecookery.client.gui.overlay.PotOverlay;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.StrawHatModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.*;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.init.PonderCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;

import static net.neoforged.neoforge.client.gui.VanillaGuiLayers.CROSSHAIR;


@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = KaleidoscopeCookery.MOD_ID)
public class ClientSetupEvent {
    @SubscribeEvent
    @SuppressWarnings("all")
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(ModItems.KITCHEN_SHOVEL.get(), KitchenShovelItem.HAS_OIL_PROPERTY, KitchenShovelItem::getTexture));
        event.enqueueWork(() -> ItemProperties.register(ModItems.STOCKPOT_LID.get(), StockpotLidItem.USING_PROPERTY, StockpotLidItem::getTexture));
        event.enqueueWork(() -> ItemProperties.register(ModItems.OIL_POT.get(), OilPotItem.HAS_OIL_PROPERTY, OilPotItem::getTexture));
        event.enqueueWork(() -> ItemProperties.register(ModItems.RAW_DOUGH.get(), RawDoughItem.PULL_PROPERTY, RawDoughItem::getTexture));
        event.enqueueWork(() -> ItemProperties.register(ModItems.RECIPE_ITEM.get(), RecipeItem.HAS_RECIPE_PROPERTY, RecipeItem::getTexture));
        event.enqueueWork(() -> ItemProperties.register(ModItems.TRANSMUTATION_LUNCH_BAG.get(), TransmutationLunchBagItem.HAS_ITEMS_PROPERTY, TransmutationLunchBagItem::getTexture));
        event.enqueueWork(() -> ItemProperties.register(ModItems.STEAMER.get(), SteamerItem.HAS_ITEMS, SteamerItem::getTexture));

        // 其他模组兼容
        PonderCompat.init();
    }

    @SubscribeEvent
    public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
        BlockEntityRenderers.register(ModBlocks.POT_BE.get(), PotBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.FRUIT_BASKET_BE.get(), FruitBasketBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.CHOPPING_BOARD_BE.get(), ChoppingBoardBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.STOCKPOT_BE.get(), StockpotBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.KITCHENWARE_RACKS_BE.get(), KitchenwareRacksBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.CHAIR_BE.get(), ChairBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.TABLE_BE.get(), TableBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.SHAWARMA_SPIT_BE.get(), ShawarmaSpitBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.MILLSTONE_BE.get(), MillstoneBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.RECIPE_BLOCK_BE.get(), RecipeBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.STEAMER_BE.get(), SteamerBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.FOOD_BITE_THREE_BY_THREE_BE.get(), FoodBiteThreeByThreeBlockEntityRender::new);
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiLayersEvent event) {
        event.registerAbove(CROSSHAIR, ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "pot_overlay"), new PotOverlay());
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new ItemRenderReplacerReloadListener());
    }

    @SubscribeEvent
    @SuppressWarnings("all")
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        // LiftBlockItem - 举起姿势
        event.registerItem(new IClientItemExtensions() {
            private final HumanoidModel.ArmPose liftPose = CustomArmPose.LIFT_POSE.getValue();

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
                if (!stack.isEmpty()) {
                    return liftPose;
                }
                return HumanoidModel.ArmPose.EMPTY;
            }
        }, ModItems.COLD_CUT_HAM_SLICES.get());

        // StrawHatItem - 草帽模型
        event.registerItem(new IClientItemExtensions() {
            private StrawHatModel cachedModel = null;

            @Override
            public int getArmorLayerTintColor(ItemStack stack, LivingEntity entity, ArmorMaterial.Layer layer, int layerIdx, int fallbackColor) {
                if (layerIdx == 0) {
                    return IClientItemExtensions.super.getArmorLayerTintColor(stack, entity, layer, layerIdx, fallbackColor);
                }
                return 0;
            }

            @Override
            public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (cachedModel == null) {
                    cachedModel = new StrawHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(StrawHatModel.LAYER_LOCATION));
                }
                ModelPart head = cachedModel.getHead();
                head.copyFrom(original.head);
                return cachedModel;
            }
        }, ModItems.STRAW_HAT.get(), ModItems.STRAW_HAT_FLOWER.get());
    }
}
