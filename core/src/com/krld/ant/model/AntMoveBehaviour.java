package com.krld.ant.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrey on 5/8/2014.
 */
public class AntMoveBehaviour implements MoveBehaviour {
    private static final double MOVE_COST = 1;
    private Ant ant;
    private MyGame context;
    private List<Point> way;

    public AntMoveBehaviour() {
        way = new ArrayList<Point>();
    }

    @Override
    public void setAnt(Ant ant) {
        this.ant = ant;
    }

    @Override
    public void update() {
        checkDestination();
        //  calcRandomDirection();

        calAntDirection();

        calcRotation();
    }

    private void checkDestination() {
        if (ant.getDestination() == AntDestination.TO_NEST) {
            if (ant.getNest().getPosition().equals(ant.getPosition())) {
                wayPointEarned();
                ant.getNest().antArrive(ant);
            }
        } else if (ant.getDestination() == AntDestination.FROM_NEST)

        {
            for (WayPoint wayPoint : context.getWayPoints()) {
                if (wayPoint.getPosition().equals(ant.getPosition())) {
                    wayPointEarned();
                    wayPoint.antArrive(ant);
                }
            }
        }
    }

    private void wayPointEarned() {
        double[][] pheromonMap;
        if (ant.getDestination() == AntDestination.FROM_NEST) {
            context.calcMaxLevelMap(ant.getDestination());
            ant.setDestination(AntDestination.TO_NEST);
            pheromonMap = context.getPheromonMapFromNest();
        } else {
            ant.setDestination(AntDestination.FROM_NEST);
            pheromonMap = context.getPheromonMapToNest();
        }
        for (Point point : way) {
            try {
                pheromonMap[point.getX()][point.getY()] += 0.8;
                pheromonMap[point.getX() + 1][point.getY()] += 0.4;
                pheromonMap[point.getX() - 1][point.getY()] += 0.4;
                pheromonMap[point.getX()][point.getY() - 1] += 0.4;
                pheromonMap[point.getX()][point.getY() + 1] += 0.4;
                pheromonMap[point.getX() + 1][point.getY() + 1] += 0.2;
                pheromonMap[point.getX() - 1][point.getY() - 1] += 0.2;
                pheromonMap[point.getX() + 1][point.getY() - 1] += 0.2;
                pheromonMap[point.getX() - 1][point.getY() - 1] += 0.2;

                pheromonMap[point.getX() - 2][point.getY() - 2] += 0.2;
                pheromonMap[point.getX() - 2][point.getY() - 1] += 0.2;
                pheromonMap[point.getX() - 2][point.getY() - 0] += 0.2;
                pheromonMap[point.getX() - 2][point.getY() + 1] += 0.2;
                pheromonMap[point.getX() - 2][point.getY() + 2] += 0.2;

                pheromonMap[point.getX() + 2][point.getY() - 2] += 0.2;
                pheromonMap[point.getX() + 2][point.getY() - 1] += 0.2;
                pheromonMap[point.getX() + 2][point.getY() - 0] += 0.2;
                pheromonMap[point.getX() + 2][point.getY() + 1] += 0.2;
                pheromonMap[point.getX() + 2][point.getY() + 2] += 0.2;


                pheromonMap[point.getX() - 1][point.getY() - 2] += 0.2;
                pheromonMap[point.getX() - 1][point.getY() + 2] += 0.2;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        context.decreasePheromonMap(pheromonMap);
        way = new ArrayList<Point>();
    }


    //TODO check can move
    private void calAntDirection() {
        ant.setAction(Action.MOVE);
        double[][] pheromonMap;
        if (ant.getDestination() == AntDestination.FROM_NEST) {
            pheromonMap = context.getPheromonMapFromNest();
        } else {
            pheromonMap = context.getPheromonMapToNest();
        }
        HashMap<Direction, DirectionValues> directionsMaps = new HashMap<Direction, DirectionValues>();
        Direction direction = Direction.NORTH;
        addDirectionIfCan(pheromonMap, directionsMaps, direction);
        direction = Direction.WEST;
        addDirectionIfCan(pheromonMap, directionsMaps, direction);
        direction = Direction.EAST;
        addDirectionIfCan(pheromonMap, directionsMaps, direction);
        direction = Direction.SOUTH;
        addDirectionIfCan(pheromonMap, directionsMaps, direction);

        if (directionsMaps.size() == 0) {
            calcRandomDirection();
            //   System.out.println("calc random way");
            return;
        }
        Double minHeuristik = null;
        Direction minDirection = null;
        for (Map.Entry<Direction, DirectionValues> entry : directionsMaps.entrySet()) {
            if (minHeuristik == null || minHeuristik > entry.getValue().getHeuristik()) {
                minHeuristik = entry.getValue().getHeuristik();
                minDirection = entry.getKey();
            }
        }
        for (Map.Entry<Direction, DirectionValues> entry : directionsMaps.entrySet()) {
            if (!entry.getKey().equals(minDirection)) {
                entry.getValue().setHeuristik(0d);
            }
        }
        Double sum = 0d;
        for (Map.Entry<Direction, DirectionValues> entry : directionsMaps.entrySet()) {
            sum += entry.getValue().getFuncValue();
            if (minHeuristik == null || minHeuristik < entry.getValue().getHeuristik()) {
                minHeuristik = entry.getValue().getHeuristik();
                minDirection = entry.getKey();
            }
        }
        HashMap<Direction, Probabilty> directionsProbability = new HashMap<Direction, Probabilty>();
        double lastProbability = 0;
        for (Map.Entry<Direction, DirectionValues> entry : directionsMaps.entrySet()) {
            Double probability = entry.getValue().getFuncValue() / sum;
            directionsProbability.put(entry.getKey(), new Probabilty(lastProbability, lastProbability + probability));
            lastProbability += probability;
        }

        double random = Math.random();
        for (Map.Entry<Direction, Probabilty> entry : directionsProbability.entrySet()) {
            if (entry.getValue().in(random)) {
                ant.setDirection(entry.getKey());
                Point point = ant.getPosition().getCopy();
                context.movePointOnDirection(ant.getDirection(), point);
                way.add(point);
                return;
            }
        }

        //   System.out.println("calc random way");
        calcRandomDirection();
    }

    private void addDirectionIfCan(double[][] pheromonMap, HashMap<Direction, DirectionValues> directions, Direction direction) {
        Point point = ant.getPosition().getCopy();
        context.movePointOnDirection(direction, point);
        if (!way.contains(point)) {
            try {
                double pheromonValue = pheromonMap[point.getX()][point.getY()];
                double heuristik = calcHeuristik(point);
                // heuristik = heuristik / (heuristik / pheromonValue) * 10;
                heuristik = heuristik / 17;
                directions.put(direction, new DirectionValues(pheromonValue, heuristik, point));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private double calcHeuristik(Point point) {
        Double minDistantce = null;
        if (ant.getDestination().equals(AntDestination.FROM_NEST)) {
            for (WayPoint wayPoint : context.getWayPoints()) {
                if (minDistantce == null || getManhattanDistance(point, wayPoint.getPosition()) < minDistantce) {
                    minDistantce = getManhattanDistance(point, wayPoint.getPosition());
                }
            }
        } else if (ant.getDestination().equals(AntDestination.TO_NEST)) {
            for (Nest nest : context.getNests()) {
                if (minDistantce == null || getManhattanDistance(point, nest.getPosition()) < minDistantce) {
                    minDistantce = getManhattanDistance(point, nest.getPosition());
                }
            }
        }
        return minDistantce;
    }

    private Double getManhattanDistance(Point position, Point position1) {
        double dx = Math.abs(position.getX() - position1.getX());
        double dy = Math.abs(position.getY() - position1.getY());
        return MOVE_COST * (dx + dy);
    }

    private void calcRandomDirection() {
        ant.setAction(Action.MOVE);

        randomDirection();
        Point newPoint = ant.getPosition().getCopy();
        context.movePointOnDirection(ant.getDirection(), newPoint);
        if (way.contains(newPoint)) {
            int i = 0;
            while (way.contains(newPoint) && i < 5) {
                // System.out.println("try next point");
                randomDirection();
                newPoint = ant.getPosition().getCopy();
                context.movePointOnDirection(ant.getDirection(), newPoint);
                i++;
            }
        }
        way.add(newPoint);
    }

    @Override
    public void setContext(MyGame context) {
        this.context = context;
    }

    private void calcRotation() {
        if (ant.getDirection() == Direction.WEST) {
            ant.setRotation(90f);
        } else if (ant.getDirection() == Direction.NORTH) {
            ant.setRotation(0f);
        } else if (ant.getDirection() == Direction.EAST) {
            ant.setRotation(270f);
        } else if (ant.getDirection() == Direction.SOUTH) {
            ant.setRotation(180f);
        }
    }

    private void randomDirection() {
        double random = Math.random();
        if (random > 0.75f) {
            ant.setDirection(Direction.SOUTH);
        } else if (random > 0.5f) {
            ant.setDirection(Direction.WEST);
        } else if (random > 0.25f) {
            ant.setDirection(Direction.NORTH);
        } else {
            ant.setDirection(Direction.EAST);
        }
    }

    public List<Point> getWay() {
        return way;
    }

    private class Probabilty {
        private final double start;
        private final double end;

        public Probabilty(double start, double end) {
            this.start = start;
            this.end = end;
        }

        public boolean in(double random) {
            if (random >= start && random <= end) {
                return true;
            }
            return false;
        }

        public String getDelta() {
            return (end - start) + "";
        }
    }

    private class DirectionValues {
        private final double pheromonLevel;
        private double heuristik;
        private final Point point;

        public DirectionValues(double pheromonLevel, double heuristik, Point point) {
            this.pheromonLevel = pheromonLevel;
            this.heuristik = heuristik;
            this.point = point;
        }

        public Double getFuncValue() {
            return pheromonLevel + heuristik;
        }

        public double getPheromonLevel() {
            return pheromonLevel;
        }

        public double getHeuristik() {
            return heuristik;
        }

        public void setHeuristik(double heuristik) {
            this.heuristik = heuristik;
        }
    }
}
