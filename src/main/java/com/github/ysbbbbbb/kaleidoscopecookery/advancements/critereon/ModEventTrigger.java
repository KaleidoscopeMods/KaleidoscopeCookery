package com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ModEventTrigger extends SimpleCriterionTrigger<ModEventTrigger.Instance> {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "mod_event");

    public static Criterion<Instance> create(String eventName) {
        return ModTrigger.EVENT.createCriterion(new ModEventTrigger.Instance(Optional.empty(), eventName));
    }

    public void trigger(LivingEntity user, String eventName) {
        if (user instanceof ServerPlayer serverPlayer) {
            super.trigger(serverPlayer, instance -> instance.matches(eventName));
        }
    }

    @Override
    public @NotNull Codec<Instance> codec() {
        return ModEventTrigger.Instance.CODEC;
    }

    public record Instance(Optional<ContextAwarePredicate> player, String eventName) implements SimpleInstance {
        public static final Codec<ModEventTrigger.Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ModEventTrigger.Instance::player),
                        Codec.STRING.fieldOf("event").forGetter(ModEventTrigger.Instance::eventName))
                .apply(instance, ModEventTrigger.Instance::new));

        public boolean matches(String eventNameIn) {
            return this.eventName.equals(eventNameIn);
        }
    }
}
