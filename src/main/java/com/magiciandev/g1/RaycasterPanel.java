package com.magiciandev.g1;

import com.magiciandev.g1.entity.Camera;
import com.magiciandev.g1.entity.CollidableEntity2D;
import com.magiciandev.g1.entity.EntityData;
import com.magiciandev.g1.entity.IntersectionDataPair;
import com.magiciandev.g1.entity.livingentity.LivingEntity;
import com.magiciandev.g1.entity.livingentity.Monster;
import com.magiciandev.g1.texture.TextureSprite;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public final class RaycasterPanel extends JPanel {
    private static final int MAX_DIST = 2000;

    public static final int RENDER_WIDTH = 320;
    public static final int RENDER_HEIGHT = 240;

    private final Game RUNNER;
    private final TileMap MAP;
    private final Camera CAMERA;
    private final Ray[] RAY_LIST;
    private final int RESOLUTION;

    public RaycasterPanel(final Game game, final TileMap map) {
        this.RUNNER = game;

        // This panel is hidden, so its physical size doesn't matter.
        setPreferredSize(new Dimension(
                RUNNER.getWidth()/2,
                RUNNER.getHeight()
        ));

        RESOLUTION = getPreferredSize().width;

        this.RAY_LIST = new Ray[this.RESOLUTION];
        this.MAP = map;//new TileMap("map3.dat");

        this.CAMERA = new Camera(this, 400, 225);

        this.addKeyListener(this.CAMERA.getKeyAdapter());

        this.setFocusable(true);

        this.initializeRayList();
    }

    public void update() {
        double oldX = this.CAMERA.getX();
        double oldY = this.CAMERA.getY();

        this.CAMERA.update();
        this.updateCollisions(oldX, oldY);

        this.updateSpriteDistances();
        this.computeRays();
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.translate(-(this.CAMERA.getX() - this.getWidth() / 2.f), -(this.CAMERA.getY() - this.getHeight() / 2.f));
        this.drawRays(g2d);
        this.MAP.draw(g2d);
        this.CAMERA.draw(g2d);
    }

    /**
     * Computes the rays originating from the Camera's position. Each ray starts as an "infinite" line segment that
     * spans up to RESOLUTION pixels. Its endpoint is then truncated to the nearest object that it intersects.
     */
    private void computeRays() {
        for (int r = 0; r < this.RESOLUTION; r++) {
            double newMin = this.CAMERA.getCurrentAngle() - this.CAMERA.getFov() / 2;
            double newMax = this.CAMERA.getCurrentAngle() + this.CAMERA.getFov() / 2;

            // Compute the angle of this ray, normalized to our FOV.
            double rayAngle = RaycasterUtils.normalize(r, 0, this.RESOLUTION, newMin, newMax);

            // Compute the coordinates of the end of this ray.
            double ex = this.CAMERA.getX() + RaycasterPanel.MAX_DIST * RaycasterUtils.cos(Math.toRadians(rayAngle));
            double ey = this.CAMERA.getY() + RaycasterPanel.MAX_DIST * RaycasterUtils.sin(Math.toRadians(rayAngle));

            // Construct the current ray object for later.
            this.RAY_LIST[r].setRayCoordinates(this.CAMERA.getX(), this.CAMERA.getY(), ex, ey);

            // Iterate through all objects in the plane and find collisions.
            Point2D.Double minPt = null;
            EntityData minData = null;
            double minDist = Integer.MAX_VALUE;
            for (CollidableEntity2D entity : this.MAP.getEntities()) {
                IntersectionDataPair ip = entity.intersectionPt(this.RAY_LIST[r].getLine());
                if (ip.getPoint() != null) {
                    double currMinDist = ip.getPoint().distance(this.CAMERA.getX(), this.CAMERA.getY());
                    if (currMinDist <= minDist) {
                        minDist = currMinDist;
                        minPt = ip.getPoint();
                        minData = ip.getData();
                    }
                }
            }

            // If we found a closest minima, assign it as the ray's end coordinate.
            this.RAY_LIST[r].setData(minData);
            this.RAY_LIST[r].setAngle(rayAngle);
            if (minPt != null) {
                double ca = RaycasterUtils.normalize(rayAngle, newMin, newMax, -this.CAMERA.getFov() / 2, this.CAMERA.getFov() / 2);
                this.RAY_LIST[r].setEndRayCoordinates(minPt.x, minPt.y);
                this.RAY_LIST[r].setDistance(minDist * RaycasterUtils.cos(Math.toRadians(ca)));
            } else {
                this.RAY_LIST[r].setDistance(Double.POSITIVE_INFINITY);
            }
        }
    }

    private void updateSpriteDistances() {
        for (TextureSprite sp : this.MAP.getSprites()) {
            sp.setDistance(Point2D.distance(this.CAMERA.getX(), this.CAMERA.getY(), sp.getX(), sp.getY()));
        }

        this.MAP.getSprites().sort(new TextureSprite.TextureSpriteComparator());
    }

    private void updateCollisions(double oldX, double oldY) {
        Rectangle2D.Double cbb = this.CAMERA.getBoundingBox();

        for (CollidableEntity2D ce2d : this.MAP.getEntities()) {
            if (cbb.intersects(ce2d.getBoundingBox())) {

                this.CAMERA.setX(oldX);
                this.CAMERA.setY(oldY);

                this.CAMERA.stopMoving();

                return;
            }
        }
    }

    /**
     * @param g2
     */
    private void drawRays(final Graphics2D g2) {
        for (int i = 0; i < this.RAY_LIST.length; i++) {
            this.RAY_LIST[i].draw(g2);
        }
    }

    private void initializeRayList() {
        for (int i = 0; i < this.RAY_LIST.length; i++) {
            this.RAY_LIST[i] = new Ray();
        }
    }

    public TileMap getTileMap() {
        return this.MAP;
    }

    public Ray[] getRayList() {
        return this.RAY_LIST;
    }

    public Camera getCamera() {
        return this.CAMERA;
    }

    public int getResolution() {
        return this.RESOLUTION;
    }
}
