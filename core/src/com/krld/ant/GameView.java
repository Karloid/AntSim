package com.krld.ant;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameView extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
    private BitmapFont font;
    private MyGame game;
    private InputProcessor inputProcessor;

    @Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        font = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        font.setColor(Color.WHITE);
        font.scale(1f);


	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 10, 770);
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
        Gdx.input.setInputProcessor(inputProcessor);
    }
}
