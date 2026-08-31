package com.magiciandev.g1.entity;

import com.magiciandev.g1.Game;
import com.magiciandev.g1.RaycasterPanel;
import com.magiciandev.g1.RaycasterUtils;
import com.magiciandev.g1.entity.livingentity.LivingEntity;
import com.magiciandev.g1.entity.player.PlayerAttributes;
import com.magiciandev.g1.entity.player.PlayerMethods;
//import com.sun.istack.internal.Nullable;

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
    public final double DEFAULT_WALK_SPEED = 2;
    public final double DEFAULT_RUN_SPEED = 4;
    public final double DEFAULT_TURN_SPEED = 1.5;
    public final double DEFAULT_TURN_RUN_SPEED = 5;
    private double x;
    private double y;
    private double fovDelta;
    public double speed;
    private double currentAngle;
    private int currentState;
    public boolean toAttack = false;
    public double distanceToMeleeTarget = 0.0;

    private static final double MELEE_RANGE = 100.0;
    private static final double MELEE_ANGLE = 30.0;
    private static final double RANGED_RANGE = 12300.0;
    private static final double RANGED_ANGLE = 15.0;

    public static boolean pressingW = false;
    public static boolean pressingS = false;
    public static boolean pressingA = false;
    public static boolean pressingD = false;
    public static boolean pressingShift = false;

    public LivingEntity meleeTarget;
    public LivingEntity rangedTarget;

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

        this.meleeTarget = this.findMeleeTarget();
        this.rangedTarget = this.findRangedTarget();

        if (meleeTarget != null) {
            double dxt = meleeTarget.getX() - getX();
            double dyt = meleeTarget.getY() - getY();

            distanceToMeleeTarget = Math.sqrt(dxt * dxt + dyt * dyt);
        } else {
            distanceToMeleeTarget = Double.MAX_VALUE;
        }

        //System.out.println("meleetarget: " + meleeTarget);
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

    private LivingEntity findMeleeTarget() {

        LivingEntity closest = null;
        double closestDistanceSquared = MELEE_RANGE * MELEE_RANGE;

        for (LivingEntity entity : this.RAYCASTER_PANEL.getTileMap().getLivingEntities()) {

            double dx = entity.getX() - this.x;
            double dy = entity.getY() - this.y;
            double distanceSquared = dx * dx + dy * dy;
            if (distanceSquared > closestDistanceSquared) {
                continue;
            }

            double angleToEntity = Math.toDegrees(Math.atan2(dy, dx));
            if (angleToEntity < 0) {
                angleToEntity += 360;
            }
            double angleDifference = Math.abs(angleToEntity - this.currentAngle);
            if (angleDifference > 180) {
                angleDifference = 360 - angleDifference;
            }
            if (angleDifference > MELEE_ANGLE) {
                continue;
            }
            closestDistanceSquared = distanceSquared;
            closest = entity;
        }
        return closest;
    }

    private LivingEntity findRangedTarget() {
        LivingEntity closest = null;

        double closestDistanceSquared = RANGED_RANGE * RANGED_RANGE;

        for (LivingEntity entity : this.RAYCASTER_PANEL.getTileMap().getLivingEntities()) {

            if (entity.health <= 0) {
                continue;
            }

            double dx = entity.getX() - this.x;
            double dy = entity.getY() - this.y;

            double distanceSquared = dx * dx + dy * dy;

            if (distanceSquared > closestDistanceSquared) {
                continue;
            }

            double angleToEntity =
                    Math.toDegrees(Math.atan2(dy, dx));

            if (angleToEntity < 0) {
                angleToEntity += 360;
            }

            double angleDifference =
                    Math.abs(angleToEntity - this.currentAngle);

            if (angleDifference > 180) {
                angleDifference = 360 - angleDifference;
            }

            if (angleDifference > RANGED_ANGLE) {
                continue;
            }

            closestDistanceSquared = distanceSquared;
            closest = entity;
        }

        return closest;
    }

    public LivingEntity getMeleeTarget() {
        return this.meleeTarget;
    }
    public LivingEntity getRangedTarget() {
        return this.rangedTarget;
    }


    public Rectangle2D.Double getBoundingBox() {
        return new Rectangle2D.Double(this.getX() - 10, this.getY() - 5, this.getWidth() - 10, this.getHeight() - 10);
    }

    public void attackMelee(LivingEntity meleeTarget){
        PlayerMethods.playerAttackMelee(meleeTarget);
    }

    public void attackRanged(LivingEntity rangedTarget){
        PlayerMethods.playerAttackRanged(rangedTarget);
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

    public void attemptAttack(LivingEntity meleeTarget, LivingEntity rangedTarget) {
        if (PlayerAttributes.rangedDamage > 0 && rangedTarget != null) {
            attackRanged(rangedTarget);
        } else if (meleeTarget != null) {
            attackMelee(meleeTarget);
        }
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

                this.CAMERA.currentState |= CameraState.WALK_BACKWARD;
                this.CAMERA.speed = -this.CAMERA.DEFAULT_WALK_SPEED;
                pressingS = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_A) {
                this.CAMERA.currentState |= CameraState.TURN_LEFT;
                if(CAMERA.isRunning()){
                    this.CAMERA.setFovDelta(-this.CAMERA.DEFAULT_TURN_RUN_SPEED);
                }else{
                    this.CAMERA.setFovDelta(-(this.CAMERA.DEFAULT_TURN_SPEED));
                }
                pressingA = true;
            } else if (e.getKeyCode() == KeyEvent.VK_D) {
                this.CAMERA.currentState |= CameraState.TURN_RIGHT;
                if(CAMERA.isRunning()){
                    this.CAMERA.setFovDelta(this.CAMERA.DEFAULT_TURN_RUN_SPEED);
                }else{
                    this.CAMERA.setFovDelta((this.CAMERA.DEFAULT_TURN_SPEED));
                }
                pressingD = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                CAMERA.attemptAttack(CAMERA.getMeleeTarget(), CAMERA.getRangedTarget());
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
