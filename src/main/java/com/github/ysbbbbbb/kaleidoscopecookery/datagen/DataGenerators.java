package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable.BlockLootTables;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGenerators implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(AdvancementGenerator::new);
        pack.addProvider(LootTableGenerator::new);
        pack.addProvider(ModRecipeGenerator::new);
    }

//    {
//        var generator = event.getGenerator();
//        var registries = event.getLookupProvider();
//        var vanillaPack = generator.getVanillaPack(true);
//        var helper = event.getExistingFileHelper();
//        var pack = generator.getPackOutput();
//
//        var block = vanillaPack.addProvider(packOutput -> new TagBlock(packOutput, registries, helper));
//        vanillaPack.addProvider(packOutput -> new TagItem(packOutput, registries, block.contentsGetter(), helper));
//        vanillaPack.addProvider(packOutput -> new TagPoiType(packOutput, registries, helper));
//        vanillaPack.addProvider(packOutput -> new TagEntityType(packOutput, registries, helper));
//        vanillaPack.addProvider(packOutput -> new TagDamage(packOutput, registries, helper));
//
//        generator.addProvider(true, new ForgeAdvancementProvider(pack, registries, helper,
//                Collections.singletonList(new AdvancementGenerator())
//        ));
//
//        generator.addProvider(event.includeServer(), new LootTableGenerator(pack));
//        generator.addProvider(event.includeServer(), new ModRecipeGenerator(pack));
//        generator.addProvider(event.includeServer(), new GlobalLootModifier(pack));
//        generator.addProvider(event.includeClient(), new ParticleDescriptionGenerator(pack, helper));
//        generator.addProvider(event.includeClient(), new BlockModelGenerator(pack, helper));
//        generator.addProvider(event.includeClient(), new BlockStateGenerator(pack, helper));
//        generator.addProvider(event.includeClient(), new ItemModelGenerator(pack, helper));
//        generator.addProvider(event.includeServer(), new SoundDefinitionsGenerator(pack, helper));
//
//    }
}
