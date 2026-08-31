package com.magiciandev.g1;

import com.magiciandev.g1.entity.livingentity.LivingEntity;
import com.magiciandev.g1.entity.livingentity.Monster;
import com.magiciandev.g1.gfx.RaycasterProjectionPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

    private final JFrame frame;

    private final RaycasterPanel RAYCASTER_PANEL;
    private final RaycasterProjectionPanel RAYCASTER_PROJ_PANEL;
    private final TileMap tileMap;
    private final ArrayList<LivingEntity> livingEntities;


    private static Timer timer;
    public static Timer renderTimer;

    public static double cameraX;
    public static double cameraY;

    public static int precipitationCount = 0;
    public static int lightningRandom = 0;
    public static int lightningRandomCount = 0;

    public static Logger gameLogger;

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
        });

        renderTimer = new Timer(1000 / 60, e -> {
            RAYCASTER_PROJ_PANEL.repaint();
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