package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ai.CatLieOnBlockGoal;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class EntityJoinWorldEvent {
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Cat cat) {
            cat.goalSelector.addGoal(5, new CatLieOnBlockGoal(cat, 1.1, 8));
        } else if (entity instanceof Creeper creeper) {
            creeper.goalSelector.addGoal(3, new AvoidEntityGoal<>(creeper, LivingEntity.class, 6,
                    1, 1.2, e -> e.hasEffect(ModEffects.MUSTARD.get())));
        } else if (entity instanceof Bee bee) {
            bee.goalSelector.addGoal(3, new TemptGoal(bee, 1.25,
                    Ingredient.of(ModItems.CANDIED_GLOW_BERRY_SKEWER.get(), ModItems.CANDIED_APPLE_SKEWER.get()), false));
        } else if (entity instanceof Fox fox) {
            fox.goalSelector.addGoal(3, new TemptGoal(fox, 1,
                    Ingredient.of(ModItems.CANDIED_SWEET_BERRY_SKEWER.get()), false));
        } else if (entity instanceof Chicken chicken) {
            chicken.goalSelector.addGoal(3, new TemptGoal(chicken, 1,
                    Ingredient.of(ModItems.CANDIED_CATERPILLAR_SKEWER.get()), false));
        } else if (entity instanceof EnderMan enderMan) {
            enderMan.goalSelector.addGoal(3, new TemptGoal(enderMan, 1,
                    Ingredient.of(ModItems.CANDIED_CHORUS_FRUIT_SKEWER.get()), false));
        }
    }
}
