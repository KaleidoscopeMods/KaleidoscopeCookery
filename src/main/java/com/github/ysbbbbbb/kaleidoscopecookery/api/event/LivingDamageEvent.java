package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class LivingDamageEvent extends ActionEvent implements IActionCancelable {
    private final LivingEntity entity;
    private final DamageSource source;
    private float amount;

    public LivingDamageEvent(LivingEntity entity, DamageSource source, float amount) {
        this.entity = entity;
        this.source = source;
        this.amount = amount;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
