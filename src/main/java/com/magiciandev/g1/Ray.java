package com.magiciandev.g1;

import com.magiciandev.g1.entity.EntityData;

import java.awt.*;
import java.awt.geom.Line2D;

public class Ray {
    private Line2D.Double line;
    private double angle;
    private double distance;
    private EntityData data;

    public Ray() {
        this(new Line2D.Double(), null, 0, Double.POSITIVE_INFINITY);
    }

    public Ray(final Line2D.Double line, final EntityData image, final double angle) {
        this(line, image, angle, Double.POSITIVE_INFINITY);
    }

    public Ray(final Line2D.Double line, final EntityData entityData, final double angle, final double distance) {
        this.line = line;
        this.data = entityData;
        this.angle = angle;
        this.distance = distance;
    }

    public void draw(final Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.draw(this.line);
    }

    public void setRayCoordinates(double x1, double y1, double x2, double y2) {
        this.line.x1 = x1;
        this.line.y1 = y1;
        this.line.x2 = x2;
        this.line.y2 = y2;
    }

    public void setEndRayCoordinates(double x2, double y2) {
        this.line.x2 = x2;
        this.line.y2 = y2;
    }

    public Line2D.Double getLine() {
        return line;
    }

    public void setLine(Line2D.Double line) {
        this.line = line;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public EntityData getData() {
        return data;
    }

    public void setData(EntityData data) {
        this.data = data;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
