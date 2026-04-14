package com.github.ysbbbbbb.kaleidoscopecookery.client.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID, value = Dist.CLIENT)
public class CameraEvent {
    @SubscribeEvent
    public static void onCameraTick(ViewportEvent.ComputeCameraAngles event) {
        CameraType cameraType = Minecraft.getInstance().options.getCameraType();
        if (!cameraType.isFirstPerson()) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        // 如果骑乘垃圾桶实体，并且是第一人称，只允许左右移动，不允许上下
        if (player.getVehicle() instanceof SitEntity sitEntity && sitEntity.getSitType() == SitEntity.TRASH_CAN) {
            event.setPitch(0);
        }
    }
}
