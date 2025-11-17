package com.github.ysbbbbbb.kaleidoscopecookery;

import com.github.ysbbbbbb.kaleidoscopecookery.event.ExtraLootTableDrop;
import com.github.ysbbbbbb.kaleidoscopecookery.init.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.CommonRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.network.NetworkHandler;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class KaleidoscopeCookery implements ModInitializer {
    public static final String MOD_ID = "kaleidoscope_cookery";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        // 药水效果优先注册
        ModEffects.registerEffects();

        CommonRegistry.init();
        NetworkHandler.init();

        ModArmorMaterials.registerArmorMaterials();
        ModTrigger.init();
        ModBlocks.registerBlocks();
        ModItems.registerItems();
        ModEntities.registerEntities();
        ModPoi.registerPoiTypes();
        ModVillager.registerVillagerProfessions();
        ModCreativeTabs.registerTabs();
        ModSounds.registerSounds();
        ModParticles.registerParticles();
        ModRecipes.registerRecipes();
        ModLootModifier.registerLootModifiers();
        ModTrades.registerTrades();
        ModSoupBases.registerSoupBases();
        ModDataComponents.registerDataComponents();
        // 事件
        ModEvents.init();

        // 注册额外的战利品表事件
        ExtraLootTableDrop.register();
    }
}
