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
    public static final Color LINK_COLOR = Color.MAGENTA;
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
