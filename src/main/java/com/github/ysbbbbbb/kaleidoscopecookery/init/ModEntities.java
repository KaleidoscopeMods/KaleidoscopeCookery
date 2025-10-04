package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ScarecrowEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ThrowableBaoziEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KaleidoscopeCookery.MOD_ID);

    public static RegistryObject<EntityType<SitEntity>> SIT = ENTITY_TYPES.register("sit", () -> SitEntity.TYPE);
    public static RegistryObject<EntityType<ScarecrowEntity>> SCARECROW = ENTITY_TYPES.register("scarecrow", () -> ScarecrowEntity.TYPE);
    public static RegistryObject<EntityType<ThrowableBaoziEntity>> THROWABLE_BAOZI = ENTITY_TYPES.register("throwable_baozi", () -> ThrowableBaoziEntity.TYPE);

    @SubscribeEvent
    public static void addEntityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ScarecrowEntity.TYPE, LivingEntity.createLivingAttributes().build());
    }
}
