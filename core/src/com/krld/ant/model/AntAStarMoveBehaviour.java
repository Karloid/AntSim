package com.krld.ant.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * Created by Andrey on 5/14/2014.
 */
public class AntAStarMoveBehaviour implements MoveBehaviour {
    private static final int START_NODE = 1;
    private static final int COMMON_NODE = 0;
    private static final double RANDOM_WAY_RATIO = 0f;
    private static final double MOVE_COST = 1;
    private static final boolean BREAK_TIES = false;
    private Ant ant;
    private MyGame context;
    private Deque<Node> openNodes;
    private ArrayDeque<Node> closedNodes;
    private Point goalPosition;

    @Override
    public void setAnt(Ant ant) {
        this.ant = ant;
    }

    @Override
    public void update() {
        //    System.out.println("AStar update");
        aStarCalc();
    }

    private void aStarCalc() {
        goalPosition = pickGoalPosition();
        closedNodes = new ArrayDeque<Node>();
        openNodes = new ArrayDeque<Node>();
        openNodes.add(new Node(ant.getPosition(), START_NODE));
        while (!openNodes.getFirst().getPosition().equals(goalPosition)) {

        }
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
        if (BREAK_TIES && ant.getDestination() != AntDestination.TO_NEST) {
            double dx1 = position.getX() - position1.getX();
            double dy1 = position.getY() - position1.getY();
            double dx2 = ant.getNest().getPosition().getX() - position1.getX();
            double dy2 = ant.getNest().getPosition().getY() - position1.getY();
            double cross = Math.abs(dx1 * dy2 - dx2 * dy1);
            return MOVE_COST * (dx + dy) + cross * 0.01d;
        } else if (BREAK_TIES && ant.getDestination() == AntDestination.TO_NEST) {
            double dx1 = position.getX() - position1.getX();
            double dy1 = position.getY() - position1.getY();
            double dx2 = ant.getPickedWayPoint().getPosition().getX() - position1.getX();
            double dy2 = ant.getPickedWayPoint().getPosition().getY() - position1.getY();
            double cross = Math.abs(dx1 * dy2 - dx2 * dy1);
            return MOVE_COST * (dx + dy) + cross * 0.01d;
        }
        {
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

        public Point getPosition() {
            return position;
        }

        private final Point position;

        public Node(Point position, int nodeType) {
            this.nodeType = nodeType;
            this.position = position;
        }
    }
}
