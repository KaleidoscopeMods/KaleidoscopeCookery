package com.github.ysbbbbbb.kaleidoscopecookery;

import com.github.ysbbbbbb.kaleidoscopecookery.config.ClientConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.init.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.network.NetworkHandler;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@net.neoforged.fml.common.Mod(KaleidoscopeCookery.MOD_ID)
public class KaleidoscopeCookery {
    public static final String MOD_ID = "kaleidoscope_cookery";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KaleidoscopeCookery(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, GeneralConfig.init());
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.init());

        modEventBus.addListener(NetworkHandler::registerPacket);

        FoodBiteRegistry.init();
        TeacupRegistry.init();

        ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        ModTrigger.TRIGGERS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.BLOCK_ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModPoi.POI_TYPES.register(modEventBus);
        ModVillager.VILLAGER_PROFESSION.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModParticles.PARTICLES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        ModDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        ModAttachmentType.ATTACHMENT_TYPES.register(modEventBus);
    }
}
