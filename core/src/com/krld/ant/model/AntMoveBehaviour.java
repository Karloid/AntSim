package com.krld.ant.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrey on 5/8/2014.
 */
public class AntMoveBehaviour implements MoveBehaviour {
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
            }
        } else if (ant.getDestination() == AntDestination.FROM_NEST)

        {
            for (WayPoint wayPoint : context.getWayPoints()) {
                if (wayPoint.getPosition().equals(ant.getPosition())) {
                    wayPointEarned();
                }
            }
        }
    }

    private void wayPointEarned() {
        double[][] pheromonMap;
        if (ant.getDestination() == AntDestination.FROM_NEST) {
            ant.setDestination(AntDestination.TO_NEST);
            pheromonMap = context.getPheromonMapFromNest();
        } else {
            pheromonMap = context.getPheromonMapToNest();
        }
        for (Point point : way) {
            pheromonMap[point.getX()][point.getY()] += 0.8;
            try {
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
        HashMap<Direction, Double> directionsMaps = new HashMap<Direction, Double>();
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
            System.out.println("calc random way");
            return;
        }
        Double sum = 0d;
        for (Map.Entry<Direction, Double> entry : directionsMaps.entrySet()) {
            sum += entry.getValue();
        }
        HashMap<Direction, Probabilty> directionsProbability = new HashMap<Direction, Probabilty>();
        double lastProbability = 0;
        for (Map.Entry<Direction, Double> entry : directionsMaps.entrySet()) {
            Double probability = entry.getValue() / sum;
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

        System.out.println("calc random way");
        calcRandomDirection();
    }

    private void addDirectionIfCan(double[][] pheromonMap, HashMap<Direction, Double> directions, Direction direction) {
        Point point = ant.getPosition().getCopy();
        context.movePointOnDirection(direction, point);
        if (!way.contains(point)) {
            try {
                directions.put(direction, pheromonMap[point.getX()][point.getY()]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
}
