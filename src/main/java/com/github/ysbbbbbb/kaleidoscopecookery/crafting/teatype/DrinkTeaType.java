package com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype.ITeaType;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.TeaDrinkBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.TeacupBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class DrinkTeaType implements ITeaType {
    protected final ResourceLocation name;
    protected final int barColor;
    protected final TeaDrinkBlock teaDrinkBlock;

    public DrinkTeaType(ResourceLocation name, int barColor, TeaDrinkBlock teaDrinkBlock) {
        this.name = name;
        this.barColor = barColor;
        this.teaDrinkBlock = teaDrinkBlock;
    }

    @Override
    public ResourceLocation getName() { return this.name; }

    @Override
    public int getBarColor() { return this.barColor; }

    @Override
    public ItemStack getDisplayStack() {
        return this.teaDrinkBlock.asItem().getDefaultInstance();
    }

    @Override
    public int onPouredOnBlock(Level level, BlockHitResult hit, @Nullable LivingEntity user, ItemStack teapot) {
        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof TeacupBlock block) {
            block.tryPourTea(level, pos, state, this);
        }

        return 1;
    }

    @Override
    public int onPouredOnEntity(Level level, LivingEntity entity, @Nullable LivingEntity user, ItemStack teapot) {
        return 0;
    }

    public TeaDrinkBlock getBlock() { return this.teaDrinkBlock; }
}
