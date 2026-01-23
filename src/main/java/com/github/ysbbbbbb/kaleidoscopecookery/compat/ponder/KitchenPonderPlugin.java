package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class KitchenPonderPlugin implements PonderPlugin {
    @Override
    public String getModId(){
        return KaleidoscopeCookery.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper){
        KitchenBlockPonderScreen.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        KitchenBlockPonderTag.register(helper);
    }

    public static void Init(){
        PonderIndex.addPlugin(new KitchenPonderPlugin());
    }
}
