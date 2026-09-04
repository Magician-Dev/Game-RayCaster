package com.magiciandev.g1;

import com.magiciandev.g1.data.SoundEngine;
import com.magiciandev.g1.entity.Camera;
import com.magiciandev.g1.entity.livingentity.LivingEntity;
import com.magiciandev.g1.entity.livingentity.Monster;
import com.magiciandev.g1.entity.player.PlayerAttributes;
import com.magiciandev.g1.gfx.RaycasterProjectionPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class Game {
    public static int level = 0;
    public static boolean precipitationLevel = false;
    public static boolean lightning = false;
    public static boolean strongLightning = false;
    public static double x_offset = 0;
    public static double y_offset = 0;
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static int ticksSinceHurt = 61;
    public static boolean strongAttack = false;
    public static boolean criticalAttack;
    private final JFrame frame;
    private final RaycasterPanel RAYCASTER_PANEL;
    private final RaycasterProjectionPanel RAYCASTER_PROJ_PANEL;
    private final TileMap tileMap;
    public  final ArrayList<LivingEntity> livingEntities;
    private static Timer timer;
    public static Timer renderTimer;
    public static double cameraX;
    public static double cameraY;
    public static int precipitationCount = 0;
    public static int lightningRandom = 0;
    public static int lightningRandomCount = 0;
    public static Logger gameLogger;
    public static String currentAnimation = "fist_1.png";
    public static int animationCounter;
    public static int idleCounter = 1;
    public static int mgAttackCounter;
    public static int healthLastTick = 100;
    public static boolean shouldDrawRecoil = false;
    private final ExecutorService renderExecutor = Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "Render-Thread");
                t.setDaemon(true);
                return t;
    });
    public static SoundEngine soundEngine = new SoundEngine();
    private final AtomicBoolean rendering = new AtomicBoolean(false);

    public Game() {

        frame = new JFrame("Raycaster");

        this.tileMap = new TileMap("map3.dat");
        this.livingEntities = tileMap.getLivingEntities();

        RAYCASTER_PANEL = new RaycasterPanel(this, tileMap);
        RAYCASTER_PROJ_PANEL = new RaycasterProjectionPanel(this, RAYCASTER_PANEL);
        RAYCASTER_PROJ_PANEL.addKeyListener(RAYCASTER_PANEL.getCamera().getKeyAdapter());
        RAYCASTER_PROJ_PANEL.setFocusable(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(RAYCASTER_PROJ_PANEL, BorderLayout.CENTER);

        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() ->
                RAYCASTER_PROJ_PANEL.requestFocusInWindow()
        );
    }

    public void start() {
        timer = new Timer(1000 / 60, e -> {
            for (LivingEntity entity : livingEntities) {
                entity.tick();
            }
            tileMap.removeDespawnedEntities();
            RAYCASTER_PANEL.update();
            RAYCASTER_PROJ_PANEL.update();
            switch (level) {
                case 0:
                    x_offset += 0.4;
                    break;
                default:
                    break;
            }
            if((PlayerAttributes.currentWeapon.equals("MACHINEGUN") && animationCounter > 0) ||
                    (PlayerAttributes.currentWeapon.equals("SNIPERRIFLE") && animationCounter > 0)){
                shouldDrawRecoil = true;
            }else{
                shouldDrawRecoil = false;
            }
            PlayerAttributes.refreshAttackDamage();
            if(PlayerAttributes.health <= 0){

            }
            if(healthLastTick != PlayerAttributes.health){
                ticksSinceHurt = 0;
                if(healthLastTick > PlayerAttributes.health+30){
                    strongAttack = true;
                }
            }else{
                ticksSinceHurt++;
            }
            if(ticksSinceHurt > 60){
                strongAttack = false;
            }
            healthLastTick = PlayerAttributes.health;
        });

        renderTimer = new Timer(1000 / 60, e -> {
            if (!rendering.compareAndSet(false, true)) {
                return;
            }

            renderExecutor.submit(() -> {
                try {
                    RAYCASTER_PANEL.updateRender();
                    RAYCASTER_PROJ_PANEL.repaint();
                    RAYCASTER_PROJ_PANEL.render();
                } finally {
                    rendering.set(false);
                }
            });
            //RAYCASTER_PROJ_PANEL.repaint();
        });

        timer.start();
        renderTimer.start();

        gameLogger = Logger.getLogger(Game.class.getName());;
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
        }
        if (renderTimer != null){
            renderTimer.stop();
        }
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.start();
        });

        if(level == 2){
            precipitationLevel = true;
        }

        Timers timers = new Timers();
        timers.start();
    }
}