package com.magiciandev.g1;

import com.magiciandev.g1.data.RandomInteger;
import com.magiciandev.g1.entity.player.PlayerAttributes;

import javax.swing.Timer;

public class Timers {
    public Timer precipitationTimer;
    public Timer lightningTimer;
    public Timer rngTimer;
    public Timer strongLightningTimer;
    public Timer attackCooldownTimer;
    public Timer animationTimer;

    public void start(){
        precipitationTimer = new Timer(1000/4, e -> {
            if(Game.precipitationCount < 4){
                Game.precipitationCount += 1;
            }
            else{
                Game.precipitationCount = 1;
            }
        });

        rngTimer = new Timer(1000/60, e -> {
            if(RandomInteger.randomIndex < 255){
                RandomInteger.randomIndex++;
            }else{
                RandomInteger.randomIndex = 0;
            }
        });

        lightningTimer = new Timer(1000/2, e -> {
            Game.lightningRandom = RandomInteger.randomIndex;

            if(Game.lightningRandom == 3){
                Game.lightning = true;
            }else{
                Game.lightning = false;
            }
        });

        strongLightningTimer = new Timer(1000/35, e -> {
           if(Game.lightning){
               Game.lightningRandomCount = RandomInteger.randomIndex;
               if(Game.lightningRandomCount < 50){
                   Game.strongLightning = true;
               }else{
                   Game.strongLightning = false;
               }
           }else{
               Game.strongLightning = false;
           }
        });

        attackCooldownTimer = new Timer(1000/60, e -> {
            if(PlayerAttributes.attackCooldown > 0){
                PlayerAttributes.attackCooldown -= 1;
            }
            if(PlayerAttributes.currentWeapon.equals("MACHINEGUN") && PlayerAttributes.attackCooldown > PlayerAttributes.DEFAULT_MACHINEGUN_COOLDOWN){
                PlayerAttributes.machineGunOnCooldown = true;
            }else{
                PlayerAttributes.machineGunOnCooldown = false;
            }
        });

        animationTimer = new Timer(1000/8, e -> {
            if(Game.animationCounter > 0){
                Game.animationCounter -= 1;
            }

            if(Game.mgAttackCounter > 0){
                Game.mgAttackCounter -= 1;
            }else{
                Game.mgAttackCounter = 4;
            }

            if(!PlayerAttributes.machineGunOnCooldown) {
                if (Game.idleCounter > 0) {
                    Game.idleCounter -= 1;
                } else if (Game.idleCounter == 0) {
                    Game.idleCounter = 4;
                } else {
                    Game.idleCounter = 0;
                }
            }else
            if(PlayerAttributes.machineGunOnCooldown){
                Game.animationCounter = 0;
            }
            if(Game.idleCounter < 0 || Game.idleCounter != 0){
                Game.idleCounter = 1;
            }
        });

        if(Game.precipitationLevel == true){
            precipitationTimer.start();
            lightningTimer.start();
            strongLightningTimer.start();
        }
        rngTimer.start();
        attackCooldownTimer.start();
        animationTimer.start();
    }

    public void stop(){
        if(precipitationTimer != null){
            precipitationTimer.stop();
        }
    }
}
