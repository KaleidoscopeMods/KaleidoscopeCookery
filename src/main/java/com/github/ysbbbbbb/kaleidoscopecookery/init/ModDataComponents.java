package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.item.FruitBasketItem;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;

public class ModDataComponents {
    public static final DataComponentType<FruitBasketItem.ItemContainer> FRUIT_BASKET_ITEMS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "fruit_basket_items"),
            DataComponentType.<FruitBasketItem.ItemContainer>builder()
                    .persistent(FruitBasketItem.ItemContainer.CODEC)
                    .networkSynchronized(FruitBasketItem.ItemContainer.STREAM_CODEC)
                    .build()
    );

    public static final DataComponentType<Boolean> KITCHEN_SHOVEL_HAS_OIL = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "kitchen_shovel_has_oil"),
            DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build()
    );

    public static void registerDataComponents() {
        // 注册方法，用于确保类被加载
    }
}
