package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teatype.ITeaType;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.ITeapot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.TeapotContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.teatype.TeaTypeManager;
import com.github.ysbbbbbb.kaleidoscopecookery.init.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeapotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.fluids.FluidType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeapotBlockEntity extends BaseBlockEntity implements ITeapot {
    public static final int MAX_FLUID_AMOUNT = 12;

    private static final String INPUT = "Input";
    private static final String TEA_TYPE_ID = "TeaTypeId";
    private static final String RECIPE_ID = "RecipeId";
    private static final String STATUS = "Status";
    private static final String CURRENT_TICK = "CurrentTick";
    private static final String FLUID_AMOUNT = "FluidAmount";

    private final RecipeManager.CachedCheck<TeapotContainer, TeapotRecipe> quickCheck = RecipeManager.createCheck(ModRecipes.TEAPOT_RECIPE);

    private ItemStack input = ItemStack.EMPTY;
    private ResourceLocation teaTypeId = ModTeaTypes.EMPTY;
    private ResourceLocation recipeId = TeapotRecipeSerializer.EMPTY_ID;
    private int status = PUT_INGREDIENT;
    private int currentTick = -1;
    private int fluidAmount = 0;

    public TeapotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.TEAPOT_BE.get(), pos, state);
    }

    public boolean hasHeatSource(Level level) {
        BlockState belowState = level.getBlockState(worldPosition.below());
        if (belowState.hasProperty(BlockStateProperties.LIT)) {
            return belowState.getValue(BlockStateProperties.LIT);
        }
        return belowState.is(TagMod.HEAT_SOURCE_BLOCKS_WITHOUT_LIT);
    }

    public void tick(Level level) {
        // 茶壶为空
        if (this.teaTypeId.equals(ModTeaTypes.EMPTY)) {
            return;
        }

        boolean heated = this.hasHeatSource(level);
        boolean fulfilled = this.fluidAmount == MAX_FLUID_AMOUNT;
        // 生成粒子效果
        if (status == BOILING && heated && fulfilled) {
            spawnParticleBoiling(level);
        } else if (TeaTypeManager.getTeaType(this.teaTypeId).doSpawnParticles()) {
            spawnParticleIdle(level);
        }

        // 茶壶未装满
        if (!fulfilled) {
            return;
        }

        // 下方没有火源
        if (!heated) {
            return;
        }

        // 如果当前状态是放入素材，且素材不为空
        if (status == PUT_INGREDIENT && level.getGameTime() % 5 == 0 && !this.input.isEmpty()) {
            Optional<TeapotRecipe> recipeOpt = this.quickCheck.getRecipeFor(getContainer(), level);
            if (recipeOpt.isPresent()) {
                this.setRecipe(level, recipeOpt.orElseThrow());
                this.status = BOILING;
                this.refresh();
                return;
            }
        }

        // 如果当前状态是烹饪中，递减当前 tick
        if (status == BOILING) {
            if (currentTick > 0) {
                currentTick--;
                return;
            }
            status = PUT_INGREDIENT;
            currentTick = -1;
            TeapotRecipe recipe = level.getRecipeManager().byType(ModRecipes.TEAPOT_RECIPE).getOrDefault(this.recipeId, null);
            this.teaTypeId = recipe != null ? recipe.resultTeaType() : ModTeaTypes.EMPTY;
            this.input.shrink(recipe != null ? recipe.ingredientCount() : TeapotRecipeSerializer.DEFAULT_INGREDIENT_COUNT);
            this.refresh();
        }
    }

    private void spawnParticleIdle(Level level) {
        if (level instanceof ServerLevel serverLevel && level.random.nextFloat() < 0.1F) {
            RandomSource random = serverLevel.random;
            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    worldPosition.getX() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    worldPosition.getY() + 0.5 + random.nextDouble() / 3,
                    worldPosition.getZ() + 0.5 + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    1,
                    (level.random.nextFloat() - 0.5) * 0.05F,
                    0.1,
                    (level.random.nextFloat() - 0.5) * 0.05F,
                    0.02);
        }
    }

    private void spawnParticleBoiling(Level level) {
        if (level instanceof ServerLevel serverLevel && serverLevel.random.nextFloat() < 0.25F) {
            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    worldPosition.getX() + 0.5 + (level.random.nextFloat() - 0.5F) * 0.3F,
                    worldPosition.getY() + 0.65,
                    worldPosition.getZ() + 0.5 + (level.random.nextFloat() - 0.5F) * 0.3F,
                    2,
                    (level.random.nextFloat() - 0.5) * 0.05F,
                    0.1,
                    (level.random.nextFloat() - 0.5) * 0.05F,
                    0.05);
        }
    }

    @Override
    public boolean addFluid(Level level, LivingEntity user, ItemStack bucket) {
        // 当前状态是放入材料
        if (this.status != PUT_INGREDIENT) {
            return false;
        }

        // 茶壶未装满
        if (this.fluidAmount >= MAX_FLUID_AMOUNT) {
            return false;
        }

        if (bucket.getItem() instanceof BucketItem bucketItem) {
            ITeaType teaType = TeaTypeManager.getTeaType(this.teaTypeId);
            FluidType fluidType = TeaTypeManager.getBoundFluid(this.teaTypeId);
            FluidType bucketType = bucketItem.getFluid().getFluidType();
            // 壶内为空 或者 壶内流体类型和桶中相同
            if ((fluidType != null && fluidType.equals(bucketType)) || teaType.getName().equals(ModTeaTypes.EMPTY)) {
                this.teaTypeId = TeaTypeManager.getBoundTeaTypeId(bucketType);
                this.fluidAmount = MAX_FLUID_AMOUNT;
                this.refresh();

                ItemStack container = bucketItem.getCraftingRemainingItem(bucket);
                bucket.shrink(1);
                ItemUtils.getItemToLivingEntity(user, container);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean addIngredient(Level level, LivingEntity user, ItemStack itemStack) {
        if (this.status != PUT_INGREDIENT) {
            return false;
        }

        // 检查放入的材料与现有的材料能否堆叠
        if (!this.input.isEmpty() && !ItemStack.isSameItemSameTags(this.input, itemStack)) {
            return false;
        }

        int count = this.input.getCount();
        // 检查是否有足够的空间放入材料
        if (count == this.input.getMaxStackSize()) {
            return false;
        }

        ItemStack toInsert = itemStack.split(this.input.getMaxStackSize() - count);
        this.input = toInsert.copyWithCount(count + toInsert.getCount());
        this.refresh();
        return true;
    }

    @Override
    public boolean removeIngredient(Level level, LivingEntity user) {
        if (status != PUT_INGREDIENT) {
            return false;
        }

        if (this.input.isEmpty()) {
            return false;
        }

        ItemUtils.getItemToLivingEntity(user, this.input.copyAndClear());
        this.refresh();
        return true;
    }

    public void setRecipe(Level level, TeapotRecipe recipe) {
        this.recipeId = recipe.id();
        this.currentTick = recipe.time();
    }

    public void loadFromItem(ItemStack itemStack) {
        this.teaTypeId = TeapotItem.getTeaType(itemStack).getName();
        this.fluidAmount = TeapotItem.getFluidAmount(itemStack);
        refresh();
    }

    public TeapotContainer getContainer() {
        return new TeapotContainer(this.input, this.teaTypeId);
    }

    public ItemStack dropAsItem() {
        ItemStack drop = ModItems.TEAPOT.get().getDefaultInstance();
        TeapotItem.setTeaType(drop, TeaTypeManager.getTeaType(this.teaTypeId));
        TeapotItem.setFluidAmount(drop, this.fluidAmount);
        return drop;
    }

    public List<ItemStack> getDrops() {
        List<ItemStack> drops = new ArrayList<>();
        if (this.status != BOILING && !this.input.isEmpty()) {
            drops.add(this.input);
        }
        drops.add(this.dropAsItem());
        return drops;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(INPUT, this.input.save(new CompoundTag()));
        tag.putString(TEA_TYPE_ID, this.teaTypeId.toString());
        tag.putString(RECIPE_ID, this.recipeId.toString());
        tag.putInt(STATUS, this.status);
        tag.putInt(CURRENT_TICK, this.currentTick);
        tag.putInt(FLUID_AMOUNT, this.fluidAmount);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(INPUT)) {
            this.input = ItemStack.of(tag.getCompound(INPUT));
        }
        if (tag.contains(TEA_TYPE_ID)) {
            this.teaTypeId = ResourceLocation.tryParse(tag.getString(TEA_TYPE_ID));
        }
        if (tag.contains(RECIPE_ID)) {
            this.recipeId = ResourceLocation.tryParse(tag.getString(RECIPE_ID));
        }
        if (tag.contains(STATUS)) {
            this.status = tag.getInt(STATUS);
        }
        if (tag.contains(CURRENT_TICK)) {
            this.currentTick = tag.getInt(CURRENT_TICK);
        }
        if (tag.contains(FLUID_AMOUNT)) {
            this.fluidAmount = tag.getInt(FLUID_AMOUNT);
        }
    }

    @Override
    public int getStatus() { return this.status; }

    public ItemStack getInput() { return this.input; }

    public ResourceLocation getTeaTypeId() { return this.teaTypeId; }

    public int getFluidAmount() { return this.fluidAmount; }
}
