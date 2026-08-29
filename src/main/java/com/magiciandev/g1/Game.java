package com.magiciandev.g1;

import com.magiciandev.g1.gfx.RaycasterProjectionPanel;

import javax.swing.*;
import java.awt.*;

public class Game {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private final JFrame frame;

    private final RaycasterPanel RAYCASTER_PANEL;
    private final RaycasterProjectionPanel RAYCASTER_PROJ_PANEL;

    private Timer timer;

    public Game() {

        frame = new JFrame("Raycaster");

        RAYCASTER_PANEL = new RaycasterPanel(this);

        RAYCASTER_PROJ_PANEL =
                new RaycasterProjectionPanel(
                        this,
                        RAYCASTER_PANEL
                );

        // Keyboard input goes to the visible panel.
        RAYCASTER_PROJ_PANEL.addKeyListener(
                RAYCASTER_PANEL.getCamera().getKeyAdapter()
        );
        RAYCASTER_PROJ_PANEL.setFocusable(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ONLY show the 3D view.
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

            RAYCASTER_PANEL.update();
            RAYCASTER_PROJ_PANEL.update();

            RAYCASTER_PROJ_PANEL.repaint();
        });

        timer.start();
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
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
    }
}