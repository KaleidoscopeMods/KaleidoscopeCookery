package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTriggerType;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ScarecrowEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ScarecrowItem extends Item {
    public ScarecrowItem() {
        super(new Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction face = context.getClickedFace();
        if (face == Direction.DOWN) {
            return InteractionResult.FAIL;
        }
        Level level = context.getLevel();
        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        BlockPos clickedPos = placeContext.getClickedPos();
        ItemStack stack = context.getItemInHand();
        Vec3 center = Vec3.atBottomCenterOf(clickedPos);
        AABB aabb = ScarecrowEntity.TYPE.getDimensions().makeBoundingBox(center.x(), center.y(), center.z());
        if (level.noCollision(null, aabb) && level.getEntities(null, aabb).isEmpty()) {
            if (level instanceof ServerLevel serverLevel) {
                Consumer<ScarecrowEntity> consumer = EntityType.createDefaultStackConfig(serverLevel, stack, context.getPlayer());
                ScarecrowEntity scarecrow = ScarecrowEntity.TYPE.create(serverLevel, stack.getTag(), consumer, clickedPos, MobSpawnType.SPAWN_EGG, true, true);
                if (scarecrow == null) {
                    return InteractionResult.FAIL;
                }
                float rotation = Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                scarecrow.moveTo(scarecrow.getX(), scarecrow.getY(), scarecrow.getZ(), rotation, 0);
                serverLevel.addFreshEntityWithPassengers(scarecrow);
                level.playSound(null, scarecrow.getX(), scarecrow.getY(), scarecrow.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                scarecrow.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
                ModTrigger.EVENT.trigger(context.getPlayer(), ModEventTriggerType.PLACE_SCARECROW);
            }
            stack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.scarecrow").withStyle(ChatFormatting.GRAY));
    }
}
