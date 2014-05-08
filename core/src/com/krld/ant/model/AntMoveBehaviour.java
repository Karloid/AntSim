package com.krld.ant.model;

import java.util.ArrayList;
import java.util.List;

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

        calcRotation();
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
}
