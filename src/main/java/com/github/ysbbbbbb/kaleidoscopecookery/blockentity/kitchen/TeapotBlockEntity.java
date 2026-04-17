package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen;

import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.ITeapot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.TeapotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.util.FluidUtils;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;
import java.util.Optional;

public class TeapotBlockEntity extends BaseBlockEntity implements ITeapot {
    /**
     * 前十秒可以取回原料，超过此时间，则无法取回，同时进入 PROCESSING 状态
     */
    public static final int INGREDIENT_TIME = 200;

    public static final String TEA_FLUID_ID = "TeaFluidId";
    public static final String RESULT = "Result";
    public static final String STATUS = "Status";

    private static final String INPUT = "Input";
    private static final String CURRENT_TICK = "CurrentTick";

    private final RecipeManager.CachedCheck<TeapotInput, TeapotRecipe> quickCheck = RecipeManager.createCheck(ModRecipes.TEAPOT_RECIPE);

    private ItemStack input = ItemStack.EMPTY;
    private ResourceLocation teaFluidId = TeapotRecipeSerializer.EMPTY_TEA_FLUID;
    private ItemStack result = ItemStack.EMPTY;

    private int status = PUT_INGREDIENT;
    private int currentTick = -1;

    public AnimationState boilingState = new AnimationState();

