package com.github.ysbbbbbb.kaleidoscopecookery;

import com.github.ysbbbbbb.kaleidoscopecookery.config.ClientConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.init.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(KaleidoscopeCookery.MOD_ID)
public class KaleidoscopeCookery {
    public static final String MOD_ID = "kaleidoscope_cookery";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KaleidoscopeCookery() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GeneralConfig.init());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.init());

        FoodBiteRegistry.init();
        TeacupRegistry.init();
        ModTrigger.init();

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
        ModLootModifier.GLOBAL_LOOT_MODIFIER_SERIALIZER.register(modEventBus);
    }
}
