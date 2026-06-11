package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PlateRegistry {
    public static final Map<ResourceLocation, PlateData> PLATE_DATA_MAP = Maps.newLinkedHashMap();

    public static ResourceLocation SHENGJIAN_MANTOU_PLATE;
    public static ResourceLocation BAOZI_PLATE;
    public static ResourceLocation QINGTUAN_PLATE;
    public static ResourceLocation STICKY_CANDY_PLATE;
    public static ResourceLocation STICKY_RICE_CAKE_PLATE;
    public static ResourceLocation ZONGZI_PLATE;
    public static ResourceLocation BERRY_PLATTER;
    public static ResourceLocation APPLE_PLATTER;
    public static ResourceLocation TOMATO_PLATTER;
    public static ResourceLocation WATERMELON_PLATTER;
    public static ResourceLocation CHORUS_FRUIT_PLATTER;

    public static void init() {
        PlateRegistry registry = new PlateRegistry();

        SHENGJIAN_MANTOU_PLATE = registry.registerPlateData("shengjian_mantou_plate", PlateData.create(5)
                .setServingItems(ModItems.SHENGJIAN_MANTOU)
                .setLootItem(Items.BOWL)
        );

        BAOZI_PLATE = registry.registerPlateData("baozi_plate", PlateData.create(5)
                .setServingItems(ModItems.BAOZI)
                .setLootItem(Items.BOWL)
        );

        QINGTUAN_PLATE = registry.registerPlateData("qingtuan_plate", PlateData.create(4)
                .setServingItems(ModItems.QINGTUAN)
                .setLootItem(Items.BOWL)
        );

        STICKY_CANDY_PLATE = registry.registerPlateData("sticky_candy_plate", PlateData.create(4)
                .setServingItems(ModItems.STICKY_CANDY)
                .setLootItem(Items.BOWL)
        );

        STICKY_RICE_CAKE_PLATE = registry.registerPlateData("sticky_rice_cake_plate", PlateData.create(5)
                .setServingItems(ModItems.STICKY_RICE_CAKE)
                .setLootItem(Items.BOWL)
        );

        ZONGZI_PLATE = registry.registerPlateData("zongzi_plate", PlateData.create(4)
                .setServingItems(ModItems.ZONGZI)
                .setLootItem(Items.BOWL)
        );

        BERRY_PLATTER = registry.registerPlateData("berry_platter", PlateData.create(4)
                .addServingItems(() -> Items.SWEET_BERRIES, () -> Items.GLOW_BERRIES)
                .setLootItem(Items.BOWL)
                .platterAABB()
        );

        APPLE_PLATTER = registry.registerPlateData("apple_platter", PlateData.create(4)
                .setServingItems(() -> Items.APPLE)
                .setLootItem(Items.BOWL)
                .platterAABB()
        );

        TOMATO_PLATTER = registry.registerPlateData("tomato_platter", PlateData.create(5)
                .setServingItems(ModItems.TOMATO)
                .setLootItem(Items.BOWL)
                .platterAABB()
        );

        WATERMELON_PLATTER = registry.registerPlateData("watermelon_platter", PlateData.create(3)
                .setServingItems(() -> Items.MELON_SLICE)
                .setLootItem(Items.BOWL)
                .platterAABB()
        );

        CHORUS_FRUIT_PLATTER = registry.registerPlateData("chorus_fruit_platter", PlateData.create(5)
                .setServingItems(() -> Items.CHORUS_FRUIT)
                .setLootItem(Items.BOWL)
                .platterAABB()
        );
    }

    public ResourceLocation registerPlateData(ResourceLocation id, PlateData data) {
        PLATE_DATA_MAP.put(id, data);
        return id;
    }

    public ResourceLocation registerPlateData(String name, PlateData data) {
        return registerPlateData(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, name), data);
    }

    public static int getCount(ResourceLocation id) {
        if (PLATE_DATA_MAP.containsKey(id)) {
            return PLATE_DATA_MAP.get(id).getMaxCount();
        }
        return 0;
    }

    public static Item getItem(ResourceLocation name) {
        return BuiltInRegistries.ITEM.get(name);
    }

    public static Block getBlock(ResourceLocation name) {
        return BuiltInRegistries.BLOCK.get(name);
    }

    public static final class PlateData {
        private final int maxCount;
        private final List<Supplier<Item>> servingItems = Lists.newArrayList();
        private final List<ItemLike> lootItems = Lists.newArrayList();
        private @Nullable VoxelShape aabb = null;

        public PlateData(int maxCount) {
            this.maxCount = maxCount;
        }

        public static PlateData create(int maxCount) {
            return new PlateData(maxCount);
        }

        @SafeVarargs
        public final PlateData addServingItems(Supplier<Item>... servingItems) {
            this.servingItems.addAll(Arrays.stream(servingItems).toList());
            return this;
        }

        public PlateData setServingItems(Supplier<Item> servingItem) {
            this.servingItems.clear();
            this.servingItems.add(servingItem);
            return this;
        }

        public PlateData addLootItems(ItemLike... lootItems) {
            this.lootItems.addAll(Arrays.stream(lootItems).toList());
            return this;
        }

        public PlateData setLootItem(ItemLike lootItem) {
            this.lootItems.clear();
            this.lootItems.add(lootItem);
            return this;
        }

        public PlateData setAABB(VoxelShape aabb) {
            this.aabb = aabb;
            return this;
        }

        public PlateData platterAABB() {
            this.aabb = Shapes.or(
                    Block.box(4, 0, 4, 12, 2, 12),
                    Block.box(6, 2, 6, 10, 4, 10),
                    Block.box(1, 4, 1, 15, 6, 15)
            );
            return this;
        }

        public int getMaxCount() {
            return maxCount;
        }

        @Nullable
        public VoxelShape getAABB() {
            return aabb;
        }

        public List<Supplier<Item>> getServingItems() {
            return servingItems;
        }

        public List<ItemLike> getLootItems() {
            return lootItems;
        }
    }
}
