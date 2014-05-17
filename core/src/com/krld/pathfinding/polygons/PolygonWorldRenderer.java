package com.krld.pathfinding.polygons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ObjectSet;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Andrey on 5/17/2014.
 */
public class PolygonWorldRenderer {
    private static final float START_END_POINT_RADIUS = 3;
    public static final Color LINK_COLOR = new Color(0.5f, 0.5f, 0.5f, 0.3f);
    private static final Color PATH_COLOR = Color.ORANGE;
    private static final float OPEN_NODE_RADIUS = 5;
    private static final Color OPEN_NODE_COLOR = Color.RED;
    private static final Color CLOSE_NODE_COLOR = Color.BLUE;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private PolygonsWorld context;

    public PolygonWorldRenderer(PolygonsWorld context) {
        setContext(context);
    }

    public void init() {
        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        font.setColor(Color.WHITE);
        font.scale(0.1f);
    }

    public void setContext(PolygonsWorld context) {
        this.context = context;
    }

    public PolygonsWorld getContext() {
        return context;
    }

    public void draw(SpriteBatch batch) {
        drawObstacles(batch);
        drawStartEndPoints(batch);
        drawViewGraph(batch);
        drawPathGraph(batch);
    }

    private void drawPathGraph(SpriteBatch batch) {
        AStarPathCalcer pathCalcer = null;
        if (context.getPathCalcer() instanceof AStarPathCalcer) {
            pathCalcer = (AStarPathCalcer) context.getPathCalcer();
        } else {
            return;
        }
        drawNodesSets(batch, pathCalcer);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(PATH_COLOR);
        Point nextPoint = null;
        for (Point currentPoint : pathCalcer.getPath()) {
            if (nextPoint != null) {
                shapeRenderer.line(currentPoint.getX(), currentPoint.getY(), nextPoint.getX(), nextPoint.getY());
            }
            nextPoint = currentPoint;
        }
        shapeRenderer.end();
        batch.begin();

    }

    private void drawNodesSets(SpriteBatch batch, AStarPathCalcer pathCalcer) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(OPEN_NODE_COLOR);
        if (pathCalcer.getOpenNodes() != null)
            for (AStarPathCalcer.Node currentNode : pathCalcer.getOpenNodes()) {
                Point currentPoint = currentNode.getPosition();
                shapeRenderer.circle(currentPoint.getX(), currentPoint.getY(), OPEN_NODE_RADIUS);
            }
        shapeRenderer.setColor(CLOSE_NODE_COLOR);
        if (pathCalcer.getClosedNodes() != null)
            for (AStarPathCalcer.Node currentNode : pathCalcer.getClosedNodes()) {
                Point currentPoint = currentNode.getPosition();
                shapeRenderer.circle(currentPoint.getX(), currentPoint.getY(), OPEN_NODE_RADIUS);
            }
        shapeRenderer.end();
        batch.begin();
        if (pathCalcer.getClosedNodes() != null)
            for (AStarPathCalcer.Node currentNode : pathCalcer.getClosedNodes()) {
                Point currentPoint = currentNode.getPosition();
                font.draw(batch, Math.round(currentNode.getF() * 100) / 100 + "", currentPoint.getX(), currentPoint.getY());
            }
        if (pathCalcer.getOpenNodes() != null)
            for (AStarPathCalcer.Node currentNode : pathCalcer.getOpenNodes()) {
                Point currentPoint = currentNode.getPosition();
                font.draw(batch, Math.round(currentNode.getF() * 100) / 100 + "", currentPoint.getX(), currentPoint.getY());
            }
    }

    private void drawViewGraph(SpriteBatch batch) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(LINK_COLOR);
        for (Link link : getContext().getAllLinks()) {
            Iterator<Point> iter = link.getPoints().iterator();
            Point point1 = iter.next();
            Point point2 = iter.next();
            shapeRenderer.line(point1.getX(), point1.getY(), point2.getX(), point2.getY());
        }
        shapeRenderer.end();
        batch.begin();
    }

    private void drawStartEndPoints(SpriteBatch batch) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Point startPoint = context.getStartPoint();
        if (startPoint != null) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(startPoint.getX(), startPoint.getY(), START_END_POINT_RADIUS);
        }
        Point endPoint = context.getEndPoint();
        if (endPoint != null) {
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.circle(endPoint.getX(), endPoint.getY(), START_END_POINT_RADIUS);
        }
        shapeRenderer.end();
        batch.begin();
    }

    private void drawObstacles(SpriteBatch batch) {
        for (Obstacle obstacle : context.getObstacles()) {
            obstacle.draw(batch, shapeRenderer);
        }
    }
}
