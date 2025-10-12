package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.util;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SimpleSoupBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.Predicate;

@Info("""
        A builder for simple soup base, used in KubeJS. <br>
        简易汤底的构建器，用于 KubeJS
        """)
public final class SimpleSoupBaseBuilder {
    private final ResourceLocation name;

    private ItemStack displayStack = Items.APPLE.getDefaultInstance();
    private ResourceLocation soupBaseTexture = StockpotRecipeSerializer.DEFAULT_COOKING_TEXTURE;
    private int bubbleColor = StockpotRecipeSerializer.DEFAULT_COOKING_BUBBLE_COLOR;

    private Predicate<ItemStack> soupBasePredicate = stack -> false;
    private Predicate<ItemStack> containerPredicate = stack -> false;

    private TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnContainerFunction =
            (level, entity, stack) -> ItemStack.EMPTY;
    private TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnSoupBaseFunction =
            (level, entity, stack) -> ItemStack.EMPTY;

    private SimpleSoupBaseBuilder(ResourceLocation name) {
        this.name = name;
    }

    @Info("""
            Create a soup base, need to provide an id, this id will be used in subsequent stockpot recipes. <br>
            创建一个汤底，需要填写 id，这个 id 将用于后续汤锅配方。
            """)
    public static SimpleSoupBaseBuilder create(ResourceLocation name) {
        return new SimpleSoupBaseBuilder(name);
    }

    @Info("""
            The item used to display the soup base in the JEI UI. <br>
            在 JEI 界面中用于显示汤底的物品。
            """)
    public SimpleSoupBaseBuilder displayStack(ItemStack displayStack) {
        this.displayStack = displayStack;
        return this;
    }

    @Info("""
            The texture used to render the soup base in the soup pot. Supports dynamic textures. The texture needs to be registered in atlases/block.json. <br>
            在汤锅中用于渲染汤底的材质，支持动态材质。材质需要在 atlases/block.json 中注册。
            """)
    public SimpleSoupBaseBuilder soupBaseTexture(ResourceLocation soupBaseTexture) {
        this.soupBaseTexture = soupBaseTexture;
        return this;
    }

    @Info("""
            The color of the bubble particles when the soup base is first added to the soup pot. <br>
            刚放入汤底时，汤锅中气泡粒子的颜色。
            """)
    public SimpleSoupBaseBuilder bubbleColor(int bubbleColor) {
        this.bubbleColor = bubbleColor;
        return this;
    }

    @Info("""
            A predicate to test whether an item is the soup base. <br>
            当玩家将物品放入汤锅时，判断该物品是否为该汤底的物品。
            """)
    public SimpleSoupBaseBuilder soupBasePredicate(Predicate<ItemStack> soupBasePredicate) {
        this.soupBasePredicate = soupBasePredicate;
        return this;
    }

    @Info("""
            A predicate to test whether an item is a valid container to retrieve the soup base. <br>
            当玩家将物品放入汤锅时，判断该物品是否为取回汤底的有效容器。
            """)
    public SimpleSoupBaseBuilder containerPredicate(Predicate<ItemStack> containerPredicate) {
        this.containerPredicate = containerPredicate;
        return this;
    }

    @Info("""
            When soupBasePredicate tests true, this function is called to get the item returned to the player. <br>
            当 soupBasePredicate 判断汤底物品符合时，调用该函数获取返回给玩家的物品。
            """)
    public SimpleSoupBaseBuilder returnContainerFunction(TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnContainerFunction) {
        this.returnContainerFunction = returnContainerFunction;
        return this;
    }

    @Info("""
            When containerPredicate tests true, this function is called to get the item returned to the player. <br>
            当 containerPredicate 判断容器物品符合时，调用该函数获取返回给玩家的物品。
            """)
    public SimpleSoupBaseBuilder returnSoupBaseFunction(TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnSoupBaseFunction) {
        this.returnSoupBaseFunction = returnSoupBaseFunction;
        return this;
    }

    @HideFromJS
    public SimpleSoupBase build() {
        return new SimpleSoupBase(
                name,
                displayStack,
                soupBaseTexture,
                bubbleColor,
                soupBasePredicate,
                containerPredicate,
                returnContainerFunction,
                returnSoupBaseFunction
        );
    }
}
