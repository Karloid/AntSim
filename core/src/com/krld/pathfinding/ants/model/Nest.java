package com.krld.pathfinding.ants.model;


/**
 * Created by Andrey on 5/8/2014.
 */
public class Nest {
    private Point position;
    private int arriveCount;

    public Nest(int x, int y) {
        this.position = new Point(x, y);
        arriveCount = 0;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void antArrive(Ant ant) {
        arriveCount++;
    }

    public int getArriveCount() {
        return arriveCount;
    }
}
