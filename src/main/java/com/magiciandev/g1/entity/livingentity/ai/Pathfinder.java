package com.magiciandev.g1.entity.livingentity.ai;

import com.magiciandev.g1.TileMap;

import java.awt.Point;
import java.util.*;

public class Pathfinder {

    private final TileMap map;

    public Pathfinder(TileMap map) {
        this.map = map;
    }

    public List<Point> findPath(
            int startX,
            int startY,
            int targetX,
            int targetY) {

        if (!map.isWalkable(targetX, targetY)) {
            return Collections.emptyList();
        }

        Point start = new Point(startX, startY);
        Point target = new Point(targetX, targetY);

        PriorityQueue<Node> openSet = new PriorityQueue<>(
                Comparator.comparingDouble(Node::fCost)
        );

        Map<Point, Node> allNodes = new HashMap<>();
        Set<Point> closedSet = new HashSet<>();

        Node startNode = new Node(start, null);

        startNode.gCost = 0;
        startNode.hCost = heuristic(start, target);

        openSet.add(startNode);
        allNodes.put(start, startNode);

        while (!openSet.isEmpty()) {

            Node current = openSet.poll();

            if (current.position.equals(target)) {
                return reconstructPath(current);
            }

            if (!closedSet.add(current.position)) {
                continue;
            }

            for (Point neighbour : getNeighbours(current.position)) {

                if (!map.isWalkable(neighbour.x, neighbour.y)) {
                    continue;
                }

                if (closedSet.contains(neighbour)) {
                    continue;
                }

                double newGCost =
                        current.gCost + 1.0;

                Node neighbourNode =
                        allNodes.get(neighbour);

                if (neighbourNode == null) {

                    neighbourNode =
                            new Node(neighbour, current);

                    neighbourNode.gCost = newGCost;
                    neighbourNode.hCost =
                            heuristic(neighbour, target);

                    allNodes.put(neighbour, neighbourNode);
                    openSet.add(neighbourNode);

                } else if (newGCost < neighbourNode.gCost) {

                    neighbourNode.gCost = newGCost;
                    neighbourNode.parent = current;

                    openSet.add(neighbourNode);
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Point> getNeighbours(Point point) {

        List<Point> neighbours = new ArrayList<>();

        neighbours.add(
                new Point(point.x + 1, point.y)
        );

        neighbours.add(
                new Point(point.x - 1, point.y)
        );

        neighbours.add(
                new Point(point.x, point.y + 1)
        );

        neighbours.add(
                new Point(point.x, point.y - 1)
        );

        return neighbours;
    }

    private double heuristic(Point a, Point b) {

        return Math.abs(a.x - b.x)
                + Math.abs(a.y - b.y);
    }

    private List<Point> reconstructPath(Node node) {

        List<Point> path = new ArrayList<>();

        while (node != null) {
            path.add(node.position);
            node = node.parent;
        }

        Collections.reverse(path);

        return path;
    }

    private static class Node {

        Point position;
        Node parent;

        double gCost;
        double hCost;

        Node(Point position, Node parent) {
            this.position = position;
            this.parent = parent;
        }

        double fCost() {
            return gCost + hCost;
        }
    }
}

