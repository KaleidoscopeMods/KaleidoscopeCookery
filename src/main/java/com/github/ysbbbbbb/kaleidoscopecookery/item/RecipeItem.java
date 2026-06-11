package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IPot;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IStockpot;
import com.github.ysbbbbbb.kaleidoscopecookery.api.event.RecipeItemEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.RecipeItemTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityEvaluator;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RecipeItem extends BlockItem {
    public static final ResourceLocation HAS_RECIPE_PROPERTY = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "has_recipe");
    public static final ResourceLocation POT = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "pot");
    public static final ResourceLocation STOCKPOT = ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "stockpot");

    private static final int NO_RECIPE = 0;
    private static final int HAS_RECIPE = 1;

    public RecipeItem() {
        super(ModBlocks.RECIPE_BLOCK.get(), new Item.Properties());
    }

    public static void setRecipe(ItemStack stack, RecipeRecord record) {
        stack.set(ModDataComponents.RECIPE_RECORD, record);
    }

    @Nullable
    public static RecipeRecord getRecipe(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof RecipeItem)) {
            return null;
        }
        return stack.get(ModDataComponents.RECIPE_RECORD);
    }

    public static boolean hasRecipe(ItemStack stack) {
        return stack.has(ModDataComponents.RECIPE_RECORD);
    }

    @OnlyIn(Dist.CLIENT)
    public static float getTexture(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (hasRecipe(stack)) {
            return HAS_RECIPE;
        }
        return NO_RECIPE;
    }

    @Override
    public Component getName(ItemStack pStack) {
        if (hasRecipe(pStack)) {
            RecipeRecord recipe = getRecipe(pStack);
            if (recipe != null) {
                Component result = recipe.output().getHoverName();
                Component type;
                if (recipe.type().equals(POT)) {
                    type = Component.translatable("block.kaleidoscope_cookery.pot");
                } else if (recipe.type().equals(STOCKPOT)) {
                    type = Component.translatable("block.kaleidoscope_cookery.stockpot");
                } else {
                    type = Component.empty();
                }
                return Component.translatable("block.kaleidoscope_cookery.recipe_block.has_record", result, type);
            }
        }
        return super.getName(pStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemInHand = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();
        BlockEntity blockEntity = context.getLevel().getBlockEntity(clickedPos);
        RecipeManager recipeManager = context.getLevel().getRecipeManager();
        if (blockEntity == null) {
            return super.useOn(context);
        }
        Player player = context.getPlayer();
        if (player == null) {
            return super.useOn(context);
        }
        if (hasRecipe(itemInHand)) {
            return this.onPutRecipe(blockEntity, player, itemInHand);
        } else {
            return this.onRecordRecipe(context.getLevel(), player, blockEntity, recipeManager, itemInHand);
        }
    }

    private InteractionResult onPutRecipe(BlockEntity blockEntity, Player player, ItemStack itemInHand) {
        RecipeRecord record = getRecipe(itemInHand);
        if (record == null) {
            return InteractionResult.PASS;
        }

        if (blockEntity instanceof PotBlockEntity pot && pot.getStatus() == IPot.PUT_INGREDIENT
            && pot.getBlockState().getValue(PotBlock.HAS_OIL) && record.type().equals(POT)) {
            List<ItemStack> inputs = pot.getInputs().stream().filter(s -> !s.isEmpty()).toList();
            if (!inputs.isEmpty()) {
                return InteractionResult.PASS;
            }
            return handlePutRecipe(player, record, () -> pot.addAllIngredients(record.input(), player));
        }

        if (blockEntity instanceof StockpotBlockEntity stockpot && stockpot.getStatus() == IStockpot.PUT_INGREDIENT && record.type().equals(STOCKPOT)) {
            List<ItemStack> inputs = stockpot.getInputs().stream().filter(s -> !s.isEmpty()).toList();
            if (!inputs.isEmpty()) {
                return InteractionResult.PASS;
            }
            return handlePutRecipe(player, record, () -> stockpot.addAllIngredients(record.input(), player));
        }

        return InteractionResult.PASS;
    }

    @NotNull
    private InteractionResult handlePutRecipe(Player player, RecipeRecord record, Runnable success) {
        // 先对配方进行数量归纳
        Reference2IntMap<Item> need = new Reference2IntOpenHashMap<>();
        for (ItemStack s : record.input()) {
            if (s.isEmpty()) {
                continue;
            }
            Item item = s.getItem();
            need.put(item, need.getInt(item) + 1);
        }

        // 开始检查身上的物品
        IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());
        Reference2IntMap<Item> supply = new Reference2IntOpenHashMap<>();
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack s = inventory.getStackInSlot(slot);
            if (s.isEmpty()) {
                continue;
            }

            // 触发特殊计数
            var event = new RecipeItemEvent.CheckItem(s, supply);
            NeoForge.EVENT_BUS.post(event);

            // 正常计数
            Item item = s.getItem();
            supply.put(item, supply.getInt(item) + s.getCount());
        }

        // 两者做对比，检查物品是否足够
        Reference2IntMap<Item> missing = new Reference2IntOpenHashMap<>();
        for (Item item : need.keySet()) {
            if (supply.getInt(item) < need.getInt(item)) {
                missing.put(item, need.getInt(item) - supply.getInt(item));
            }
        }

        if (!missing.isEmpty()) {
            // 物品不足，提示缺少的物品
            MutableComponent component = Component.translatable("tooltip.kaleidoscope_cookery.recipe_item.missing");
            int i = 0;
            for (Item s : missing.keySet()) {
                Component hoverName = s.getDefaultInstance().getHoverName();
                MutableComponent count = Component.literal("×%d".formatted(missing.getInt(s)));
                if (i != 0) {
                    component = component.append(CommonComponents.SPACE);
                }
                component.append(CommonComponents.SPACE).append(hoverName).append(count);
                i++;
            }
            if (!player.level().isClientSide()) {
                player.sendSystemMessage(component);
            }
            return InteractionResult.FAIL;
        }

        // 物品足够，开始扣除物品
        for (Item item : need.keySet()) {
            int needCount = need.getInt(item);
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack inSlot = inventory.getStackInSlot(i);
                if (inSlot.isEmpty()) {
                    continue;
                }

                // 触发特殊扣除
                var event = new RecipeItemEvent.DeductItem(inSlot, item, new int[]{needCount});
                NeoForge.EVENT_BUS.post(event);
                needCount = event.getNeedCount();
                if (needCount <= 0) {
                    break;
                }

                // 正常扣除
                if (inSlot.is(item)) {
                    int extracted = Math.min(needCount, inSlot.getCount());
                    inventory.extractItem(i, extracted, false);
                    needCount -= extracted;
                    if (needCount <= 0) {
                        break;
                    }
                }
            }
        }

        // 扣除完毕，放入锅中
        success.run();
        return InteractionResult.SUCCESS;
    }

    private InteractionResult onRecordRecipe(Level level, Player player, BlockEntity blockEntity,
                                             RecipeManager recipeManager, ItemStack itemInHand) {
        if (blockEntity instanceof PotBlockEntity pot && pot.getStatus() == IPot.PUT_INGREDIENT) {
            List<ItemStack> inputs = pot.getInputs().stream().filter(s -> !s.isEmpty()).toList();
            if (inputs.isEmpty()) {
                return InteractionResult.PASS;
            }
            ItemStack recordStack = itemInHand.split(1);
            RecipeResult recipeResult = getPotRecipeResult(level, recipeManager, pot, inputs, recordStack);
            setRecipe(recordStack, new RecipeRecord(inputs, recipeResult.output(), POT, recipeResult.flexRecipe()));
            ItemUtils.getItemToLivingEntity(player, recordStack);
            return InteractionResult.SUCCESS;
        }

        if (blockEntity instanceof StockpotBlockEntity stockpot && stockpot.getStatus() == IStockpot.PUT_INGREDIENT) {
            List<ItemStack> inputs = stockpot.getInputs().stream().filter(s -> !s.isEmpty()).toList();
            if (inputs.isEmpty()) {
                return InteractionResult.PASS;
            }
            ItemStack recordStack = itemInHand.split(1);
            RecipeResult recipeResult = getStockpotRecipeResult(level, recipeManager, stockpot, inputs, recordStack);
            setRecipe(recordStack, new RecipeRecord(inputs, recipeResult.output(), STOCKPOT, recipeResult.flexRecipe()));
            ItemUtils.getItemToLivingEntity(player, recordStack);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private RecipeResult getPotRecipeResult(Level level, RecipeManager recipeManager, PotBlockEntity pot,
                                            List<ItemStack> inputs, ItemStack recordStack) {
        var container = pot.getContainer();
        var potRecipe = recipeManager.getRecipeFor(ModRecipes.POT_RECIPE, container, level);
        if (potRecipe.isPresent()) {
            ItemStack assemble = potRecipe.get().value()
                    .assemble(container, level.registryAccess());
            return new RecipeResult(assemble, false);
        }

        var flexPotRecipe = recipeManager.getRecipeFor(ModRecipes.FLEX_POT_RECIPE, container, level);
        if (flexPotRecipe.isPresent()) {
            var recipe = flexPotRecipe.get();
            ItemStack result = recipe.value().assemble(container, level.registryAccess());
            setQuality(level, inputs, recipe.value().ingredients(), recipe.id(), result, recordStack);
            return new RecipeResult(result, true);
        }

        ItemStack instance = FoodBiteRegistry.getItem(FoodBiteRegistry.SUSPICIOUS_STIR_FRY).getDefaultInstance();
        return new RecipeResult(instance, false);
    }

    private RecipeResult getStockpotRecipeResult(Level level, RecipeManager recipeManager, StockpotBlockEntity stockpot,
                                                 List<ItemStack> inputs, ItemStack recordStack) {
        var container = stockpot.getInput();
        var stockpotRecipe = recipeManager.getRecipeFor(ModRecipes.STOCKPOT_RECIPE, container, level);
        if (stockpotRecipe.isPresent()) {
            ItemStack assemble = stockpotRecipe.get().value()
                    .assemble(container, level.registryAccess());
            return new RecipeResult(assemble, false);
        }

        var flexStockpotRecipe = recipeManager.getRecipeFor(ModRecipes.FLEX_STOCKPOT_RECIPE, container, level);
        if (flexStockpotRecipe.isPresent()) {
            var recipe = flexStockpotRecipe.get();
            ItemStack result = recipe.value().assemble(container, level.registryAccess());
            setQuality(level, inputs, recipe.value().ingredients(), recipe.id(), result, recordStack);
            return new RecipeResult(result, true);
        }

        return new RecipeResult(Items.SUSPICIOUS_STEW.getDefaultInstance(), false);
    }

    private void setQuality(Level level, List<ItemStack> inputs, List<Ingredient> ingredients,
                            ResourceLocation recipeId, ItemStack result, ItemStack recordStack) {
        if (level instanceof ServerLevel serverLevel) {
            Quality quality = QualityEvaluator.evaluate(inputs, ingredients, recipeId, serverLevel.getSeed());
            QualityUtils.setQuality(result, quality);
            QualityUtils.setQuality(recordStack, quality);
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (hasRecipe(stack)) {
            RecipeRecord recipe = getRecipe(stack);
            if (recipe == null) {
                return Optional.empty();
            }
            Quality quality = null;
            if (QualityUtils.hasQuality(stack)) {
                quality = QualityUtils.getQuality(stack);
            }
            return Optional.of(new RecipeItemTooltip(recipe, quality));
        }
        return Optional.empty();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.recipe_item").withStyle(ChatFormatting.GRAY));
    }

    private record RecipeResult(ItemStack output, boolean flexRecipe) {
    }

    public record RecipeRecord(List<ItemStack> input, ItemStack output, ResourceLocation type, boolean flexRecipe) {
        public static final Codec<RecipeRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStack.OPTIONAL_CODEC.listOf().fieldOf("input").forGetter(RecipeRecord::input),
                ItemStack.CODEC.fieldOf("output").forGetter(RecipeRecord::output),
                ResourceLocation.CODEC.fieldOf("type").forGetter(RecipeRecord::type),
                Codec.BOOL.fieldOf("flex_recipe").forGetter(RecipeRecord::flexRecipe)
        ).apply(instance, RecipeRecord::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, RecipeRecord> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public RecipeRecord decode(RegistryFriendlyByteBuf buffer) {
                int size = buffer.readVarInt();
                List<ItemStack> inputs = Lists.newArrayList();
                for (int i = 0; i < size; i++) {
                    inputs.add(ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer));
                }
                ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
                ResourceLocation type = buffer.readResourceLocation();
                boolean flexRecipe = buffer.readBoolean();
                return new RecipeRecord(inputs, output, type, flexRecipe);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, RecipeRecord value) {
                buffer.writeVarInt(value.input().size());
                for (ItemStack s : value.input()) {
                    ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, s);
                }
                ItemStack.STREAM_CODEC.encode(buffer, value.output());
                buffer.writeResourceLocation(value.type());
                buffer.writeBoolean(value.flexRecipe());
            }
        };
        public static RecipeRecord pot(ItemLike output, ItemLike... input) {
            List<ItemStack> inputList = Arrays.stream(input).map(ItemStack::new).toList();
            return new RecipeRecord(inputList, new ItemStack(output), POT, false);
        }

        @SafeVarargs
        public static RecipeRecord pot(DeferredItem<Item> output, DeferredItem<Item>... input) {
            List<ItemStack> inputList = Arrays.stream(input).map(s -> new ItemStack(s.get())).toList();
            return new RecipeRecord(inputList, new ItemStack(output.get()), POT, false);
        }

        public static RecipeRecord stockpot(ItemLike output, ItemLike... input) {
            List<ItemStack> inputList = Arrays.stream(input).map(ItemStack::new).toList();
            return new RecipeRecord(inputList, new ItemStack(output), STOCKPOT, false);
        }

        @SafeVarargs
        public static RecipeRecord stockpot(DeferredItem<Item> output, DeferredItem<Item>... input) {
            List<ItemStack> inputList = Arrays.stream(input).map(s -> new ItemStack(s.get())).toList();
            return new RecipeRecord(inputList, new ItemStack(output.get()), STOCKPOT, false);
        }
    }
}
