package com.magiciandev.g1.data;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class SoundEngine {
    private final Clip[] clips = new Clip[4];
    private final URL[] soundURL = new URL[4];

    public SoundEngine() {
        soundURL[0] = getClass().getResource("/sfx/9mmgunshot.wav");
        soundURL[1] = getClass().getResource("/sfx/44magshot.wav");
        soundURL[2] = getClass().getResource("/sfx/rifleShoot.ogg");
        soundURL[3] = getClass().getResource("/sfx/sniperShot.wav");

        for (int i = 0; i < soundURL.length; i++) {
            load(i);
        }
    }

    private void load(int i) {
        try (AudioInputStream ais =
                     AudioSystem.getAudioInputStream(soundURL[i])) {

            clips[i] = AudioSystem.getClip();
            clips[i].open(ais);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(int i) {
        Clip clip = clips[i];

        if (clip == null) {
            return;
        }

        if (clip.isRunning()) {
            clip.stop();
        }

        clip.setFramePosition(0);
        clip.start();
    }

    public void loop(int i) {
        Clip clip = clips[i];

        if (clip == null) {
            return;
        }

        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(int i) {
        Clip clip = clips[i];

        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
        }
    }

    public void close() {
        for (Clip clip : clips) {
            if (clip != null) {
                clip.close();
            }
        }
    }
}
