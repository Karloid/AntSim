package com.krld.pathfinding.ants;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.krld.pathfinding.ants.model.MyGame;

public class GameView extends ApplicationAdapter {
    SpriteBatch batch;
    private BitmapFont font;
    private MyGame game;
    private InputProcessor inputProcessor;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        font.setColor(Color.WHITE);
        font.scale(1f);
        initInputProcessor();
        game.runGameLoop();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        game.draw(batch);
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 10, game.getHeight() - 10);
        batch.end();
    }


    public void setGame(MyGame game) {
        this.game = game;
    }

    public MyGame getGame() {
        return game;
    }

    public void setInputProcessor(InputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void initInputProcessor() {
        setInputProcessor(game.getInputProcessor());
        game.getInputProcessor().setGameView(this);
        Gdx.input.setInputProcessor(inputProcessor);
    }
}
