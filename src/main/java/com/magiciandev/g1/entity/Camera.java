package com.magiciandev.g1.entity;

import com.magiciandev.g1.Game;
import com.magiciandev.g1.RaycasterPanel;
import com.magiciandev.g1.RaycasterUtils;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

public final class Camera {
    private final KeyAdapter KEY_ADAPTER;
    private final double FOV = 70;
    private final RaycasterPanel RAYCASTER_PANEL;
    private final double DISTANCE_TO_PROJECTION_PLANE;
    private final int WIDTH = 20;
    private final int HEIGHT = 20;
    public final double DEFAULT_WALK_SPEED = 1.5;
    public final double DEFAULT_RUN_SPEED = 3;
    public final double DEFAULT_TURN_SPEED = 3;
    private double x;
    private double y;
    private double fovDelta;
    public double speed;
    private double currentAngle;
    private int currentState;

    public static boolean pressingW = false;
    public static boolean pressingS = false;
    public static boolean pressingA = false;
    public static boolean pressingD = false;
    public static boolean pressingShift = false;

    public Camera(final RaycasterPanel raycasterPanel, final double x, final double y){
        this.x = x;
        this.y = y;
        this.currentAngle = 0;
        this.RAYCASTER_PANEL = raycasterPanel;
        this.DISTANCE_TO_PROJECTION_PLANE = (this.RAYCASTER_PANEL.getPreferredSize().width / 2.f) / (Math.tan(Math.toRadians(this.FOV / 2.f)));
        this.KEY_ADAPTER = new CameraKeyAdapter(this);
    }

    public void update() {
        if (this.isMoving()) {
            this.x += this.speed * RaycasterUtils.cos(Math.toRadians(this.currentAngle));
            this.y += this.speed * RaycasterUtils.sin(Math.toRadians(this.currentAngle));
        }
        if (this.isTurning()) {
            this.currentAngle += this.fovDelta;
        }

        // Check to make sure angle is still in bounds
        if (this.currentAngle >= 360) this.currentAngle-=360;
        if (this.currentAngle < 0) this.currentAngle+=360;

        Game.cameraX = getX();
        Game.cameraY = getY();
    }

    public void draw(final Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.setColor(Color.RED);
        g2.rotate(Math.toRadians(this.currentAngle), this.getX(), this.getY());
        Path2D path = new Path2D.Double();
        path.moveTo(this.getX() - 20, this.getY() - 10);
        path.lineTo(this.getX() + 5, this.getY());
        path.lineTo(this.getX() - 20, this.getY() + 10);
        path.lineTo(this.getX() - 10, this.getY());
        path.closePath();
        g2.fill(path);
        g2.setTransform(old);
    }

    public Rectangle2D.Double getBoundingBox() {
        return new Rectangle2D.Double(this.getX() - 10, this.getY() - 5, this.getWidth() - 10, this.getHeight() - 10);
    }

    public int getWidth() {
        return this.WIDTH;
    }

    public int getHeight() {
        return this.HEIGHT;
    }

