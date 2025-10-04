package com.github.ysbbbbbb.kaleidoscopecookery.loot;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModLootModifier;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public class AdvanceEntityMatchTool implements LootItemCondition {
    private final EquipmentSlot slot;
    private final ItemPredicate predicate;

    public AdvanceEntityMatchTool(EquipmentSlot slot, ItemPredicate predicate) {
        this.slot = slot;
        this.predicate = predicate;
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootModifier.ADVANCE_ENTITY_MATCH_TOOL;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.KILLER_ENTITY);
    }

    @Override
    public boolean test(LootContext context) {
        if (context.hasParam(LootContextParams.KILLER_ENTITY)) {
            Entity entity = context.getParam(LootContextParams.KILLER_ENTITY);
            if (entity instanceof LivingEntity livingEntity) {
                ItemStack stack = livingEntity.getItemBySlot(this.slot);
                return this.predicate.matches(stack);
            }
        }
        return false;
    }

    public static LootItemCondition.Builder toolMatches(EquipmentSlot slot, ItemPredicate builder) {
        return () -> new AdvanceEntityMatchTool(slot, builder);
    }

    public static class AdvanceEntityMatchToolSerializer implements Serializer<AdvanceEntityMatchTool> {
        @Override
        public void serialize(JsonObject object, AdvanceEntityMatchTool matchTool, JsonSerializationContext context) {
            object.addProperty("slot", matchTool.slot.getName());
            object.add("predicate", matchTool.predicate.serializeToJson());
        }

        @Override
        public AdvanceEntityMatchTool deserialize(JsonObject object, JsonDeserializationContext context) {
            EquipmentSlot slot = EquipmentSlot.byName(object.get("slot").getAsString());
            ItemPredicate itemPredicate = ItemPredicate.fromJson(object.get("predicate"));
            return new AdvanceEntityMatchTool(slot, itemPredicate);
        }
    }
}
