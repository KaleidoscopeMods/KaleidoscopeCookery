package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SimpleSoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModSoupBases {
    public static final ResourceLocation WATER = new ResourceLocation("minecraft", "water");
    public static final ResourceLocation LAVA = new ResourceLocation("minecraft", "lava");
    public static final ResourceLocation MILK = new ResourceLocation("minecraft", "milk");
    public static final ResourceLocation AXOLOTL_BUCKET = new ResourceLocation("minecraft", "axolotl_bucket");
    public static final ResourceLocation COD_BUCKET = new ResourceLocation("minecraft", "cod_bucket");
    public static final ResourceLocation SALMON_BUCKET = new ResourceLocation("minecraft", "salmon_bucket");
    public static final ResourceLocation TROPICAL_FISH_BUCKET = new ResourceLocation("minecraft", "tropical_fish_bucket");
    public static final ResourceLocation PUFFERFISH_BUCKET = new ResourceLocation("minecraft", "pufferfish_bucket");
    public static final ResourceLocation TADPOLE_BUCKET = new ResourceLocation("minecraft", "tadpole_bucket");

    public static void registerAll() {
        SoupBaseManager.registerFluidSoupBase(WATER, Items.WATER_BUCKET, 0x3F76E4);
        SoupBaseManager.registerFluidSoupBase(LAVA, Items.LAVA_BUCKET, 0xFF9838);

        SoupBaseManager.registerSoupBase(new SimpleSoupBase(
                MILK,
                new ItemStack(Items.MILK_BUCKET),
                new ResourceLocation(KaleidoscopeCookery.MOD_ID, "stockpot/milk"),
                0xFFF4D6,
                stack -> stack.is(Items.MILK_BUCKET),
                stack -> stack.is(Items.BUCKET),
                (level, user, stack) -> {
                    level.playSound(null, user.blockPosition(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return new ItemStack(Items.BUCKET);
                },
                (level, user, stack) -> {
                    level.playSound(null, user.blockPosition(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return new ItemStack(Items.MILK_BUCKET);
                }
        ));

        SoupBaseManager.registerMobSoupBase(AXOLOTL_BUCKET, Items.AXOLOTL_BUCKET);
        SoupBaseManager.registerMobSoupBase(COD_BUCKET, Items.COD_BUCKET);
        SoupBaseManager.registerMobSoupBase(SALMON_BUCKET, Items.SALMON_BUCKET);
        SoupBaseManager.registerMobSoupBase(TROPICAL_FISH_BUCKET, Items.TROPICAL_FISH_BUCKET);
        SoupBaseManager.registerMobSoupBase(PUFFERFISH_BUCKET, Items.PUFFERFISH_BUCKET);
        SoupBaseManager.registerMobSoupBase(TADPOLE_BUCKET, Items.TADPOLE_BUCKET);
    }
}
