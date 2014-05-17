package com.krld.pathfinding.polygons;

import java.util.HashSet;

/**
 * Created by Andrey on 5/17/2014.
 */
public class Point {
    private final int id;
    private int x;
    private int y;
    private static int maxId;

    static {
        maxId = 0;
    }

    private HashSet<Link> links;
    private PolygonsWorld context;
    private Obstacle obstacle;

    public Point(int x, int y, PolygonsWorld context) {
        setX(x);
        setY(y);
        id = maxId;
        maxId++;
        setContext(context);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public Point getCopy() {
        return new Point(getX(), getY(), context);
    }

    public void calcLinks() {
        links = new HashSet<Link>();
        if (this == context.getStartPoint()) {
            addLinkToIfCan(context.getEndPoint());
        } else if (this == context.getEndPoint()) {
            addLinkToIfCan(context.getStartPoint());
        } else {
            addLinkToIfCan(context.getEndPoint());
            addLinkToIfCan(context.getStartPoint());
        }
        for (Obstacle obstacle : getContext().getObstacles()) {
            for (Point point : obstacle.getPoints()) {
                if (point != this) {
                    addLinkToIfCan(point);
                }
            }
        }

    }

    private void addLinkToIfCan(Point pointGoal) {
        boolean haveIntersect = false;
        for (Obstacle obstacle : context.getObstacles()) {
            Point prevPoint = null;
            for (Point pointObstacle : obstacle.getPoints()) {
                if (prevPoint == null) {
                    if (obstacle.getPoints().size() > 2) {
                        prevPoint = obstacle.getPoints().get(obstacle.getPoints().size() - 1);
                    }
                }
                if (prevPoint != null && !(this == prevPoint || this == pointObstacle || pointGoal == prevPoint || pointGoal == pointObstacle)
                        && checkIntersect(this, pointGoal, prevPoint, pointObstacle)) {
                    haveIntersect = true;
                    break;
                }
                prevPoint = pointObstacle;
            }
            if (haveIntersect) {
                break;
            }
        }
        if (haveIntersect) {
            return;
        }

        Link link = context.getLink(this, pointGoal);
        if (link == null) {
            link = new Link(this, pointGoal);
            context.getAllLinks().add(link);
        }
        links.add(link);
    }

    private boolean checkIntersect(Point a1, Point a2, Point b1, Point b2) {
        return segmentsIntersect(a1.getX(), a1.getY(), a2.getX(), a2.getY(), b1.getX(), b1.getY(), b2.getX(), b2.getY());
    }

    public static boolean segmentsIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0) return false;
        double xi = Math.floor(((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d);
        double yi = Math.floor(((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d);
        if (xi < Math.min(x1, x2) || xi > Math.max(x1, x2)) return false;
        if (xi < Math.min(x3, x4) || xi > Math.max(x3, x4)) return false;
        if (yi < Math.min(y1, y2) || yi > Math.max(y1, y2)) return false;
        if (yi < Math.min(y3, y4) || yi > Math.max(y3, y4)) return false;
        /*Косяки с вычислениями в double*/
       /* log("[segmentsIntersect] OBSTACLE FIND!");
        log("[segmentsIntersect] x1 y1 x2 y2 x3 x4 : " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + x3 + " " + y3 + " " + x4 + " " + y4);

        log("[segmentsIntersect]xi, yi : " + xi + " " + yi);
        */
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (id != point.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void setContext(PolygonsWorld context) {
        this.context = context;
    }

    public PolygonsWorld getContext() {
        return context;
    }

    @Override
    public String toString() {
        return " p - id: " + id + "; x: " + x + "; y: " + y;
    }

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }
}
