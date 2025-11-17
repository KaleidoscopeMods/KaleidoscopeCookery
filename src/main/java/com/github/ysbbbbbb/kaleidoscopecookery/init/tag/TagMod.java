package com.github.ysbbbbbb.kaleidoscopecookery.init.tag;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface TagMod {
    /**
     * 本模组的物品标签，用于成就
     */
    TagKey<Item> COOKERY_MOD_ITEMS = itemTag("cookery_mod_items");
    /**
     * 本模组的作物种子，用于成就
     */
    TagKey<Item> COOKERY_MOD_SEEDS = itemTag("cookery_mod_seeds");
    /**
     * 任意可以点燃本模组炉灶的物品
     */
    TagKey<Item> LIT_STOVE = itemTag("lit_stove");
    /**
     * 任意可以熄灭本模组炉灶的物品
     */
    TagKey<Item> EXTINGUISH_STOVE = itemTag("extinguish_stove");
    /**
     * 任意可以用作炒锅油的物品
     */
    TagKey<Item> OIL = itemTag("oil");
    /**
     * 可以当做草帽的物品
     */
    TagKey<Item> STRAW_HAT = itemTag("straw_hat");
    /**
     * 可以当做稻草捆的方块
     */
    TagKey<Item> STRAW_BALE = itemTag("straw_bale");
    /**
     * 可以当本模组菜刀的物品
     */
    TagKey<Item> KITCHEN_KNIFE = itemTag("kitchen_knife");
    /**
     * 可以视作本模组锅铲炒菜的工具
     */
    TagKey<Item> KITCHEN_SHOVEL = itemTag("kitchen_shovel");
    /**
     * 农夫套装
     */
    TagKey<Item> FARMER_ARMOR = itemTag("farmer_armor");
    /**
     * 可以加入汤锅炒锅的原料
     */
    TagKey<Item> POT_INGREDIENT = itemTag("pot_ingredient");
    /**
     * 取出原料所识别的容器
     */
    TagKey<Item> INGREDIENT_CONTAINER = itemTag("ingredient_container");
    /**
     * 返回碗容器的物品，程序内嵌了本模组和原版所有的碗装食物的支持
     * <p>
     * 所以这个 tag 只需要添加其他不符合上述设定的碗容器食物即可
     */
    TagKey<Item> BOWL_CONTAINER = itemTag("bowl_container");
    /**
     * 返回桶容器的物品
     */
    TagKey<Item> BUCKET_CONTAINER = itemTag("bucket_container");
    /**
     * 返回玻璃瓶容器的物品
     */
    TagKey<Item> GLASS_BOTTLE_CONTAINER = itemTag("glass_bottle_container");
    /**
     * 石磨取出面团所使用的容器
     */
    TagKey<Item> MILLSTONE_DOUGH_CONTAINER = itemTag("millstone_dough_container");
    /**
     * 寒带疾行效果可以提速的方块
     */
    TagKey<Block> TUNDRA_STRIDER_SPEED_BLOCKS = blockTag("tundra_strider_speed_blocks");
    /**
     * 温暖效果下，认为是热源的方块
     */
    TagKey<Block> WARMTH_HEAT_SOURCE_BLOCKS = blockTag("warmth_heat_source_blocks");
    /**
     * 吸引猫躺下的方块
     */
    TagKey<Block> CAT_LIE_ON_BLOCKS = blockTag("cat_lie_on_blocks");
    /**
     * 可以当做本模组热源的方块
     * <p>
     * 默认是任何具有 LIT 标签的方块或拥有此 tag 的方块，故这里需要添加的是没有 LIT 标签的热源方块
     */
    TagKey<Block> HEAT_SOURCE_BLOCKS_WITHOUT_LIT = blockTag("heat_source_blocks_without_lit");
    /**
     * 被本模组当做猪油来源的实体
     */
    TagKey<EntityType<?>> PIG_OIL_SOURCE = entityTag("pig_oil_source");
    /**
     * 可以拉磨的生物，必须继承自 Mob
     */
    TagKey<EntityType<?>> MILLSTONE_BINDABLE = entityTag("millstone_bindable");
    /**
     * 可以促进本模组水稻生长的水生生物
     */
    TagKey<EntityType<?>> RICE_GROWTH_BOOSTER = entityTag("rice_growth_booster");
    /**
     * 饱腹代偿效果抵御效果较差的伤害来源
     */
    TagKey<DamageType> SATIATED_SHIELD_WEAKNESS = damageTypeTag("satiated_shield_weakness");

    static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(KaleidoscopeCookery.MOD_ID, name));
    }

    static TagKey<Block> blockTag(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(KaleidoscopeCookery.MOD_ID, name));
    }

    static TagKey<EntityType<?>> entityTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, name));
    }

    static TagKey<DamageType> damageTypeTag(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(KaleidoscopeCookery.MOD_ID, name));
    }
}
