package com.github.ysbbbbbb.kaleidoscopecookery.loot;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModLootModifier;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RecipeRandomlyFunction extends LootItemConditionalFunction {
    private final List<RecipeItem.RecipeRecord> possibleRecipes;

    protected RecipeRandomlyFunction(LootItemCondition[] predicates, Collection<RecipeItem.RecipeRecord> possibleRecipes) {
        super(predicates);
        this.possibleRecipes = ImmutableList.copyOf(possibleRecipes);
    }

    @Override
    public LootItemFunctionType getType() {
        return ModLootModifier.RECIPE_RANDOMLY;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        RandomSource randomsource = context.getRandom();
        RecipeItem.RecipeRecord record;
        // 如果配置了配方，则从配置的配方中随机一个
        if (!this.possibleRecipes.isEmpty()) {
            record = this.possibleRecipes.get(randomsource.nextInt(this.possibleRecipes.size()));
            RecipeItem.setRecipe(stack, record);
            return stack;
        }

        // 否则从所有模型食物中随机一个
        List<ResourceLocation> keys = FoodBiteRegistry.FOOD_DATA_MAP.keySet().stream().toList();
        if (keys.isEmpty()) {
            return stack;
        }
        ResourceLocation randomKey = keys.get(randomsource.nextInt(keys.size()));
        Item result = FoodBiteRegistry.getItem(randomKey);
        RegistryAccess registryAccess = context.getLevel().registryAccess();

        // 炒锅配方
        List<PotRecipe> potRecipes = context.getLevel().getRecipeManager().getAllRecipesFor(ModRecipes.POT_RECIPE);
        for (PotRecipe recipe : potRecipes) {
            ItemStack resultItem = recipe.getResultItem(registryAccess);
            if (!resultItem.is(result)) {
                continue;
            }
            List<ItemStack> inputs = recipe.getIngredients().stream()
                    .filter(i -> !i.isEmpty())
                    .map(i -> i.getItems()[0]).toList();
            record = new RecipeItem.RecipeRecord(inputs, resultItem, RecipeItem.POT);
            RecipeItem.setRecipe(stack, record);
            return stack;
        }

        // 汤锅配方
        List<StockpotRecipe> stockpotRecipes = context.getLevel().getRecipeManager().getAllRecipesFor(ModRecipes.STOCKPOT_RECIPE);
        for (StockpotRecipe recipe : stockpotRecipes) {
            ItemStack resultItem = recipe.getResultItem(registryAccess);
            if (!resultItem.is(result)) {
                continue;
            }
            List<ItemStack> inputs = recipe.getIngredients().stream()
                    .filter(i -> !i.isEmpty())
                    .map(i -> i.getItems()[0]).toList();
            record = new RecipeItem.RecipeRecord(inputs, resultItem, RecipeItem.STOCKPOT);
            RecipeItem.setRecipe(stack, record);
            return stack;
        }

        return stack;
    }

    public static RecipeRandomlyFunction.Builder randomRecipe() {
        return new RecipeRandomlyFunction.Builder();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<RecipeRandomlyFunction.Builder> {
        private final List<RecipeItem.RecipeRecord> recipes = Lists.newArrayList();

        @Override
        protected RecipeRandomlyFunction.Builder getThis() {
            return this;
        }

        public RecipeRandomlyFunction.Builder withRecord(RecipeItem.RecipeRecord record) {
            this.recipes.add(record);
            return this;
        }

        public RecipeRandomlyFunction.Builder pot(ItemLike output, ItemLike... input) {
            List<ItemStack> list = Arrays.stream(input).map(ItemStack::new).toList();
            RecipeItem.RecipeRecord record = new RecipeItem.RecipeRecord(list, new ItemStack(output), RecipeItem.POT);
            return withRecord(record);
        }

        public RecipeRandomlyFunction.Builder stockpot(ItemLike output, ItemLike... input) {
            List<ItemStack> list = Arrays.stream(input).map(ItemStack::new).toList();
            RecipeItem.RecipeRecord record = new RecipeItem.RecipeRecord(list, new ItemStack(output), RecipeItem.STOCKPOT);
            return withRecord(record);
        }

        @Override
        public LootItemFunction build() {
            return new RecipeRandomlyFunction(this.getConditions(), this.recipes);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RecipeRandomlyFunction> {
        @Override
        public void serialize(JsonObject json, RecipeRandomlyFunction function, JsonSerializationContext context) {
            super.serialize(json, function, context);
            if (function.possibleRecipes.isEmpty()) {
                return;
            }
            JsonArray records = new JsonArray();
            for (RecipeItem.RecipeRecord record : function.possibleRecipes) {
                JsonObject root = new JsonObject();
                JsonObject output = new JsonObject();
                JsonArray inputs = new JsonArray();

                root.addProperty("type", record.type().toString());

                output.addProperty("item", ForgeRegistries.ITEMS.getKey(record.output().getItem()).toString());
                output.addProperty("count", record.output().getCount());
                root.add("output", output);

                for (ItemStack input : record.input()) {
                    JsonObject inputJson = new JsonObject();
                    inputJson.addProperty("item", ForgeRegistries.ITEMS.getKey(input.getItem()).toString());
                    inputs.add(inputJson);
                }
                root.add("inputs", inputs);
                records.add(root);
            }
            json.add("records", records);
        }

        @Override
        public RecipeRandomlyFunction deserialize(JsonObject object, JsonDeserializationContext context, LootItemCondition[] conditions) {
            if (!object.has("records")) {
                return new RecipeRandomlyFunction(conditions, Collections.emptyList());
            }
            JsonArray records = object.getAsJsonArray("records");
            if (records.size() <= 0) {
                return new RecipeRandomlyFunction(conditions, Collections.emptyList());
            }

            List<RecipeItem.RecipeRecord> recipeRecords = Lists.newArrayList();
            for (int i = 0; i < records.size(); i++) {
                JsonObject record = records.get(i).getAsJsonObject();

                ResourceLocation type = new ResourceLocation(record.get("type").getAsString());

                JsonObject outputJson = record.getAsJsonObject("output");
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(outputJson, "item")));
                int count = GsonHelper.getAsInt(outputJson, "count", 1);
                if (item == null) {
                    throw new JsonSyntaxException("No such item " + GsonHelper.getAsString(outputJson, "item"));
                }
                if (count <= 0) {
                    throw new JsonSyntaxException("Item count must be positive");
                }
                ItemStack output = new ItemStack(item, count);

                JsonArray inputsJson = record.getAsJsonArray("inputs");
                List<ItemStack> inputs = Lists.newArrayList();
                for (int j = 0; j < inputsJson.size(); j++) {
                    JsonObject inputJson = inputsJson.get(j).getAsJsonObject();
                    Item inputItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(inputJson, "item")));
                    if (inputItem == null) {
                        throw new JsonSyntaxException("No such item " + GsonHelper.getAsString(inputJson, "item"));
                    }
                    inputs.add(new ItemStack(inputItem));
                }
                recipeRecords.add(new RecipeItem.RecipeRecord(inputs, output, type));
            }
            return new RecipeRandomlyFunction(conditions, recipeRecords);
        }
    }
}
