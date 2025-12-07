package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.api.event.SickleHarvestEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 镰刀收割地狱疣事件特判
 */
@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class SickleHarvestNetherWartEvent {
    @SubscribeEvent
    public static void onSickleHarvestNetherWart(SickleHarvestEvent event) {
        BlockState harvestState = event.getHarvestState();
        if (harvestState.is(Blocks.NETHER_WART)) {
            boolean isMaxAge = harvestState.getValue(NetherWartBlock.AGE) >= 3;
            if (isMaxAge) {
                var player = event.getEntity();
                var pos = event.getHarvestPos();
                var level = player.level();
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.gameMode.destroyBlock(pos);
                    level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(harvestState));
                    level.setBlock(pos, Blocks.NETHER_WART.defaultBlockState(), Block.UPDATE_ALL);
                }
                event.setCostDurability(true);
            }
            event.setCanceled(true);
        }
    }
}
