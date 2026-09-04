package com.magiciandev.g1.data;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class SoundEngine {

    private static final int SOUND_COUNT = 4;
    private static final int CLIPS_PER_SOUND = 32;

    private final URL[] soundURL = new URL[SOUND_COUNT];

    // clips[sound][instance]
    private final Clip[][] clips =
            new Clip[SOUND_COUNT][CLIPS_PER_SOUND];

    // Keeps track of where to start searching next
    private final int[] poolIndex =
            new int[SOUND_COUNT];

    public SoundEngine() {

        soundURL[0] = getClass().getResource("/sfx/9mmgunshot.wav");
        soundURL[1] = getClass().getResource("/sfx/44magshot.wav");
        soundURL[2] = getClass().getResource("/sfx/44magshot.wav");
        soundURL[3] = getClass().getResource("/sfx/sniperShot.wav");

        for (int sound = 0; sound < SOUND_COUNT; sound++) {

            for (int instance = 0;
                 instance < CLIPS_PER_SOUND;
                 instance++) {

                clips[sound][instance] =
                        loadClip(soundURL[sound]);
            }

            if (clips[sound][0] != null) {

                System.out.println(
                        "Loaded sound " + sound +
                                ": " + soundURL[sound]
                );

                System.out.println(
                        "Duration: " +
                                clips[sound][0].getMicrosecondLength() /
                                        1000.0 +
                                " ms"
                );

                System.out.println(
                        "Format: " +
                                clips[sound][0].getFormat()
                );
            }
        }
    }

    private Clip loadClip(URL url) {

        if (url == null) {
            System.err.println("Sound URL is null");
            return null;
        }

        try (AudioInputStream ais =
                     AudioSystem.getAudioInputStream(url)) {

            Clip clip = AudioSystem.getClip();

            clip.open(ais);

            return clip;

        } catch (Exception e) {

            System.err.println(
                    "Could not load sound: " + url
            );

            e.printStackTrace();

            return null;
        }
    }

    public synchronized void play(int soundIndex) {

        if (soundIndex < 0 ||
                soundIndex >= SOUND_COUNT) {
            return;
        }

        Clip[] pool = clips[soundIndex];

        /*
         * Search for an available clip.
         */
        for (int i = 0; i < pool.length; i++) {

            int index =
                    (poolIndex[soundIndex] + i) %
                            pool.length;

            Clip clip = pool[index];

            if (clip != null && !clip.isRunning()) {

                clip.setFramePosition(0);
                clip.start();

                poolIndex[soundIndex] =
                        (index + 1) % pool.length;

                return;
            }
        }

        /*
         * Every clip is currently playing.
         *
         * We deliberately DON'T stop one here.
         * This prevents cutting off another sound.
         */
    }

    public synchronized void stop(int soundIndex) {

        if (soundIndex < 0 ||
                soundIndex >= SOUND_COUNT) {
            return;
        }

        for (Clip clip : clips[soundIndex]) {

            if (clip != null) {
                clip.stop();
                clip.setFramePosition(0);
            }
        }
    }

    public synchronized void close() {

        for (Clip[] pool : clips) {

            for (Clip clip : pool) {

                if (clip != null) {
                    clip.close();
                }
            }
        }
    }
}
