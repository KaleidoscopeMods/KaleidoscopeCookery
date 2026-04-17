package com.github.ysbbbbbb.kaleidoscopecookery.client.model;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.TeapotBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class TeapotModel extends HierarchicalModel<Entity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "teapot"), "main");
    private final ModelPart root;
    private final ModelPart top;
    private final ModelPart body;
    private final ModelPart stick;
    private final ModelPart chain;
    private final ModelPart base;

    public TeapotModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
        this.top = this.root.getChild("top");
        this.body = this.root.getChild("body");
        this.stick = this.body.getChild("stick");
        this.chain = root.getChild("chain");
        this.base = root.getChild("base");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(8.0F, 24.0F, -8.0F));

        PartDefinition top = root.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -1.25F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(10, 8).addBox(-3.0F, -0.25F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -7.75F, 8.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-5.8333F, -3.5833F, -5.0F, 10.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(23, 0).addBox(4.1667F, -1.5833F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 0).addBox(6.1667F, -1.5833F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(23, 0).addBox(4.1667F, 1.4167F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(7, 0).mirror().addBox(-7.8333F, -1.5833F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).addBox(-8.8333F, -1.5833F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.1667F, -2.4167F, 8.0F));

        PartDefinition stick = body.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(36, 10).addBox(-3.0F, -1.5625F, -1.25F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, -1.3125F, -1.0F, 8.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.8333F, -7.2708F, 0.0F));

        PartDefinition chain = partdefinition.addOrReplaceChild("chain", CubeListBuilder.create(), PartPose.offset(0.0F, 11.25F, 0.0F));

        PartDefinition cube_r1 = chain.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(50, -2).addBox(0.0F, -3.5F, -2.5F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r2 = chain.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(50, -2).addBox(0.0F, -3.5F, -2.5F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 38).addBox(-17.0F, -0.9F, 0.1F, 18.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 25.0F, -8.0F));

        PartDefinition cube_r3 = base.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(52, 21).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 21).addBox(-1.0F, -7.0F, -11.2F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.75F, 7.0F, 13.1F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r4 = base.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(52, 21).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 21).addBox(-1.0F, -7.0F, -11.2F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 7.0F, 13.1F, 0.0F, 0.0F, -0.3927F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        chain.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        base.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public void updateVariant(int variant) {
        if (variant == TeapotBlock.BASED) {
            this.chain.visible = false;
            this.base.visible = true;
        } else if (variant == TeapotBlock.CHAINED) {
            this.chain.visible = true;
            this.base.visible = false;
        } else {
            this.chain.visible = false;
            this.base.visible = false;
        }
    }

    @Override
    public void setupAnim(Entity entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    }
}
