package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenwareRacksBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.ModPlugin;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.*;

import java.util.List;

public enum KitchenwareRackComponentProvider implements IServerExtensionProvider<Object, ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
    INSTANCE;

    @Override
    public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> list) {
        return ClientViewGroup.map(list, ItemView::new, null);
    }

    @Override
    @Nullable
    public List<ViewGroup<ItemStack>> getGroups(ServerPlayer serverPlayer, ServerLevel serverLevel, Object target, boolean showDetails) {
        if (target instanceof KitchenwareRacksBlockEntity kitchenwareRacks) {
            List<ItemStack> list = Lists.newArrayList();
            list.add(kitchenwareRacks.getItemLeft());
            list.add(kitchenwareRacks.getItemRight());
            return List.of(new ViewGroup<>(list));
        }
        return null;
    }

    @Override
    public ResourceLocation getUid() {
        return ModPlugin.KITCHENWARE_RACK;
    }
}
