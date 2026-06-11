package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

public abstract class StockpotMatchRecipeEvent extends Event {
    private final Level level;
    private final StockpotBlockEntity stockpot;
    private final StockpotInput input;

    private @Nullable RecipeHolder<StockpotRecipe> output = null;

    public StockpotMatchRecipeEvent(Level level, StockpotBlockEntity stockpot, StockpotInput input) {
        this.level = level;
        this.stockpot = stockpot;
        this.input = input;
    }

    public Level getLevel() {
        return level;
    }

    public StockpotBlockEntity getStockpot() {
        return stockpot;
    }

    public StockpotInput getInput() {
        return input;
    }

    /**
     * 如果返回为 null，则表示不修改配方输出
     */
    @Nullable
    public RecipeHolder<StockpotRecipe> getOutput() {
        return output;
    }

    public void setOutput(@Nullable RecipeHolder<StockpotRecipe> output) {
        this.output = output;
    }

    /**
     * 在汤锅检索自己配方之前触发
     */
    public static class Pre extends StockpotMatchRecipeEvent {
        public Pre(Level level, StockpotBlockEntity stockpot, StockpotInput container) {
            super(level, stockpot, container);
        }
    }

    /**
     * 在汤锅检索自己配方之后触发
     */
    public static class Post extends StockpotMatchRecipeEvent {
        private final ResourceLocation rawOutput;

        public Post(Level level, StockpotBlockEntity stockpot, StockpotInput container, ResourceLocation rawOutput) {
            super(level, stockpot, container);
            this.rawOutput = rawOutput;
        }

        /**
         * 原来匹配的配方输出
         * <p>
         * 如果原来没有匹配到配方，那么该值为 StockpotRecipeSerializer.EMPTY_ID
         */
        public ResourceLocation getRawOutput() {
            return rawOutput;
        }
    }
}
