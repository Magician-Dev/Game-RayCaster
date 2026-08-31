package com.magiciandev.g1.entity.livingentity;

import com.magiciandev.g1.Game;
import com.magiciandev.g1.TileMap;
import com.magiciandev.g1.entity.Camera;
import com.magiciandev.g1.entity.livingentity.ai.Pathfinder;
import com.magiciandev.g1.texture.TextureCache;

import java.awt.Point;
import java.util.Collections;
import java.util.List;
import java.awt.image.BufferedImage;
import java.util.Collections;

public class Monster extends LivingEntity{

    private String fileName;
    private BufferedImage TEXTURE;

    private boolean shouldChase = true;

    private List<Point> currentPath = Collections.emptyList();

    private int pathIndex = 0;

    private int lastTargetX = -1;
    private int lastTargetY = -1;


    private Pathfinder pathfinder;
    private int pathRecalculationTimer = 0;

    public Monster(double x, double y, double w, double h, double speedFactor, TileMap tileMap) {
        super(x, y, w, h, "monster.png", speedFactor);
        this.fileName = "monster.png";
        this.TEXTURE = TextureCache.getImage(fileName);
        this.pathfinder = new Pathfinder(tileMap);
    }

    private void updatePathIfNeeded() {

        int playerTileX = (int) Math.floor(Game.cameraX / 64.0);
        int playerTileY = (int) Math.floor(Game.cameraY / 64.0);

        if (playerTileX == lastTargetX && playerTileY == lastTargetY) {
            return;
        }

        lastTargetX = playerTileX;
        lastTargetY = playerTileY;

        int monsterTileX = (int) Math.floor(getX() / 64.0);
        int monsterTileY = (int) Math.floor(getY() / 64.0);

        currentPath = pathfinder.findPath(monsterTileX, monsterTileY, playerTileX, playerTileY);
        pathIndex = 0;

        System.out.println("Monster path: " + currentPath);
    }

    @Override
    public void tick() {

        if (!shouldChase) {
            return;
        }

        pathRecalculationTimer++;

        if (pathRecalculationTimer >= 10) {

            pathRecalculationTimer = 0;

            calculatePath();
        }

        followPath();

        System.out.println("x: " + this.getX());
        System.out.println("y: " + this.getY());
    }

    private void calculatePath() {

        int monsterTileX = (int) Math.floor(getX() / 64.0);
        int monsterTileY = (int) Math.floor(getY() / 64.0);
        int playerTileX = (int) Math.floor(Game.cameraX / 64.0);
        int playerTileY = (int) Math.floor(Game.cameraY / 64.0);

        currentPath = pathfinder.findPath(monsterTileX, monsterTileY, playerTileX, playerTileY);
        pathIndex = 0;

        if (!currentPath.isEmpty() && currentPath.get(0).x == monsterTileX && currentPath.get(0).y == monsterTileY) {
            pathIndex = 1;
        }
    }

    private void followPath() {

        if (currentPath == null || currentPath.isEmpty()) {
            return;
        }

        if (pathIndex >= currentPath.size()) {
            return;
        }

        Point targetTile = currentPath.get(pathIndex);
        double targetX = targetTile.x * 64.0 + 32.0;
        double targetY = targetTile.y * 64.0 + 32.0;
        double dx = targetX - getX();
        double dy = targetY - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < 200.0) {
            pathIndex++;
            System.out.println("should despawn");
            despawn();
            return;
        }

        double directionX = dx / distance;
        double directionY = dy / distance;
        double speed = speedFactor;

        setX(getX() + directionX * speed);
        setY(getY() + directionY * speed);
    }



    @Override
    public BufferedImage getTexture(){
        return this.TEXTURE;
    }
}
