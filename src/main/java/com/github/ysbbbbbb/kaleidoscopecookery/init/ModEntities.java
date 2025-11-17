package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ScarecrowEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ThrowableBaoziEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public class ModEntities {
    public static final EntityType<SitEntity> SIT = SitEntity.TYPE;
    public static final EntityType<ScarecrowEntity> SCARECROW = ScarecrowEntity.TYPE;
    public static final EntityType<ThrowableBaoziEntity> THROWABLE_BAOZI = ThrowableBaoziEntity.TYPE;

    public static void registerEntities() {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "sit"), SIT);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "scarecrow"), SCARECROW);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "throwable_baozi"), THROWABLE_BAOZI);

        // Register entity attributes
        FabricDefaultAttributeRegistry.register(SCARECROW, LivingEntity.createLivingAttributes());
    }
}
