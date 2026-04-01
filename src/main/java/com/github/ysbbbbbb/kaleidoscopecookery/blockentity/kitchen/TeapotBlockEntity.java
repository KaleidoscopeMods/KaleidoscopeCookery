package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.api.client.ITipProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.teafluid.ITeaFluid;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.ITeapot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.TeapotContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.teafluid.TeaFluidManager;
import com.github.ysbbbbbb.kaleidoscopecookery.init.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeapotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TeapotBlockEntity extends BaseBlockEntity implements ITeapot, ITipProvider {
    public static final int MAX_FLUID_AMOUNT = 12;

    private static final String INPUT = "Input";
    private static final String TEA_FLUID_ID = "TeaFluidId";
    private static final String RECIPE_ID = "RecipeId";
    private static final String STATUS = "Status";
    private static final String CURRENT_TICK = "CurrentTick";
    private static final String FLUID_AMOUNT = "FluidAmount";

    private final RecipeManager.CachedCheck<TeapotContainer, TeapotRecipe> quickCheck = RecipeManager.createCheck(ModRecipes.TEAPOT_RECIPE);

    private ItemStack input = ItemStack.EMPTY;
    private ResourceLocation teaFluidId = ModTeaFluids.EMPTY;
    private ResourceLocation recipeId = TeapotRecipeSerializer.EMPTY_ID;
    private int status = PUT_INGREDIENT;
    private int currentTick = -1;
    private int fluidAmount = 0;

    public TeapotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.TEAPOT_BE.get(), pos, state);
    }

    public void tick(Level level) {
        // 茶壶为空
        if (fluidAmount <= 0) {
            return;
        }

        ITeaFluid teaFluid = TeaFluidManager.getTeaFluid(this.teaFluidId);
        boolean heated = hasHeatSource(level);
        boolean filled = fluidAmount >= MAX_FLUID_AMOUNT;
        // 生成粒子
        if (status == BOILING) {
            if (heated && filled) {
                spawnParticleBoiling(level);
            }
        } else {
            if (teaFluid.spawnParticles()) {
                spawnParticleIdle(level);
            }
        }

        // 装有茶基、受热且装满液体
        if (!teaFluid.isTeaBase() || !heated || !filled) {
            return;
        }

        // 如果当前状态是放入素材，且素材不为空
        if (this.status == PUT_INGREDIENT && level.getGameTime() % 5 == 0 && !this.input.isEmpty()) {
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
            status = FINISHED;
            currentTick = -1;
            TeapotRecipe recipe = level.getRecipeManager().byType(ModRecipes.TEAPOT_RECIPE).getOrDefault(this.recipeId, null);
            this.teaFluidId = recipe != null ? recipe.resultTeaFluid() : ModTeaFluids.EMPTY;
            this.refresh();
        }
    }

    @Override
    public boolean hasHeatSource(Level level) {
        BlockState belowState = level.getBlockState(worldPosition.below());
        if (belowState.hasProperty(BlockStateProperties.LIT)) {
            return belowState.getValue(BlockStateProperties.LIT);
        }
        return belowState.is(TagMod.HEAT_SOURCE_BLOCKS_WITHOUT_LIT);
    }

    @Override
    public boolean addTeaFluid(Level level, LivingEntity user, ItemStack itemStack) {
        // 当前状态是放入材料
        if (this.status != PUT_INGREDIENT) {
            return false;
        }

        // 茶壶未装满
        if (this.fluidAmount >= MAX_FLUID_AMOUNT) {
            return false;
        }

        ITeaFluid teaFluid = TeaFluidManager.getTeaFluid(this.teaFluidId);
        if (teaFluid.isEmpty()) {
            for (Map.Entry<ResourceLocation, ITeaFluid> entry : TeaFluidManager.getAllTeaFluids().entrySet()) {
                ITeaFluid tea = entry.getValue();
                if (tea.isTeaFluid(itemStack)) {
                    this.fluidAmount = MAX_FLUID_AMOUNT;
                    this.teaFluidId = tea.getName();
                    this.refresh();
                    ItemUtils.getItemToLivingEntity(user, ItemUtils.getContainerItem(itemStack).getDefaultInstance());
                    if (!(user instanceof Player player && player.isCreative())) {
                        itemStack.shrink(1);
                    }
                    return true;
                }
            }
        } else {
            if (teaFluid.isTeaFluid(itemStack)) {
                this.fluidAmount = MAX_FLUID_AMOUNT;
                this.refresh();
                ItemUtils.getItemToLivingEntity(user, ItemUtils.getContainerItem(itemStack).getDefaultInstance());
                if (!(user instanceof Player player && player.isCreative())) {
                    itemStack.shrink(1);
                }
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
        if (status == BOILING) {
            return false;
        }

        if (this.input.isEmpty()) {
            return false;
        }

        ItemUtils.getItemToLivingEntity(user, this.input.copyAndClear());
        this.refresh();
        return true;
    }

    @Override
    public boolean takeTeapot(Level level, LivingEntity user) {
        if (this.status != FINISHED) {
            return false;
        }

        getDrops().forEach(s -> ItemUtils.getItemToLivingEntity(user, s));
        level.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);

        return true;
    }

    private void spawnParticleIdle(Level level) {
        if (level instanceof ServerLevel serverLevel && level.random.nextFloat() < 0.05F) {
            RandomSource random = serverLevel.random;
            serverLevel.sendParticles(ModParticles.COOKING.get(),
                    worldPosition.getX() + 0.5 + random.nextDouble() / 4 * (random.nextBoolean() ? 1 : -1),
                    worldPosition.getY() + 0.8 + random.nextDouble() / 3,
                    worldPosition.getZ() + 0.5 + random.nextDouble() / 4 * (random.nextBoolean() ? 1 : -1),
                    1,
                    (level.random.nextFloat() - 0.5) * 0.05F,
                    0.1,
                    (level.random.nextFloat() - 0.5) * 0.05F,
                    0.02);
        }
    }

    private void spawnParticleBoiling(Level level) {
        if (level instanceof ServerLevel serverLevel && serverLevel.random.nextFloat() < 0.1F) {
            serverLevel.sendParticles(ModParticles.COOKING.get(),
                    worldPosition.getX() + 0.5 + (level.random.nextFloat() - 0.5F) * 0.3F,
                    worldPosition.getY() + 0.8 + level.random.nextDouble() / 3,
                    worldPosition.getZ() + 0.5 + (level.random.nextFloat() - 0.5F) * 0.3F,
                    2,
                    (level.random.nextFloat() - 0.5) * 0.05F,
                    0.1,
                    (level.random.nextFloat() - 0.5) * 0.05F,
                    0.02);
        }
    }

    public void setRecipe(Level level, TeapotRecipe recipe) {
        this.recipeId = recipe.id();
        this.currentTick = recipe.time();
        this.input.shrink(recipe.ingredientCount());
    }

    public void loadFromItem(ItemStack itemStack) {
        ITeaFluid teaFluid = TeapotItem.getTeaFluid(itemStack);
        this.teaFluidId = teaFluid.getName();
        this.fluidAmount = TeapotItem.getFluidAmount(itemStack);
        this.status = teaFluid.isTeaBase() ? PUT_INGREDIENT : FINISHED;
        refresh();
    }

    public TeapotContainer getContainer() {
        return new TeapotContainer(this.input, this.teaFluidId);
    }

    public ItemStack dropAsItem() {
        ItemStack drop = ModItems.TEAPOT.get().getDefaultInstance();
        TeapotItem.setTeaFluid(drop, TeaFluidManager.getTeaFluid(this.teaFluidId));
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
    public Component getTip() {
        Component hoverName = input.getHoverName();

        Component text = input.isEmpty() ? Component.empty() : Component.literal("内含 %dx%s\n".formatted(this.input.getCount(), hoverName.getString()));
        if (status == BOILING) {
            return text.copy().append(Component.literal("正在煮茶中…"));
        }

        if (this.status == FINISHED) {
            return text.copy().append(Component.literal("茶水开了！"));
        }

        if (fluidAmount <= 0) {
            return text.copy().append(Component.literal("未添加液体"));
        }

        if (input.isEmpty()) {
            return Component.literal("未添加物品");
        }

        return text.copy().append(Component.literal("物品数量过少或错误!"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(INPUT, this.input.save(new CompoundTag()));
        tag.putString(TEA_FLUID_ID, this.teaFluidId.toString());
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
        if (tag.contains(TEA_FLUID_ID)) {
            this.teaFluidId = ResourceLocation.tryParse(tag.getString(TEA_FLUID_ID));
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

    public ResourceLocation getTeaFluidId() { return this.teaFluidId; }

    public int getFluidAmount() { return this.fluidAmount; }
}
