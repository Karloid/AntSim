package com.krld.ant;

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
    public boolean touchDown(int i, int i2, int i3, int i4) {
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
