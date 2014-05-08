package com.krld.ant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.krld.ant.model.*;

import java.util.HashMap;

/**
 * Created by Andrey on 5/8/2014.
 */
public class WorldRenderer {
    public static final int ANT_SIZE = 4;
    private static final boolean SHOW_ANTS_WAYS = false;
    public static final String DOT_TEXTURE_KEY = "dot";
    private static final int NEST_SIZE = 8;
    private MyGame game;
    private HashMap<String, TextureRegion> textures;

    public void setGame(MyGame game) {
        this.game = game;
    }

    public MyGame getGame() {
        return game;
    }

    public void draw(SpriteBatch batch) {
        try {
            for (Ant ant : game.getAnts()) {
                String key = ant.getClass().getSimpleName();
                batch.draw(textures.get(key), ant.getPosition().getX() - ANT_SIZE / 2, ant.getPosition().getY() - ANT_SIZE / 2, ANT_SIZE / 2, ANT_SIZE / 2, ANT_SIZE, ANT_SIZE, 1, 1, ant.getRotation());
                if (SHOW_ANTS_WAYS) {
                    AntMoveBehaviour moveBehaviour = (AntMoveBehaviour) ant.getMoveBehaviour();
                    for (Point point : moveBehaviour.getWay()) {
                        batch.draw(textures.get(DOT_TEXTURE_KEY), point.getX(), point.getY());
                    }
                }
            }
            for (Nest nest : game.getNests()) {
                String key = nest.getClass().getSimpleName();
                batch.draw(textures.get(key), nest.getPosition().getX() - NEST_SIZE / 2, nest.getPosition().getY() - NEST_SIZE / 2);
            }

            for (WayPoint wayPoint : game.getWayPoints()) {
                String key = wayPoint.getClass().getSimpleName();
                batch.draw(textures.get(key), wayPoint.getPosition().getX() - NEST_SIZE / 2, wayPoint.getPosition().getY() - NEST_SIZE / 2);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        textures = new HashMap<String, TextureRegion>();
        textures.put(Ant.class.getSimpleName(), loadTextureRegion("ant_little.png", ANT_SIZE, ANT_SIZE));
        textures.put(Nest.class.getSimpleName(), loadTextureRegion("nest.png", NEST_SIZE, NEST_SIZE));
        textures.put(WayPoint.class.getSimpleName(), loadTextureRegion("wayPoint.png", NEST_SIZE, NEST_SIZE));
        textures.put(DOT_TEXTURE_KEY, loadTextureRegion("dot.png", 1, 1));

    }

    private TextureRegion loadTextureRegion(String fileName, int width, int height) {
        return new TextureRegion(new Texture(Gdx.files.internal(fileName)), 0, 0, width, height);
    }
}
