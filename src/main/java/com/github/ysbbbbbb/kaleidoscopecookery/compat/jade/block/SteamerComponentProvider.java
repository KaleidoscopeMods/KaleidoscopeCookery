package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.ModPlugin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.*;

import java.util.List;

public enum SteamerComponentProvider implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
    INSTANCE;

    @Override
    public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> list) {
        return ClientViewGroup.map(list, ItemView::new, null);
    }

    @Override
    @Nullable
    public List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
        Object target = accessor.getTarget();
        if (target instanceof SteamerBlockEntity steamer) {
            return List.of(new ViewGroup<>(steamer.getItems()));
        }
        return null;
    }

    @Override
    public ResourceLocation getUid() {
        return ModPlugin.STEAMER;
    }
}
