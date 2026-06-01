package com.github.ysbbbbbb.kaleidoscopecookery.crafting.output;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

public record RandomOutput(ItemStack stack, float chance) {
    public static final RandomOutput EMPTY = new RandomOutput(ItemStack.EMPTY, 1.0F);

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public static RandomOutput deserialize(JsonElement element) {
        JsonObject jsonObject = element.getAsJsonObject();
        ItemStack result = CraftingHelper.getItemStack(jsonObject, true, true);
        float chance = GsonHelper.getAsFloat(jsonObject, "chance", 1.0F);
        chance = Mth.clamp(chance, 0.0F, 1.0F);
        return new RandomOutput(result, chance);
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
        buf.writeFloat(this.chance);
    }

    public static RandomOutput fromNetwork(FriendlyByteBuf buf) {
        return new RandomOutput(buf.readItem(), buf.readFloat());
    }
}