package com.github.ysbbbbbb.kaleidoscopecookery.client.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class ModModelEvent {
    private static final String MODELS = "models/";
    private static final String MODELS_CHOPPING_BOARD = MODELS + "chopping_board";
    private static final String MODELS_CARPET = MODELS + "block/carpet";
    private static final String JSON = ".json";

//
//    public static void registerModels() {
//        ClientTickEvents.START_CLIENT_TICK.register(client -> {
//            var resourceManager = client.getResourceManager();
//            resourceManager.listResources(MODELS_CHOPPING_BOARD, id -> id.getPath().endsWith(JSON))
//                    .keySet().stream().map(ModModelEvent::handleModelId).forEach(modelResourceLocation -> );
//
//            resourceManager.listResources(MODELS_CARPET, id -> id.getPath().endsWith(JSON))
//                    .keySet().stream().map(ModModelEvent::handleModelId).forEach(event::register);
//
//            // 额外添加的模型注册
//            // 蜂蜜瓶，用来替换锅内渲染
//            event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/honey")));
//            // 鸡蛋，用来替换锅内渲染
//            event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/egg")));
//            // 面团
//            event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/raw_dough_in_millstone")));
//            // 油
//            event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/oil_in_millstone")));
//
//            // 重置缓存
//            ItemRenderReplacer.resetCache();
//        });
//
//    }
//
//    private static ModelResourceLocation handleModelId(ResourceLocation input) {
//        String namespace = input.getNamespace();
//        String path = input.getPath();
//        String substring = path.substring(MODELS.length(), path.length() - JSON.length());
//        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, substring);
//        return ModelResourceLocation.standalone(id);
//    }
}
