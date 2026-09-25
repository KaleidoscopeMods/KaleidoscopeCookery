package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IChoppingBoard;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEnchantments;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class ChoppingBoardBlockEntity extends BaseBlockEntity implements IChoppingBoard {
    private static final String MODEL_ID = "ModelId";
    private static final String CURRENT_CUT_STACK = "CurrentCutStack";
    private static final String RESULT_ITEM = "ResultItem";
    private static final String MAX_CUT_COUNT = "MaxCutCount";
    private static final String CURRENT_CUT_COUNT = "CurrentCutCount";
    /**
     * 仅用于客户端渲染
     */
    public @Nullable ResourceLocation[] cacheModels = null;
    public @Nullable ResourceLocation previousModel = null;
    /**
     * 服务端客户端共通数据
     */
    private @Nullable ResourceLocation modelId = null;
    private int maxCutCount = 0;
    private int currentCutCount = 0;
    private ItemStack currentCutStack = ItemStack.EMPTY;
    private List<ItemStack> results = Lists.newArrayList();

    public ChoppingBoardBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlocks.CHOPPING_BOARD_BE.get(), pos, blockState);
    }

    public static void popResource(Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide && !stack.isEmpty()) {
            ItemEntity entity = new ItemEntity(level,
                    pos.getX() + 0.5,
                    pos.getY() + 0.25,
                    pos.getZ() + 0.5,
                    stack, 0, 0, 0);
            entity.setDefaultPickUpDelay();
            level.addFreshEntity(entity);
        }
    }

    @Override
    public boolean onPutItem(Level level, @Nullable LivingEntity user, ItemStack putOnItem) {
        if (!this.results.isEmpty()) {
            return false;
        }
        SimpleContainer container = new SimpleContainer(putOnItem);
        Optional<ChoppingBoardRecipe> recipeOptional = level.getRecipeManager()
                .getRecipeFor(ModRecipes.CHOPPING_BOARD_RECIPE, container, level);
        if (recipeOptional.isPresent()) {
            ChoppingBoardRecipe recipe = recipeOptional.get();
            this.modelId = recipe.getModelId();
            this.maxCutCount = recipe.getCutCount();
            this.currentCutCount = 0;
            this.currentCutStack = putOnItem.split(1);
            this.results = recipe.getResults();
            this.refresh();
            level.playSound(null, this.worldPosition,
                    SoundEvents.WOOD_PLACE,
                    SoundSource.BLOCKS,
                    1, 1.2F);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCutItem(Level level, @Nullable LivingEntity user, ItemStack cutterItem) {
        if (this.results.isEmpty()) {
            return false;
        }
        // 如果已经切完，执行取出逻辑
        if (this.currentCutCount >= this.maxCutCount) {
            for (ItemStack result : this.results) {
                popResource(level, worldPosition, result.copy());
            }
            this.resetBoardData();
            level.playSound(null, this.worldPosition,
                    SoundEvents.WOOD_PLACE,
                    SoundSource.BLOCKS,
                    1, 2 + level.random.nextFloat() * 0.2f);
            return true;
        } else if (cutterItem.is(TagMod.KITCHEN_KNIFE)) {
            // 否则，检测是否是刀具，进行切菜逻辑
            Enchantment quickKnife = ModEnchantments.QUICK_KNIFE.get();
            int enchantmentLevel = cutterItem.getEnchantmentLevel(quickKnife);
            enchantmentLevel = Mth.clamp(enchantmentLevel, 0, quickKnife.getMaxLevel());
            this.currentCutCount = Math.min(this.maxCutCount, this.currentCutCount + (1 << enchantmentLevel));
            this.playParticlesSound();
            this.refresh();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onTakeOut(Level level, LivingEntity user) {
        if (this.currentCutCount == 0 && !this.currentCutStack.isEmpty()) {
            if (user instanceof Player player) {
                ItemHandlerHelper.giveItemToPlayer(player, this.currentCutStack);
            } else {
                popResource(level, this.worldPosition, this.currentCutStack);
            }
            this.resetBoardData();
            level.playSound(null, this.worldPosition,
                    SoundEvents.ITEM_FRAME_REMOVE_ITEM,
                    SoundSource.BLOCKS,
                    1, 1.2f + level.random.nextFloat() * 0.2f);
            return true;
        }
        return false;
    }

    @Override
    public void playParticlesSound() {
        if (this.level instanceof ServerLevel serverLevel) {
            RandomSource random = serverLevel.getRandom();
            serverLevel.sendParticles(ParticleTypes.CRIT,
                    this.worldPosition.getX() + 0.25 + random.nextDouble() / 2,
                    this.worldPosition.getY() + 0.25,
                    this.worldPosition.getZ() + 0.25 + random.nextDouble() / 2,
                    2, 0, 0, 0, 0.1);
            serverLevel.playSound(null, this.worldPosition,
                    SoundEvents.WOOD_PLACE,
                    SoundSource.BLOCKS,
                    1, 1.5f + level.random.nextFloat() * 0.4f);
        }
    }

    private void resetBoardData() {
        this.modelId = null;
        this.results = Lists.newArrayList();
        this.currentCutStack = ItemStack.EMPTY;
        this.currentCutCount = 0;
        this.maxCutCount = 0;
        this.refresh();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (this.modelId != null) {
            tag.putString(MODEL_ID, this.modelId.toString());
        }

        tag.putInt(MAX_CUT_COUNT, this.maxCutCount);
        tag.putInt(CURRENT_CUT_COUNT, this.currentCutCount);
        tag.put(CURRENT_CUT_STACK, this.currentCutStack.serializeNBT());

        ListTag resultTag = new ListTag();
        for (ItemStack result : this.results) {
            resultTag.add(result.save(new CompoundTag()));
        }
        tag.put(RESULT_ITEM, resultTag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains(MODEL_ID)) {
            this.modelId = new ResourceLocation(tag.getString(MODEL_ID));
        } else {
            this.modelId = null;
        }

        this.maxCutCount = tag.getInt(MAX_CUT_COUNT);
        this.currentCutCount = tag.getInt(CURRENT_CUT_COUNT);

        if (tag.contains(CURRENT_CUT_STACK)) {
            this.currentCutStack = ItemStack.of(tag.getCompound(CURRENT_CUT_STACK));
        }

        if (tag.contains(RESULT_ITEM, Tag.TAG_LIST)) {
            ListTag resultTag = tag.getList(RESULT_ITEM, Tag.TAG_COMPOUND);
            this.results = IntStream.range(0, resultTag.size())
                    .mapToObj(index -> ItemStack.of(resultTag.getCompound(index)))
                    .toList();
        } else if (tag.contains(RESULT_ITEM, Tag.TAG_COMPOUND)) {
            this.results = Lists.newArrayList(ItemStack.of(tag.getCompound(RESULT_ITEM)));
        }
    }

    @Nullable
    public ResourceLocation getModelId() {
        return modelId;
    }

    public int getMaxCutCount() {
        return maxCutCount;
    }

    public int getCurrentCutCount() {
        return currentCutCount;
    }

    public ItemStack getCurrentCutStack() {
        return currentCutStack;
    }
}
