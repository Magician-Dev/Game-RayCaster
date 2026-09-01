package com.magiciandev.g1.data;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class SoundEngine {
    Clip clip;
    URL soundURL[] = new URL[30];

    public SoundEngine(){
        soundURL[0] = getClass().getResource("/sfx/9mmgunshot.wav");
        soundURL[1] = getClass().getResource("/sfx/44magshot.wav");
        soundURL[2] = getClass().getResource("/sfx/rifleShot.ogg");
        soundURL[3] = getClass().getResource("/sfx/sniperShot.wav");
    }

    public void setFile(int i){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }catch (Exception e){
        }
    }

    public void play(){
        clip.start();
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.stop();
    }
}
