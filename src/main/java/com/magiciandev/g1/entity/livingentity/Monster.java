package com.magiciandev.g1.entity.livingentity;

import com.magiciandev.g1.Game;
import com.magiciandev.g1.entity.Camera;
import com.magiciandev.g1.texture.TextureCache;

import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Monster extends LivingEntity{

    private String fileName;
    private BufferedImage TEXTURE;

    public Monster(double x, double y, double w, double h, double speedFactor) {
        super(x, y, w, h, "monster.png", speedFactor);
        this.fileName = "monster.png";
        this.TEXTURE = TextureCache.getImage(fileName);
    }

    @Override
    public void tick() {
        //wip
        this.setX(Game.cameraX-10);
        this.setX(Game.cameraY-10);
    }
}
