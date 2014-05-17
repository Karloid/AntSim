package com.krld.pathfinding.polygons;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andrey on 5/17/2014.
 */
public class Link {
    private HashSet<Point> points;

    public Link(Point point1, Point point2) {
        this.points = new HashSet<Point>();
        points.add(point1);
        points.add(point2);
    }

    public boolean contain(Point point, Point point2) {
        return points.contains(point) && points.contains(point2);
    }

    public Set<Point> getPoints() {
        return points;
    }
}
