package com.github.ysbbbbbb.kaleidoscopecookery.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import static com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects.*;
import static net.minecraft.world.effect.MobEffects.*;

public interface ModFoods {
    // 番茄
    FoodProperties TOMATO = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.5F)
            .alwaysEat().build();

    // 辣椒
    FoodProperties CHILI = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0)
            .alwaysEat().build();

    // 生菜
    FoodProperties LETTUCE = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0)
            .alwaysEat().build();

    // 猪儿虫
    FoodProperties CATERPILLAR = (new FoodProperties.Builder())
            .nutrition(18).saturationMod(0.2F).alwaysEat()
            .effect(() -> new MobEffectInstance(CONFUSION, 200), 1F)
            .build();

    // 刺身
    FoodProperties SASHIMI = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.5F)
            .alwaysEat().build();

    // 生羊排
    FoodProperties RAW_LAMB_CHOPS = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.5F)
            .alwaysEat().build();

    // 生牛杂
    FoodProperties RAW_COW_OFFAL = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.3F)
            .alwaysEat().build();

    // 生五花肉
    FoodProperties RAW_PORK_BELLY = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.3F)
            .alwaysEat().build();

    // 生驴肉
    FoodProperties RAW_DONKEY_MEAT = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.3F)
            .meat().alwaysEat().build();

    // 熟羊排
    FoodProperties COOKED_LAMB_CHOPS = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.8F)
            .alwaysEat().build();

    // 熟牛杂
    FoodProperties COOKED_COW_OFFAL = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.8F)
            .alwaysEat().build();

    // 熟五花肉
    FoodProperties COOKED_PORK_BELLY = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.8F)
            .alwaysEat().build();

    // 熟驴肉
    FoodProperties COOKED_DONKEY_MEAT = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.8F)
            .meat().alwaysEat().build();

    // 驴肉火烧
    FoodProperties DONKEY_BURGER = (new FoodProperties.Builder())
            .nutrition(12).saturationMod(0.8F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 45 * 20), 1.0F)
            .alwaysEat().build();

    // 包子
    FoodProperties BAOZI = (new FoodProperties.Builder())
            .nutrition(8).saturationMod(1)
            .effect(() -> new MobEffectInstance(ABSORPTION, 80 * 20), 1.0F)
            .alwaysEat().build();

    // 饺子
    FoodProperties DUMPLING = (new FoodProperties.Builder())
            .nutrition(8).saturationMod(1)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 80 * 20), 1.0F)
            .alwaysEat().build();

    // 烤包子
    FoodProperties SAMSA = (new FoodProperties.Builder())
            .nutrition(8).saturationMod(1)
            .effect(() -> new MobEffectInstance(DIG_SPEED, 100 * 20, 1), 1.0F)
            .alwaysEat().build();

    // 馒头
    FoodProperties MANTOU = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.9F)
            .fast().alwaysEat().build();

    // 馅饼
    FoodProperties MEAT_PIE = (new FoodProperties.Builder())
            .nutrition(8).saturationMod(1)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 80 * 20), 1.0F)
            .alwaysEat().build();

    // 牛肉面
    FoodProperties BEEF_NOODLE = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.643f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 烩面
    FoodProperties HUI_NOODLE = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.643f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 乌冬面
    FoodProperties UDON_NOODLE = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.643f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 煎蛋
    FoodProperties FRIED_EGG = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.5F)
            .alwaysEat().build();

    // 黑暗料理
    FoodProperties DARK_CUISINE_BLOCK = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0).alwaysEat()
            .effect(() -> new MobEffectInstance(BLINDNESS, 300), 0.33F)
            .effect(() -> new MobEffectInstance(POISON, 100), 0.33F)
            .build();

    FoodProperties DARK_CUISINE_ITEM = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0).alwaysEat()
            .effect(() -> new MobEffectInstance(BLINDNESS, 300), 0.33F)
            .effect(() -> new MobEffectInstance(POISON, 100), 0.33F)
            .build();

    // 迷之炒菜
    FoodProperties SUSPICIOUS_STIR_FRY_BLOCK = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.4F).alwaysEat()
            .effect(() -> new MobEffectInstance(MOVEMENT_SPEED, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(JUMP, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(DIG_SPEED, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(LUCK, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(DIG_SLOWDOWN, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(CONFUSION, 400), 0.15F)
            .build();

    FoodProperties SUSPICIOUS_STIR_FRY_ITEM = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.4F).alwaysEat()
            .effect(() -> new MobEffectInstance(MOVEMENT_SPEED, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(JUMP, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(DIG_SPEED, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(LUCK, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(DIG_SLOWDOWN, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(CONFUSION, 400), 0.15F)
            .build();

    // 粘液饭
    FoodProperties SLIME_BALL_MEAL_BLOCK = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0)
            .effect(() -> new MobEffectInstance(FLATULENCE.get(), 35 * 20), 1)
            .alwaysEat().build();

    FoodProperties SLIME_BALL_MEAL_ITEM = (new FoodProperties.Builder())
            .nutrition(9).saturationMod(0)
            .effect(() -> new MobEffectInstance(FLATULENCE.get(), 35 * 20), 1)
            .alwaysEat().build();

    // 翻糖派
    FoodProperties FONDANT_PIE_BLOCK = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 80 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties FONDANT_PIE_ITEM = (new FoodProperties.Builder())
            .nutrition(20).saturationMod(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 80 * 20), 1.0F)
            .alwaysEat().build();

    // 东坡肉
    FoodProperties DONGPO_PORK_BLOCK = (new FoodProperties.Builder())
            .nutrition(7).saturationMod(0.521f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 80 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties DONGPO_PORK_ITEM = (new FoodProperties.Builder())
            .nutrition(20).saturationMod(0.55f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 80 * 20), 1.0F)
            .alwaysEat().build();

    // 翻糖蛛眼
    FoodProperties FONDANT_SPIDER_EYE_BLOCK = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0)
            .effect(() -> new MobEffectInstance(FLATULENCE.get(), 35 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties FONDANT_SPIDER_EYE_ITEM = (new FoodProperties.Builder())
            .nutrition(8).saturationMod(0)
            .effect(() -> new MobEffectInstance(FLATULENCE.get(), 35 * 20), 1.0F)
            .alwaysEat().build();

    // 荷包紫颂烧
    FoodProperties CHORUS_FRIED_EGG_BLOCK = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0)
            .effect(() -> new MobEffectInstance(FLATULENCE.get(), 35 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties CHORUS_FRIED_EGG_ITEM = (new FoodProperties.Builder())
            .nutrition(9).saturationMod(0)
            .effect(() -> new MobEffectInstance(FLATULENCE.get(), 35 * 20), 1.0F)
            .alwaysEat().build();

    // 红烧鱼
    FoodProperties BRAISED_FISH_BLOCK = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.667F)
            .effect(() -> new MobEffectInstance(WATER_BREATHING, 3600), 1.0F)
            .alwaysEat().build();

    FoodProperties BRAISED_FISH_ITEM = (new FoodProperties.Builder())
            .nutrition(13).saturationMod(0.615F)
            .effect(() -> new MobEffectInstance(WATER_BREATHING, 3600), 1.0F)
            .alwaysEat().build();

    // 黄金沙拉
    FoodProperties GOLDEN_SALAD_BLOCK = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.55F)
            .effect(() -> new MobEffectInstance(DAMAGE_RESISTANCE, 2000), 1.0F)
            .effect(() -> new MobEffectInstance(REGENERATION, 200), 1.0F)
            .alwaysEat().build();

    FoodProperties GOLDEN_SALAD_ITEM = (new FoodProperties.Builder())
            .nutrition(20).saturationMod(0.55F)
            .effect(() -> new MobEffectInstance(DAMAGE_RESISTANCE, 2000), 1.0F)
            .effect(() -> new MobEffectInstance(REGENERATION, 200), 1.0F)
            .alwaysEat().build();

    // 辣子鸡
    FoodProperties SPICY_CHICKEN_BLOCK = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 80 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties SPICY_CHICKEN_ITEM = (new FoodProperties.Builder())
            .nutrition(20).saturationMod(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 80 * 20), 1.0F)
            .alwaysEat().build();

    // 烧鸟串
    FoodProperties YAKITORI_BLOCK = (new FoodProperties.Builder())
            .nutrition(5).saturationMod(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 80 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties YAKITORI_ITEM = (new FoodProperties.Builder())
            .nutrition(20).saturationMod(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 80 * 20), 1.0F)
            .alwaysEat().build();

    // 水晶羊排
    FoodProperties CRYSTAL_LAMB_CHOP_BLOCK = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.625F)
            .effect(() -> new MobEffectInstance(DIG_SPEED, 5 * 60 * 20, 1), 1.0F)
            .alwaysEat().build();

    FoodProperties CRYSTAL_LAMB_CHOP_ITEM = (new FoodProperties.Builder())
            .nutrition(13).saturationMod(0.615F)
            .effect(() -> new MobEffectInstance(DIG_SPEED, 5 * 60 * 20, 1), 1.0F)
            .alwaysEat().build();

    // 下界风味刺身
    FoodProperties NETHER_STYLE_SASHIMI_BLOCK = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD.get(), 5 * 60 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties NETHER_STYLE_SASHIMI_ITEM = (new FoodProperties.Builder())
            .nutrition(16).saturationMod(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD.get(), 5 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 香煎骑士牛排
    FoodProperties PAN_SEARED_KNIGHT_STEAK_BLOCK = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.625F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 1800), 1.0F)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 800), 1.0F)
            .alwaysEat().build();

    FoodProperties PAN_SEARED_KNIGHT_STEAK_ITEM = (new FoodProperties.Builder())
            .nutrition(13).saturationMod(0.615F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 1800), 1.0F)
            .alwaysEat().build();

    // 仰望星空派
    FoodProperties STARGAZY_PIE_BLOCK = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.667f)
            .effect(() -> new MobEffectInstance(FLATULENCE.get(), 600), 1.0F)
            .effect(() -> new MobEffectInstance(UNLUCK, 3600), 1.0F)
            .alwaysEat().build();

    FoodProperties STARGAZY_PIE_ITEM = (new FoodProperties.Builder())
            .nutrition(13).saturationMod(0.615F)
            .effect(() -> new MobEffectInstance(FLATULENCE.get(), 600), 1.0F)
            .effect(() -> new MobEffectInstance(UNLUCK, 3600), 1.0F)
            .alwaysEat().build();

    // 珍珠咕噜肉
    FoodProperties SWEET_AND_SOUR_ENDER_PEARLS_BLOCK = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0)
            .effect(() -> new MobEffectInstance(FLATULENCE.get(), 35 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties SWEET_AND_SOUR_ENDER_PEARLS_ITEM = (new FoodProperties.Builder())
            .nutrition(9).saturationMod(0)
            .effect(() -> new MobEffectInstance(FLATULENCE.get(), 35 * 20), 1.0F)
            .alwaysEat().build();

    // 烈焰羊排
    FoodProperties BLAZE_LAMB_CHOP_BLOCK = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.625f)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 1600), 1.0F)
            .alwaysEat().build();

    FoodProperties BLAZE_LAMB_CHOP_ITEM = (new FoodProperties.Builder())
            .nutrition(13).saturationMod(0.615F)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 1600), 1.0F)
            .alwaysEat().build();

    // 凛冬羊排
    FoodProperties FROST_LAMB_CHOP_BLOCK = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.625f)
            .effect(() -> new MobEffectInstance(TUNDRA_STRIDER.get(), 1600), 1.0F)
            .alwaysEat().build();

    FoodProperties FROST_LAMB_CHOP_ITEM = (new FoodProperties.Builder())
            .nutrition(13).saturationMod(0.615F)
            .effect(() -> new MobEffectInstance(TUNDRA_STRIDER.get(), 1600), 1.0F)
            .alwaysEat().build();

    // 末地风味刺身
    FoodProperties END_STYLE_SASHIMI_BLOCK = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD.get(), 5 * 60 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties END_STYLE_SASHIMI_ITEM = (new FoodProperties.Builder())
            .nutrition(16).saturationMod(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD.get(), 5 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 沙漠风味刺身
    FoodProperties DESERT_STYLE_SASHIMI_BLOCK = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD.get(), 5 * 60 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties DESERT_STYLE_SASHIMI_ITEM = (new FoodProperties.Builder())
            .nutrition(16).saturationMod(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD.get(), 5 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 苔原风味刺身
    FoodProperties TUNDRA_STYLE_SASHIMI_BLOCK = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD.get(), 5 * 60 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties TUNDRA_STYLE_SASHIMI_ITEM = (new FoodProperties.Builder())
            .nutrition(16).saturationMod(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD.get(), 5 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 寒带风味刺身
    FoodProperties COLD_STYLE_SASHIMI_BLOCK = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD.get(), 5 * 60 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties COLD_STYLE_SASHIMI_ITEM = (new FoodProperties.Builder())
            .nutrition(16).saturationMod(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD.get(), 5 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 水煎包
    FoodProperties SHENGJIAN_MANTOU_ITEM = (new FoodProperties.Builder())
            .nutrition(8).saturationMod(1)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 80 * 20), 1.0F)
            .alwaysEat().build();

    FoodProperties SHENGJIAN_MANTOU_BLOCK = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(1)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 6000), 1.0F)
            .alwaysEat().build();

    // 番茄炒蛋
    FoodProperties SCRAMBLE_EGG_WITH_TOMATOES = (new FoodProperties.Builder())
            .nutrition(9).saturationMod(0.611f)
            .effect(() -> new MobEffectInstance(VIGOR.get(), 90 * 20), 1.0F)
            .alwaysEat().build();

    // 爆炒牛杂
    FoodProperties STIR_FRIED_BEEF_OFFAL = (new FoodProperties.Builder())
            .nutrition(9).saturationMod(0.611f)
            .effect(() -> new MobEffectInstance(VIGOR.get(), 90 * 20), 1.0F)
            .alwaysEat().build();

    // 红烧牛肉
    FoodProperties BRAISED_BEEF = (new FoodProperties.Builder())
            .nutrition(9).saturationMod(0.611f)
            .effect(() -> new MobEffectInstance(VIGOR.get(), 90 * 20), 1.0F)
            .alwaysEat().build();

    // 青椒炒肉
    FoodProperties STIR_FRIED_PORK_WITH_PEPPERS = (new FoodProperties.Builder())
            .nutrition(9).saturationMod(0.611f)
            .effect(() -> new MobEffectInstance(VIGOR.get(), 90 * 20), 1.0F)
            .alwaysEat().build();

    // 糖醋里脊
    FoodProperties SWEET_AND_SOUR_PORK = (new FoodProperties.Builder())
            .nutrition(9).saturationMod(0.611f)
            .effect(() -> new MobEffectInstance(VIGOR.get(), 90 * 20), 1.0F)
            .alwaysEat().build();

    // 鱼香肉丝
    FoodProperties FISH_FLAVORED_SHREDDED_PORK = (new FoodProperties.Builder())
            .nutrition(9).saturationMod(0.611f)
            .effect(() -> new MobEffectInstance(VIGOR.get(), 90 * 20), 1.0F)
            .alwaysEat().build();

    // 田园杂蔬
    FoodProperties COUNTRY_STYLE_MIXED_VEGETABLES = (new FoodProperties.Builder())
            .nutrition(9).saturationMod(0.611f)
            .effect(() -> new MobEffectInstance(PRESERVATION.get(), 90 * 20), 1.0F)
            .alwaysEat().build();

    // 米饭
    FoodProperties COOKED_RICE = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(1)
            .alwaysEat().build();

    // 蛋炒饭
    FoodProperties EGG_FRIED_RICE = (new FoodProperties.Builder())
            .nutrition(8).saturationMod(0.875F)
            .alwaysEat().build();

    // 美味蛋炒饭
    FoodProperties DELICIOUS_EGG_FRIED_RICE = (new FoodProperties.Builder())
            .nutrition(12).saturationMod(0.667f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 谜之炒菜盖饭
    FoodProperties SUSPICIOUS_STIR_FRY_RICE_BOWL = (new FoodProperties.Builder())
            .nutrition(8).saturationMod(0.4F)
            .effect(() -> new MobEffectInstance(MOVEMENT_SPEED, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(JUMP, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(DIG_SPEED, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(LUCK, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(DIG_SLOWDOWN, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(CONFUSION, 400), 0.15F)
            .alwaysEat().build();

    // 番茄炒蛋盖饭
    FoodProperties SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 爆炒牛杂盖饭
    FoodProperties STIR_FRIED_BEEF_OFFAL_RICE_BOWL = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 红烧牛肉盖饭
    FoodProperties BRAISED_BEEF_RICE_BOWL = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 青椒炒肉盖饭
    FoodProperties STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 糖醋里脊盖饭
    FoodProperties SWEET_AND_SOUR_PORK_RICE_BOWL = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 鱼香肉丝盖饭
    FoodProperties FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 红烧鱼盖饭
    FoodProperties BRAISED_FISH_RICE_BOWL = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 辣子鸡盖饭
    FoodProperties SPICY_CHICKEN_RICE_BOWL = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 大骨汤
    FoodProperties PORK_BONE_SOUP = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.667f)
            .effect(() -> new MobEffectInstance(VIGOR.get(), 5 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 海鲜味噌汤
    FoodProperties SEAFOOD_MISO_SOUP = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.667f)
            .effect(() -> new MobEffectInstance(WATER_BREATHING, 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 恐惧浓汤
    FoodProperties FEARSOME_THICK_SOUP = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0)
            .effect(() -> new MobEffectInstance(SULFUR.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 萝卜羊肉汤
    FoodProperties LAMB_AND_RADISH_SOUP = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.667f)
            .effect(() -> new MobEffectInstance(TUNDRA_STRIDER.get(), 5 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 土豆炖牛肉
    FoodProperties BRAISED_BEEF_WITH_POTATOES = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.667f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 野菌兔肉汤
    FoodProperties WILD_MUSHROOM_RABBIT_SOUP = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.667f)
            .effect(() -> new MobEffectInstance(MOVEMENT_SPEED, 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 番茄牛腩汤
    FoodProperties TOMATO_BEEF_BRISKET_SOUP = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.667f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 河豚汤
    FoodProperties PUFFERFISH_SOUP = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0)
            .effect(() -> new MobEffectInstance(MUSTARD.get(), 10 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 罗宋汤
    FoodProperties BORSCHT = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.667f)
            .effect(() -> new MobEffectInstance(FLATULENCE.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 牛丸汤
    FoodProperties BEEF_MEATBALL_SOUP = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.667f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 小鸡炖蘑菇
    FoodProperties CHICKEN_AND_MUSHROOM_STEW = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.667f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 驴肉汤
    FoodProperties DONKEY_SOUP = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.667f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();
}
