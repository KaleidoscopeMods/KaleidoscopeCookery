package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.CompatRegistry;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BowlFoodBlockItem extends BlockItem implements IHasContainer {
    private final List<MobEffectInstance> effectInstances = Lists.newArrayList();

    public BowlFoodBlockItem(Block block, FoodProperties properties) {
        super(block, new Item.Properties().stacksTo(16).food(
                new FoodProperties(
                        properties.nutrition(),
                        properties.saturation(),
                        properties.canAlwaysEat(),
                        properties.eatSeconds(),
                        Optional.of(Items.BOWL.getDefaultInstance()),
                        properties.effects())
        ));
        properties.effects().forEach(effect -> {
            if (effect.probability() >= 1F) {
                effectInstances.add(effect.effect());
            }
        });
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (level instanceof ServerLevel serverLevel && this.getBlock() instanceof FoodBiteBlock foodBiteBlock) {
            LootParams.Builder builder = (new LootParams.Builder(serverLevel))
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(entity.blockPosition()))
                    .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, entity)
                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, null);
            BlockState state = foodBiteBlock.defaultBlockState().setValue(foodBiteBlock.getBites(), foodBiteBlock.getMaxBites());
            List<ItemStack> drops = getDrops(state, builder);
            drops.forEach(itemStack -> {
                if (itemStack.isEmpty()) {
                    return;
                }
                if (entity instanceof Player player) {
                    ItemHandlerHelper.giveItemToPlayer(player, itemStack);
                } else {
                    ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), itemStack);
                    level.addFreshEntity(itemEntity);
                }
            });
        }
        return super.finishUsingItem(stack, level, entity);
    }

    private List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        ResourceKey<LootTable> resourcekey = state.getBlock().getLootTable();
        if (resourcekey == BuiltInLootTables.EMPTY) {
            return Collections.emptyList();
        } else {
            LootParams lootParams = params.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
            ServerLevel serverLevel = lootParams.getLevel();
            LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(resourcekey);
            return lootTable.getRandomItems(lootParams);
        }
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (id != null) {
            String key = "tooltip.%s.%s.maxim".formatted(id.getNamespace(), id.getPath());
            tooltip.add(Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }
        if (!this.effectInstances.isEmpty() && CompatRegistry.SHOW_POTION_EFFECT_TOOLTIPS) {
            tooltip.add(CommonComponents.space());
            PotionContents.addPotionTooltip(this.effectInstances, tooltip::add, 1.0F, context.tickRate());
        }
    }

    @Override
    public Item getContainerItem() {
        return Items.BOWL;
    }
}
