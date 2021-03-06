package com.krld.pathfinding.ants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.krld.pathfinding.ants.model.*;

import java.util.HashMap;

/**
 * Created by Andrey on 5/8/2014.
 */
public class WorldRenderer {
    public static final int ANT_SIZE = 4;
    public static final float UPDATE_PHEROMON_MAP_RATIO = 0.9f;
    private static final boolean SHOW_ANTS_WAYS = false;
    public static final String DOT_TEXTURE_KEY = "dot";
    private static final int NEST_SIZE = 8;
    private static final boolean DRAW_RECT_ANTS = true;
    public static final float MIN_LEVEL = 0.3f;
    private MyGame game;
    private HashMap<String, TextureRegion> textures;
    private ShapeRenderer renderer;
    private BitmapFont font;
    private Pixmap pixMap;
    private Texture pixMapTexture;
    private double[][] pheromonMapToView;

    public void setGame(MyGame game) {
        this.game = game;
    }

    public MyGame getGame() {
        return game;
    }

    public void draw(SpriteBatch batch) {
        // drawPheromon(batch);
       //  drawPheromonOverTexture(batch);
        drawAStarTexture(batch);
        //  drawObstacleOverTexture(batch);
        try {

            drawAnts(batch);
            for (Nest nest : game.getNests()) {
                String key = nest.getClass().getSimpleName();
                batch.draw(textures.get(key), nest.getPosition().getX() - NEST_SIZE / 2, nest.getPosition().getY() - NEST_SIZE / 2);
                font.draw(batch, nest.getArriveCount() + "", nest.getPosition().getX(), nest.getPosition().getY() + NEST_SIZE / 2);
            }

            for (WayPoint wayPoint : game.getWayPoints()) {
                String key = wayPoint.getClass().getSimpleName();
                batch.draw(textures.get(key), wayPoint.getPosition().getX() - NEST_SIZE / 2, wayPoint.getPosition().getY() - NEST_SIZE / 2);
                //font.draw(batch, wayPoint.getArriveCount() + "", wayPoint.getPosition().getX(), wayPoint.getPosition().getY() + NEST_SIZE / 2);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawAStarTexture(SpriteBatch batch) {
        if (Math.random() > UPDATE_PHEROMON_MAP_RATIO) {
            Ant ant = game.getAnts().get(0);
            if (ant.getMoveBehaviour() instanceof AntAStarMoveBehaviour) {
                pixMap.setColor(Color.WHITE);
                pixMap.fillRectangle(0, 0, game.getWidth(), game.getHeight());
                for (int x = 0; x < game.getWidth(); x++) {
                    for (int y = 0; y < game.getHeight(); y++) {
                        if (game.getObstacleMap()[x][y] == MyGame.NO_PASS_MAP) {
                            pixMap.setColor(Color.MAGENTA);
                            pixMap.drawPixel(x, game.getHeight() - y);
                        }
                    }
                }
                AntAStarMoveBehaviour moveBehaviour = (AntAStarMoveBehaviour) (ant.getMoveBehaviour());
                pixMap.setColor(Color.CYAN);
                try {
                    if (moveBehaviour.getOpenNodes() != null)
                        for (AntAStarMoveBehaviour.Node node : moveBehaviour.getOpenNodes()) {
                            pixMap.drawPixel(node.getPosition().getX(), game.getHeight() - node.getPosition().getY());
                        }
                    pixMap.setColor(Color.GRAY);
                    if (moveBehaviour.getClosedNodes() != null)
                        for (AntAStarMoveBehaviour.Node node : moveBehaviour.getClosedNodes()) {
                            pixMap.drawPixel(node.getPosition().getX(), game.getHeight() - node.getPosition().getY());
                        }
                    pixMap.setColor(Color.GREEN);
                    if (moveBehaviour.getPath() != null)
                        for (Point pos : moveBehaviour.getPath()) {
                            pixMap.drawPixel(pos.getX(), game.getHeight() - pos.getY());
                        }
                } catch (Exception e) {
                    //do nothing
                }
                pixMapTexture = new Texture(pixMap, Pixmap.Format.RGB888, false);
            }
        }
        batch.draw(pixMapTexture, 1f / 2f, game.getWidth() / game.getHeight() / 2f);
    }

    private void drawAnts(SpriteBatch batch) {
        if (DRAW_RECT_ANTS) {
            batch.end();
            renderer.begin(ShapeRenderer.ShapeType.Filled);

            for (Ant ant : game.getAnts()) {
                if (ant.getDestination() == AntDestination.FROM_NEST) {
                    renderer.setColor(Color.BLUE);
                } else {
                    renderer.setColor(Color.RED);
                }
                renderer.rect(ant.getPosition().getX(), ant.getPosition().getY(), 1, 1);
            }
            renderer.end();
            batch.begin();
        } else {
            for (Ant ant : game.getAnts()) {
                String key = ant.getClass().getSimpleName();
                batch.draw(textures.get(key), ant.getPosition().getX() - ANT_SIZE / 2, ant.getPosition().getY() - ANT_SIZE / 2, ANT_SIZE / 2, ANT_SIZE / 2, ANT_SIZE, ANT_SIZE, 1, 1, ant.getRotation());
                if (SHOW_ANTS_WAYS) {
                    AntMagicMoveBehaviour moveBehaviour = (AntMagicMoveBehaviour) ant.getMoveBehaviour();
                    for (Point point : moveBehaviour.getWay()) {
                        batch.draw(textures.get(DOT_TEXTURE_KEY), point.getX(), point.getY());
                    }
                }
            }
        }
    }

    private void drawPheromonOverTexture(SpriteBatch batch) {
        if (Math.random() > UPDATE_PHEROMON_MAP_RATIO) {
            double[][] pheromonMap = getPheromonMapToView();
            for (int x = 0; x < game.getWidth(); x++) {
                for (int y = 0; y < game.getHeight(); y++) {
                    if (game.getObstacleMap()[x][y] == MyGame.NO_PASS_MAP) {
                        pixMap.setColor(Color.MAGENTA);
                    } else {
                        float level = (float) (pheromonMap[x][y] / game.getMaxPheromonLevelFromNest());
                        if (pheromonMap[x][y] == game.INITIAL_PHEROMON) {
                            level = 0;
                        }
                        if (level > 1) {
                            level = 1;
                        }
                        if (level != 0 && level < MIN_LEVEL) {
                            level = MIN_LEVEL;
                        }
                        Color newColor = new Color(1f - level, 1f - level, 1f - level, 1f);
                        pixMap.setColor(newColor);
                    }
                    pixMap.drawPixel(x, game.getHeight() - y);
                    //  renderer.setColor(newColor);
                    //  renderer.rect(x, y, 1, 1);

                }
            }
            pixMapTexture = new Texture(pixMap, Pixmap.Format.RGB888, false);
        }
        batch.draw(pixMapTexture, 1f / 2f, game.getWidth() / game.getHeight() / 2f);
    }

    private void drawPheromon(SpriteBatch batch) {
        //    batch.end();
        //   renderer.begin(ShapeRenderer.ShapeType.Filled);
        double[][] pheromonMap = game.getPheromonMapFromNest();
        for (int x = 0; x < game.getWidth(); x++) {
            for (int y = 0; y < game.getHeight(); y++) {
                float level = (float) ((pheromonMap[x][y] - game.INITIAL_PHEROMON) / game.getMaxPheromonLevelFromNest());
                if (pheromonMap[x][y] == game.INITIAL_PHEROMON) {
                    level = 0;
                }
                if (level > 1) {
                    level = 1;
                }
                if (level != 0 && level < MIN_LEVEL && level > 0.01f) {
                    level = MIN_LEVEL;
                }
                Color newColor = new Color(1f - level, 1f - level, 1f - level, 1f);
                pixMap.setColor(newColor);
                pixMap.drawPixel(x, y);
                //  renderer.setColor(newColor);
                //  renderer.rect(x, y, 1, 1);

            }
        }
        pixMapTexture = new Texture(pixMap, Pixmap.Format.RGB888, false);
        // renderer.end();
        //batch.begin();
    }

    public void init() {
        textures = new HashMap<String, TextureRegion>();
        textures.put(Ant.class.getSimpleName(), loadTextureRegion("ant_little.png", ANT_SIZE, ANT_SIZE));
        textures.put(Nest.class.getSimpleName(), loadTextureRegion("nest.png", NEST_SIZE, NEST_SIZE));
        textures.put(WayPoint.class.getSimpleName(), loadTextureRegion("wayPoint.png", NEST_SIZE, NEST_SIZE));
        textures.put(DOT_TEXTURE_KEY, loadTextureRegion("dot.png", 1, 1));

        renderer = new ShapeRenderer();

        font = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        font.setColor(Color.WHITE);
        font.scale(0.1f);
        setPheromonMapToView(game.getPheromonMapFromNest());
        initPheromonPixMap();
    }


    private void initPheromonPixMap() {
        pixMap = new Pixmap(game.getWidth(), game.getHeight(), Pixmap.Format.RGBA8888);
        pixMap.setColor(Color.WHITE);
        pixMap.fillRectangle(0, 0, game.getWidth(), game.getHeight());
        pixMapTexture = new Texture(pixMap, Pixmap.Format.RGB888, false);
    }

    private TextureRegion loadTextureRegion(String fileName, int width, int height) {
        return new TextureRegion(new Texture(Gdx.files.internal(fileName)), 0, 0, width, height);
    }

    public void setPheromonMapToView(double[][] pheromonMapToView) {
        this.pheromonMapToView = pheromonMapToView;
    }

    public double[][] getPheromonMapToView() {
        return pheromonMapToView;
    }
}
