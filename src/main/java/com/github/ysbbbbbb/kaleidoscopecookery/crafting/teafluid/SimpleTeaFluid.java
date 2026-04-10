package com.github.ysbbbbbb.kaleidoscopecookery.crafting.teafluid;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teafluid.ITeaFluid;
import com.mojang.datafixers.util.Function4;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SimpleTeaFluid implements ITeaFluid {
    protected final ResourceLocation name;
    protected final int barColor;
    protected final ItemStack displayStack;
    protected final Predicate<ItemStack> predicate;
    protected final boolean isTeaBase;
    protected final Function4<Level, BlockHitResult, LivingEntity, ItemStack, Integer> pouredOnBlockFunction;
    protected final Function4<Level, LivingEntity, LivingEntity, ItemStack, Integer> pouredOnEntityFunction;

    public SimpleTeaFluid(
            ResourceLocation name,
            int barColor,
            ItemStack displayStack,
            Predicate<ItemStack> predicate,
            boolean isTeaBase,
            Function4<Level, BlockHitResult, LivingEntity, ItemStack, Integer> pouredOnBlockFunction,
            Function4<Level, LivingEntity, LivingEntity, ItemStack, Integer> pouredOnEntityFunction
    ) {
        this.name = name;
        this.barColor = barColor;
        this.displayStack = displayStack;
        this.predicate = predicate;
        this.isTeaBase = isTeaBase;
        this.pouredOnBlockFunction = pouredOnBlockFunction;
        this.pouredOnEntityFunction = pouredOnEntityFunction;
    }

    @Override
    public ResourceLocation getName() { return this.name; }

    @Override
    public int getBarColor() { return this.barColor; }

    @Override
    public ItemStack getDisplayStack() {
        return displayStack;
    }

    @Override
    public boolean isTeaFluid(ItemStack stack) {
        return this.predicate.test(stack);
    }

    @Override
    public boolean isTeaBase() {
        return isTeaBase;
    }

    @Override
    public int onPouredOnBlock(Level level, BlockHitResult hit, @Nullable LivingEntity user, ItemStack teapot) {
        return pouredOnBlockFunction.apply(level, hit, user, teapot);
    }

    @Override
    public int onPouredOnEntity(Level level, LivingEntity entity, @Nullable LivingEntity user, ItemStack teapot) {
        return pouredOnEntityFunction.apply(level, entity, user, teapot);
    }
}
