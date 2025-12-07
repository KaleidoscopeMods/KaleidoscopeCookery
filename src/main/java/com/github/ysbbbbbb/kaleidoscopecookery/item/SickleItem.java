package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.common.SimpleTier;

import java.util.List;

public class SickleItem extends SwordItem {
    private static final SimpleTier SICKLE_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            2000, // 耐久度
            4.0F, // 挖掘速度
            1.0F, // 伤害加成
            5, // 附魔值
            () -> Ingredient.of(Items.FLINT)
    );

    public SickleItem() {
        super(SICKLE_TIER, new Properties());
    }

    @Override
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        // 生成挥动音效和粒子
        Player player = context.getPlayer();
        if (player == null) {
            return super.useOn(context);
        }
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = context.getClickedPos();
        int breakCount = 0;
        // 搜索方块的 5x5x2 范围内的可收割作物、草丛、灌木等并收割
        for (int x = -2; x <= 2; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (harvest(pos, x, y, z, level, player)) {
                        breakCount++;
                    }
                }
            }
        }

        ItemStack stack = context.getItemInHand();
        serverLevel.playSound(null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(),
                1.0F, 1.0F);
        player.sweepAttack();
        stack.hurtAndBreak(breakCount, player, EquipmentSlot.MAINHAND);
        player.getCooldowns().addCooldown(this, 10);
        return InteractionResult.SUCCESS;
    }

    private boolean harvest(BlockPos pos, int x, int y, int z, Level level, Player player) {
        BlockPos newPos = pos.offset(x, y, z);
        if (!level.mayInteract(player, newPos)) {
            return false;
        }
        BlockState blockState = level.getBlockState(newPos);
        if (blockState.isAir()) {
            return false;
        }

        Block block = blockState.getBlock();

        // 如果是作物，那么检查是否成熟
        if (block instanceof CropBlock cropBlock) {
            // 水稻特判
            if (block instanceof RiceCropBlock) {
                int position = blockState.getValue(RiceCropBlock.LOCATION);
                newPos = newPos.below(position);
                blockState = level.getBlockState(newPos);
            }
            if (cropBlock.isMaxAge(blockState)) {
                // 成熟则收割
                cropBlock.playerDestroy(level, player, newPos, blockState, null, ItemStack.EMPTY);
                BlockState stateForAge = cropBlock.getStateForAge(0);
                // 同步水属性状态
                BooleanProperty waterlogged = BlockStateProperties.WATERLOGGED;
                if (stateForAge.hasProperty(waterlogged)) {
                    stateForAge = stateForAge.setValue(waterlogged, blockState.getValue(waterlogged));
                }
                level.setBlock(newPos, stateForAge, Block.UPDATE_ALL);
                level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, newPos, Block.getId(blockState));
                return true;
            }
            return false;
        }

        // 如果是灌木，直接破坏
        if (block instanceof BushBlock) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.gameMode.destroyBlock(newPos);
                level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, newPos, Block.getId(blockState));
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.kaleidoscope_cookery.sickle").withStyle(ChatFormatting.GRAY));
    }
}
