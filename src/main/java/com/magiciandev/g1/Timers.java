package com.magiciandev.g1;

import javax.swing.Timer;

public class Timers {
    public Timer precipitationTimer;

    public void start(){
        precipitationTimer = new Timer(1000/4, e -> {
            if(Game.precipitationCount < 4){
                Game.precipitationCount += 1;
            }
            else{
                Game.precipitationCount = 1;
            }
        });

        if(Game.precipitationLevel == true){
            precipitationTimer.start();
        }
    }

    public void stop(){
        if(precipitationTimer != null){
            precipitationTimer.stop();
        }
    }
}
