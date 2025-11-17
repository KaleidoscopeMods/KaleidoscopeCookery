package com.github.ysbbbbbb.kaleidoscopecookery.client.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/honey")));
        // 鸡蛋，用来替换锅内渲染
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/egg")));
        // 油
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/oil_in_millstone")));

        // 重置缓存
        ItemRenderReplacer.resetCache();
    }

    private static ModelResourceLocation handleModelId(ResourceLocation input) {
        String namespace = input.getNamespace();
        String path = input.getPath();
        String substring = path.substring(MODELS.length(), path.length() - JSON.length());
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, substring);
        return ModelResourceLocation.standalone(id);
    }
}
