package com.github.ysbbbbbb.kaleidoscopecookery.crafting.output;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.world.item.ItemStack.ITEM_NON_AIR_CODEC;

public record RandomOutput(ItemStack stack, float chance) {
    public static final RandomOutput EMPTY = new RandomOutput(ItemStack.EMPTY, 1.0F);

    public static final Codec<RandomOutput> CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create(instance -> instance.group(
                    ITEM_NON_AIR_CODEC
                            .fieldOf("id")
                            .forGetter(r -> r.stack.getItemHolder()),

                    ExtraCodecs.intRange(1, 99)
                            .optionalFieldOf("count", 1)
                            .forGetter(r -> r.stack.getCount()),

                    DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(r -> r.stack.getComponentsPatch()),

                    Codec.FLOAT
                            .optionalFieldOf("chance", 1.0F)
                            .forGetter(RandomOutput::chance)
            ).apply(instance, RandomOutput::new))
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RandomOutput> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, RandomOutput::stack,
            ByteBufCodecs.FLOAT, RandomOutput::chance,
            RandomOutput::new
    );

    public RandomOutput(Holder<Item> tag, int count, DataComponentPatch components, float chance) {
        this(new ItemStack(tag, count, components), Mth.clamp(chance, 0.0F, 1.0F));
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}