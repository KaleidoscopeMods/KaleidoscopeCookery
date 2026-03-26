package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.TeaDrinkBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.TeacupBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

public class TeaDrinkBlockItem extends TeacupBlockItem implements IHasContainer {
    public TeaDrinkBlockItem(Block block) {
        super(block, new Properties()
                .stacksTo(16)
                .craftRemainder(ModItems.TEACUP.get()));
    }

    public TeaDrinkBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }
        // this.addDrinkEffect(stack, level, entity);
        if (entity instanceof Player player && !player.isCreative()) {
            stack.shrink(1);
        }
        return returnContainerToEntity(stack, level, entity);
    }

    @Override
    protected boolean tryIncreaseCount(Block self, BlockState state, Level level, BlockPos pos, ItemStack stack, Player player) {
        if (!(state.getBlock() instanceof TeacupBlock teacup)) {
            return false;
        }

        if (!(self instanceof TeaDrinkBlock drink)) {
            return false;
        }

        // 尝试增加堆叠数
        if (teacup.tryIncreaseCount(level, pos, state, stack, false)) {
            // 播放音效
            SoundType soundType = state.getSoundType(level, pos, player);
            SoundEvent sound = this.getPlaceSound(state, level, pos, player);
            level.playSound(
                    player, pos, sound, SoundSource.BLOCKS,
                    (soundType.getVolume() + 1) / 2f,
                    soundType.getPitch() * 0.8f
            );
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            return true;
        }

        // 如果全空且存在空位
        if (teacup.isAllEmpty(state) && state.getValue(teacup.getCountProperty()) < Math.min(teacup.getMaxCount(), drink.getMaxCount())) {
            teacup.transformToDrink(level, pos, state.cycle(teacup.getCountProperty()), drink, 1);
            // 播放音效
            SoundType soundType = state.getSoundType(level, pos, player);
            SoundEvent sound = this.getPlaceSound(state, level, pos, player);
            level.playSound(
                    player, pos, sound, SoundSource.BLOCKS,
                    (soundType.getVolume() + 1) / 2f,
                    soundType.getPitch() * 0.8f
            );
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            return true;
        }

        return false;
    }

    @Override
    public Item getContainerItem() {
        return ModItems.TEACUP.get();
    }

    public ItemStack returnContainerToEntity(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack carried = this.getContainerItem().getDefaultInstance();
        if (stack.isEmpty()) {
            return carried;
        }
        if (entity instanceof Player player) {
            ItemHandlerHelper.giveItemToPlayer(player, carried);
        } else {
            ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), carried);
            level.addFreshEntity(itemEntity);
        }
        return stack;
    }
}
