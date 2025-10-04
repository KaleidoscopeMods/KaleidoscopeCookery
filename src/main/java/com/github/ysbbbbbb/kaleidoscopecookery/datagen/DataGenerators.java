package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.model.BlockModelGenerator;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.model.BlockStateGenerator;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.model.ItemModelGenerator;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag.*;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var registries = event.getLookupProvider();
        var vanillaPack = generator.getVanillaPack(true);
        var helper = event.getExistingFileHelper();
        var pack = generator.getPackOutput();

        var block = vanillaPack.addProvider(packOutput -> new TagBlock(packOutput, registries, helper));
        vanillaPack.addProvider(packOutput -> new TagItem(packOutput, registries, block.contentsGetter(), helper));
        vanillaPack.addProvider(packOutput -> new TagPoiType(packOutput, registries, helper));
        vanillaPack.addProvider(packOutput -> new TagEntityType(packOutput, registries, helper));
        vanillaPack.addProvider(packOutput -> new TagDamage(packOutput, registries, helper));

        generator.addProvider(true, new ForgeAdvancementProvider(pack, registries, helper,
                Collections.singletonList(new AdvancementGenerator())
        ));

        generator.addProvider(event.includeServer(), new LootTableGenerator(pack));
        generator.addProvider(event.includeServer(), new ModRecipeGenerator(pack));
        generator.addProvider(event.includeServer(), new GlobalLootModifier(pack));
        generator.addProvider(event.includeClient(), new ParticleDescriptionGenerator(pack, helper));
        generator.addProvider(event.includeClient(), new BlockModelGenerator(pack, helper));
        generator.addProvider(event.includeClient(), new BlockStateGenerator(pack, helper));
        generator.addProvider(event.includeClient(), new ItemModelGenerator(pack, helper));
        generator.addProvider(event.includeServer(), new SoundDefinitionsGenerator(pack, helper));
    }
}
