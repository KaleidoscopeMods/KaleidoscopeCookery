package com.github.ysbbbbbb.kaleidoscopecookery.client.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

/**
 * 自定义的第三人称姿势
 */
public class CustomArmPose {
    /**
     * 玩家手持大盘菜时，举起的动画
     */
    public static final HumanoidModel.ArmPose LIFT_POSE = HumanoidModel.ArmPose.create("KC_LIFT", false, (model, entity, arm) -> {
        if (arm == HumanoidArm.RIGHT) {
            model.rightArm.xRot = -Mth.PI;
            model.rightArm.zRot = -Mth.PI * 0.025f;
        } else {
            model.leftArm.xRot = -Mth.PI * 0.5f;
            model.leftArm.zRot = Mth.PI * 0.025f;
        }
    });
}
