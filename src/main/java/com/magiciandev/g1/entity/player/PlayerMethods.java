package com.magiciandev.g1.entity.player;

import com.magiciandev.g1.Game;
import com.magiciandev.g1.TileMap;
import com.magiciandev.g1.data.SoundEngine;
import com.magiciandev.g1.entity.livingentity.LivingEntity;

public class PlayerMethods {

    static SoundEngine sound = new SoundEngine();

    public static boolean playerCanAttack(LivingEntity target){
        if(target == null){
            return false;
        }
        if(target.health > 0){
            if(PlayerAttributes.attackCooldown <= 0){
                return true;
            }
        }
        return false;
    }

    public static double playerAttackMelee(LivingEntity target) {
        boolean canAttack = playerCanAttack(target);
        if(canAttack){
            target.health -= PlayerAttributes.meleeDamage;
            System.out.println("APPLIED DAMAGE: " + PlayerAttributes.meleeDamage);
            PlayerAttributes.attackCooldown = PlayerAttributes.DEFAULT_MELEE_COOLDOWN;
        }
        Game.animationCounter = 4;
        return -69420.3313;
    }

    public static double playerAttackRanged(LivingEntity target, TileMap tileMap) {
        boolean canAttack = playerCanAttack(target);

        if (!canAttack || PlayerAttributes.rangedDamage <= 0) {
            return -69420.3313;
        }

        if (tileMap == null) {
            return -69420.3313;
        }

        double targetX = target.getX() + target.getWidth() / 2.0;
        double targetY = target.getY() + target.getHeight() / 2.0;

        if (!tileMap.hasLineOfSight(
                Game.cameraX,
                Game.cameraY,
                targetX,
                targetY
        )) {
            return -69420.3313;
        }

        double dx = targetX - Game.cameraX;
        double dy = targetY - Game.cameraY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        double falloffSteps = Math.floor(distance / 64.0);
        double damageMultiplier = 1.0 - (falloffSteps * 0.03);
        damageMultiplier = Math.max(0.1, damageMultiplier);
        double damage = PlayerAttributes.rangedDamage * damageMultiplier;

        target.health -= damage;
        System.out.println("APPLIED RANGED DAMAGE: " + damage + " distance: " + distance + " multiplier: " + damageMultiplier);
        PlayerAttributes.attackCooldown = PlayerAttributes.DEFAULT_9MM_COOLDOWN;
        switch(PlayerAttributes.currentWeapon){
            case "PISTOL9MM": {
                PlayerAttributes.attackCooldown = PlayerAttributes.DEFAULT_9MM_COOLDOWN;
                sound.setFile(0);
                sound.play();
                break;
            }
            case "REVOLVER": {
                PlayerAttributes.attackCooldown = PlayerAttributes.DEFAULT_REVOLVER_COOLDOWN;
                sound.setFile(1);
                sound.play();
                break;
            }
            case "SNIPERRIFLE": {
                PlayerAttributes.attackCooldown = PlayerAttributes.DEFAULT_SNIPERRIFLE_COOLDOWN;
                sound.setFile(3);
                sound.play();
                break;
            }
            case "MACHINEGUN": {
                PlayerAttributes.attackCooldown = PlayerAttributes.DEFAULT_MACHINEGUN_COOLDOWN;
                sound.setFile(0);
                sound.play();
                break;
            }
            default: {
                break;
            }
        }
        Game.animationCounter = 4;
        return -69420.3313;
    }
}
