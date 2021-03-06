package com.krld.pathfinding.polygons.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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
    public static final Color COLOR_FILL_POLYGON = Color.PINK;
    private final List<Point> points;
    private PolygonsWorld context;

    public static final Color OBSTACLE_FILL_COLOR = Color.RED;

    private static Texture textureSolid;

    static {
        initPoly();


    }

    private static void initPoly() {
        Pixmap pixMap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixMap.setColor(OBSTACLE_FILL_COLOR);
        pixMap.fillRectangle(0, 0, 1, 1);
        textureSolid = new Texture(pixMap, Pixmap.Format.RGB888, false);
    }

    private PolygonSprite poly;
    private PolygonSpriteBatch polyBatch;

    public Obstacle(PolygonsWorld context) {
        points = new ArrayList<Point>();
        setContext(context);
    }

    public void addPoint(Point point) {
        points.add(point);
        point.setObstacle(this);
        calcPoly();
    }

    private void calcPoly() {
        float a = 100;
        float b = 100;
        float[] polygonPoints = new float[points.size() * 2];

        int i = 0;
        for (Point point : points) {
            polygonPoints[i] = point.getX();
            i++;
            polygonPoints[i] = point.getY();
            i++;
        }

        int indexPoint = 2;
        if (points.size() < 3) {
            return;
        }
        short[] triangles = new short[(points.size() - indexPoint) * 2 * 3];
        int indexValueInTriangle = 0;
        while (indexPoint <= points.size() - 1 && points.size() > 2 ) {
            triangles[indexValueInTriangle] = (short) polygonPoints[(indexPoint - 2) * 2];
            indexValueInTriangle++;
            triangles[indexValueInTriangle] = (short) polygonPoints[(indexPoint - 2) * 2 + 1];
            indexValueInTriangle++;

            triangles[indexValueInTriangle] = (short) polygonPoints[(indexPoint - 1) * 2];
            indexValueInTriangle++;
            triangles[indexValueInTriangle] = (short) polygonPoints[(indexPoint - 1) * 2 + 1];
            indexValueInTriangle++;
            triangles[indexValueInTriangle] = (short) polygonPoints[(indexPoint - 0) * 2];
            indexValueInTriangle++;
            triangles[indexValueInTriangle] = (short) polygonPoints[(indexPoint - 0) * 2 + 1];
            indexValueInTriangle++;

            indexPoint++;
        }
        PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid), polygonPoints, triangles);
        poly = new PolygonSprite(polyReg);
        poly.setOrigin(a, b);
        polyBatch = new PolygonSpriteBatch();
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
            shapeRenderer.line(points.get(0).getX() - OFFSET, points.get(0).getY() - OFFSET,
                    points.get(points.size() - 1).getX() + OFFSET, points.get(points.size() - 1).getY() + OFFSET);
        }
        shapeRenderer.end();
   /*     shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        float[] polygonPoints = new float[points.size() * 2];
        int i = 0;
        for (Point point : points) {
            polygonPoints[i] = point.getX();
            i++;
            polygonPoints[i] = point.getY();
            i++;
        }
        shapeRenderer.setColor(COLOR_FILL_POLYGON);
        shapeRenderer.polygon(polygonPoints);
        shapeRenderer.end();*/

        /*Here is a LIBGDX example which draws 2D concave poly.

// define class members for PolygonSprite PolygonSpriteBatch

PolygonSprite poly;
PolygonSpriteBatch polyBatch;
Texture textureSolid;
// create instances, 1x1 size texture used with red pixel as workaround, (x,y) array of coords used for initialization of poly

ctor() {
    textureSolid = makeTextureBox(1, 0xFFFF0000, 0, 0);
    float a = 100;
    float b = 100;
    PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
      new float[] {
        a*0, b*0,
        a*0, b*2,
        a*3, b*2,
        a*3, b*0,
        a*2, b*0,
        a*2, b*1,
        a*1, b*1,
        a*1, b*0,
    });
    poly = new PolygonSprite(polyReg);
    poly.setOrigin(a, b);
    polyBatch = new PolygonSpriteBatch();
}
// draw and rotate poly

void draw() {
    super.draw();
    polyBatch.begin();
    poly.draw(polyBatch);
    polyBatch.end();
    poly.rotate(1.1f);
}
        * */
      /*  if (poly != null && polyBatch != null) {
            polyBatch.begin();
            poly.draw(polyBatch);
            polyBatch.end();
        }*/
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
