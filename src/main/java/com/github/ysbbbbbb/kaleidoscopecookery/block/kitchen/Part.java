package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import net.minecraft.util.StringRepresentable;

public enum Part implements StringRepresentable {
    SINGLE("single"),
    LEFT("left"),
    MIDDLE("middle"),
    RIGHT("right");

    private final String name;

    Part(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
