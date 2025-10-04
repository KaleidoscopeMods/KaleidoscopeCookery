package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SteamerItem extends BlockItem {
    public static final ResourceLocation HAS_ITEMS = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "has_items");
    private static final int NONE = 0;
    private static final int HAS = 1;

    public SteamerItem() {
        super(ModBlocks.STEAMER.get(), new Item.Properties());
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        Level level = context.getLevel();
        Direction face = context.getClickedFace();
        BlockPos clickedPos = context.getClickedPos();
        // 点击顶部才能放置
        if (face != Direction.UP) {
            return false;
        }
        BlockEntity blockEntity = level.getBlockEntity(clickedPos);
        ItemStack stack = context.getItemInHand();
        if (blockEntity instanceof SteamerBlockEntity steamer && stack.is(this) && stack.getCount() == 1) {
            steamer.mergeItem(stack);
        }
        return super.placeBlock(context, state);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        if (stack.hasTag()) {
            return 1;
        }
        return super.getMaxStackSize(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public static float getTexture(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        CompoundTag data = BlockItem.getBlockEntityData(stack);
        if (data != null) {
            return HAS;
        }
        return NONE;
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.steamer").withStyle(ChatFormatting.GRAY));
    }
}
