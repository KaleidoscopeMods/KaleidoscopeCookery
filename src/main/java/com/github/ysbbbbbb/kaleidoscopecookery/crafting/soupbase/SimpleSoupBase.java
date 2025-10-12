package com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.api.client.render.ISoupBaseRender;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.soupbase.SimpleSoupBaseRender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.Predicate;

public class SimpleSoupBase implements ISoupBase {
    protected final ResourceLocation name;

    protected final ItemStack displayStack;
    protected final ResourceLocation soupBaseTexture;
    protected final int bubbleColor;

    protected final Predicate<ItemStack> soupBasePredicate;
    protected final Predicate<ItemStack> containerPredicate;

    protected final TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnContainerFunction;
    protected final TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnSoupBaseFunction;

    public SimpleSoupBase(
            ResourceLocation name,
            ItemStack displayStack,
            ResourceLocation soupBaseTexture,
            int bubbleColor,
            Predicate<ItemStack> soupBasePredicate,
            Predicate<ItemStack> containerPredicate,
            TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnContainerFunction,
            TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnSoupBaseFunction
    ) {
        this.name = name;
        this.displayStack = displayStack;
        this.soupBaseTexture = soupBaseTexture;
        this.bubbleColor = bubbleColor;
        this.soupBasePredicate = soupBasePredicate;
        this.containerPredicate = containerPredicate;
        this.returnContainerFunction = returnContainerFunction;
        this.returnSoupBaseFunction = returnSoupBaseFunction;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public int getBubbleColor() {
        return bubbleColor;
    }

    @Override
    public ItemStack getDisplayStack() {
        return displayStack;
    }

    @Override
    public boolean isSoupBase(ItemStack stack) {
        return this.soupBasePredicate.test(stack);
    }

    @Override
    public ItemStack getReturnContainer(Level level, LivingEntity user, ItemStack soupBase) {
        return returnContainerFunction.apply(level, user, soupBase);
    }

    @Override
    public boolean isContainer(ItemStack stack) {
        return containerPredicate.test(stack);
    }

    @Override
    public ItemStack getReturnSoupBase(Level level, LivingEntity user, ItemStack container) {
        return returnSoupBaseFunction.apply(level, user, container);
    }

    @Override
    public ISoupBaseRender getRender() {
        return new SimpleSoupBaseRender(soupBaseTexture);
    }
}
