package com.github.ysbbbbbb.kaleidoscopecookery.item.quality;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum Quality implements StringRepresentable {
    /**
     * 极佳、优秀、普通、生疏
     */
    SUPERB(0, "superb", ChatFormatting.GOLD, 0.95, 1.2),
    EXCELLENT(1, "excellent", ChatFormatting.GREEN, 0.82, 0.9),
    STANDARD(2, "standard", ChatFormatting.WHITE, 0.55, 0.6),
    POOR(3, "poor", ChatFormatting.DARK_GRAY, 0, 0.3);

    public static final IntFunction<Quality> BY_ID = ByIdMap.continuous(
            Quality::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO
    );

    private final int id;
    private final String name;
    private final ChatFormatting color;
    private final double score;
    private final double ratio;

    Quality(int id, String name, ChatFormatting color, double score, double ratio) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.score = score;
        this.ratio = ratio;
    }

    public int getId() {
        return id;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }

    public ChatFormatting getColor() {
        return color;
    }

    public double getScore() {
        return score;
    }

    public double getRatio() {
        return ratio;
    }

    public MutableComponent getTooltip() {
        String key = "tooltip.kaleidoscope_cookery.cuisine_quality.%s".formatted(this.name);
        return Component.translatable(key).withStyle(this.color);
    }
}
