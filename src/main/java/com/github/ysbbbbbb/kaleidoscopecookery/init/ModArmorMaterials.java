package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

import static net.minecraft.world.item.ArmorItem.Type.*;

public class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, KaleidoscopeCookery.MOD_ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FARMER = ARMOR_MATERIALS.register("cookery_farmer", () -> new ArmorMaterial(
            Util.make(Maps.newHashMap(), map -> {
                map.put(HELMET, 3);
                map.put(CHESTPLATE, 4);
                map.put(LEGGINGS, 5);
                map.put(BOOTS, 2);
            }),
            12,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(Items.LEATHER),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("cookery_farmer"))),
            0, 0
    ));
}
