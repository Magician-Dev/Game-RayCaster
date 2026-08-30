package com.magiciandev.g1.entity.livingentity;

import com.magiciandev.g1.texture.TextureSprite;

public abstract class LivingEntity extends TextureSprite {

    public double speedFactor;

    public LivingEntity(double x, double y, double w, double h, String fileName, double speedFactor) {
        super(x, y, w, h, fileName);
        speedFactor = this.speedFactor;
    }

    public abstract void tick();

    public void setSpeedFactor(double speedFactor){
        this.speedFactor = speedFactor;
    }

    public double getSpeedFactor(){
        return this.speedFactor;
    }
}
