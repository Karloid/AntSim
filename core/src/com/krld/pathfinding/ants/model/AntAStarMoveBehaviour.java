package com.krld.pathfinding.ants.model;

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
    private static final int MAX_LENGTH_PATH = 30;
    private Ant ant;
    private MyGame context;
    private PriorityQueue<Node> openNodes;
    private ArrayDeque<Node> closedNodes;
    private Point goalPosition;
    private Node startNode;
    private ArrayList<Point> path;

    @Override
    public void setAnt(Ant ant) {
        this.ant = ant;
    }

    @Override
    public void update() {
        //    System.out.println("AStar update");
        //    if (true) return;
        if (goalPosition == null || ant.getPosition().equals(goalPosition) || ant.getPosition().equals(path.get(path.size() - 1))) {
            if (ant.getPosition().equals(goalPosition)) {
                if (ant.getDestination() == AntDestination.TO_NEST) {
                    ant.setDestination(AntDestination.FROM_NEST);
                    ant.getNest().antArrive(ant);
                } else {
                    ant.setDestination(AntDestination.TO_NEST);
                    WayPoint wayPoint = context.getWayPointByPosition(ant.getPosition());
                    if (wayPoint != null)
                        wayPoint.antArrive(ant);
                }
            }
            goalPosition = pickGoalPosition();
            aStarCalc();
            runOnPath();
        } else {
            runOnPath();
        }
    }

    private void runOnPath() {
        int indexCurrentPos = 0;
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).equals(ant.getPosition())) {
                indexCurrentPos = i;
                break;
            }
        }
        if (indexCurrentPos + 1 >= path.size()) {
            return;
        }
        Point nextPoint = path.get(indexCurrentPos + 1);
        Direction direction = context.getDirectionByPoints(ant.getPosition(), nextPoint);
        ant.setDirection(direction);
        ant.setAction(Action.MOVE);
    }

    private void aStarCalc() {

        closedNodes = new ArrayDeque<Node>();
        openNodes = new PriorityQueue<Node>(300, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getF() < o2.getF()) {
                    return -1;
                } else if (o1.getF() == o2.getF()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        startNode = new Node(ant.getPosition(), START_NODE);
        calcF(startNode);
        openNodes.add(startNode);
        while (!openNodes.peek().getPosition().equals(goalPosition) && !(openNodes.peek().getParentsCount() > MAX_LENGTH_PATH)) {
            Node current = openNodes.peek();
            openNodes.remove(current);
            closedNodes.add(current);
            for (Node neighbor : getNeighbors(current)) {
                double costG = current.getG() + MOVE_COST;
                if (openNodes.contains(neighbor) && costG < neighbor.getG()) {
                    openNodes.remove(neighbor);
                }
                if (closedNodes.contains(neighbor) && costG < neighbor.getG()) {
                    closedNodes.remove(neighbor);
                }
                if (!openNodes.contains(neighbor) && !closedNodes.contains(neighbor)) {
                    neighbor.setG(costG);
                    neighbor.recalcF();
                    openNodes.add(neighbor);
            //        sortOpenNodes();
                    neighbor.setParent(current);
                }
            }
        }
        //    System.out.println("done AStar");
        //   System.out.println("" + openNodes.get(0).printParents());
        calcPath();
    }

    private void calcPath() {
        path = new ArrayList<Point>();
        Node node = openNodes.peek();
        while (true) {
            path.add(node.getPosition());
            node = node.getParent();
            if (node == null) {
                break;
            }
        }
        Collections.reverse(path);
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<Node>();
        Direction direction = Direction.NORTH;
        addPointIfCan(direction, node, neighbors);
        direction = Direction.WEST;
        addPointIfCan(direction, node, neighbors);
        direction = Direction.EAST;
        addPointIfCan(direction, node, neighbors);
        direction = Direction.SOUTH;
        addPointIfCan(direction, node, neighbors);
        return neighbors;
    }

    private void addPointIfCan(Direction direction, Node node, List<Node> neighbors) {
        Point point = node.getPosition().getCopy();
        context.movePointOnDirection(direction, point);
        if (context.canMoveToPoint(point)) {

            Node nodeToAdd = findNodeByPosition(point);
            if (nodeToAdd == null) {
                nodeToAdd = new Node(point, COMMON_NODE);
                nodeToAdd.setParent(node);
                calcF(nodeToAdd);
            }
            neighbors.add(nodeToAdd);

        }
    }

    private Node findNodeByPosition(Point point) {
        for (Node node : openNodes) {
            if (node.getPosition().equals(point)) {
                return node;
            }
        }
        for (Node node : closedNodes) {
            if (node.getPosition().equals(point)) {
                return node;
            }
        }
        return null;
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
      /*  Collections.sort(openNodes, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getF() < o2.getF()) {
                    return -1;
                } else if (o1.getF() == o2.getF()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });*/
    }

    private Point pickGoalPosition() {
        if (ant.getDestination() == AntDestination.FROM_NEST) {
            Double minDistantce = null;
            WayPoint pickedWayPoint = null;
            for (WayPoint wayPoint : context.getWayPoints()) {
                if ((minDistantce == null || getManhattanDistance(ant.getPosition(), wayPoint.getPosition()) < minDistantce) && Math.random() > RANDOM_WAY_RATIO) {
                    minDistantce = getManhattanDistance(ant.getPosition(), wayPoint.getPosition());
                    pickedWayPoint = wayPoint;
                }
            }
            return pickedWayPoint.getPosition().getCopy();
        } else if (ant.getDestination() == AntDestination.TO_NEST) {
            return ant.getNest().getPosition();
        }
        return new Point(10, 10);
    }

    private double getManhattanDistance(Point position, Point position1) {
        double dx = Math.abs(position.getX() - position1.getX());
        double dy = Math.abs(position.getY() - position1.getY());
        if (BREAK_TIES && startNode != null) {
            double dx1 = position.getX() - position1.getX();
            double dy1 = position.getY() - position1.getY();
            double dx2 = startNode.getPosition().getX() - position1.getX();
            double dy2 = startNode.getPosition().getY() - position1.getY();
            double cross = Math.abs(dx1 * dy2 - dx2 * dy1);
            return MOVE_COST * (dx + dy) + cross * 0.001d;
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

    public PriorityQueue<Node> getOpenNodes() {
        return openNodes;
    }

    public ArrayDeque<Node> getClosedNodes() {
        return closedNodes;
    }

    public ArrayList<Point> getPath() {
        return path;
    }

    public class Node {
        private final int nodeType;
        private double f;
        private double g;
        private double h;
        private Node parent;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            if (!position.equals(node.position)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return position.hashCode();
        }

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

        @Override
        public String toString() {
            return "f: " + getF() + "; g: " + getG() + "; h: " + getH() + "; pos: " + getPosition();
        }

        public void recalcF() {
            setF(getG() + getH());
        }

        public String printParents() {
            if (getParent() != null) {
                return this + " > " + getParent().printParents();
            } else {
                return this.toString();
            }
        }

        public int getParentsCount() {
            int count = 0;
            Node node = this;
            while (true) {
                node = node.getParent();
                if (node == null) {
                    return count;
                }
                count++;
            }
        }
    }

}
