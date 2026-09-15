package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class TeaBannerBlockEntity extends BaseBlockEntity {
    public static final String COLOR_TAG = "BaseColor";
    public static final String PATTERN_ITEM_TAG = "PatternItem";

    private DyeColor color = DyeColor.RED;
    private ItemStack patternItem = ItemStack.EMPTY;

    public TeaBannerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlocks.TEA_BANNER_BE.get(), pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(COLOR_TAG, this.color.getId());
        if (!this.patternItem.isEmpty()) {
            tag.put(PATTERN_ITEM_TAG, this.patternItem.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.color = tag.contains(COLOR_TAG, Tag.TAG_INT) ? DyeColor.byId(tag.getInt(COLOR_TAG)) : DyeColor.RED;
        this.patternItem = tag.contains(PATTERN_ITEM_TAG, Tag.TAG_COMPOUND)
                ? ItemStack.of(tag.getCompound(PATTERN_ITEM_TAG))
                : ItemStack.EMPTY;
        if (!isSupportedPattern(this.patternItem)) {
            this.patternItem = ItemStack.EMPTY;
        }
    }

    public DyeColor getColor() {
        return this.color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
        this.refresh();
    }

    public ItemStack getPatternItem() {
        return this.patternItem;
    }

    public boolean hasPattern() {
        return !this.patternItem.isEmpty();
    }

    public ItemStack setPatternItem(ItemStack patternItem) {
        ItemStack previousPattern = this.patternItem;
        this.patternItem = patternItem.copyWithCount(1);
        this.refresh();
        return previousPattern;
    }

    public ItemStack removePatternItem() {
        ItemStack previousPattern = this.patternItem;
        this.patternItem = ItemStack.EMPTY;
        this.refresh();
        return previousPattern;
    }

    public ItemStack createItemStack() {
        ItemStack stack = new ItemStack(ModItems.TEA_BANNER.get());
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        BlockItem.setBlockEntityData(stack, ModBlocks.TEA_BANNER_BE.get(), tag);
        return stack;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.worldPosition).inflate(3);
    }

    public static DyeColor getColor(CompoundTag tag) {
        return tag.contains(COLOR_TAG, Tag.TAG_INT) ? DyeColor.byId(tag.getInt(COLOR_TAG)) : DyeColor.RED;
    }

    public static ItemStack getPatternItem(CompoundTag tag) {
        if (!tag.contains(PATTERN_ITEM_TAG, Tag.TAG_COMPOUND)) {
            return ItemStack.EMPTY;
        }
        ItemStack pattern = ItemStack.of(tag.getCompound(PATTERN_ITEM_TAG));
        return isSupportedPattern(pattern) ? pattern : ItemStack.EMPTY;
    }

    public static boolean isSupportedPattern(ItemStack stack) {
        return getPatternTexture(stack) != null;
    }

    @Nullable
    public static String getPatternTexture(ItemStack stack) {
        if (stack.is(Items.CREEPER_BANNER_PATTERN)) {
            return "creeper";
        }
        if (stack.is(Items.SKULL_BANNER_PATTERN)) {
            return "skull";
        }
        if (stack.is(Items.FLOWER_BANNER_PATTERN)) {
            return "flower";
        }
        if (stack.is(Items.MOJANG_BANNER_PATTERN)) {
            return "mojang";
        }
        if (stack.is(Items.GLOBE_BANNER_PATTERN)) {
            return "globe";
        }
        if (stack.is(Items.PIGLIN_BANNER_PATTERN)) {
            return "piglin";
        }
        return null;
    }
}
