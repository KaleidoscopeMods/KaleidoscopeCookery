package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexStockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlexStockpotRecipeSerializer implements RecipeSerializer<FlexStockpotRecipe> {
    @Override
    public FlexStockpotRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
        List<Ingredient> inputs = Lists.newArrayList();
        for (JsonElement e : ingredients) {
            inputs.add(Ingredient.fromJson(e));
        }

        ResourceLocation soupBase = StockpotRecipeSerializer.DEFAULT_SOUP_BASE;
        if (json.has("soup_base")) {
            soupBase = new ResourceLocation(GsonHelper.getAsString(json, "soup_base"));
        }

        ItemStack result = CraftingHelper.getItemStack(
                GsonHelper.getAsJsonObject(json, "result"),
                true, true
        );

        int time = GsonHelper.getAsInt(json, "time", StockpotRecipeSerializer.DEFAULT_TIME);

        Ingredient carrier = StockpotRecipeSerializer.DEFAULT_CARRIER;
        if (json.has("carrier")) {
            carrier = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "carrier"));
        }

        ResourceLocation cookingTexture = new ResourceLocation(GsonHelper.getAsString(
                json, "cooking_texture",
                StockpotVisuals.DEFAULT_COOKING_TEXTURE.toString()
        ));
        ResourceLocation finishedTexture = new ResourceLocation(GsonHelper.getAsString(
                json, "finished_texture",
                StockpotVisuals.DEFAULT_FINISHED_TEXTURE.toString()
        ));

        int cookingBubbleColor = GsonHelper.getAsInt(
                json, "cooking_bubble_color",
                StockpotVisuals.DEFAULT_COOKING_BUBBLE_COLOR
        );
        int finishedBubbleColor = GsonHelper.getAsInt(
                json, "finished_bubble_color",
                StockpotVisuals.DEFAULT_FINISHED_BUBBLE_COLOR
        );

        StockpotVisuals visuals = new StockpotVisuals(
                cookingTexture, finishedTexture,
                cookingBubbleColor, finishedBubbleColor
        );

        return new FlexStockpotRecipe(recipeId, inputs, soupBase, result, time, carrier, visuals);
    }

    @Override
    public @Nullable FlexStockpotRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
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
        StockpotVisuals visuals = new StockpotVisuals(
                cookingTexture, finishedTexture,
                cookingBubbleColor, finishedBubbleColor
        );
        return new FlexStockpotRecipe(
                recipeId, inputs, soupBase, result,
                time, carrier, visuals
        );
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, FlexStockpotRecipe recipe) {
        buffer.writeVarInt(recipe.getIngredients().size());
        recipe.getIngredients().forEach(i -> i.toNetwork(buffer));
        buffer.writeResourceLocation(recipe.soupBase());
        buffer.writeItem(recipe.result());
        buffer.writeVarInt(recipe.time());
        recipe.carrier().toNetwork(buffer);
        StockpotVisuals visuals = recipe.visuals();
        buffer.writeResourceLocation(visuals.cookingTexture());
        buffer.writeResourceLocation(visuals.finishedTexture());
        buffer.writeVarInt(visuals.cookingBubbleColor());
        buffer.writeVarInt(visuals.finishedBubbleColor());
    }
}
