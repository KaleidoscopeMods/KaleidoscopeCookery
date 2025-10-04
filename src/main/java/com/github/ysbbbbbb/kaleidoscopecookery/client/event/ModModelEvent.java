package com.github.ysbbbbbb.kaleidoscopecookery.client.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModModelEvent {
    private static final String MODELS = "models/";
    private static final String MODELS_CHOPPING_BOARD = MODELS + "chopping_board";
    private static final String MODELS_CARPET = MODELS + "block/carpet";
    private static final String JSON = ".json";

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        resourceManager.listResources(MODELS_CHOPPING_BOARD, id -> id.getPath().endsWith(JSON))
                .keySet().stream().map(ModModelEvent::handleModelId).forEach(event::register);

        resourceManager.listResources(MODELS_CARPET, id -> id.getPath().endsWith(JSON))
                .keySet().stream().map(ModModelEvent::handleModelId).forEach(event::register);

        // 额外添加的模型注册
        // 蜂蜜瓶，用来替换锅内渲染
        event.register(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "item/honey"));
        // 鸡蛋，用来替换锅内渲染
        event.register(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "item/egg"));
        // 面团
        event.register(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "item/raw_dough_in_millstone"));
        // 油
        event.register(new ResourceLocation(KaleidoscopeCookery.MOD_ID, "item/oil_in_millstone"));

        // 重置缓存
        ItemRenderReplacer.resetCache();
    }

    private static ResourceLocation handleModelId(ResourceLocation input) {
        String namespace = input.getNamespace();
        String path = input.getPath();
        return new ResourceLocation(namespace, path.substring(MODELS.length(), path.length() - JSON.length()));
    }
}
