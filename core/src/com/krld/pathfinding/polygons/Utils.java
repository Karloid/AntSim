package com.krld.pathfinding.polygons;

/**
 * Created by Andrey on 5/17/2014.
 */
public class Utils {
    public static double getEuclideDistanceSimple(Point position, Point position1) {
        return Math.sqrt(Math.pow(position.getX() - position1.getX(), 2) + Math.pow(position.getY() - position1.getY(), 2));
    }
}
