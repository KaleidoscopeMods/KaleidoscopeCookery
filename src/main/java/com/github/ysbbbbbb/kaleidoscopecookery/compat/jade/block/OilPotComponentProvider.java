package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.OilPotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.ModPlugin;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.*;

import java.util.Collections;
import java.util.List;

public enum OilPotComponentProvider implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
    INSTANCE;

    @Override
    public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> list) {
        return ClientViewGroup.map(list, ItemView::new, null);
    }

    @Override
    @Nullable
    public List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
        Object target = accessor.getTarget();
        if (target instanceof OilPotBlockEntity oilPot) {
            int oilCount = oilPot.getOilCount();
            if (oilCount > 0) {
                ItemStack stack = new ItemStack(ModItems.OIL.get(), oilCount);
                return List.of(new ViewGroup<>(Collections.singletonList(stack)));
            }
        }
        return null;
    }

    @Override
    public ResourceLocation getUid() {
        return ModPlugin.OIL_POT;
    }
}
