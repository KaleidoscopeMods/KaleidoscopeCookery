package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

// 兼容屠夫乐事（Butcher's Delight）：该模组将动物宰杀改为"尸体方块 + 菜刀肢解"流程，
// 绕过了原版死亡战利品表，导致森罗挂在原版表上的油脂掉落失效。
// 此监听器在肢解尸体方块掉肉时，同步补掉油脂。
@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class ButchersDelightCompatEvent {

    private static final TagKey<Item> KNIVES =
            ItemTags.create(new ResourceLocation("forge", "tools/knives"));

    // 用高优先级，确保在屠夫乐事修改计数之前读到旧值
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        //必须手持刀
        ItemStack heldItem = event.getItemStack();
        if (!heldItem.is(KNIVES)) {
            return;
        }

        //被右键的方块，必须是屠夫乐事的"猪尸体"方块
        // 通过方块注册 ID 判断，不直接依赖屠夫乐事的类，
        // 这样未安装屠夫乐事时本逻辑只会空跑、不会报错。
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
        if (blockId == null) {
            return;
        }
        boolean isButchersPigCarcass = blockId.getNamespace().equals("butchersdelight")
                && (blockId.getPath().contains("pig") || blockId.getPath().contains("hoglin"));
        if (!isButchersPigCarcass) {
            return;
        }

        //读取屠夫乐事的肢解进度计数
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) {
            return;
        }
        double process = blockEntity.getPersistentData().getDouble("ButcherProcess");

        // 屠夫乐事每次右键 ButcherProcess+1，计数 >3 时才掉肉并进入下一阶段。
        // 因此当前计数正好为 3 的这次右键即触发掉肉，此时同步掉一个油脂。
        if (process != 3.0) {
            return;
        }

        //掉一个油脂
        if (level instanceof ServerLevel serverLevel) {
            ItemEntity oilEntity = new ItemEntity(serverLevel,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    new ItemStack(ModItems.OIL.get()));
            oilEntity.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(oilEntity);
        }
    }
}