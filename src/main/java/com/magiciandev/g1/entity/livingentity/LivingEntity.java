package com.magiciandev.g1.entity.livingentity;

import com.magiciandev.g1.texture.TextureSprite;

public abstract class LivingEntity extends TextureSprite {

    public boolean toRemove = false;
    public double speedFactor;
    public double health = 0; //subclasses should change this

    public LivingEntity(double x, double y, double w, double h, String fileName, double speedFactor) {
        super(x, y, w, h, fileName);
        this.speedFactor = speedFactor;
    }

    public abstract void tick();

    public void despawn(){
        if(this != null){
            this.toRemove = true; //tilemap handles the rest
        }else{
            this.toRemove = true;
        }
    }

    public boolean isToDespawn(){
        return this.toRemove;
    }

    public void setSpeedFactor(double speedFactor){
        this.speedFactor = speedFactor;
    }

    public double getSpeedFactor(){
        return this.speedFactor;
    }
}
