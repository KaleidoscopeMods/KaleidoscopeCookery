package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class StockpotMatchRecipeEvent {
    private final Level level;
    private final StockpotBlockEntity stockpot;
    private final StockpotContainer container;

    private @Nullable StockpotRecipe output = null;

    public StockpotMatchRecipeEvent(Level level, StockpotBlockEntity stockpot, StockpotContainer container) {
        this.level = level;
        this.stockpot = stockpot;
        this.container = container;
    }

    public Level getLevel() {
        return level;
    }

    public StockpotBlockEntity getStockpot() {
        return stockpot;
    }

    public StockpotContainer getContainer() {
        return container;
    }

    /**
     * 如果返回为 null，则表示不修改配方输出
     */
    @Nullable
    public StockpotRecipe getOutput() {
        return output;
    }

    public void setOutput(@Nullable StockpotRecipe output) {
        this.output = output;
    }

    /**
     * 在汤锅检索自己配方之前触发
     */
    public static class Pre extends StockpotMatchRecipeEvent {
        public Pre(Level level, StockpotBlockEntity stockpot, StockpotContainer container) {
            super(level, stockpot, container);
        }
    }

    /**
     * 在汤锅检索自己配方之后触发
     */
    public static class Post extends StockpotMatchRecipeEvent {
        private final StockpotRecipe rawOutput;

        public Post(Level level, StockpotBlockEntity stockpot, StockpotContainer container, StockpotRecipe rawOutput) {
            super(level, stockpot, container);
            this.rawOutput = rawOutput;
        }

        /**
         * 原来匹配的配方输出
         * <p>
         * 如果原来没有匹配到配方，那么输出的配方 ID 为 StockpotRecipeSerializer.EMPTY_ID
         */
        public StockpotRecipe getRawOutput() {
            return rawOutput;
        }
    }
}
