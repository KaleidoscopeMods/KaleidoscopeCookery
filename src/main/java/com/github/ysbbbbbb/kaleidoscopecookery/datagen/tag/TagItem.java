package com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon.FD_KNIVES;
import static com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod.*;
import static net.minecraft.tags.ItemTags.VILLAGER_PLANTABLE_SEEDS;
import static net.minecraft.world.item.Items.*;

public class TagItem extends ItemTagsProvider {
    public TagItem(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider,
                   CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, KaleidoscopeCookery.MOD_ID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("all")
    protected void addTags(HolderLookup.Provider provider) {
        // 模组 tag
        tag(OIL).add(ModItems.OIL.get());
        tag(LIT_STOVE).add(FLINT_AND_STEEL, FIRE_CHARGE);
        tag(STRAW_HAT).add(ModItems.STRAW_HAT.get(),
                ModItems.STRAW_HAT_FLOWER.get());

        tag(KITCHEN_KNIFE).add(ModItems.IRON_KITCHEN_KNIFE.get(),
                        ModItems.GOLD_KITCHEN_KNIFE.get(),
                        ModItems.DIAMOND_KITCHEN_KNIFE.get(),
                        ModItems.NETHERITE_KITCHEN_KNIFE.get())
                .addOptionalTag(ResourceLocation.parse("farmersdelight:tools/knives"));

        tag(KITCHEN_SHOVEL).add(ModItems.KITCHEN_SHOVEL.get());

        // 农夫乐事
        tag(FD_KNIVES).add(ModItems.IRON_KITCHEN_KNIFE.get(),
                ModItems.GOLD_KITCHEN_KNIFE.get(),
                ModItems.DIAMOND_KITCHEN_KNIFE.get(),
                ModItems.NETHERITE_KITCHEN_KNIFE.get());

        tag(FARMER_ARMOR).add(ModItems.FARMER_CHEST_PLATE.get(),
                        ModItems.FARMER_LEGGINGS.get(),
                        ModItems.FARMER_BOOTS.get())
                .addTag(STRAW_HAT);
        this.tag(TagMod.STRAW_BALE).add(HAY_BLOCK, ModItems.STRAW_BLOCK.get());

        this.tag(COOKERY_MOD_SEEDS).add(
                ModItems.TOMATO_SEED.get(), ModItems.CHILI_SEED.get(),
                ModItems.WILD_RICE_SEED.get(), ModItems.LETTUCE_SEED.get()
        );

        this.tag(INGREDIENT_CONTAINER).add(BUCKET, BOWL, GLASS_BOTTLE);
        this.tag(GLASS_BOTTLE_CONTAINER).add(HONEY_BOTTLE);
        this.tag(BUCKET_CONTAINER).add(WATER_BUCKET, LAVA_BUCKET, MILK_BUCKET,
                SALMON_BUCKET, COD_BUCKET, TROPICAL_FISH_BUCKET, PUFFERFISH_BUCKET,
                AXOLOTL_BUCKET, TADPOLE_BUCKET, POWDER_SNOW_BUCKET);

        // 给食物分类，简单分为食物和可放置食物
        IntrinsicTagAppender<Item> meal = this.tag(MEALS);
        IntrinsicTagAppender<Item> feasts = this.tag(FEASTS);
        BuiltInRegistries.ITEM.keySet().stream()
                .filter(id -> id.getNamespace().equals(KaleidoscopeCookery.MOD_ID))
                .forEach(id -> {
                    Item item = BuiltInRegistries.ITEM.get(id);
                    if (item == null) {
                        return;
                    }
                    ItemStack stack = item.getDefaultInstance();
                    if (item.getFoodProperties(stack, null) != null) {
                        meal.add(item);
                        if (item instanceof BowlFoodBlockItem) {
                            feasts.add(item);
                        }
                    }
                });

        this.addModItems();

        // 原料黑名单
        tag(INGREDIENT_BLOCKLIST).addTags(
                Tags.Items.TOOLS,
                Tags.Items.ARMORS
        ).add(
                // 可以存储物品的不允许放入
                SHULKER_BOX,
                WHITE_SHULKER_BOX,
                ORANGE_SHULKER_BOX,
                MAGENTA_SHULKER_BOX,
                LIGHT_BLUE_SHULKER_BOX,
                YELLOW_SHULKER_BOX,
                LIME_SHULKER_BOX,
                PINK_SHULKER_BOX,
                GRAY_SHULKER_BOX,
                LIGHT_GRAY_SHULKER_BOX,
                CYAN_SHULKER_BOX,
                PURPLE_SHULKER_BOX,
                BLUE_SHULKER_BOX,
                BROWN_SHULKER_BOX,
                GREEN_SHULKER_BOX,
                RED_SHULKER_BOX,
                BLACK_SHULKER_BOX,
                BUNDLE,
                // 桶类
                BUCKET,
                MILK_BUCKET,
                WATER_BUCKET,
                LAVA_BUCKET,
                POWDER_SNOW_BUCKET,
                PUFFERFISH_BUCKET,
                SALMON_BUCKET,
                COD_BUCKET,
                TROPICAL_FISH_BUCKET,
                AXOLOTL_BUCKET,
                TADPOLE_BUCKET,
                // 碗
                BOWL,
                // 本模组的食谱
                ModItems.RECIPE_ITEM.get()
        );

        // 原版兼容
        tag(ItemTags.SHOVELS).add(ModItems.KITCHEN_SHOVEL.get());
        tag(ItemTags.SWORDS).addTag(KITCHEN_KNIFE).add(ModItems.SICKLE.get());
        tag(EXTINGUISH_STOVE).addTag(ItemTags.SHOVELS);
        tag(VILLAGER_PLANTABLE_SEEDS).add(ModItems.TOMATO_SEED.get(),
                ModItems.CHILI_SEED.get(), ModItems.LETTUCE_SEED.get()
        );
        tag(Tags.Items.SEEDS).add(
                ModItems.CHILI_SEED.get(),
                ModItems.TOMATO_SEED.get(),
                ModItems.LETTUCE_SEED.get(),
                ModItems.WILD_RICE_SEED.get(),
                ModItems.RICE_SEED.get()
        );

        // 社区兼容
        tag(TagCommon.CROPS_CHILI_PEPPER).add(ModItems.RED_CHILI.get(), ModItems.GREEN_CHILI.get());
        tag(TagCommon.CROPS_TOMATO).add(ModItems.TOMATO.get());
        tag(TagCommon.CROPS_LETTUCE).add(ModItems.LETTUCE.get());
        tag(TagCommon.CROPS_RICE).add(ModItems.RICE_SEED.get());
        tag(TagCommon.CROPS).addTag(TagCommon.CROPS_CHILI_PEPPER)
                .addTag(TagCommon.CROPS_TOMATO)
                .addTag(TagCommon.CROPS_LETTUCE)
                .addTag(TagCommon.CROPS_RICE);

        tag(TagCommon.VEGETABLES_CHILI_PEPPER).add(ModItems.RED_CHILI.get(), ModItems.GREEN_CHILI.get());
        tag(TagCommon.VEGETABLES_TOMATO).add(ModItems.TOMATO.get());
        tag(TagCommon.VEGETABLES_LETTUCE).add(ModItems.LETTUCE.get());
        tag(TagCommon.VEGETABLES).addTag(TagCommon.VEGETABLES_CHILI_PEPPER)
                .addTag(TagCommon.VEGETABLES_TOMATO)
                .addTag(TagCommon.VEGETABLES_LETTUCE)
                .addOptionalTag(TagCommon.CROPS_CABBAGE); // 兼容农夫乐事卷心菜

        tag(TagCommon.SEEDS_CHILI_PEPPER).add(ModItems.CHILI_SEED.get());
        tag(TagCommon.SEEDS_TOMATO).add(ModItems.TOMATO_SEED.get());
        tag(TagCommon.SEEDS_LETTUCE).add(ModItems.LETTUCE_SEED.get());
        tag(TagCommon.SEEDS_RICE).add(ModItems.RICE_SEED.get());

        tag(TagCommon.GRAIN_RICE).add(ModItems.RICE_SEED.get());

        tag(TagCommon.COOKED_BEEF).add(ModItems.COOKED_COW_OFFAL.get(), COOKED_BEEF);
        tag(TagCommon.COOKED_PORK).add(ModItems.COOKED_PORK_BELLY.get(), COOKED_PORKCHOP);
        tag(TagCommon.COOKED_MUTTON).add(ModItems.COOKED_LAMB_CHOPS.get(), COOKED_MUTTON);
        tag(TagCommon.COOKED_EGGS).add(ModItems.FRIED_EGG.get());
        tag(TagCommon.COOKED_RICE).add(ModItems.COOKED_RICE.get());

        tag(TagCommon.RAW_BEEF).add(ModItems.RAW_COW_OFFAL.get(), BEEF);
        tag(TagCommon.RAW_CHICKEN).add(CHICKEN);
        tag(TagCommon.RAW_PORK).add(ModItems.RAW_PORK_BELLY.get(), PORKCHOP);
        tag(TagCommon.RAW_MUTTON).add(ModItems.RAW_LAMB_CHOPS.get(), MUTTON);
        tag(Tags.Items.EGGS).add(EGG, TURTLE_EGG);
        tag(TagCommon.RAW_FISHES_TROPICAL).add(ModItems.SASHIMI.get());
        tag(TagCommon.RAW_FISHES_COD).add(COD);
        tag(TagCommon.RAW_FISHES_SALMON).add(SALMON);

        tag(TagCommon.RAW_FISHES).addTag(TagCommon.RAW_FISHES_COD)
                .addTag(TagCommon.RAW_FISHES_SALMON)
                .addTag(TagCommon.RAW_FISHES_TROPICAL);

        tag(TagCommon.RAW_MEATS).addTag(TagCommon.RAW_BEEF)
                .addTag(TagCommon.RAW_CHICKEN)
                .addTag(TagCommon.RAW_PORK)
                .addTag(TagCommon.RAW_MUTTON)
                .addTag(TagCommon.RAW_FISHES_COD)
                .addTag(TagCommon.RAW_FISHES_SALMON)
                .addTag(TagCommon.RAW_FISHES_TROPICAL)
                .add(ModItems.RAW_DONKEY_MEAT.get())
                .add(ModItems.RAW_CUT_SMALL_MEATS.get());

        tag(TagCommon.FLOUR).add(ModItems.FLOUR.get());
        tag(TagCommon.DOUGHS);
        tag(TagCommon.FOODS_DOUGH);
        tag(TagCommon.DOUGH).add(ModItems.RAW_DOUGH.get()).addTags(TagCommon.DOUGHS, TagCommon.FOODS_DOUGH);

        // 均衡饮食兼容
        tag(TagCommon.GRAINS).add(ModItems.RICE_SEED.get(), ModItems.RICE_PANICLE.get());
        tag(TagCommon.PROTEINS).add(ModItems.CATERPILLAR.get(), ModItems.RAW_CUT_SMALL_MEATS.get(), ModItems.RAW_DONKEY_MEAT.get(), ModItems.COOKED_DONKEY_MEAT.get());
        tag(TagCommon.DIET_VEGETABLES).addTag(TagCommon.VEGETABLES);

        // 兼容静谧四季
        tag(TagCommon.SPRING_CROPS).add(ModItems.LETTUCE_SEED.get());
        tag(TagCommon.SUMMER_CROPS).add(ModItems.TOMATO_SEED.get(), ModItems.CHILI_SEED.get(), ModItems.RICE_SEED.get(), ModItems.WILD_RICE_SEED.get());
        tag(TagCommon.AUTUMN_CROPS).add(ModItems.TOMATO_SEED.get(), ModItems.CHILI_SEED.get(), ModItems.LETTUCE_SEED.get(), ModItems.RICE_SEED.get(), ModItems.WILD_RICE_SEED.get());
    }

    private void addModItems() {
        IntrinsicTagAppender<Item> modTags = tag(COOKERY_MOD_ITEMS);
        for (Item item : BuiltInRegistries.ITEM) {
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
            if (itemId.getNamespace().equals(KaleidoscopeCookery.MOD_ID)) {
                modTags.add(item);
            }
        }
    }
}