    public double getX() {
        return this.x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double getCurrentAngle() {
        return this.currentAngle;
    }

    public void setCurrentAngle(final double currentAngle) {
        this.currentAngle = currentAngle;
    }

    public double getFov() {
        return this.FOV;
    }

    public double getFovDelta() {
        return this.fovDelta;
    }

    public void setFovDelta(double fovDelta) {
        this.fovDelta = fovDelta;
    }

    public void setCurrentState(int stateFlags) {
        this.currentState = stateFlags;
    }

    public void stopMoving() {
        this.currentState &= ~CameraState.WALK_FORWARD | ~CameraState.WALK_BACKWARD | ~CameraState.RUN_FORWARD;
        this.speed = 0;
    }

    public boolean isMoving() {
        return CameraState.isFlagEnabled(this.currentState, CameraState.WALK_FORWARD)
                || CameraState.isFlagEnabled(this.currentState, CameraState.WALK_BACKWARD)
                || CameraState.isFlagEnabled(this.currentState, CameraState.RUN_FORWARD);

    }

    public boolean isWalking() {
        return CameraState.isFlagEnabled(this.currentState, CameraState.WALK_FORWARD)
                || CameraState.isFlagEnabled(this.currentState, CameraState.WALK_BACKWARD);
    }

    public boolean isRunning() {
        return CameraState.isFlagEnabled(this.currentState, CameraState.RUN_FORWARD);
    }

    public boolean isTurning() {
        return CameraState.isFlagEnabled(this.currentState, CameraState.TURN_LEFT)
                || CameraState.isFlagEnabled(this.currentState, CameraState.TURN_RIGHT);
    }

    public double getDistanceToProjectionPlane() {
        return this.DISTANCE_TO_PROJECTION_PLANE;
    }

    public KeyAdapter getKeyAdapter() {
        return this.KEY_ADAPTER;
    }

    /**
     * Static class to handle keyboard input for the Camera object.
     */
    private static class CameraKeyAdapter extends KeyAdapter {

        /**
         * Camera instance for variables and whatnot.
         */
        private final Camera CAMERA;

        /**
         * Set of keys currently toggled. Useful for determining multiple states.
         */
        private final Set<Integer> PRESSED_KEYS;

        public CameraKeyAdapter(final Camera camera) {
            this.CAMERA = camera;
            this.PRESSED_KEYS = new HashSet<>();
        }

        @Override
        public void keyPressed(final KeyEvent e) {
            this.PRESSED_KEYS.add(e.getKeyCode());
            // If they press W and hold the shift key, they sprint. Otherwise, they walk.
            if (this.PRESSED_KEYS.contains(KeyEvent.VK_W)) {
                this.CAMERA.currentState |= CameraState.WALK_FORWARD;
                if (this.PRESSED_KEYS.contains(KeyEvent.VK_SHIFT)) {
                    this.CAMERA.currentState |= CameraState.RUN_FORWARD;
                    this.CAMERA.speed = this.CAMERA.DEFAULT_RUN_SPEED;
                    pressingShift = true;
                } else {
                    this.CAMERA.speed = this.CAMERA.DEFAULT_WALK_SPEED;
                }
                pressingW = true;
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                // Walking backwards...
                this.CAMERA.currentState |= CameraState.WALK_BACKWARD;
                this.CAMERA.speed = -this.CAMERA.DEFAULT_WALK_SPEED;
                pressingS = true;
            }

            // Turning.
            if (e.getKeyCode() == KeyEvent.VK_A) {
                this.CAMERA.currentState |= CameraState.TURN_LEFT;
                this.CAMERA.setFovDelta(-this.CAMERA.DEFAULT_TURN_SPEED);
                pressingA = true;
            } else if (e.getKeyCode() == KeyEvent.VK_D) {
                this.CAMERA.currentState |= CameraState.TURN_RIGHT;
                this.CAMERA.setFovDelta(this.CAMERA.DEFAULT_TURN_SPEED);
                pressingD = true;
            }
        }

        @Override
        public void keyReleased(final KeyEvent e) {
            this.PRESSED_KEYS.remove(e.getKeyCode());
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                this.CAMERA.currentState &= ~CameraState.RUN_FORWARD;
                this.CAMERA.speed = this.CAMERA.DEFAULT_WALK_SPEED;
                pressingShift = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) {
                this.CAMERA.currentState &= ~(CameraState.WALK_FORWARD | CameraState.WALK_BACKWARD);
                this.CAMERA.speed = 0;
            }

            if(e.getKeyCode() == KeyEvent.VK_W){
                pressingW = false;
            }

            if(e.getKeyCode() == KeyEvent.VK_S){
                pressingS = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
                this.CAMERA.currentState &= ~(CameraState.TURN_LEFT | CameraState.TURN_RIGHT);
                pressingA = false;
                pressingD = false;
            }
        }
    }
}
