package com.krld.pathfinding.polygons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.krld.pathfinding.ants.model.MyGame;

/**
 * Created by Andrey on 5/17/2014.
 */
public class PolygonsView extends ApplicationAdapter {
    SpriteBatch batch;
    private BitmapFont font;
    private PolygonsWorld polygonsWorld;
    private InputProcessor inputProcessor;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        font.setColor(Color.WHITE);
        font.scale(1f);
        initInputProcessor();
        polygonsWorld.runGameLoop();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        polygonsWorld.draw(batch);
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 10, polygonsWorld.getHeight() - 10);
        batch.end();
    }


    public void setPolygonsWorld(PolygonsWorld polygonsWorld) {
        this.polygonsWorld = polygonsWorld;
    }

    public PolygonsWorld getPolygonsWorld() {
        return polygonsWorld;
    }

    public void setInputProcessor(InputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void initInputProcessor() {
        setInputProcessor(polygonsWorld.getInputProcessor());
//        polygonsWorld.getInputProcessor().setPolygonView(this);
    //    Gdx.input.setInputProcessor(inputProcessor);
    }
}
