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

    private boolean[][] walkable;
    private int mapWidth;
    private int mapHeight;
    private char[][] mapGrid;



    public TileMap(final String mapFile) {
        this.ENTITIES = new ArrayList<>();
        this.SPRITES = new ArrayList<>();
        this.LIVING_ENTITIES = new ArrayList<>();
        this.parseFile(mapFile);
    }

    public char getTile(int tileX, int tileY) {

        if (this.mapGrid == null) {
            return '#';
        }

        if (tileX < 0 ||
                tileY < 0 ||
                tileY >= this.mapHeight ||
                tileX >= this.mapWidth) {

            return '#';
        }

        return this.mapGrid[tileY][tileX];
    }


    public int worldToTileX(double x) {
        return (int) Math.floor(x / TILE_SIZE);
    }

    public int worldToTileY(double y) {
        return (int) Math.floor(y / TILE_SIZE);
    }

    public double tileToWorldX(int tileX) {
        return tileX * TILE_SIZE + TILE_SIZE / 2.0;
    }

    public double tileToWorldY(int tileY) {
        return tileY * TILE_SIZE + TILE_SIZE / 2.0;
    }


    public boolean isWalkable(int tileX, int tileY) {

        char tile = getTile(tileX, tileY);

        return tile == '0'
                || tile == 'M';
    }


    int playerTileX =
            (int) Math.floor(Game.cameraX / 64.0);

    int playerTileY =
            (int) Math.floor(Game.cameraY / 64.0);

    public void parseFile(final String mapFile) {

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    Objects.requireNonNull(
                                            this.getClass()
                                                    .getClassLoader()
                                                    .getResourceAsStream(mapFile)
                                    )
                            )
                    );

            ArrayList<String> lines = new ArrayList<>();

            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            reader.close();

            this.mapHeight = lines.size();

            this.mapWidth = 0;

            for (String currentLine : lines) {
                this.mapWidth =
                        Math.max(this.mapWidth, currentLine.length());
            }

            this.mapGrid =
                    new char[this.mapHeight][this.mapWidth];

            for (int row = 0; row < this.mapHeight; row++) {

                String currentLine = lines.get(row);

                for (int column = 0;
                     column < currentLine.length();
                     column++) {

                    this.mapGrid[row][column] =
                            currentLine.charAt(column);
                }
            }

            for (int row = 0; row < this.mapHeight; row++) {

                String currentLine = lines.get(row);

                for (int column = 0;
                     column < currentLine.length();
                     column++) {

                    char ch = currentLine.charAt(column);

                    int x = column * TILE_SIZE;
                    int y = row * TILE_SIZE;

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

                            Monster monster = new Monster(x, y, TILE_SIZE, TILE_SIZE, 1, this);
                            this.LIVING_ENTITIES.add(monster);
                            this.SPRITES.add(monster);
                            System.out.println("ADDED");
                            break;
                        }
                    }
                }
            }

        } catch (IOException e) {

            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }

    public void removeDespawnedEntities() {

        LIVING_ENTITIES.removeIf(LivingEntity::isToDespawn);

        SPRITES.removeIf(sprite ->
                sprite instanceof LivingEntity
                        && ((LivingEntity) sprite).isToDespawn()
        );
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
        System.out.println("entities: " + this.LIVING_ENTITIES);
        return this.LIVING_ENTITIES;
    }
}
