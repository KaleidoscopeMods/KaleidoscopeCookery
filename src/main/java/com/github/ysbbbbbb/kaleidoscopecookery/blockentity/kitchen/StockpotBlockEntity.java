package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IStockpot;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StockpotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.particle.StockpotParticleOptions;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.FluidSoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag.TagItem;
import com.github.ysbbbbbb.kaleidoscopecookery.init.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.util.BlockDrop;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Objects;

public class StockpotBlockEntity extends BaseBlockEntity implements IStockpot {
    public static final int MAX_TAKEOUT_COUNT = 9;

    private static final String INPUTS = "Inputs";
    private static final String RECIPE_ID = "RecipeId";
    private static final String SOUP_BASE_ID = "SoupBaseId";
    private static final String RESULT = "Result";
    private static final String STATUS = "Status";
    private static final String CURRENT_TICK = "CurrentTick";
    private static final String TAKEOUT_COUNT = "TakeoutCount";
    private static final String LID_ITEM = "LidItem";

    private NonNullList<ItemStack> inputs = NonNullList.withSize(StockpotRecipe.RECIPES_SIZE, ItemStack.EMPTY);
    private ResourceLocation recipeId = StockpotRecipeSerializer.EMPTY_ID;
    private ResourceLocation soupBaseId = ModSoupBases.WATER;
    private ItemStack result = ItemStack.EMPTY;
    private int status = PUT_SOUP_BASE;
    private int currentTick = -1;
    private int takeoutCount = 0;
    /**
     * 盖子，因为盖子可以当做盾牌，所以会记录很多额外内容，需要专门保存
     */
    private ItemStack lidItem = ItemStack.EMPTY;

    /**
     * 主要用于客户端渲染的字段，recipe 里缓存了数据包中定义的部分客户端渲染需要的东西
     */
    public StockpotRecipe recipe = StockpotRecipeSerializer.getEmptyRecipe();
    public @Nullable Entity renderEntity = null;

