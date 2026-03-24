package com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype.ITeaType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class WaterTeaType implements ITeaType {
    protected final ResourceLocation name;
    protected final int barColor;

    public WaterTeaType(ResourceLocation name, int barColor) {
        this.name = name;
        this.barColor = barColor;
    }

    @Override
    public ResourceLocation getName() { return this.name; }

    @Override
    public int getBarColor() { return this.barColor; }

    @Override
    public ItemStack getDisplayStack() {
        return Items.WATER_BUCKET.getDefaultInstance();
    }

    @Override
    public int onPouredOnBlock(Level level, BlockHitResult hit, @Nullable LivingEntity user, ItemStack teapot) {
        return 1;
    }

    @Override
    public int onPouredOnEntity(Level level, LivingEntity entity, @Nullable LivingEntity user, ItemStack teapot) {
        if (entity.isOnFire()) {
            entity.clearFire();
        }
        return 1;
    }
}
