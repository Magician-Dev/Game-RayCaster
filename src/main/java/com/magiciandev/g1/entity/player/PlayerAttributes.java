package com.magiciandev.g1.entity.player;


import com.magiciandev.g1.Game;
import sun.rmi.runtime.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerAttributes {
    public static int health;
    public static int armour;
    public static int meleeDamage;
    public static int rangedDamage;
    public static int attackCooldown;
    public static int rangedCooldown;
    public static String weapons[] = {
            "FIST", "KNIFE", "PISTOL9MM", "REVOLVER", "MACHINEGUN", "SNIPERRIFLE"
    };
    public static String currentWeapon = weapons[4];

    public static final int DEFAULT_MELEE_COOLDOWN = 60;
    public static final int DEFAULT_9MM_COOLDOWN = 20;
    public static final int DEFAULT_REVOLVER_COOLDOWN = 40;
    public static final int DEFAULT_RIFLE_COOLDOWN = 10;
    public static final int DEFAULT_SNIPERRIFLE_COOLDOWN = 300;
    public static final int DEFAULT_MACHINEGUN_COOLDOWN = 4;

    public static void refreshAttackDamage(){
        switch(currentWeapon){
            case "FIST": {
                meleeDamage = 4;
                rangedDamage = 0;
                Game.currentAnimation = "fist_1.png";
                break;
            }
            case "KNIFE": {
                meleeDamage = 12;
                rangedDamage = 0;
                if(Game.animationCounter == 4){
                    Game.currentAnimation = "knifeattack0.png";
                }else
                if(Game.animationCounter == 3){
                    Game.currentAnimation = "knifeattack2.png";
                }else
                if(Game.animationCounter == 2){
                    Game.currentAnimation = "knifeattack1.png";
                }else
                if(Game.animationCounter == 1){
                    Game.currentAnimation = "knifeattack0.png";
                }else{
                    Game.currentAnimation = "knifeattack0.png";
                }
                break;
            }
            case "PISTOL9MM": {
                meleeDamage = 5;
                rangedDamage = 40;
                if(Game.animationCounter == 4){
                    Game.currentAnimation = "pistolshoot0.png";
                }else
                if(Game.animationCounter == 3){
                    Game.currentAnimation = "pistolshoot1.png";
                }else
                if(Game.animationCounter == 2){
                    Game.currentAnimation = "pistolshoot2.png";
                }else
                if(Game.animationCounter == 1){
                    Game.currentAnimation = "pistolidle.png";
                }else{
                    Game.currentAnimation = "pistolidle.png";
                }
                break;
            }
            case "REVOLVER": {
                meleeDamage = 7;
                rangedDamage = 80;
                if(Game.animationCounter == 4){
                    Game.currentAnimation = "revolvershoot2.png";
                }else
                if(Game.animationCounter == 3){
                    Game.currentAnimation = "revolvershoot0.png";
                }else
                if(Game.animationCounter == 2){
                    Game.currentAnimation = "revolvershoot1.png";
                }else
                if(Game.animationCounter == 1){
                    Game.currentAnimation = "revolvershoot0.png";
                }else{
                    Game.currentAnimation = "revolveridle.png";
                }
                break;
            }
            case "MACHINEGUN": {
                meleeDamage = 15;
                rangedDamage = 80;
                if(Game.animationCounter == 4){
                    Game.currentAnimation = "chaingunshoot0.png";
                }else
                if(Game.animationCounter == 3){
                    Game.currentAnimation = "chaingunshoot1.png";
                }else
                if(Game.animationCounter == 2){
                    Game.currentAnimation = "chaingunshoot3.png";
                }else
                if(Game.animationCounter == 1){
                    Game.currentAnimation = "chaingunshoot4.png";
                }else{
                    if(Game.idleCounter == 4){
                        Game.currentAnimation = "chaingunidle0.png";
                    }else
                    if(Game.idleCounter == 3){
                        Game.currentAnimation = "chaingunidle1.png";
                    }else
                    if(Game.idleCounter == 2){
                        Game.currentAnimation = "chaingunidle2.png";
                    }else
                    if(Game.idleCounter == 1){
                        Game.currentAnimation = "chaingunidle3.png";
                    }else{
                        Game.currentAnimation = "chaingunidle0.png";
                    }
                }
                break;
            }
            case "SNIPERRIFLE": {
                rangedDamage = 1200;
                meleeDamage = 12;
                if(Game.animationCounter == 4){
                    Game.currentAnimation = "rifleshoot.png";
                }else
                if(Game.animationCounter == 3){
                    Game.currentAnimation = "rifleidle.png";
                }else
                if(Game.animationCounter == 2){
                    Game.currentAnimation = "rifleidle.png";
                }else
                if(Game.animationCounter == 1){
                    Game.currentAnimation = "rifleidle.png";
                }else{
                    Game.currentAnimation = "rifleidle.png";
                }
                break;
            }
            default: {
                meleeDamage = 4;
                rangedDamage = 0;
                Game.gameLogger.log(Level.WARNING, "WARNING: No weapon detected!");
                break;
            }
        }
    }
}