    public StockpotBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.STOCKPOT_BE.get(), pPos, pBlockState);
    }

    public void clientTick() {
        if (this.renderEntity != null) {
            this.renderEntity.tickCount++;
        }
    }

    @Override
    public boolean hasHeatSource(Level level) {
        BlockState belowState = level.getBlockState(worldPosition.below());
        if (belowState.hasProperty(BlockStateProperties.LIT) && belowState.getValue(BlockStateProperties.LIT)) {
            return true;
        }
        return belowState.is(TagMod.HEAT_SOURCE_BLOCKS_WITHOUT_LIT);
    }

    @Override
    public boolean hasLid() {
        if (level == null) {
            return false;
        }
        BlockState blockState = level.getBlockState(worldPosition);
        return level != null && blockState.hasProperty(StockpotBlock.HAS_LID)
               && blockState.getValue(StockpotBlock.HAS_LID);
    }

    public void tick(Level level) {
        // 下方没有火源
        if (!hasHeatSource(level)) {
            return;
        }
        boolean hasLid = hasLid();
        // 音效播放
        if (status != PUT_SOUP_BASE && level.getGameTime() % 15 == 0) {
            float volume = hasLid ? 0.075f : 0.2f;
            float pitch = hasLid ? 0.1f + level.random.nextFloat() * 0.05f : 1f + level.random.nextFloat() * 0.1f;
            level.playSound(null,
                    worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5,
                    ModSounds.BLOCK_STOCKPOT.get(), SoundSource.BLOCKS, volume, pitch);
        }

        // 没有盖子时，不进行任何 tick，只生成粒子
        if (!hasLid) {
            if (status != PUT_SOUP_BASE && level instanceof ServerLevel serverLevel && level.random.nextFloat() < 0.25F) {
                int color = getBubbleColor();
                serverLevel.sendParticles(new StockpotParticleOptions(Vec3.fromRGB24(color).toVector3f(), 1f),
                        worldPosition.getX() + 0.25 + (level.random.nextFloat() * 0.5F),
                        worldPosition.getY() + 0.375,
                        worldPosition.getZ() + 0.25 + (level.random.nextFloat() * 0.5F),
                        2,
                        (level.random.nextFloat() - 0.5) * 0.1F,
                        0,
                        (level.random.nextFloat() - 0.5) * 0.1F,
                        0
                );
            }
            return;
        }

        // 有盖子时，生成白色粒子
        if (status != PUT_SOUP_BASE && level instanceof ServerLevel serverLevel && level.random.nextFloat() < 0.05F) {
            RandomSource random = serverLevel.random;
            serverLevel.sendParticles(ModParticles.COOKING.get(),
                    worldPosition.getX() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    worldPosition.getY() + 0.375 + random.nextDouble() / 3,
                    worldPosition.getZ() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    1, 0, 0, 0, 0.05);
        }

        // 如果当前状态是放入素材，且素材不为空
        // 因为 isEmpty() 可能耗时，所以每隔 5 tick 检查一次
        if (status == PUT_INGREDIENT && level.getGameTime() % 5 == 0 && !this.isEmpty()) {
            this.setRecipe(level);
            status = COOKING;
            this.refresh();
            return;
        }

        // 如果当前状态是烹饪中，递减当前 tick
        if (status == COOKING) {
            if (currentTick > 0) {
                currentTick--;
                return;
            }
            status = FINISHED;
            currentTick = -1;
            this.inputs.clear();
            this.refresh();
        }
    }

    private int getBubbleColor() {
        // 需要检查下 recipe 是否更新
        if (this.level != null && this.recipeId != StockpotRecipeSerializer.EMPTY_ID && this.recipe.getId() == StockpotRecipeSerializer.EMPTY_ID) {
            StockpotRecipe stockpotRecipe = this.level.getRecipeManager().byType(ModRecipes.STOCKPOT_RECIPE).get(this.recipeId);
            this.recipe = Objects.requireNonNullElseGet(stockpotRecipe, StockpotRecipeSerializer::getEmptyRecipe);
        }
        if (status == COOKING) {
            return this.recipe.cookingBubbleColor();
        }
        if (status == FINISHED) {
            return this.recipe.finishedBubbleColor();
        }
        ISoupBase soup = this.getSoupBase();
        if (soup != null) {
            return soup.getBubbleColor();
        }
        return 0xffffff;
    }

    @Override
    public boolean onLitClick(Level level, LivingEntity user, ItemStack stack) {
        BlockState blockState = level.getBlockState(worldPosition);
        boolean hasLid = this.hasLid();

        // 第一种情况，放上盖子
        if (!hasLid && stack.is(ModItems.STOCKPOT_LID.get())) {
            this.setLidItem(stack.split(1));
            this.setChanged();
            level.setBlockAndUpdate(worldPosition, blockState.setValue(StockpotBlock.HAS_LID, true));
            user.playSound(SoundEvents.LANTERN_PLACE, 0.5F, 0.5F);
            return true;
        }

        // 第二种情况，取下盖子
        if (hasLid) {
            ItemStack lid = this.getLidItem().isEmpty() ? ModItems.STOCKPOT_LID.get().getDefaultInstance() : this.getLidItem().copy();
            this.setLidItem(ItemStack.EMPTY);
            if (stack.isEmpty()) {
                user.setItemInHand(InteractionHand.MAIN_HAND, lid);
            } else {
                BlockDrop.popResource(level, worldPosition, 0.5, lid);
            }
            this.setChanged();
            level.setBlockAndUpdate(worldPosition, blockState.setValue(StockpotBlock.HAS_LID, false));
            user.playSound(SoundEvents.LANTERN_BREAK, 0.5F, 0.5F);
            return true;
        }

        return false;
    }

    private void setRecipe(Level levelIn) {
        StockpotContainer container = new StockpotContainer(this.inputs, this.soupBaseId);
        levelIn.getRecipeManager().getRecipeFor(ModRecipes.STOCKPOT_RECIPE, container, levelIn).ifPresentOrElse(recipe -> {
            this.recipeId = recipe.getId();
            this.recipe = recipe;
            this.result = recipe.assemble(container, levelIn.registryAccess());
            this.currentTick = recipe.time();
            this.takeoutCount = Math.min(this.result.getCount(), MAX_TAKEOUT_COUNT);
        }, () -> {
            this.recipeId = StockpotRecipeSerializer.EMPTY_ID;
            this.recipe = StockpotRecipeSerializer.getEmptyRecipe();
            this.result = Items.SUSPICIOUS_STEW.getDefaultInstance();
            this.currentTick = StockpotRecipeSerializer.DEFAULT_TIME;
            this.takeoutCount = 1;
        });
    }

    @Override
    public boolean addSoupBase(Level level, LivingEntity user, ItemStack bucket) {
        // 必须打开盖子才能放入汤底
        if (hasLid()) {
            return false;
        }
        // 当前状态是放入汤底
        if (status != PUT_SOUP_BASE) {
            return false;
        }
        for (var entry : SoupBaseManager.getAllSoupBases().entrySet()) {
            ResourceLocation key = entry.getKey();
            ISoupBase soupBase = entry.getValue();
            if (soupBase.isSoupBase(bucket)) {
                this.soupBaseId = key;
                this.status = PUT_INGREDIENT;
                this.refresh();

                ItemStack container = soupBase.getReturnContainer(level, user, bucket);
                bucket.shrink(1);
                ItemUtils.getItemToLivingEntity(user, container);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeSoupBase(Level level, LivingEntity user, ItemStack bucket) {
        // 当前是放入食材，但是还没放
        if (status == PUT_INGREDIENT && this.isEmpty() && SoupBaseManager.containsSoupBase(this.soupBaseId)) {
            ISoupBase soupBase = this.getSoupBase();
            if (soupBase == null || !soupBase.isContainer(bucket)) {
                return false;
            }
            this.renderEntity = null;
            this.soupBaseId = ModSoupBases.WATER;
            this.status = PUT_SOUP_BASE;
            this.refresh();

            ItemStack container = soupBase.getReturnContainer(level, user, bucket);
            bucket.shrink(1);
            ItemUtils.getItemToLivingEntity(user, container);
            return true;
        }
        return false;
    }

    @Override
    public boolean addIngredient(Level level, LivingEntity user, ItemStack itemStack) {
        if (hasLid()) {
            return false;
        }
        if (status != PUT_INGREDIENT) {
            return false;
        }
        if (!itemStack.isEdible() && !itemStack.is(TagItem.POT_INGREDIENT)) {
            return false;
        }
        // 检查是否有足够的空间放入食材
        for (int i = 0; i < this.inputs.size(); i++) {
            if (!this.inputs.get(i).isEmpty()) {
                continue;
            }
            this.inputs.set(i, itemStack.split(1));
            level.playSound(null, user.getX(), user.getY() + 0.5, user.getZ(),
                    SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F,
                    ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            this.refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeIngredient(Level level, LivingEntity user) {
        if (hasLid()) {
            return false;
        }
        if (status != PUT_INGREDIENT) {
            return false;
        }
        for (int i = this.inputs.size() - 1; i >= 0; i--) {
            ItemStack stack = this.inputs.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            ItemUtils.getItemToLivingEntity(user, stack.copy());
            this.inputs.set(i, ItemStack.EMPTY);
            // 如果是汤底，且温度过高，玩家会受到伤害
            ISoupBase soupBase = this.getSoupBase();
            if (soupBase instanceof FluidSoupBase fluidSoupBase && fluidSoupBase.getFluid().getFluidType().getTemperature() > 500) {
                user.hurt(level.damageSources().inFire(), 1);
            }
            this.refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean takeOutProduct(Level level, LivingEntity user, ItemStack stack) {
        if (hasLid()) {
            return false;
        }
        // 如果当前状态是烹饪完成
        if (status != FINISHED || this.result.isEmpty() || this.takeoutCount <= 0) {
            return false;
        }
        // 目前限定死了只能用碗来取出成品
        if (!stack.is(Items.BOWL)) {
            return false;
        }
        // 放入碗中
        stack.shrink(1);
        ItemStack resultCopy = this.result.copyWithCount(1);
        ItemUtils.getItemToLivingEntity(user, resultCopy);
        this.takeoutCount--;
        if (this.takeoutCount <= 0) {
            this.status = PUT_SOUP_BASE;
            this.inputs.clear();
            this.recipeId = StockpotRecipeSerializer.EMPTY_ID;
            this.soupBaseId = ModSoupBases.WATER;
            this.result = ItemStack.EMPTY;
            this.currentTick = -1;
        }
        this.refresh();
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(INPUTS, ContainerHelper.saveAllItems(new CompoundTag(), this.inputs));
        tag.putString(RECIPE_ID, this.recipeId.toString());
        tag.putString(SOUP_BASE_ID, this.soupBaseId.toString());
        tag.put(RESULT, this.result.save(new CompoundTag()));
        tag.putInt(STATUS, this.status);
        tag.putInt(CURRENT_TICK, this.currentTick);
        tag.putInt(TAKEOUT_COUNT, this.takeoutCount);
        if (!this.lidItem.isEmpty()) {
            tag.put(LID_ITEM, this.lidItem.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(INPUTS)) {
            this.inputs = NonNullList.withSize(StockpotRecipe.RECIPES_SIZE, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag.getCompound(INPUTS), this.inputs);
        }
        if (tag.contains(RECIPE_ID)) {
            this.recipeId = ResourceLocation.tryParse(tag.getString(RECIPE_ID));
            if (this.level != null) {
                StockpotRecipe stockpotRecipe = this.level.getRecipeManager().byType(ModRecipes.STOCKPOT_RECIPE).get(this.recipeId);
                this.recipe = Objects.requireNonNullElseGet(stockpotRecipe, StockpotRecipeSerializer::getEmptyRecipe);
            }
        }
        if (tag.contains(SOUP_BASE_ID)) {
            this.soupBaseId = ResourceLocation.tryParse(tag.getString(SOUP_BASE_ID));
        }
        if (tag.contains(RESULT)) {
            this.result = ItemStack.of(tag.getCompound(RESULT));
        }
        this.status = tag.getInt(STATUS);
        this.currentTick = tag.getInt(CURRENT_TICK);
        this.takeoutCount = tag.getInt(TAKEOUT_COUNT);
        if (tag.contains(LID_ITEM)) {
            this.lidItem = ItemStack.of(tag.getCompound(LID_ITEM));
        }
    }

    public boolean isEmpty() {
        for (ItemStack stack : this.inputs) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public NonNullList<ItemStack> getInputs() {
        return inputs;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public int getTakeoutCount() {
        return takeoutCount;
    }

    public ItemStack getResult() {
        return result;
    }

    public ResourceLocation getSoupBaseId() {
        return soupBaseId;
    }

    @Nullable
    public ISoupBase getSoupBase() {
        return SoupBaseManager.getSoupBase(this.soupBaseId);
    }

    public ItemStack getLidItem() {
        return lidItem;
    }

    public void setLidItem(ItemStack lidItem) {
        this.lidItem = lidItem;
    }
}
