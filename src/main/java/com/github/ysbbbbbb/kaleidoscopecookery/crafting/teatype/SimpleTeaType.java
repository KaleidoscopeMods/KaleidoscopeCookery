package com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype.ITeaType;
import com.mojang.datafixers.util.Function4;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SimpleTeaType implements ITeaType {
    protected final ResourceLocation name;
    protected final int barColor;
    protected final ItemStack displayStack;
    protected final Function4<Level, BlockHitResult, LivingEntity, ItemStack, Integer> pouredOnBlockFunction;
    protected final Function4<Level, LivingEntity, LivingEntity, ItemStack, Integer> pouredOnEntityFunction;

    public SimpleTeaType(
            ResourceLocation name,
            int barColor,
            ItemStack displayStack,
            Function4<Level, BlockHitResult, LivingEntity, ItemStack, Integer> pouredOnBlockFunction,
            Function4<Level, LivingEntity, LivingEntity, ItemStack, Integer> pouredOnEntityFunction
    ) {
        this.name = name;
        this.barColor = barColor;
        this.displayStack = displayStack;
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
    public int onPouredOnBlock(Level level, BlockHitResult hit, @Nullable LivingEntity user, ItemStack teapot) {
        return pouredOnBlockFunction.apply(level, hit, user, teapot);
    }

    @Override
    public int onPouredOnEntity(Level level, LivingEntity entity, @Nullable LivingEntity user, ItemStack teapot) {
        return pouredOnEntityFunction.apply(level, entity, user, teapot);
    }

    public static Function4<Level, BlockHitResult, LivingEntity, ItemStack, Integer> simpleBlockFunc(int consumed) {
        return (l, h, u, i) -> consumed;
    }

    public static Function4<Level, LivingEntity, LivingEntity, ItemStack, Integer> simpleEntityFunc(int consumed) {
        return (l, e, u, i) -> consumed;
    }
}
