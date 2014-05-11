package com.krld.ant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.krld.ant.model.MyGame;

/**
 * Created by Andrey on 5/8/2014.
 */
public class MyInputProcessor implements com.badlogic.gdx.InputProcessor {
    private MyGame game;
    private GameView gameView;

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            boolean safe = true;
            game.createAnt(x, GameManager.HEIGHT - y, safe);
            System.out.println("touch down: " + x + " : y: " + y);
        } else /*if (button == Input.Buttons.RIGHT)*/ {
            boolean safe = true;
            game.createWayPoint(x, GameManager.HEIGHT - y, safe);
        }
        return false;
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }

    public void setGame(MyGame game) {
        this.game = game;
    }

    public MyGame getGame() {
        return game;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public GameView getGameView() {
        return gameView;
    }
}
