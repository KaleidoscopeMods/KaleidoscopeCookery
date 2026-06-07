package com.github.ysbbbbbb.kaleidoscopecookery.item.quality;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QualityEvaluator {
    private static final int MAX_CAPACITY = 9;

    public static Quality evaluate(List<ItemStack> inputs, List<Ingredient> ingredients,
                                   ResourceLocation recipeId, long worldSeed) {
        // 先剔除空占位符
        List<Ingredient> nonEmpty = Lists.newArrayList();
        ingredients.forEach(ingredient -> {
            if (!ingredient.isEmpty()) {
                nonEmpty.add(ingredient);
            }
        });

        // 任意为空则直接返回 POOR 品质，避免除零和无意义的评估
        if (inputs.isEmpty() || nonEmpty.isEmpty()) {
            return Quality.POOR;
        }

        // 如果种类数量为 1，那么仅依据投料数量进行判断
        if (nonEmpty.size() == 1) {
            return oneInputQuality(inputs, recipeId, worldSeed);
        }

        var recipeVector = randomVector(nonEmpty, recipeId, worldSeed);
        return evalQuality(inputs, recipeVector);
    }

    private static @NotNull Quality oneInputQuality(List<ItemStack> inputs, ResourceLocation recipeId, long worldSeed) {
        // 生成专属的种子
        long recipeSeed = worldSeed * 31 + recipeId.hashCode();
        Random random = new Random(recipeSeed);

        int count = inputs.size();
        if (count <= 1) {
            return Quality.POOR;
        }

        int standard = 1 + random.nextInt(2);
        if (count <= standard) {
            return Quality.STANDARD;
        }

        int excellent = 2 + random.nextInt(2);
        if (count <= excellent) {
            return Quality.EXCELLENT;
        }

        return Quality.SUPERB;
    }

    /**
     * 依据配方的 ID 和世界的 seed，生成随机但是固定的配方比例
     */
    private static List<Pair<Ingredient, Integer>> randomVector(List<Ingredient> ingredients, ResourceLocation id, long seed) {
        // 生成专属的种子
        long recipeSeed = seed * 31 + id.hashCode();
        Random random = new Random(recipeSeed);

        // 按总数生成比例
        int size = ingredients.size();
        IntList countPool = new IntArrayList(size);

        switch (size) {
            case 2:
                // 主料 (2~4) + 辅料 (1~2)
                countPool.add(2 + random.nextInt(3)); // 2, 3, 4
                countPool.add(1 + random.nextInt(2)); // 1, 2
                break;

            case 3:
                // 阶梯式：大 (2~4) + 中 (1~3) + 小 (1~2)
                countPool.add(2 + random.nextInt(3)); // 2, 3, 4
                countPool.add(1 + random.nextInt(3)); // 1, 2, 3
                countPool.add(1 + random.nextInt(2)); // 1, 2
                break;

            default:
                // 4种及以上，精细控量防止总数爆炸，全员 1~2
                for (int i = 0; i < size; i++) {
                    countPool.add(1 + random.nextInt(2)); // 1, 2
                }
                break;
        }

        Collections.shuffle(countPool, random);

        // 5. 将打乱后的数量与 Ingredient 映射绑定
        List<Pair<Ingredient, Integer>> recipeVector = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            recipeVector.add(Pair.of(ingredients.get(i), countPool.getInt(i)));
        }

        return recipeVector;
    }

    /**
     * 评估函数，依据输入的物品和配方，得出最终的品质档位
     */
    private static Quality evalQuality(List<ItemStack> inputs, List<Pair<Ingredient, Integer>> recipes) {
        int size = recipes.size();

        // 先获取投料和配方的比例向量
        int[] inputsVec = new int[size];
        int[] recipesVec = new int[size];
        for (int i = 0; i < size; i++) {
            var pair = recipes.get(i);
            for (ItemStack stack : inputs) {
                if (pair.left().test(stack)) {
                    inputsVec[i]++;
                }
            }
            recipesVec[i] = pair.right();
        }

        // 评估函数获取分数
        double finalScore = getFinalScore(size, inputs.size(), inputsVec, recipesVec);

        // 根据得分划分品质档位
        for (Quality quality : Quality.values()) {
            if (finalScore >= quality.getScore()) {
                return quality;
            }
        }

        return Quality.STANDARD;
    }

    private static double getFinalScore(int size, int inputSize, int[] inputsVec, int[] recipesVec) {
        // 计算基础余弦相似度
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < size; i++) {
            double a = inputsVec[i];
            double b = recipesVec[i];
            dotProduct += a * b;
            normA += a * a;
            normB += b * b;
        }
        double cosine = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));

        // 四次方陡峭化，将低容量下密集的余弦值拉开档次
        double similarityScore = Math.pow(cosine, 4);

        // 数量因子
        double quantityFactor = 0.8 + 0.2 * (inputSize / (double) MAX_CAPACITY);

        // 复合得分
        return Mth.clamp(similarityScore * quantityFactor, 0.0, 1.0);
    }
}
