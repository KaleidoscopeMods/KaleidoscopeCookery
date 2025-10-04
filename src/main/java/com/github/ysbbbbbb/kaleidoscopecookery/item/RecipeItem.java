package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IPot;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IStockpot;
import com.github.ysbbbbbb.kaleidoscopecookery.api.event.RecipeItemEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.RecipeItemTooltip;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RecipeItem extends BlockItem {
    public static final ResourceLocation HAS_RECIPE_PROPERTY = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "has_recipe");
    public static final ResourceLocation POT = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "pot");
    public static final ResourceLocation STOCKPOT = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot");

    public static final String RECIPE_TAG = "RecipeRecord";
    public static final String INPUT = "input";
    public static final String OUTPUT = "output";
    public static final String TYPE = "type";

    private static final int NO_RECIPE = 0;
    private static final int HAS_RECIPE = 1;

    public RecipeItem() {
        super(ModBlocks.RECIPE_BLOCK.get(), new Item.Properties());
    }

    public static void setRecipe(ItemStack stack, RecipeRecord record) {
        CompoundTag root = new CompoundTag();
        ListTag ingredients = new ListTag();
        for (ItemStack s : record.input()) {
            ingredients.add(s.save(new CompoundTag()));
        }
        root.put(INPUT, ingredients);
        root.put(OUTPUT, record.output().save(new CompoundTag()));
        root.putString(TYPE, record.type().toString());
        stack.getOrCreateTag().put(RECIPE_TAG, root);
    }

    @Nullable
    public static RecipeRecord getRecipe(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof RecipeItem)) {
            return null;
        }
        CompoundTag root = stack.getTagElement(RECIPE_TAG);
        if (root == null) {
            return null;
        }
        List<ItemStack> inputs = Lists.newArrayList();
        ListTag list = root.getList(INPUT, Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            inputs.add(ItemStack.of(list.getCompound(i)));
        }
        ItemStack output = ItemStack.of(root.getCompound(OUTPUT));
        ResourceLocation type = new ResourceLocation(root.getString(TYPE));
        return new RecipeRecord(inputs, output, type);
    }

    public static boolean hasRecipe(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(RECIPE_TAG);
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
            return this.onRecordRecipe(context, blockEntity, recipeManager, itemInHand);
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
            MinecraftForge.EVENT_BUS.post(event);

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
                MinecraftForge.EVENT_BUS.post(event);
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

    private InteractionResult onRecordRecipe(UseOnContext context, BlockEntity blockEntity, RecipeManager recipeManager, ItemStack itemInHand) {
        Level level = context.getLevel();
        if (blockEntity instanceof PotBlockEntity pot && pot.getStatus() == IPot.PUT_INGREDIENT) {
            List<ItemStack> inputs = pot.getInputs().stream().filter(s -> !s.isEmpty()).toList();
            if (inputs.isEmpty()) {
                return InteractionResult.PASS;
            }
            recipeManager.getRecipeFor(ModRecipes.POT_RECIPE, pot.getContainer(), level).ifPresentOrElse(recipe -> {
                ItemStack resultItem = recipe.getResultItem(level.registryAccess());
                setRecipe(itemInHand, new RecipeRecord(inputs, resultItem, POT));
            }, () -> {
                ItemStack instance = FoodBiteRegistry.getItem(FoodBiteRegistry.SUSPICIOUS_STIR_FRY).getDefaultInstance();
                setRecipe(itemInHand, new RecipeRecord(inputs, instance, POT));
            });
            return InteractionResult.SUCCESS;
        }

        if (blockEntity instanceof StockpotBlockEntity stockpot && stockpot.getStatus() == IStockpot.PUT_INGREDIENT) {
            List<ItemStack> inputs = stockpot.getInputs().stream().filter(s -> !s.isEmpty()).toList();
            if (inputs.isEmpty()) {
                return InteractionResult.PASS;
            }
            recipeManager.getRecipeFor(ModRecipes.STOCKPOT_RECIPE, stockpot.getContainer(), level).ifPresentOrElse(recipe -> {
                ItemStack resultItem = recipe.getResultItem(level.registryAccess());
                setRecipe(itemInHand, new RecipeRecord(inputs, resultItem, STOCKPOT));
            }, () -> {
                ItemStack instance = Items.SUSPICIOUS_STEW.getDefaultInstance();
                setRecipe(itemInHand, new RecipeRecord(inputs, instance, STOCKPOT));
            });
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (hasRecipe(stack)) {
            RecipeRecord recipe = getRecipe(stack);
            if (recipe == null) {
                return Optional.empty();
            }
            return Optional.of(new RecipeItemTooltip(recipe));
        }
        return Optional.empty();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.recipe_item").withStyle(ChatFormatting.GRAY));
    }

    public record RecipeRecord(List<ItemStack> input, ItemStack output, ResourceLocation type) {
        public static RecipeRecord pot(ItemLike output, ItemLike... input) {
            List<ItemStack> inputList = Arrays.stream(input).map(ItemStack::new).toList();
            return new RecipeRecord(inputList, new ItemStack(output), POT);
        }

        @SafeVarargs
        public static RecipeRecord pot(RegistryObject<Item> output, RegistryObject<Item>... input) {
            List<ItemStack> inputList = Arrays.stream(input).map(s -> new ItemStack(s.get())).toList();
            return new RecipeRecord(inputList, new ItemStack(output.get()), POT);
        }

        public static RecipeRecord stockpot(ItemLike output, ItemLike... input) {
            List<ItemStack> inputList = Arrays.stream(input).map(ItemStack::new).toList();
            return new RecipeRecord(inputList, new ItemStack(output), STOCKPOT);
        }

        @SafeVarargs
        public static RecipeRecord stockpot(RegistryObject<Item> output, RegistryObject<Item>... input) {
            List<ItemStack> inputList = Arrays.stream(input).map(s -> new ItemStack(s.get())).toList();
            return new RecipeRecord(inputList, new ItemStack(output.get()), STOCKPOT);
        }
    }
}
