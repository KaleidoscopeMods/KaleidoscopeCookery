package com.github.ysbbbbbb.kaleidoscopecookery.crafting.teafluid;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teafluid.ITeaFluid;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.TeaBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.TeacupBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DrinkTeaFluid implements ITeaFluid {
    protected final ResourceLocation name;
    protected final int barColor;
    protected final TeaBlock teaBlock;

    public DrinkTeaFluid(ResourceLocation name, int barColor, TeaBlock teaBlock) {
        this.name = name;
        this.barColor = barColor;
        this.teaBlock = teaBlock;
    }

    @Override
    public ResourceLocation getName() { return this.name; }

    @Override
    public int getBarColor() { return this.barColor; }

    @Override
    public ItemStack getDisplayStack() {
        return this.teaBlock.asItem().getDefaultInstance();
    }

    @Override
    public boolean spawnParticles() {
        return true;
    }

    @Override
    public boolean instantPouring(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof TeacupBlock teacup && teacup.tryPourTeaOn(level, pos, state, this, true);
    }

    @Override
    public boolean isTeaBase() {
        return false;
    }

    @Override
    public int onPouredOnBlock(Level level, BlockHitResult hit, @Nullable LivingEntity user, ItemStack teapot) {
        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof TeacupBlock block) {
            if (block.tryPourTeaOn(level, pos, state, this, false)) {
                level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS);

                if (level instanceof ServerLevel serverLevel) {
                    Vec3 vec3 = pos.getCenter();
                    serverLevel.sendParticles(ParticleTypes.GLOW,
                            vec3.x(), vec3.y(), vec3.z(),
                            10,
                            (level.random.nextFloat() - 0.5) * 0.05F,
                            (level.random.nextFloat() - 0.5) * 0.05F,
                            (level.random.nextFloat() - 0.5) * 0.05F,
                            0.02);
                }
            }
        }

        return 1;
    }

    @Override
    public int onPouredOnEntity(Level level, LivingEntity entity, @Nullable LivingEntity user, ItemStack teapot) {
        return 0;
    }

    public TeaBlock getBlock() { return this.teaBlock; }
}
