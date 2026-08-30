package com.magiciandev.g1;

import com.magiciandev.g1.entity.CollidableEntity2D;
import com.magiciandev.g1.entity.livingentity.LivingEntity;
import com.magiciandev.g1.entity.livingentity.Monster;
import com.magiciandev.g1.texture.TextureCircleObject2D;
import com.magiciandev.g1.texture.TextureRectangleObject2D;
import com.magiciandev.g1.texture.TextureSprite;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class TileMap {

    /**
     * Each tile in the tile map is 64 x 64 pixels.
     */
    private static final int TILE_SIZE = 64;

    /**
     * List of collidable entities in our world.
     */
    private final ArrayList<CollidableEntity2D> ENTITIES;

    /**
     * List of sprites to project in the world. For now, these are non-collidable.
     */
    private final ArrayList<TextureSprite> SPRITES;

    /**
     * Array which stores all living entities
     */
    public final ArrayList<LivingEntity> LIVING_ENTITIES;


    public TileMap(final String mapFile) {
        this.ENTITIES = new ArrayList<>();
        this.SPRITES = new ArrayList<>();
        this.LIVING_ENTITIES = new ArrayList<>();
        this.parseFile(mapFile);
    }

    public void parseFile(final String mapFile) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(mapFile))));
        int x = 0;
        int y = 0;
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                for (char ch : line.toCharArray()) {
                    switch (ch) {
                        case '1': {
                            this.ENTITIES.add(new TextureRectangleObject2D(x, y, TILE_SIZE, TILE_SIZE, "bird.png"));
                            break;
                        }
                        case '2': {
                            this.ENTITIES.add(new TextureRectangleObject2D(x, y, TILE_SIZE, TILE_SIZE, "redbrick.png"));
                            break;
                        }
                        case '3': {
                            this.ENTITIES.add(new TextureRectangleObject2D(x, y, TILE_SIZE, TILE_SIZE, "purplestone.png"));
                            break;
                        }
                        case '4': {
                            this.ENTITIES.add(new TextureRectangleObject2D(x, y, TILE_SIZE, TILE_SIZE, "stonebrick.png"));
                            break;
                        }
                        case '5': {
                            this.ENTITIES.add(new TextureRectangleObject2D(x, y, TILE_SIZE, TILE_SIZE, "bluestone.png"));
                            break;
                        }
                        case '6': {
                            this.ENTITIES.add(new TextureRectangleObject2D(x, y, TILE_SIZE, TILE_SIZE, "mossystone.png"));
                            break;
                        }
                        case '7': {
                            this.ENTITIES.add(new TextureRectangleObject2D(x, y, TILE_SIZE, TILE_SIZE, "wood.png"));
                            break;
                        }
                        case '8': {
                            this.ENTITIES.add(new TextureRectangleObject2D(x, y, TILE_SIZE, TILE_SIZE, "colorstone.png"));
                            break;
                        }
                        case '9': {
                            this.ENTITIES.add(new TextureCircleObject2D(x, y, TILE_SIZE / 2.f, "redbrick.png"));
                            break;
                        }
                        case 'S': {
                            this.SPRITES.add(new TextureSprite(x, y, TILE_SIZE, TILE_SIZE, "tree_2_tall.png"));
                            break;
                        }
                        case 'M': {
                            this.SPRITES.add(new Monster(x, y, TILE_SIZE, TILE_SIZE, 1));
                            break;
                        }
                    }
                    x += TILE_SIZE;
                }
                x = 0;
                y += TILE_SIZE;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void draw(final Graphics2D g2) {
        this.ENTITIES.forEach(entity -> entity.draw(g2));
        this.SPRITES.forEach(sprite -> sprite.draw(g2));
    }

    public ArrayList<CollidableEntity2D> getEntities() {
        return this.ENTITIES;
    }

    public ArrayList<TextureSprite> getSprites() {
        return this.SPRITES;
    }

    public ArrayList<LivingEntity> getLivingEntities(){
        return this.LIVING_ENTITIES;
    }
}
