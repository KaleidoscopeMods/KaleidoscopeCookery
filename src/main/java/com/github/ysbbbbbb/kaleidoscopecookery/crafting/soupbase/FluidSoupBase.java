package com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.api.client.render.ISoupBaseRender;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.soupbase.FluidSoupBaseRender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.SoundActions;

public class FluidSoupBase implements ISoupBase {
    protected final ResourceLocation name;
    protected final Item bucketItem;
    protected final Fluid fluid;
    protected final int bubbleColor;

    public FluidSoupBase(ResourceLocation name, Item bucketItem, int bubbleColor) {
        this(name, bucketItem, getBucketFluid(bucketItem), bubbleColor);
    }

    public FluidSoupBase(ResourceLocation name, Item bucketItem, Fluid fluid, int bubbleColor) {
        this.name = name;
        this.bucketItem = bucketItem;
        this.fluid = fluid;
        this.bubbleColor = bubbleColor;
    }

    private static Fluid getBucketFluid(Item bucketItem) {
        if (bucketItem instanceof BucketItem bucket) {
            return bucket.content;
        }
        throw new IllegalArgumentException("Item must be a bucket item when fluid is not specified!");
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public int getBubbleColor() {
        return bubbleColor;
    }

    @Override
    public ItemStack getDisplayStack() {
        return bucketItem.getDefaultInstance();
    }

    @Override
    public boolean isSoupBase(ItemStack stack) {
        return stack.is(bucketItem);
    }

    @Override
    public ItemStack getReturnContainer(Level level, LivingEntity user, ItemStack soupBase) {
        SoundEvent sound = fluid.getFluidType().getSound(user, SoundActions.BUCKET_EMPTY);
        if (sound != null) {
            Vec3 position = user.position();
            level.playSound(null, position.x(), position.y() + 0.5, position.z(),
                    sound, SoundSource.BLOCKS, 1, 1);
        }
        return new ItemStack(Items.BUCKET);
    }

    @Override
    public boolean isContainer(ItemStack stack) {
        return stack.is(Items.BUCKET);
    }

    @Override
    public ItemStack getReturnSoupBase(Level level, LivingEntity user, ItemStack container) {
        SoundEvent sound = fluid.getFluidType().getSound(user, SoundActions.BUCKET_FILL);
        if (sound != null) {
            Vec3 position = user.position();
            level.playSound(null, position.x(), position.y() + 0.5, position.z(),
                    sound, SoundSource.BLOCKS, 1, 1);
        }
        return this.bucketItem.getDefaultInstance();
    }

    public Fluid getFluid() {
        return fluid;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ISoupBaseRender getRender() {
        return new FluidSoupBaseRender(this.fluid);
    }
}
