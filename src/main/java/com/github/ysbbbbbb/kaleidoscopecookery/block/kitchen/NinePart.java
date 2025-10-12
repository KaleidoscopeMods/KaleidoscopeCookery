package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;

public enum NinePart implements StringRepresentable {
    LEFT_UP("left_up", -1, -1),
    UP("up", 0, -1),
    RIGHT_UP("right_up", 1, -1),
    LEFT_CENTER("left_center", -1, 0),
    CENTER("center", 0, 0),
    RIGHT_CENTER("right_center", 1, 0),
    LEFT_DOWN("left_down", -1, 1),
    DOWN("down", 0, 1),
    RIGHT_DOWN("right_down", 1, 1);

    private final String name;
    private final int posX;
    private final int posY;

    NinePart(String name, int posX, int posY) {
        this.name = name;
        this.posX = posX;
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    @Override
    public String toString() {
        return String.format("%s[%d, %d]", this.getSerializedName(), this.posX, this.posY);
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public boolean isCenter() {
        return this == CENTER;
    }

    @Nullable
    public static NinePart getPartByPos(int x, int y) {
        for (NinePart part : NinePart.values()) {
            if (part.getPosX() == x && part.getPosY() == y) {
                return part;
            }
        }
        return null;
    }
}
