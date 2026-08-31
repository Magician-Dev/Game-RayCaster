package com.magiciandev.g1.entity.player;

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
}
