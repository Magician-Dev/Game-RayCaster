package com.magiciandev.g1.entity.player;

import com.magiciandev.g1.Game;
import com.magiciandev.g1.entity.livingentity.LivingEntity;

public class PlayerMethods {

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
        return -69420.3313;
    }

    public static double playerAttackRanged(LivingEntity target) {
        boolean canAttack = playerCanAttack(target);

        if (canAttack && PlayerAttributes.rangedDamage > 0) {

            double dx = target.getX() - Game.cameraX;
            double dy = target.getY() - Game.cameraY;

            double distance = Math.sqrt(dx * dx + dy * dy);

            double falloffSteps = Math.floor(distance / 64.0);
            double damageMultiplier = 1.0 - (falloffSteps * 0.03);

            damageMultiplier = Math.max(0.1, damageMultiplier);

            double damage = PlayerAttributes.rangedDamage * damageMultiplier;

            target.health -= damage;

            System.out.println("apply rangedmage: " + damage + " distance: " + distance + " multiplier: " + damageMultiplier);
            PlayerAttributes.attackCooldown = PlayerAttributes.DEFAULT_MELEE_COOLDOWN;
        }
        return -69420.3313;
    }
}
