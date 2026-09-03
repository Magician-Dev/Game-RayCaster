package com.magiciandev.g1.entity.livingentity.monsters;

import com.magiciandev.g1.TileMap;
import com.magiciandev.g1.entity.livingentity.Monster;
import com.magiciandev.g1.texture.TextureCache;

import java.awt.image.BufferedImage;

public class Plebb extends Monster {
    private String fileName;
    private BufferedImage TEXTURE;

    public Plebb(double x, double y, double w, double h, double speedFactor, TileMap tileMap) {
        super(x, y, w, h, speedFactor, "monster.png", tileMap, true, true, 50);
        this.fileName = "monster.png";
        this.TEXTURE = TextureCache.getImage(fileName);
        this.health = 10000;
    }

    @Override
    public void tick() {
        super.tick();
    }

}
