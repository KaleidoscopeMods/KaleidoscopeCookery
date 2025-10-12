package com.github.ysbbbbbb.kaleidoscopecookery.client.init;

import com.github.ysbbbbbb.kaleidoscopecookery.client.model.MillstoneModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.ScarecrowModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.StrawHatModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity.ScarecrowRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity.SitRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ScarecrowEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ThrowableBaoziEntity;
import net.fabricmc.api.EnvType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class ModEntitiesRender {
    public static void register() {
        // 注册实体渲染器
        EntityRendererRegistry.register(SitEntity.TYPE, SitRenderer::new);
        EntityRendererRegistry.register(ScarecrowEntity.TYPE, ScarecrowRender::new);
//        EntityRenderers.register(ThrowableBaoziEntity.TYPE, ThrownItemRenderer::new);

        // 注册模型层定义
        EntityModelLayerRegistry.registerModelLayer(ScarecrowModel.LAYER_LOCATION, ScarecrowModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(StrawHatModel.LAYER_LOCATION, StrawHatModel::createBodyLayer);
//        event.registerLayerDefinition(MillstoneModel.LAYER_LOCATION, MillstoneModel::createBodyLayer);
    }
}
