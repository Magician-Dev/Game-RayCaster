package com.magiciandev.g1.entity.livingentity;

import com.magiciandev.g1.Game;
import com.magiciandev.g1.TileMap;
import com.magiciandev.g1.data.RandomInteger;
import com.magiciandev.g1.entity.Camera;
import com.magiciandev.g1.entity.livingentity.ai.Pathfinder;
import com.magiciandev.g1.entity.player.PlayerAttributes;
import com.magiciandev.g1.texture.TextureCache;

import java.awt.Point;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.Collections;

public abstract class Monster extends LivingEntity{

    private String fileName;
    private BufferedImage TEXTURE;
    private boolean shouldChase = true;
    private List<Point> currentPath = Collections.emptyList();
    private int pathIndex = 0;
    private int lastTargetX = -1;
    private int lastTargetY = -1;
    private Pathfinder pathfinder;
    private int pathRecalculationTimer = 0;
    double dxp = Game.cameraX - getX();
    double dyp = Game.cameraY - getY();
    double distanceToPlayer = Math.sqrt(dxp * dxp + dyp * dyp);
    protected boolean hasMeleeAttack;
    protected boolean hasRangedAttack;
    protected int meleeDamage;
    protected int rangedDamage;
    protected int attackCooldown = 0;
    protected int meleeCooldown = 60;
    protected int rangedCooldown = 70;
    protected int missChance;
    protected int missIndex = 0;
    protected double meleeRange = 80.0;
    protected double rangedRange = 640.0;
    protected double missChanceDouble;
    protected double randomDouble;
    private final TileMap tileMap;

    public Monster(double x, double y, double w, double h, double speedFactor, String fileName, TileMap tileMap, boolean hasMeleeAttack, boolean hasRangedAttack, int missChance) {
        super(x, y, w, h, fileName, speedFactor);
        this.fileName = fileName;
        this.TEXTURE = TextureCache.getImage(fileName);
        this.pathfinder = new Pathfinder(tileMap);
        this.health = 20;
        this.hasMeleeAttack = hasMeleeAttack;
        this.hasRangedAttack = hasRangedAttack;
        this.tileMap = tileMap;
        this.meleeDamage = 8;
        this.rangedDamage = 15;
        this.missChance = missChance;
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

        //System.out.println("Monster path: " + currentPath);
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

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        attackPlayer();

        if(health <= 0){
            despawn();
        }

        Random random = new Random();
        missChanceDouble = (double)this.missChance;
        randomDouble = random.nextDouble();
    }

    private void performRangedAttack() {

        if(randomDouble < missChanceDouble/100){
            attackCooldown = rangedCooldown;
            return;
        }

        double attackerX = getX() + getWidth() / 2.0;
        double attackerY = getY() + getHeight() / 2.0;

        double targetX = Game.cameraX;
        double targetY = Game.cameraY;

        if (!tileMap.hasLineOfSight(
                attackerX,
                attackerY,
                targetX,
                targetY
        )) {
            return;
        }

        double dx = targetX - attackerX;
        double dy = targetY - attackerY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        double falloffSteps = Math.floor(distance / 64.0);

        double damageMultiplier =
                Math.max(0.1, 1.0 - (falloffSteps * 0.03));

        double finalDamage = rangedDamage * damageMultiplier;

        PlayerAttributes.health -= finalDamage;

        attackCooldown = rangedCooldown;

        System.out.println(
                "MONSTER RANGED: "
                        + finalDamage
                        + " distance: "
                        + distance
        );
    }

    private void performMeleeAttack() {
        if(randomDouble < (missChanceDouble/100)/2){
            attackCooldown = meleeCooldown;
            return;
        }
        PlayerAttributes.health -= meleeDamage;

        attackCooldown = meleeCooldown;

        System.out.println(
                "MONSTER MELEE: " + meleeDamage
        );
    }

    private void attackPlayer() {
        if (attackCooldown > 0) {
            return;
        }

        double dx = Game.cameraX - getX();
        double dy = Game.cameraY - getY();

        double distance = Math.sqrt(dx * dx + dy * dy);

        if (hasMeleeAttack && distance <= meleeRange) {
            performMeleeAttack();
            return;
        }

        if (hasRangedAttack && distance <= rangedRange) {
            performRangedAttack();
        }
    }

    public static boolean canAttack(
            LivingEntity target,
            int cooldown
    ) {
        return target != null
                && target.health > 0
                && cooldown <= 0;
    }

    public static void meleeAttack(
            LivingEntity target,
            double damage
    ) {
        if (target == null || target.health <= 0) {
            return;
        }

        target.health -= damage;

        System.out.println(
                "APPLIED MELEE DAMAGE: " + damage
        );
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

        if (distance <= speedFactor) {
            setX(targetX);
            setY(targetY);
            pathIndex++;
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
