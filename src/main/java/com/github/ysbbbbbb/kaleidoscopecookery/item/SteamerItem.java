package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SteamerItem extends BlockItem {
    public static final ResourceLocation HAS_ITEMS = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "has_items");
    private static final int NONE = 0;
    private static final int HAS = 1;

    public SteamerItem() {
        super(ModBlocks.STEAMER.get(), new Item.Properties());
    }

    @OnlyIn(Dist.CLIENT)
    public static float getTexture(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (stack.has(DataComponents.BLOCK_ENTITY_DATA)) {
            return HAS;
        }
        return NONE;
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
        boolean hasData = stack.has(DataComponents.BLOCK_ENTITY_DATA);
        if (blockEntity instanceof SteamerBlockEntity steamer && stack.is(this) && hasData && stack.getCount() == 1) {
            steamer.mergeItem(stack, context.getLevel());
        }
        return super.placeBlock(context, state);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || data == null || !data.contains(SteamerBlockEntity.ITEMS_TAG)) {
            return Optional.empty();
        }
        NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(data.copyTag(), items, level.registryAccess());
        ItemStackHandler handler = new ItemStackHandler(items);
        return Optional.of(new ItemContainerTooltip(handler));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.steamer").withStyle(ChatFormatting.GRAY));
    }
}
