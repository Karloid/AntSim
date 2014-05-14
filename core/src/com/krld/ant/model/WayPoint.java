package com.krld.ant.model;

/**
 * Created by Andrey on 5/8/2014.
 */
public class WayPoint {
    private static final int MAX_ARRIVE_COUNT = 25;
    private final MyGame context;
    private Point point;
    private int arriveCount;

    public WayPoint(int x, int y, MyGame myGame) {
        this.context = myGame;
        setPosition(new Point(x, y));
        arriveCount = 0;

    }

    public void setPosition(Point point) {
        this.point = point;
    }

    public Point getPosition() {
        return point;
    }

    public void antArrive(Ant ant) {
        arriveCount++;
    }

    public boolean isEmpty() {
        return arriveCount> MAX_ARRIVE_COUNT;

    }

    public int getArriveCount() {
        return arriveCount;
    }
}
