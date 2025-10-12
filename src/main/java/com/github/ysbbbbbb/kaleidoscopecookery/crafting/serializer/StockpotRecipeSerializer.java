package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoupBases;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StockpotRecipeSerializer implements RecipeSerializer<StockpotRecipe> {
    public static final int DEFAULT_TIME = 300;
    public static final int DEFAULT_COOKING_BUBBLE_COLOR = 0xFFECC3;
    public static final int DEFAULT_FINISHED_BUBBLE_COLOR = 0xF4AA8B;
    public static final Ingredient DEFAULT_CARRIER = Ingredient.of(Items.BOWL);
    public static final ResourceLocation DEFAULT_SOUP_BASE = ModSoupBases.WATER;
    public static final ResourceLocation EMPTY_ID = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot/empty");
    public static final ResourceLocation DEFAULT_COOKING_TEXTURE = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot/default_cooking");
    public static final ResourceLocation DEFAULT_FINISHED_TEXTURE = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot/default_finished");

    public static StockpotRecipe getEmptyRecipe() {
        return new StockpotRecipe(EMPTY_ID,
                Lists.newArrayList(), DEFAULT_SOUP_BASE,
                ItemStack.EMPTY, DEFAULT_TIME, DEFAULT_CARRIER,
                DEFAULT_COOKING_TEXTURE, DEFAULT_FINISHED_TEXTURE,
                DEFAULT_COOKING_BUBBLE_COLOR,
                DEFAULT_FINISHED_BUBBLE_COLOR);
    }

    @Override
    public StockpotRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
        List<Ingredient> inputs = Lists.newArrayList();
        for (JsonElement e : ingredients) {
            inputs.add(Ingredient.fromJson(e));
        }
        ResourceLocation soupBase = DEFAULT_SOUP_BASE;
        if (json.has("soup_base")) {
            soupBase = new ResourceLocation(GsonHelper.getAsString(json, "soup_base"));
        }
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        int time = GsonHelper.getAsInt(json, "time", DEFAULT_TIME);
        Ingredient carrier = DEFAULT_CARRIER;
        if (json.has("carrier")) {
            carrier = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "carrier"));
        }
        ResourceLocation cookingTexture = new ResourceLocation(GsonHelper.getAsString(json, "cooking_texture", DEFAULT_COOKING_TEXTURE.toString()));
        ResourceLocation finishedTexture = new ResourceLocation(GsonHelper.getAsString(json, "finished_texture", DEFAULT_FINISHED_TEXTURE.toString()));
        int cookingBubbleColor = GsonHelper.getAsInt(json, "cooking_bubble_color", DEFAULT_COOKING_BUBBLE_COLOR);
        int finishedBubbleColor = GsonHelper.getAsInt(json, "finished_bubble_color", DEFAULT_FINISHED_BUBBLE_COLOR);
        return new StockpotRecipe(recipeId, inputs, soupBase, result, time, carrier, cookingTexture, finishedTexture, cookingBubbleColor, finishedBubbleColor);
    }

    @Override
    public @Nullable StockpotRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
        int ingredientsSize = buf.readVarInt();
        List<Ingredient> inputs = Lists.newArrayList();
        for (int i = 0; i < ingredientsSize; i++) {
            inputs.add(Ingredient.fromNetwork(buf));
        }
        ResourceLocation soupBase = buf.readResourceLocation();
        ItemStack result = buf.readItem();
        int time = buf.readVarInt();
        Ingredient carrier = Ingredient.fromNetwork(buf);
        ResourceLocation cookingTexture = buf.readResourceLocation();
        ResourceLocation finishedTexture = buf.readResourceLocation();
        int cookingBubbleColor = buf.readVarInt();
        int finishedBubbleColor = buf.readVarInt();
        return new StockpotRecipe(recipeId, inputs, soupBase, result,
                time, carrier, cookingTexture, finishedTexture,
                cookingBubbleColor, finishedBubbleColor);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, StockpotRecipe recipe) {
        buffer.writeVarInt(recipe.getIngredients().size());
        recipe.getIngredients().forEach(i -> i.toNetwork(buffer));
        buffer.writeResourceLocation(recipe.soupBase());
        buffer.writeItem(recipe.result());
        buffer.writeVarInt(recipe.time());
        recipe.carrier().toNetwork(buffer);
        buffer.writeResourceLocation(recipe.cookingTexture());
        buffer.writeResourceLocation(recipe.finishedTexture());
        buffer.writeVarInt(recipe.cookingBubbleColor());
        buffer.writeVarInt(recipe.finishedBubbleColor());
    }
}
