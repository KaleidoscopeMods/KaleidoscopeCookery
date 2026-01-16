package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FoodBiteAnimateTicks {
    public static final AnimateTick DARK_CUISINE_ANIMATE_TICK = new AnimateTick() {
        @Override
        public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
            // 50% 概率不产生粒子
            if (random.nextInt(2) != 0) {
                return;
            }

            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;

            level.addParticle(ParticleTypes.SMOKE,
                    x + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    y + random.nextDouble() / 3,
                    z + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    0, 0.02, 0);
        }
    };

    public static final AnimateTick SUSPICIOUS_STIR_FRY_ANIMATE_TICK = new AnimateTick() {
        @Override
        public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
            // 66% 概率不产生粒子
            if (random.nextInt(3) != 0) {
                return;
            }

            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;

            level.addParticle(ParticleTypes.ENTITY_EFFECT,
                    x + random.nextDouble() / 5 * (random.nextBoolean() ? 1 : -1),
                    y + random.nextDouble() / 5,
                    z + random.nextDouble() / 5 * (random.nextBoolean() ? 1 : -1),
                    104 / 255f, 54 / 255f, 128 / 255f);
        }
    };

    public static final AnimateTick POT_SOUP_ANIMATE_TICK = new AnimateTick() {
        @Override
        public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
            // 50% 概率不产生粒子
            if (random.nextInt(2) != 0) {
                return;
            }

            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;

            level.addParticle(ModParticles.COOKING.get(),
                    x + random.nextDouble() / 4 * (random.nextBoolean() ? 1 : -1),
                    y + random.nextDouble() / 4,
                    z + random.nextDouble() / 4 * (random.nextBoolean() ? 1 : -1),
                    0, 0.03, 0);
        }
    };

    public interface AnimateTick {
        default void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        }
    }
}
