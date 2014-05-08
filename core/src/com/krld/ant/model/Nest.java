package com.krld.ant.model;


/**
 * Created by Andrey on 5/8/2014.
 */
public class Nest {
    private Point position;

    public Nest(int x, int y) {
        this.position = new Point(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
