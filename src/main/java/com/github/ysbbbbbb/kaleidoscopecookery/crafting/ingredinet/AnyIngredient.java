package com.github.ysbbbbbb.kaleidoscopecookery.crafting.ingredinet;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.*;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnyIngredient extends AbstractIngredient {
    public static final ResourceLocation ID = new ResourceLocation("kaleidoscopecookery", "any_ingredient");

    private final List<Ingredient> ingredients;
    @javax.annotation.Nullable
    private ItemStack[] itemStacks;
    @javax.annotation.Nullable
    private IntList stackingIds;

    protected AnyIngredient(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            if (CraftingHelper.getID(ingredient.getSerializer()) == null) {
                throw new RuntimeException("AnyIngredient only support ingredient that has id, the ingredient not support: " + ingredient);
            }
        }
        this.ingredients = Collections.unmodifiableList(ingredients);
    }

    public static AnyIngredient of(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            if (CraftingHelper.getID(ingredient.getSerializer()) == null) {
                throw new RuntimeException("AnyIngredient only support ingredient that has id, the ingredient not support: " + ingredient);
            }
        }
        return new AnyIngredient(ingredients);
    }

    public static AnyIngredient of(Ingredient... ingredients) {
        for (Ingredient ingredient : ingredients) {
            if (CraftingHelper.getID(ingredient.getSerializer()) == null) {
                throw new RuntimeException("AnyIngredient only support ingredient that has id, the ingredient not support: " + ingredient);
            }
        }
        return new AnyIngredient(List.of(ingredients));
    }

    @Override
    public boolean isSimple() {
        return ingredients.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public boolean isEmpty() {
        return ingredients.stream().allMatch(Ingredient::isEmpty);
    }

    @Override
    public boolean test(@Nullable ItemStack pStack) {
        if (pStack == null || pStack.isEmpty()) {
            return false;
        }
        for (Ingredient ingredient : ingredients) {
            if (ingredient.test(pStack)) {
                return true;
            }
        }
        return false;
    }

    public ItemStack[] getItems() {
        if (this.itemStacks == null) {
            this.itemStacks = this.ingredients.stream().flatMap((ingredient) -> {
                return Arrays.stream(ingredient.getItems());
            }).distinct().toArray(ItemStack[]::new);
        }

        return this.itemStacks;
    }

    public IntList getStackingIds() {
        if (this.stackingIds == null || checkInvalidation()) {
            this.markValid();
            ItemStack[] aitemstack = this.getItems();
            this.stackingIds = new IntArrayList(aitemstack.length);

            for(ItemStack itemstack : aitemstack) {
                this.stackingIds.add(StackedContents.getStackingIndex(itemstack));
            }

            this.stackingIds.sort(IntComparators.NATURAL_COMPARATOR);
        }

        return this.stackingIds;
    }

    protected void invalidate() {
        super.invalidate();
        this.itemStacks = null;
        this.stackingIds = null;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("type", ID.toString());

        JsonArray json = new JsonArray();
        for (Ingredient ingredient : this.ingredients) {
            ResourceLocation id = CraftingHelper.getID(ingredient.getSerializer());
            assert id != null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", id.toString());
            jsonObject.add("value", ingredient.toJson());

            json.add(jsonObject);
        }
        jsonObj.add("ingredients", json);

        return jsonObj;
    }

    public static class Serializer implements IIngredientSerializer<AnyIngredient> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public AnyIngredient parse(FriendlyByteBuf buffer) {
            List<Ingredient> ingredients = Lists.newArrayList();
            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                ResourceLocation ingredientSe = buffer.readResourceLocation();
                Ingredient ingredient = CraftingHelper.getIngredient(ingredientSe, buffer);
                ingredients.add(ingredient);
            }
            return new AnyIngredient(ingredients);
        }

        @Override
        public AnyIngredient parse(JsonObject json) {
            List<Ingredient> ingredients = Lists.newArrayList();

            JsonArray jsonArray = json.getAsJsonArray("ingredients");
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject().getAsJsonObject("value");
                Ingredient ingredient = Ingredient.fromJson(jsonObject, true);
                ingredients.add(ingredient);
            }

            return new AnyIngredient(ingredients);
        }

        @Override
        public void write(FriendlyByteBuf buffer, AnyIngredient nbtIngredient) {
            buffer.writeVarInt(nbtIngredient.ingredients.size());
            for (Ingredient ingredient : nbtIngredient.ingredients) {
                IIngredientSerializer<? extends Ingredient> serializer = ingredient.getSerializer();
                ResourceLocation id = CraftingHelper.getID(serializer);
                assert id != null;
                buffer.writeResourceLocation(id);
                writeIngredient(buffer, ingredient, serializer);
            }
        }

        @SuppressWarnings("unchecked")
        private <T extends Ingredient> void writeIngredient(FriendlyByteBuf buffer, Ingredient ingredient, IIngredientSerializer<T> serializer) {
            serializer.write(buffer, (T) ingredient);
        }
    }
}
