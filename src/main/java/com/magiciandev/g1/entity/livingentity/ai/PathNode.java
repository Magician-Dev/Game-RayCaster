package com.magiciandev.g1.entity.livingentity.ai;

public class PathNode {

    public final int x;
    public final int y;

    public double gCost;
    public double hCost;

    public PathNode parent;

    public PathNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getFCost() {
        return gCost + hCost;
    }
}
