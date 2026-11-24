package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class CandiedSkewerItem extends BlockItem implements IHasContainer {
    private static final ResourceLocation KNOCKBACK_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(
            KaleidoscopeCookery.MOD_ID, "candied_skewer_knockback"
    );
    private final @Nullable Holder<MobEffect> attackEffect;
    private final int attackEffectDuration;
    private final Supplier<Block> sideBlock;

    public CandiedSkewerItem(Supplier<Block> uprightBlock, Supplier<Block> sideBlock, FoodProperties foodProperties) {
        this(uprightBlock, sideBlock, foodProperties, null, 0);
    }

    public CandiedSkewerItem(Supplier<Block> uprightBlock, Supplier<Block> sideBlock, FoodProperties foodProperties,
                             @Nullable Holder<MobEffect> attackEffect, int attackEffectDuration) {
        super(
                uprightBlock.get(),
                new Properties().food(foodProperties).attributes(
                        ItemAttributeModifiers.builder().add(
                                Attributes.ATTACK_KNOCKBACK,
                                new AttributeModifier(KNOCKBACK_MODIFIER_ID, 3, ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).build()
                )
        );
        this.sideBlock = sideBlock;
        this.attackEffect = attackEffect;
        this.attackEffectDuration = attackEffectDuration;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || player.isSecondaryUseActive()) {
            return super.useOn(context);
        }
        return InteractionResult.PASS;
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        Direction face = context.getClickedFace();

        if (face == Direction.UP) {
            Block target = this.getBlock();
            return target.defaultBlockState().setValue(FACING,
                    context.getHorizontalDirection().getOpposite());
        }

        if (face.getAxis().isHorizontal()) {
            Block target = this.sideBlock.get();
            return target.defaultBlockState().setValue(FACING, face);
        }

        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(this);
        tooltip.add(Component.translatable(key.toLanguageKey("tooltip"))
                .withStyle(ChatFormatting.DARK_GRAY)
                .withStyle(ChatFormatting.ITALIC));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.attackEffect != null && !target.level().isClientSide) {
            target.addEffect(new MobEffectInstance(this.attackEffect, this.attackEffectDuration), attacker);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack itemStack = super.finishUsingItem(stack, level, entity);
        return this.returnContainerToEntity(itemStack, level, entity);
    }

    @Override
    public Item getContainerItem() {
        return Items.STICK;
    }
}
