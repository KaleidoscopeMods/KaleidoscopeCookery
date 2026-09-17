package com.github.ysbbbbbb.kaleidoscopecookery.client.model.banner;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class PatternModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "pattern"), "main");

    private final ModelPart bone;
    private final ModelPart back;
    private final ModelPart front;

    public PatternModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.bone = root.getChild("bone");
        this.back = this.bone.getChild("back");
        this.front = this.bone.getChild("front");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition back = bone.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, 0.0F, -16.0F, 0.0F, 48.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(2.25F, -65.0F, 0.0F));

        PartDefinition front = bone.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, -32).addBox(0.0F, 0.0F, -16.0F, 0.0F, 48.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.25F, -65.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 96);
    }

    public void setWaveAngle(float angle) {
        this.front.zRot = angle;
        this.back.zRot = angle;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
    }
}
