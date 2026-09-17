package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;

import java.util.function.IntFunction;

public class BambooTrayRecipe extends SingleItemRecipe {
    private final Subtype subtype;
    private final int duration;

    public BambooTrayRecipe(Ingredient ingredient, ItemStack result, Subtype subtype, int duration) {
        super(ModRecipes.BAMBOO_TRAY_RECIPE, ModRecipes.BAMBOO_TRAY_SERIALIZER.get(), StringUtils.EMPTY, ingredient, result);
        this.subtype = subtype;
        this.duration = Math.max(duration, 1);
    }

    @Override
    public boolean matches(SingleRecipeInput inv, Level level) {
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

    public enum Subtype implements StringRepresentable {
        WETTING(0, "wetting"),
        DRYING(1, "drying");

        private static final IntFunction<Subtype> BY_ID = ByIdMap.continuous(
                Subtype::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final Codec<Subtype> CODEC = StringRepresentable.fromEnum(Subtype::values);
        public static final StreamCodec<ByteBuf, Subtype> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Subtype::getId);

        private final int id;
        private final String serializedName;

        Subtype(int id, String serializedName) {
            this.id = id;
            this.serializedName = serializedName;
        }

        private int getId() {
            return this.id;
        }

        @Override
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
