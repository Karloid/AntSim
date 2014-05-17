package com.krld.pathfinding.polygons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sun.scenario.effect.Offset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 5/17/2014.
 */
public class Obstacle {
    private static final float RADIUS_POINT = 5;
    public static final Color NOT_CURRENT_COLOR = Color.GRAY;
    public static final Color CURRENT_OBSTACLE_COLOR = Color.GREEN;
    private static final int OFFSET = 0;
    private final List<Point> points;
    private PolygonsWorld context;

    public Obstacle(PolygonsWorld context) {
        points = new ArrayList<Point>();
        setContext(context);
    }

    public void addPoint(Point point) {
        points.add(point);
        point.setObstacle(this);
    }

    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        if (this == context.getCurrentObstacle()) {
            shapeRenderer.setColor(CURRENT_OBSTACLE_COLOR);
        } else {
            shapeRenderer.setColor(NOT_CURRENT_COLOR);
        }
        Point prevPoint = null;
        for (Point point : points) {
            shapeRenderer.circle(point.getX(), point.getY(), RADIUS_POINT);

            if (prevPoint != null) {
                shapeRenderer.line(point.getX() - OFFSET, point.getY() - OFFSET, prevPoint.getX() + OFFSET, prevPoint.getY() + OFFSET);
            }
            prevPoint = point;
        }
        if (points.size() > 2) {
            shapeRenderer.line(points.get(0).getX()  - OFFSET, points.get(0).getY() - OFFSET,
                    points.get(points.size() - 1).getX() + OFFSET, points.get(points.size() - 1).getY() + OFFSET);
        }
        shapeRenderer.end();
        batch.begin();
    }

    public void removeLastPoint() {
        if (points.size() > 0) {
            Point point = points.get(points.size() - 1);
            points.remove(point);
            point.setObstacle(null);

        }
    }

    public void setContext(PolygonsWorld context) {
        this.context = context;
    }

    public PolygonsWorld getContext() {
        return context;
    }

    public List<Point> getPoints() {
        return points;
    }
}