    public TeapotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.TEAPOT_BE.get(), pos, state);
    }

    public void tick(Level level) {
        // 如果现在处于 PUT_INGREDIENT 阶段
        if (status == ITeapot.PUT_INGREDIENT) {
            // 每 23 tick 检查一次
            long offset = level.getGameTime() + worldPosition.hashCode();
            if (Math.floorMod(offset, 23) == 0) {
                // 茶壶为空
                if (teaFluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID)) {
                    return;
                }
                // 没有热源，啥进度也不进行
                if (!hasHeatSource(level)) {
                    return;
                }

                // 此时播放音效和粒子
                this.onProcessingEffects(level);

                // 输入原料是空的
                if (input.isEmpty()) {
                    return;
                }
                // 材料放入冷却阶段
                if (this.currentTick > 0) {
                    this.currentTick = Math.max(-1, this.currentTick - 23);
                    this.refresh();
                    return;
                }
                // 时间到，开始进入 PROCESSING 状态
                TeapotInput container = new TeapotInput(this.input, this.teaFluidId);
                Optional<RecipeHolder<TeapotRecipe>> recipeOpt = this.quickCheck.getRecipeFor(container, level);
                if (recipeOpt.isPresent()) {
                    TeapotRecipe teapotRecipe = recipeOpt.get().value();
                    this.result = teapotRecipe.assemble(container, level.registryAccess());
                    this.currentTick = teapotRecipe.time();
                    this.status = PROCESSING;
                    this.refresh();
                    return;
                }
                // 如果配方找不到，弹出
                Block.popResource(level, worldPosition, input);
                this.input = ItemStack.EMPTY;
                this.result = ItemStack.EMPTY;
                this.status = ITeapot.PUT_INGREDIENT;
                this.currentTick = -1;
                this.refresh();
            }
            return;
        }

        // 如果处于 PROCESSING 阶段，只需要检查热源并计数即可
        if (status == PROCESSING) {
            // 每 23 tick 检查一次
            long offset = level.getGameTime() + worldPosition.hashCode();
            if (Math.floorMod(offset, 23) == 0) {
                if (!hasHeatSource(level)) {
                    return;
                }

                // 此时播放音效和粒子
                this.onProcessingEffects(level);

                if (currentTick > 0) {
                    this.currentTick = Math.max(-1, this.currentTick - 23);
                    this.refresh();
                    return;
                }
                this.status = FINISHED;
                this.currentTick = -1;
                this.refresh();
            }
        }

        // 完成状态
        if (status == FINISHED) {
            // 每 11 tick 检查一次
            long offset = level.getGameTime() + worldPosition.hashCode();
            if (Math.floorMod(offset, 11) == 0) {
                // 没有热源，停止播放沸腾动画
                if (!hasHeatSource(level)) {
                    this.boilingState.stop();
                    this.onFinishEffects(level);
                } else {
                    this.boilingState.start((int) level.getGameTime());
                    this.onBoilingEffects(level);
                }
            }
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
        // 当前状态正确
        if (this.status != PUT_INGREDIENT) {
            this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_tea_fluid.state_incorrect", this.getStatusText());
            return false;
        }

        // 手持物品必须拥有流体能力
        var cap = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (cap == null) {
            return false;
        }

        // 流体已满
        if (!this.teaFluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID)) {
            this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_tea_fluid.has_fluid");
            return false;
        }

        // 茶壶有流体
        // 检查流体是否相同

        FluidStack fluidInTank = cap.getFluidInTank(0);
        Fluid fluid = fluidInTank.getFluid();

        ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid);

        // 固定扣 1000 mb
        int amount = fluidInTank.getAmount();
        if (amount < FluidType.BUCKET_VOLUME) {
            this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_tea_fluid.fluid_not_enough");
            return false;
        }

        // 填满
        FluidTank needFluidHandler = new FluidTank(FluidType.BUCKET_VOLUME, stack -> FluidStack.isSameFluidSameComponents(stack, fluidInTank));
        if (!FluidUtils.emptyItem(user, itemStack, needFluidHandler, FluidType.BUCKET_VOLUME)) {
            return false;
        }

        this.teaFluidId = id;
        this.refresh();
        return true;
    }

    @Override
    public boolean removeTeaFluid(Level level, LivingEntity user, ItemStack itemStack) {
        if (this.status != PUT_INGREDIENT || this.teaFluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID) || !this.input.isEmpty()) {
            this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.take_tea_fluid.blocked");
            return false;
        }

        var cap = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (cap == null) {
            return false;
        }

        Fluid fluid = BuiltInRegistries.FLUID.get(this.teaFluidId);
        if (fluid == Fluids.EMPTY) {
            return false;
        }

        FluidTank sourceFluidHandler = new FluidTank(FluidType.BUCKET_VOLUME);
        sourceFluidHandler.setFluid(new FluidStack(fluid, FluidType.BUCKET_VOLUME));
        if (!FluidUtils.fillItem(user, itemStack, sourceFluidHandler, FluidType.BUCKET_VOLUME)) {
            return false;
        }

        this.teaFluidId = TeapotRecipeSerializer.EMPTY_TEA_FLUID;
        this.currentTick = -1;
        this.refresh();
        return true;
    }

    @Override
    public boolean addIngredient(Level level, LivingEntity user, ItemStack itemStack) {
        if (this.status != PUT_INGREDIENT) {
            this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_ingredient.state_incorrect", this.getStatusText());
            return false;
        }

        // 茶壶没有流体
        if (this.teaFluidId.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID)) {
            this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_ingredient.no_fluid");
            return false;
        }

        // 里面有物品，失败
        if (!this.input.isEmpty()) {
            this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_ingredient.has_ingredient");
            return false;
        }

        // 查询配方
        TeapotInput container = new TeapotInput(itemStack, this.teaFluidId);
        Optional<RecipeHolder<TeapotRecipe>> recipeOpt = this.quickCheck.getRecipeFor(container, level);
        if (recipeOpt.isPresent()) {
            TeapotRecipe recipe = recipeOpt.get().value();
            int count = recipe.ingredientCount();

            this.input = itemStack.copyWithCount(count);
            this.currentTick = INGREDIENT_TIME;
            this.refresh();

            itemStack.shrink(count);
            return true;
        }

        this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.add_ingredient.recipe_incorrect");
        return false;
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

    @Override
    public boolean takeTeapot(Level level, LivingEntity user) {
        if (status == PROCESSING) {
            this.sendActionBarMessage(user, "tooltip.kaleidoscope_cookery.teapot.take_teapot.state_incorrect");
            return false;
        }

        for (ItemStack drop : getDrops()) {
            ItemUtils.getItemToLivingEntity(user, drop);
        }

        level.playSound(null, worldPosition, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS, 0.6f,
                0.8f + level.random.nextFloat() * 0.2F);
        level.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);

        return true;
    }

    /**
     * 茶壶变成物品时执行的逻辑
     */
    public List<ItemStack> getDrops() {
        List<ItemStack> drops = Lists.newArrayList();

        ItemStack teapot = ModItems.TEAPOT.get().getDefaultInstance();

        // 如果当前是准备状态
        if (this.status == PUT_INGREDIENT) {
            // 如果有原料，掉落原料
            if (!this.input.isEmpty()) {
                drops.add(this.input.copy());
            }

            CompoundTag tag = new CompoundTag();
            tag.putString(TEA_FLUID_ID, this.teaFluidId.toString());
            BlockItem.setBlockEntityData(teapot, ModBlocks.TEAPOT_BE.get(), tag);
            drops.add(teapot);

            return drops;
        }

        // 如果是进行中，那么直接空
        if (this.status == PROCESSING) {
            drops.add(teapot);
            return drops;
        }

        // 完成，那么返回部分数值
        if (this.level != null) {
            CompoundTag tag = new CompoundTag();
            tag.put(RESULT, result.copy().saveOptional(this.level.registryAccess()));
            tag.putInt(STATUS, this.status);
            BlockItem.setBlockEntityData(teapot, ModBlocks.TEAPOT_BE.get(), tag);
        }
        drops.add(teapot);

        return drops;
    }

    private void onProcessingEffects(Level level) {
        RandomSource random = level.random;
        level.playSound(null, worldPosition, ModSounds.BLOCK_TEAPOT_PROCESSING.get(), SoundSource.BLOCKS, 0.6f,
                0.8f + random.nextFloat() * 0.2F);
        this.onFinishEffects(level);
    }

    private void onBoilingEffects(Level level) {
        RandomSource random = level.random;
        level.playSound(null, worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.4f,
                0.8f + random.nextFloat() * 0.2F);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ModParticles.COOKING.get(),
                    worldPosition.getX() + 0.5 + (level.random.nextFloat() - 0.5F),
                    worldPosition.getY() + 0.6 + level.random.nextDouble() / 5,
                    worldPosition.getZ() + 0.5 + (level.random.nextFloat() - 0.5F),
                    3,
                    (level.random.nextFloat() - 0.5) * 0.05F,
                    0.1,
                    (level.random.nextFloat() - 0.5) * 0.05F,
                    0.02);
        }
    }

    private void onFinishEffects(Level level) {
        RandomSource random = level.random;

        if (level instanceof ServerLevel serverLevel) {
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

    private void sendActionBarMessage(LivingEntity user, String key, Object... args) {
        if (user instanceof ServerPlayer serverPlayer) {
            MutableComponent message = Component.translatable(key, args);
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(message));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(INPUT, this.input.saveOptional(registries));
        tag.putString(TEA_FLUID_ID, this.teaFluidId.toString());
        tag.put(RESULT, this.result.saveOptional(registries));
        tag.putInt(STATUS, this.status);
        tag.putInt(CURRENT_TICK, this.currentTick);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.input = ItemStack.parseOptional(registries, tag.getCompound(INPUT));
        this.teaFluidId = ResourceLocation.parse(tag.getString(TEA_FLUID_ID));
        this.result = ItemStack.parseOptional(registries, tag.getCompound(RESULT));
        this.status = tag.getInt(STATUS);
        this.currentTick = tag.getInt(CURRENT_TICK);
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    public Component getStatusText() {
        if (status == PUT_INGREDIENT) {
            return Component.translatable("tooltip.kaleidoscope_cookery.teapot.statue.put_ingredient");
        }
        if (status == PROCESSING) {
            return Component.translatable("tooltip.kaleidoscope_cookery.teapot.statue.processing");
        }
        if (status == FINISHED) {
            return Component.translatable("tooltip.kaleidoscope_cookery.teapot.statue.finished");
        }
        return Component.empty();
    }

    public ItemStack getInput() {
        return input;
    }

    public ResourceLocation getTeaFluidId() {
        return teaFluidId;
    }

    public ItemStack getResult() {
        return result;
    }

    public int getCurrentTick() {
        return currentTick;
    }
}