package com.krld.ant.model;

/**
 * Created by Andrey on 5/8/2014.
 */
public class WayPoint {
    private final MyGame context;
    private Point point;

    public WayPoint(int x, int y, MyGame myGame) {
        this.context = myGame;
        setPosition(new Point(x, y));

    }

    public void setPosition(Point point) {
        this.point = point;
    }

    public Point getPosition() {
        return point;
    }
}
