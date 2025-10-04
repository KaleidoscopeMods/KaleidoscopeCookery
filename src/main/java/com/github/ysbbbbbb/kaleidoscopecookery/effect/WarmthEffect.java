package com.github.ysbbbbbb.kaleidoscopecookery.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class WarmthEffect extends BaseEffect {
    public WarmthEffect(int color) {
        super(color);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // 为了避免卡顿，每秒检查一次
        return duration % 20 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getHealth() >= livingEntity.getMaxHealth()) {
            return;
        }
        // 当玩家周围 5x5x3 范围内有热源时，恢复玩家生命值
        BlockPos.MutableBlockPos mutable = livingEntity.blockPosition().mutable();
        for (int x = -2; x <= 2; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockState blockState = livingEntity.level().getBlockState(mutable.offset(x, y, z));
                    boolean hasLit = blockState.hasProperty(BlockStateProperties.LIT) && blockState.getValue(BlockStateProperties.LIT);
                    if (hasLit || blockState.is(TagMod.WARMTH_HEAT_SOURCE_BLOCKS)) {
                        livingEntity.heal(1);
                        // 找到热源后立即返回，避免重复恢复
                        return;
                    }
                }
            }
        }
        // 如果玩家在下界，那么缓慢恢复
        if (livingEntity.level().dimension().equals(Level.NETHER)) {
            // 缓慢的话。那就概率恢复
            if (livingEntity.getRandom().nextInt(4) == 0) {
                livingEntity.heal(0.5F);
            }
        }
    }
}
