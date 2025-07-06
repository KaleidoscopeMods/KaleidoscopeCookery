package com.github.ysbbbbbb.kaleidoscopecookery.datagen.model;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenShovelItem;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, KaleidoscopeCookery.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("stove", modLoc("block/stove"));
        withExistingParent("pot", modLoc("block/pot"));
        withExistingParent("stockpot", modLoc("block/stockpot"));
        withExistingParent("enamel_basin", modLoc("block/enamel_basin/base"));

        handheldItem(ModItems.IRON_KITCHEN_KNIFE.get());
        handheldItem(ModItems.GOLD_KITCHEN_KNIFE.get());
        handheldItem(ModItems.DIAMOND_KITCHEN_KNIFE.get());
        handheldItem(ModItems.NETHERITE_KITCHEN_KNIFE.get());

        basicItem(ModItems.OIL.get());
        basicItem(ModItems.FRIED_EGG.get());
        basicItem(ModItems.SCARECROW.get());
        basicItem(ModItems.TOMATO.get());
        basicItem(ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get());
        basicItem(ModItems.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL.get());
        basicItem(ModItems.STIR_FRIED_BEEF_OFFAL.get());
        basicItem(ModItems.STIR_FRIED_BEEF_OFFAL_RICE_BOWL.get());
        basicItem(ModItems.BRAISED_BEEF.get());
        basicItem(ModItems.BRAISED_BEEF_RICE_BOWL.get());
        basicItem(ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get());
        basicItem(ModItems.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL.get());
        basicItem(ModItems.SWEET_AND_SOUR_PORK.get());
        basicItem(ModItems.SWEET_AND_SOUR_PORK_RICE_BOWL.get());
        basicItem(ModItems.COUNTRY_STYLE_MIXED_VEGETABLES.get());
        basicItem(ModItems.FISH_FLAVORED_SHREDDED_PORK.get());
        basicItem(ModItems.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL.get());
        basicItem(ModItems.BRAISED_FISH_RICE_BOWL.get());
        basicItem(ModItems.SPICY_CHICKEN_RICE_BOWL.get());
        basicItem(ModItems.SUSPICIOUS_STEW_RICE_BOWL.get());
        basicItem(ModItems.EGG_FRIED_RICE.get());
        basicItem(ModItems.DELICIOUS_EGG_FRIED_RICE.get());
        basicItem(ModItems.PORK_BONE_SOUP.get());
        basicItem(ModItems.SEAFOOD_MISO_SOUP.get());
        basicItem(ModItems.FEARSOME_THICK_SOUP.get());
        basicItem(ModItems.LAMB_AND_RADISH_SOUP.get());
        basicItem(ModItems.BRAISED_BEEF_WITH_POTATOES.get());
        basicItem(ModItems.WILD_MUSHROOM_RABBIT_SOUP.get());
        basicItem(ModItems.TOMATO_BEEF_BRISKET_SOUP.get());
        basicItem(ModItems.PUFFERFISH_SOUP.get());
        basicItem(ModItems.BORSCHT.get());
        basicItem(ModItems.BEEF_MEATBALL_SOUP.get());
        basicItem(ModItems.CHICKEN_AND_MUSHROOM_STEW.get());
        basicItem(ModItems.STRAW_HAT.get());
        basicItem(ModItems.STRAW_HAT_FLOWER.get());
        basicItem(ModItems.TOMATO_SEED.get());
        basicItem(ModItems.RICE_SEED.get());
        basicItem(ModItems.WILD_RICE_SEED.get());
        basicItem(ModItems.RICE_PANICLE.get());
        basicItem(ModItems.SASHIMI.get());
        basicItem(ModItems.RAW_LAMB_CHOPS.get());
        basicItem(ModItems.RAW_COW_OFFAL.get());
        basicItem(ModItems.RAW_PORK_BELLY.get());
        basicItem(ModItems.COOKED_LAMB_CHOPS.get());
        basicItem(ModItems.COOKED_COW_OFFAL.get());
        basicItem(ModItems.COOKED_PORK_BELLY.get());
        basicItem(ModItems.COOKED_RICE.get());
        basicItem(ModItems.RED_CHILI.get());
        basicItem(ModItems.GREEN_CHILI.get());
        basicItem(ModItems.CHILI_SEED.get());
        basicItem(ModItems.LETTUCE.get());
        basicItem(ModItems.LETTUCE_SEED.get());
        basicItem(ModItems.CATERPILLAR.get());

        ResourceLocation chileRistra = ForgeRegistries.ITEMS.getKey(ModItems.CHILI_RISTRA.get());
        if (chileRistra != null) {
            getBuilder(chileRistra.toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", new ResourceLocation(KaleidoscopeCookery.MOD_ID, "block/chili_ristra/head"));
        }

        ResourceLocation shovel = ForgeRegistries.ITEMS.getKey(ModItems.KITCHEN_SHOVEL.get());
        if (shovel != null) {
            ItemModelBuilder shovelNoOil = handheldItem(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "kitchen_shovel_no_oil"));
            ItemModelBuilder shovelHasOil = handheldItem(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "kitchen_shovel_has_oil"));
            getBuilder(shovel.toString())
                    .override().model(shovelNoOil).predicate(KitchenShovelItem.HAS_OIL_PROPERTY, 0).end()
                    .override().model(shovelHasOil).predicate(KitchenShovelItem.HAS_OIL_PROPERTY, 1).end();
        }

        FoodBiteRegistry.FOOD_DATA_MAP.forEach((key, value) -> {
            Item item = ForgeRegistries.ITEMS.getValue(key);
            if (item != null) {
                basicItem(item);
            }
        });

        withExistingParent("cook_stool_oak", modLoc("block/cook_stool/oak"));
        withExistingParent("cook_stool_spruce", modLoc("block/cook_stool/spruce"));
        withExistingParent("cook_stool_acacia", modLoc("block/cook_stool/acacia"));
        withExistingParent("cook_stool_bamboo", modLoc("block/cook_stool/bamboo"));
        withExistingParent("cook_stool_birch", modLoc("block/cook_stool/birch"));
        withExistingParent("cook_stool_cherry", modLoc("block/cook_stool/cherry"));
        withExistingParent("cook_stool_crimson", modLoc("block/cook_stool/crimson"));
        withExistingParent("cook_stool_dark_oak", modLoc("block/cook_stool/dark_oak"));
        withExistingParent("cook_stool_jungle", modLoc("block/cook_stool/jungle"));
        withExistingParent("cook_stool_mangrove", modLoc("block/cook_stool/mangrove"));
        withExistingParent("cook_stool_warped", modLoc("block/cook_stool/warped"));

        withExistingParent("chopping_board", modLoc("block/chopping_board"));
        withExistingParent("oil_block", modLoc("block/oil_block"));
        withExistingParent("kitchenware_racks", modLoc("block/kitchenware_racks"));
        withExistingParent("straw_block", modLoc("block/straw_block"));

        ItemModelBuilder fruitBasketFull = new ItemModelBuilder(modLoc("fruit_basket"), this.existingFileHelper)
                .parent(new ModelFile.UncheckedModelFile(modLoc("item/fruit_basket_full")));
        ItemModelBuilder fruitBasketItem = new ItemModelBuilder(modLoc("fruit_basket"), this.existingFileHelper)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/fruit_basket"));
        ItemModelBuilder fruitBasketBlock = new ItemModelBuilder(modLoc("fruit_basket"), this.existingFileHelper)
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/fruit_basket")));
        getBuilder("fruit_basket")
                .guiLight(BlockModel.GuiLight.FRONT)
                .customLoader(SeparateTransformsModelBuilder::begin).base(fruitBasketFull)
                .perspective(ItemDisplayContext.GROUND, fruitBasketBlock)
                .perspective(ItemDisplayContext.GUI, fruitBasketItem)
                .perspective(ItemDisplayContext.FIXED, fruitBasketItem)
                .perspective(ItemDisplayContext.GROUND, fruitBasketItem);
    }

    public ItemModelBuilder handheldItem(Item item) {
        return handheldItem(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
    }

    public ItemModelBuilder handheldItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }
}
