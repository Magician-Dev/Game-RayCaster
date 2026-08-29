package com.magiciandev.g1.entity;

import com.magiciandev.g1.gfx.Drawable2D;

import java.awt.*;

public abstract class Entity2D implements Drawable2D {
    private double x;
    private double y;
    private double w; //width of entity
    private double h; //height of entity

    public Entity2D(final double x, final double y, final double w, final double h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public abstract void draw(Graphics2D g2);

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

    public double getWidth() {
        return w;
    }

    public void setWidth(double w) {
        this.w = w;
    }

    public double getHeight() {
        return h;
    }

    public void setHeight(double h) {
        this.h = h;
    }
}
