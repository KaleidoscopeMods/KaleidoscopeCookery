package com.github.ysbbbbbb.kaleidoscopecookery.crafting.output;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class RandomOutput {
    public static final RandomOutput EMPTY;
    private final ItemStack stack;
    private final float chance;

    public RandomOutput(ItemStack stack, float chance) {
        this.stack = stack;
        this.chance = chance;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public float getChance() {
        return this.chance;
    }

    public ItemStack rollOutput(RandomSource random) {
        if (random.nextFloat() < getChance()) {
            return stack.copy();
        }
        return ItemStack.EMPTY;
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public JsonElement serialize() {
        JsonObject json = new JsonObject();
        ResourceLocation itemLoc = ForgeRegistries.ITEMS.getKey(this.stack.getItem());
        Objects.requireNonNull(itemLoc, "Item does not exist!");
        json.addProperty("item", itemLoc.toString());
        int count = this.stack.getCount();
        if (count != 1) {
            json.addProperty("count", count);
        }

        if (this.stack.hasTag()) {
            json.add("nbt", JsonParser.parseString(this.stack.getTag().toString()));
        }

        if (this.chance != 1.0F) {
            json.addProperty("chance", this.chance);
        }

        return json;
    }

    public static RandomOutput deserialize(JsonElement je) {
        if (!je.isJsonObject()) {
            throw new JsonSyntaxException("RandomOutput must be a json object");
        } else {
            JsonObject json = je.getAsJsonObject();
            String itemId = GsonHelper.getAsString(json, "item");
            int count = GsonHelper.getAsInt(json, "count", 1);
            float chance = GsonHelper.isValidNode(json, "chance") ? GsonHelper.getAsFloat(json, "chance") : 1.0F;
            ItemStack itemstack = new ItemStack((ItemLike)ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)), count);
            if (GsonHelper.isValidNode(json, "nbt")) {
                try {
                    JsonElement element = json.get("nbt");
                    itemstack.setTag(TagParser.parseTag(element.isJsonObject() ? KaleidoscopeCookery.GSON.toJson(element) : GsonHelper.convertToString(element, "nbt")));
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            }

            return new RandomOutput(itemstack, chance);
        }
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
        buf.writeFloat(this.chance);
    }

    public static RandomOutput fromNetwork(FriendlyByteBuf buf) {
        return new RandomOutput(buf.readItem(), buf.readFloat());
    }

    static {
        EMPTY = new RandomOutput(ItemStack.EMPTY, 1.0F);
    }
}
