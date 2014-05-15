package com.krld.ant.model;

import java.util.*;

/**
 * Created by Andrey on 5/14/2014.
 */
public class AntAStarMoveBehaviour implements MoveBehaviour {
    private static final int START_NODE = 1;
    private static final int COMMON_NODE = 0;
    private static final double RANDOM_WAY_RATIO = 0f;
    private static final double MOVE_COST = 1;
    private static final boolean BREAK_TIES = true;
    private Ant ant;
    private MyGame context;
    private List<Node> openNodes;
    private ArrayDeque<Node> closedNodes;
    private Point goalPosition;
    private Node startNode;

    @Override
    public void setAnt(Ant ant) {
        this.ant = ant;
    }

    @Override
    public void update() {
        //    System.out.println("AStar update");
        //    if (true) return;
        aStarCalc();
    }

    private void aStarCalc() {
        goalPosition = pickGoalPosition();
        closedNodes = new ArrayDeque<Node>();
        openNodes = new ArrayList<Node>();
        startNode = new Node(ant.getPosition(), START_NODE);
        calcF(startNode);
        openNodes.add(startNode);
        while (!openNodes.get(0).getPosition().equals(goalPosition)) {
            Node current = openNodes.get(0);
            openNodes.remove(current);
            closedNodes.add(current);
            for (Node neighbor : getNeighbors(current)) {

            }
            sortOpenNodes();
            break;
        }
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<Node>();
        return neighbors;
    }

    private void calcF(Node node) {
        double heuristik = getManhattanDistance(node.getPosition(), goalPosition);
        double pathCost = (node.getParent() == null ? 0 : node.getParent().getG() + MOVE_COST);
        double f = heuristik + pathCost;
        node.setF(f);
        node.setG(pathCost);
        node.setH(heuristik);
    }

    private void sortOpenNodes() {
        Collections.sort(openNodes, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getF() > o2.getF()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    private Point pickGoalPosition() {
        Double minDistantce = null;
        WayPoint pickedWayPoint = null;
        for (WayPoint wayPoint : context.getWayPoints()) {
            if ((minDistantce == null || getManhattanDistance(ant.getPosition(), wayPoint.getPosition()) < minDistantce) && Math.random() > RANDOM_WAY_RATIO) {
                minDistantce = getManhattanDistance(ant.getPosition(), wayPoint.getPosition());
                pickedWayPoint = wayPoint;
            }
        }
        return pickedWayPoint.getPosition().getCopy();
    }

    private double getManhattanDistance(Point position, Point position1) {
        double dx = Math.abs(position.getX() - position1.getX());
        double dy = Math.abs(position.getY() - position1.getY());
        if (BREAK_TIES && startNode != null) {
            double dx1 = position.getX() - position1.getX();
            double dy1 = position.getY() - position1.getY();
            double dx2 = startNode.getPosition().getX() - position1.getX();
            double dy2 = startNode.getPosition().getX() - position1.getY();
            double cross = Math.abs(dx1 * dy2 - dx2 * dy1);
            return MOVE_COST * (dx + dy) + cross * 0.01d;
        } else {
            return MOVE_COST * (dx + dy);
        }
    }

    @Override
    public void setContext(MyGame context) {
        this.context = context;
    }

    public Point getGoalPosition() {
        return goalPosition;
    }

    private class Node {
        private final int nodeType;
        private double f;
        private double g;
        private double h;
        private Node parent;

        public Point getPosition() {
            return position;
        }

        private final Point position;

        public Node(Point position, int nodeType) {
            this.nodeType = nodeType;
            this.position = position;
        }

        public double getF() {
            return f;
        }

        public void setF(double f) {
            this.f = f;
        }

        public void setG(double g) {
            this.g = g;
        }

        public double getG() {
            return g;
        }

        public void setH(double h) {
            this.h = h;
        }

        public double getH() {
            return h;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }
    }
}
