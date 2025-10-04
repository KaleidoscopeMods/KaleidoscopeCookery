package com.github.ysbbbbbb.kaleidoscopecookery.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MobBucketItem.class)
public class MobBucketItemMixin {
    @Inject(method = "checkExtraContent", at = @At("RETURN"))
    private void onCheckExtraContent(Player player, Level level, ItemStack containerStack, BlockPos pos, CallbackInfo ci) {
        if (level.isClientSide) {
            return;
        }
        // 检查桶内鱼类是否是所需的
        if (!(this instanceof MobBucketItemAccessor accessor) || !accessor.kaleidoscope$GetFishType().is(TagMod.RICE_GROWTH_BOOSTER)) {
            return;
        }
        // 搜索周围 3x3x1 范围，看有没有水稻
        BlockPos.MutableBlockPos mutable = pos.mutable();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (level.getBlockState(mutable.offset(x, 0, z)).is(ModBlocks.RICE_CROP.get())) {
                    ModTrigger.EVENT.trigger(player, ModEventTriggerType.PLACE_FISH_IN_RICE_FIELD);
                    return;
                }
            }
        }
    }
}
