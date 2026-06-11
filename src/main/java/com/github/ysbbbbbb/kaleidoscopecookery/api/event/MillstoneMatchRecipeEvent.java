package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

public abstract class MillstoneMatchRecipeEvent extends Event {
    private final Level level;
    private final MillstoneBlockEntity millstone;
    private final SimpleInput input;

    private @Nullable RecipeHolder<MillstoneRecipe> output = null;

    public MillstoneMatchRecipeEvent(Level level, MillstoneBlockEntity millstone, SimpleInput input) {
        this.level = level;
        this.millstone = millstone;
        this.input = input;
    }

    public Level getLevel() {
        return level;
    }

    public MillstoneBlockEntity getMillstone() {
        return millstone;
    }

    public SimpleInput getInput() {
        return input;
    }

    /**
     * 如果返回为 null，则表示不修改配方输出
     */
    @Nullable
    public RecipeHolder<MillstoneRecipe> getOutput() {
        return output;
    }

    public void setOutput(@Nullable RecipeHolder<MillstoneRecipe> output) {
        this.output = output;
    }

    /**
     * 在石磨检索自己配方之前触发
     */
    public static class Pre extends MillstoneMatchRecipeEvent {
        public Pre(Level level, MillstoneBlockEntity millstone, SimpleInput container) {
            super(level, millstone, container);
        }
    }

    /**
     * 在石磨检索自己配方之后触发
     */
    public static class Post extends MillstoneMatchRecipeEvent {
        private final RecipeHolder<MillstoneRecipe> rawOutput;

        public Post(Level level, MillstoneBlockEntity millstone, SimpleInput container, RecipeHolder<MillstoneRecipe> rawOutput) {
            super(level, millstone, container);
            this.rawOutput = rawOutput;
        }

        /**
         * 原来匹配的配方输出
         * <p>
         * 如果原来没有匹配到配方，那么输出的配方 ID 为 MillstoneRecipeSerializer.EMPTY_ID
         */
        public RecipeHolder<MillstoneRecipe> getRawOutput() {
            return rawOutput;
        }
    }
}
