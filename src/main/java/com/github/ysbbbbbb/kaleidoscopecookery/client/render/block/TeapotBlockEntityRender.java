package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.ITeapot;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.TeapotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.animation.TeapotAnimation;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.TeapotModel;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.function.Function;

public class TeapotBlockEntityRender implements BlockEntityRenderer<TeapotBlockEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(KaleidoscopeCookery.MOD_ID, "textures/block/teapot.png");
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    private final BlockEntityRendererProvider.Context context;
    private final TeapotModel model;

    private final Function<ResourceLocation, Component> fluidNameCache = Util.memoize(id -> {
        if (id.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID)) {
            return Component.translatable("mco.configure.world.slot.empty");
        }

        Fluid value = ForgeRegistries.FLUIDS.getValue(id);
        if (value == null) {
            return Component.literal(id.toString());
        }
        String descriptionId = value.getFluidType().getDescriptionId();
        return Component.translatable(descriptionId);
    });

    public TeapotBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.model = new TeapotModel(context.bakeLayer(TeapotModel.LAYER_LOCATION));
    }

    @Override
    public void render(TeapotBlockEntity teapot, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = teapot.getLevel();
        if (level == null) {
            return;
        }

        BlockEntityRenderDispatcher dispatcher = this.context.getBlockEntityRenderDispatcher();

        // 当玩家指向的方块就是这个时，显示文本
        if (dispatcher.cameraHitResult instanceof BlockHitResult hitResult
            && hitResult.getBlockPos().equals(teapot.getBlockPos())) {
            Camera camera = dispatcher.camera;
            Font font = this.context.getFont();

            poseStack.pushPose();
            poseStack.translate(0.5, 1, 0.5);
            poseStack.mulPose(Axis.YN.rotationDegrees(180 + camera.getYRot()));
            poseStack.scale(0.015625F, -0.015625F, 0.015625F);

            Component statusText = teapot.getStatusText();
            float width = (float) (-font.width(statusText) / 2) + 0.5f;
            font.drawInBatch(statusText, width, -5, 0xFFFFFF, false,
                    poseStack.last().pose(), buffer, Font.DisplayMode.POLYGON_OFFSET, 0, packedLight);

            int status = teapot.getStatus();

            // 如果是准备状态，提示加入的流体和物品
            if (status == ITeapot.PUT_INGREDIENT) {
                Component fluidText = this.fluidNameCache.apply(teapot.getTeaFluidId());
                ItemStack input = teapot.getInput();
                int count = input.getCount();
                Component itemText = input.isEmpty() ?
                        Component.translatable("mco.configure.world.slot.empty") :
                        ComponentUtils.formatList(Arrays.asList(
                                input.getHoverName(),
                                Component.literal("x%d".formatted(count))
                        ), CommonComponents.space());

                Component info = Component.translatable("tooltip.kaleidoscope_cookery.teapot.statue.fluid_ingredient", fluidText, itemText, count);
                float infoWidth = (float) (-font.width(info) / 2) + 0.5f;
                font.drawInBatch(info, infoWidth, 5, 0xFFFFFF, false,
                        poseStack.last().pose(), buffer, Font.DisplayMode.POLYGON_OFFSET, 0, packedLight);
            }

            // 如果是完成状态，提示产物
            if (status == ITeapot.FINISHED) {
                ItemStack result = teapot.getResult();
                int count = result.getCount();
                Component itemText = result.isEmpty() ?
                        Component.translatable("mco.configure.world.slot.empty") :
                        ComponentUtils.formatList(Arrays.asList(
                                result.getHoverName(),
                                Component.literal("x%d".formatted(count))
                        ), CommonComponents.space());
                Component info = Component.translatable("tooltip.kaleidoscope_cookery.teapot.statue.result", itemText, count);

                float infoWidth = (float) (-font.width(info) / 2) + 0.5f;
                font.drawInBatch(info, infoWidth, 5, 0xFFFFFF, false,
                        poseStack.last().pose(), buffer, Font.DisplayMode.POLYGON_OFFSET, 0, packedLight);
            }

            poseStack.popPose();
        }

        Direction facing = teapot.getBlockState().getValue(TeapotBlock.FACING);
        int facingDeg = facing.get2DDataValue() * 90;
        float ageInTicks = teapot.getLevel().getGameTime() + partialTick;
        int variant = teapot.getBlockState().getValue(TeapotBlock.VARIANT);

        this.model.root().getAllParts().forEach(ModelPart::resetPose);
        this.model.updateVariant(variant);
        teapot.boilingState.updateTime(ageInTicks, 1.0f);
        teapot.boilingState.ifStarted(state ->
                KeyframeAnimations.animate(this.model, TeapotAnimation.BOILING, state.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE)
        );

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(180 - facingDeg));
        VertexConsumer checkerBoardBuff = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.renderToBuffer(poseStack, checkerBoardBuff, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}
