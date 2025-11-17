package com.github.ysbbbbbb.kaleidoscopecookery.loot;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModLootModifier;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class AdvanceEntityMatchTool implements LootItemCondition {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "advance_entity_match_tool");
    public static final MapCodec<AdvanceEntityMatchTool> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EquipmentSlot.CODEC.fieldOf("slot").forGetter(tool -> tool.slot),
            ItemPredicate.CODEC.fieldOf("predicate").forGetter(tool -> tool.predicate)
    ).apply(instance, AdvanceEntityMatchTool::new));

    private final EquipmentSlot slot;
    private final ItemPredicate predicate;

    public AdvanceEntityMatchTool(EquipmentSlot slot, ItemPredicate predicate) {
        this.slot = slot;
        this.predicate = predicate;
    }

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootModifier.ADVANCE_ENTITY_MATCH_TOOL;
    }

    @Override
    public @NotNull Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.LAST_DAMAGE_PLAYER);
    }

    @Override
    public boolean test(LootContext context) {
        if (context.hasParam(LootContextParams.LAST_DAMAGE_PLAYER)) {
            Player player = context.getParam(LootContextParams.LAST_DAMAGE_PLAYER);
            ItemStack stack = player.getItemBySlot(this.slot);
            return this.predicate.test(stack);
        }
        return false;
    }

    public static Builder toolMatches(EquipmentSlot slot, ItemPredicate builder) {
        return () -> new AdvanceEntityMatchTool(slot, builder);
    }
}
