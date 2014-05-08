package com.krld.ant.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.krld.ant.GameManager;
import com.krld.ant.MyInputProcessor;
import com.krld.ant.WorldRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Andrey on 5/8/2014.
 */
public class MyGame {
    private static final long UPDATE_DELAY = 10;
    public static final int INITIAL_ANTS_COUNT = 100;
    public static final float INITIAL_PHEROMON = 5f;
    private final WorldRenderer worldRenderer;
    private final Vector<Nest> nests;
    private MyInputProcessor inputProcessor;
    private GameLoopThread gameLoopThread;
    private static List<Ant> ants;
    private Ant newAnt;
    private List<WayPoint> wayPoints;
    private WayPoint newWayPoint;
    private int width;
    private int height;
    private int[][] obstacleMap;
    private double[][] pheromonMap;


    public MyGame() {
        inputProcessor = new MyInputProcessor();
        inputProcessor.setGame(this);

        worldRenderer = new WorldRenderer();
        worldRenderer.setGame(this);

        ants = new Vector<Ant>();
        nests = new Vector<Nest>();
        wayPoints = new Vector<WayPoint>();

        createAnts(INITIAL_ANTS_COUNT);

    }

    private void createAnts(int n) {
        boolean safe = false;
        nests.add(new Nest(GameManager.WIDTH / 2, GameManager.HEIGHT / 2));
        for (int i = 0; i < n; i++) {
            //   createAnt((int) (Math.random() * GameManager.WIDTH), (int) (Math.random() * GameManager.HEIGHT));
            createAnt(GameManager.WIDTH / 2, GameManager.HEIGHT / 2, safe);
        }
    }

    public MyInputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void setInputProcessor(MyInputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    public void runGameLoop() {
        if (gameLoopThread != null) {
            gameLoopThread.interrupt();
        }
        worldRenderer.init();
        gameLoopThread = new GameLoopThread();
        gameLoopThread.start();
    }

    public void draw(SpriteBatch batch) {
        worldRenderer.draw(batch);
    }

    public List<Ant> getAnts() {
        return ants;
    }

    public void createAnt(int x, int y, boolean safe) {
        Ant ant = new Ant(x, y, this, nests.get(0));
        if (safe) {
            newAnt = ant;
        } else {
            ants.add(ant);
        }
    }

    public Vector<Nest> getNests() {
        return nests;
    }

    public void createWayPoint(int x, int y, boolean safe) {
        WayPoint wayPoint = new WayPoint(x, y, this);
        if (safe) {
            newWayPoint = wayPoint;
        } else {
            wayPoints.add(wayPoint);
        }
    }

    public List<WayPoint> getWayPoints() {
        return wayPoints;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void initMap() {
        obstacleMap = new int[width][height];
        pheromonMap = new double[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                pheromonMap[x][y] = INITIAL_PHEROMON;
            }
    }

    private class GameLoopThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    update();
                    Thread.sleep(UPDATE_DELAY);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        if (newAnt != null) {
            ants.add(newAnt);
            newAnt = null;
        }
        if (newWayPoint != null) {
            wayPoints.add(newWayPoint);
            newWayPoint = null;
        }
        for (Ant ant : ants) {
            ant.update();
        }

        applyUnitMove();
    }

    private void applyUnitMove() {
        for (Ant ant : ants) {
            if (ant.getAction() == Action.MOVE) {
                unitMove(ant);
            }
        }

    }

    private void unitMove(Ant ant) {
        if (ant.getAction() != Action.MOVE) {
            return;
        }
        Direction direction = ant.getDirection();
        Point newPoint = ant.getPosition().getCopy();
        movePointOnDirection(direction, newPoint);

        if (canMoveToPoint(newPoint)) {
            ant.setPosition(newPoint);
        }
    }

    public void movePointOnDirection(Direction direction, Point newPoint) {
        if (direction == Direction.WEST) {
            newPoint.setX(newPoint.getX() - 1);
        } else if (direction == Direction.EAST) {
            newPoint.setX(newPoint.getX() + 1);
        } else if (direction == Direction.SOUTH) {
            newPoint.setY(newPoint.getY() + 1);
        } else if (direction == Direction.NORTH) {
            newPoint.setY(newPoint.getY() - 1);
        }
    }

    public boolean canMoveToPoint(Point point) {
        if (inMap(point)) {
            return true;
        }
        return false;
    }

    private boolean inMap(Point point) {
        if (point.getX() > GameManager.WIDTH && point.getX() < 0 && point.getY() > GameManager.HEIGHT && point.getY() < 0) {
            return false;
        }
        return true;
    }

}
