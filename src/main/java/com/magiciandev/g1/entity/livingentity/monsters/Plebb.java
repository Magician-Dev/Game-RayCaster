package com.magiciandev.g1.entity.livingentity.monsters;

import com.magiciandev.g1.TileMap;
import com.magiciandev.g1.entity.livingentity.Monster;
import com.magiciandev.g1.texture.TextureCache;

import java.awt.image.BufferedImage;

public class Plebb extends Monster {
    private String fileName;
    private BufferedImage TEXTURE;

    public Plebb(double x, double y, double w, double h, double speedFactor, TileMap tileMap) {
        super(x, y, w, h, speedFactor, "plebb0.png", tileMap);
        this.fileName = "plebb0.png";
        this.TEXTURE = TextureCache.getImage(fileName);
        this.health = 3000;
    }

    @Override
    public void tick() {
        super.tick();
    }

}
