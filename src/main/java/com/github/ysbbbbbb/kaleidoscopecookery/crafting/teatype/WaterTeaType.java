package com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype.ITeaType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
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
        if (level instanceof ServerLevel serverLevel) {
            Vec3 pos = hit.getLocation();
            RandomSource random = level.getRandom();
            serverLevel.sendParticles(ParticleTypes.RAIN,
                    pos.x(), pos.y() + 0.1, pos.z(),
                    10,
                    (random.nextFloat() - 0.5) * 0.05F,
                    (random.nextFloat() - 0.5) * 0.05F,
                    (random.nextFloat() - 0.5) * 0.05F,
                    0.02);
        }
        return 12;
    }

    @Override
    public int onPouredOnEntity(Level level, LivingEntity entity, @Nullable LivingEntity user, ItemStack teapot) {
        if (entity.isOnFire()) {
            entity.clearFire();
        }

        if (level instanceof ServerLevel serverLevel) {
            Vec3 pos = entity.position();
            RandomSource random = level.getRandom();
            serverLevel.sendParticles(ParticleTypes.RAIN,
                    pos.x(), pos.y() + entity.getBbHeight(), pos.z(),
                    10,
                    (random.nextFloat() - 0.5) * 0.05F,
                    (random.nextFloat() - 0.5) * 0.05F,
                    (random.nextFloat() - 0.5) * 0.05F,
                    0.02);
        }
        return 12;
    }
}
