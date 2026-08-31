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
    public static String weapons[] = {
            "FIST", "KNIFE", "PISTOL9MM", "REVOLVER", "MACHINEGUN", "SNIPERRIFLE"
    };
    public static String currentWeapon = weapons[2];

    public static final int DEFAULT_MELEE_COOLDOWN = 60;
    public static final int DEFAULT_9MM_COOLDOWN = 20;

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
                break;
            }
            case "MACHINEGUN": {
                meleeDamage = 15;
                rangedDamage = 80;
                break;
            }
            case "SNIPERRIFLE": {
                rangedDamage = 1200;
                meleeDamage = 12;
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
