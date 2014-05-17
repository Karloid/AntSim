package com.krld.pathfinding.polygons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Andrey on 5/17/2014.
 */
public class PolygonsView extends ApplicationAdapter {
    SpriteBatch batch;
    private BitmapFont font;
    private PolygonsWorld polygonsWorld;
    private PolygonInputProcessor inputProcessor;
    private BitmapFont fontLittle;
    private boolean showHelp;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        font.setColor(Color.WHITE);
        font.scale(1f);
        fontLittle = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        fontLittle.setColor(Color.WHITE);
        fontLittle.scale(0.01f);
        initInputProcessor();
        polygonsWorld.getWorldRenderer().init();
        polygonsWorld.runGameLoop();
        setShowHelp(true);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        polygonsWorld.draw(batch);
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 10, polygonsWorld.getHeight() - 10);
        drawState();
        if (isShowHelp())
            drawHelp();
        batch.end();
    }

    private void drawState() {
        fontLittle.draw(batch, inputProcessor.getState() + "", polygonsWorld.getWidth() / 2, polygonsWorld.getHeight() - 10);
    }

    private void drawHelp() {
        fontLittle.draw(batch, "1: add or delete obstacles points ", 10, polygonsWorld.getHeight() - 50);
        fontLittle.draw(batch, "  lmb: add;| rmb: delete;| space: new obstacle", 10, polygonsWorld.getHeight() - 70);
        fontLittle.draw(batch, "2: add start or end  points ", 10, polygonsWorld.getHeight() - 100);
        fontLittle.draw(batch, "  lmb: StartPoint;| rmb: EndPoint;", 10, polygonsWorld.getHeight() - 120);
        fontLittle.draw(batch, "F1: show/hide help ", 10, 30);
    }


    public void setPolygonsWorld(PolygonsWorld polygonsWorld) {
        this.polygonsWorld = polygonsWorld;
    }

    public PolygonsWorld getPolygonsWorld() {
        return polygonsWorld;
    }

    public void setInputProcessor(PolygonInputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void initInputProcessor() {
        setInputProcessor(polygonsWorld.getInputProcessor());
        polygonsWorld.getInputProcessor().setPolygonView(this);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public void setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
    }
}
