package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.datamap.TeaEffectData;
import com.github.ysbbbbbb.kaleidoscopecookery.datamap.resources.TeaEffectDataReloadListener;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemHandlerHelper;

public class TeaBlockItem extends TeacupBlockItem implements IHasContainer {
    public TeaBlockItem(Block block) {
        super(block, new Properties()
                .stacksTo(16)
                .craftRemainder(ModItems.TEACUP.get()));
    }

    public TeaBlockItem(Block block, Properties properties) {
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
        this.addTeaEffect(stack, level, entity);
        if (entity instanceof Player player && !player.isCreative()) {
            stack.shrink(1);
        }
        return returnContainerToEntity(stack, level, entity);
    }

    protected void addTeaEffect(ItemStack stack, Level level, LivingEntity entity) {
        TeaEffectData effectData = TeaEffectDataReloadListener.INSTANCE.get(stack.getItem());
        if (effectData == null) {
            return;
        }
        for (var entry : effectData.effects()) {
            if (!level.isClientSide && level.random.nextFloat() < entry.probability()) {
                MobEffect effect = entry.effect();
                // json 里的持续时间是秒，但是内部游戏是 tick，需要转化
                int duration = entry.duration() * 20;
                int amplifier = entry.amplifier();
                MobEffectInstance instance = new MobEffectInstance(effect, duration, amplifier);
                entity.addEffect(instance);
            }
        }
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
