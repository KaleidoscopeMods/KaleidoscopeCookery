package com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype.ITeaType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class LavaTeaType implements ITeaType {
    protected final ResourceLocation name;
    protected final int barColor;

    public LavaTeaType(ResourceLocation name, int barColor) {
        this.name = name;
        this.barColor = barColor;
    }

    @Override
    public ResourceLocation getName() { return this.name; }

    @Override
    public int getBarColor() { return this.barColor; }

    @Override
    public ItemStack getDisplayStack() {
        return Items.LAVA_BUCKET.getDefaultInstance();
    }

    @Override
    public boolean spawnParticles() {
        return true;
    }

    @Override
    public int onPouredOnBlock(Level level, BlockHitResult hit, @Nullable LivingEntity user, ItemStack teapot) {
        BlockState blockstate = level.getBlockState(hit.getBlockPos());
        if (!CampfireBlock.canLight(blockstate) && !CandleBlock.canLight(blockstate) && !CandleCakeBlock.canLight(blockstate)) {
            BlockPos pos = hit.getBlockPos().relative(hit.getDirection());
            if (BaseFireBlock.canBePlacedAt(level, pos, user == null ? Direction.NORTH : user.getDirection())) {
                level.playSound(user, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState state = BaseFireBlock.getState(level, pos);
                level.setBlock(pos, state, 11);
                level.gameEvent(user, GameEvent.BLOCK_PLACE, hit.getBlockPos());
                level.playSound(null, hit.getBlockPos(), SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.PLAYERS);
                if (user instanceof ServerPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)user, pos, teapot);
                }
            }
        } else {
            BlockPos pos = hit.getBlockPos();
            level.playSound(user, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(pos, blockstate.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
            level.gameEvent(user, GameEvent.BLOCK_CHANGE, pos);
            level.playSound(null, hit.getBlockPos(), SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.PLAYERS);
        }

        return 1;
    }

    @Override
    public int onPouredOnEntity(Level level, LivingEntity entity, @Nullable LivingEntity user, ItemStack teapot) {
        entity.setSecondsOnFire(8);
        entity.hurt(entity.damageSources().lava(), 2);
        level.playSound(null, entity.blockPosition(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS);
        return 1;
    }
}
