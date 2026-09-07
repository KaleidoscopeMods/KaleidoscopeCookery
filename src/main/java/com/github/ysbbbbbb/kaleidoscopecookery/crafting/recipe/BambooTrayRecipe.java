package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;

public class BambooTrayRecipe extends SingleItemRecipe {
    private final Subtype subtype;
    private final int duration;

    public BambooTrayRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, Subtype subtype, int duration) {
        super(ModRecipes.BAMBOO_TRAY_RECIPE, ModRecipes.BAMBOO_TRAY_SERIALIZER.get(), id, StringUtils.EMPTY, ingredient, result);
        this.subtype = subtype;
        this.duration = Math.max(duration, 1);
    }

    @Override
    public boolean matches(Container inv, Level level) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public Subtype getSubtype() {
        return subtype;
    }

    public int getDuration() {
        return duration;
    }

    public enum Subtype {
        WETTING("wetting"),
        DRYING("drying");

        private final String serializedName;

        Subtype(String serializedName) {
            this.serializedName = serializedName;
        }

        public String getSerializedName() {
            return serializedName;
        }

        public static Subtype fromSerializedName(String name) {
            for (Subtype subtype : values()) {
                if (subtype.serializedName.equalsIgnoreCase(name)) {
                    return subtype;
                }
            }
            throw new IllegalArgumentException("Unknown bamboo tray recipe subtype: " + name);
        }
    }
}
