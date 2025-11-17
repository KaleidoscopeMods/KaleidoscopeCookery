package com.github.ysbbbbbb.kaleidoscopecookery.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Villager.class)
public class VillagerMixin {
    @Unique
    private static Set<Item> MOD_WANTED_ITEMS = null;

    @Inject(method = "wantsToPickUp(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    public void onVillagerWantsToPickUp(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        // 避免过早初始化，导致读取的 Item 全部为 null
        if (MOD_WANTED_ITEMS == null) {
            MOD_WANTED_ITEMS = ImmutableSet.of(
                    ModItems.TOMATO, ModItems.TOMATO_SEED,
                    ModItems.RED_CHILI, ModItems.GREEN_CHILI, ModItems.CHILI_SEED,
                    ModItems.LETTUCE, ModItems.LETTUCE_SEED
            );
        }
        if (MOD_WANTED_ITEMS.contains(stack.getItem())) {
            cir.setReturnValue(true);
        }
    }
}
