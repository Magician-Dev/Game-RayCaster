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
    public static String weapons[] = {
            "FIST", "KNIFE", "PISTOL9MM", "REVOLVER", "MACHINEGUN", "SNIPERRIFLE"
    };
    public static String currentWeapon = weapons[0];

    public static void refreshAttackDamage(){
        switch(currentWeapon){
            case "FIST": {
                meleeDamage = 4;
                rangedDamage = 0;
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
                break;
            }
            case "REVOLVER": {
                meleeDamage = 7;
                rangedDamage = 80;
                break;
            }
            case "MACHINEGUN": {
                meleeDamage = 80;
                break;
            }
            case "SNIPERRIFLE": {
                meleeDamage = 1200;
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
