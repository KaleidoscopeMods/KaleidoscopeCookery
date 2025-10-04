package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.carryon.CarryOnBlackList;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

import static com.github.ysbbbbbb.kaleidoscopecookery.compat.carryon.CarryOnBlackList.CARRY_ON_ID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CompatRegistry {
    public static boolean SHOW_POTION_EFFECT_TOOLTIPS = true;

    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> checkModLoad(CARRY_ON_ID, CarryOnBlackList::addBlackList));
        event.enqueueWork(() -> {
            // 当安装 Food Effect Tooltips (Forge) 模组时，关闭药水效果提示
            SHOW_POTION_EFFECT_TOOLTIPS = !ModList.get().isLoaded("foodeffecttooltips");
        });
    }

    private static void checkModLoad(String modId, Runnable runnable) {
        if (ModList.get().isLoaded(modId)) {
            runnable.run();
        }
    }
}
