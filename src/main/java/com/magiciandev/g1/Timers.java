package com.magiciandev.g1;

import com.magiciandev.g1.data.RandomInteger;

import javax.swing.Timer;

public class Timers {
    public Timer precipitationTimer;
    public Timer lightningTimer;
    public Timer rngTimer;
    public Timer strongLightningTimer;

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

        if(Game.precipitationLevel == true){
            precipitationTimer.start();
            lightningTimer.start();
            strongLightningTimer.start();
        }
        rngTimer.start();
    }

    public void stop(){
        if(precipitationTimer != null){
            precipitationTimer.stop();
        }
    }
}
