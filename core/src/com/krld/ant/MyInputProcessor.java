package com.krld.ant;

import com.badlogic.gdx.Input;
import com.krld.ant.model.AntAStarMoveBehaviour;
import com.krld.ant.model.AntMagicMoveBehaviour;
import com.krld.ant.model.MyGame;

/**
 * Created by Andrey on 5/8/2014.
 */
public class MyInputProcessor implements com.badlogic.gdx.InputProcessor {
    private MyGame game;
    private GameView gameView;
    private int mapValue;

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.SPACE) {
            game.setStopedUpdate(!game.isStopedUpdate());
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        if (c == '1') {
            System.out.println("Switch ant move behaviour to Ant move magic algorithm");
            game.switchAntsMoveBehaviour(MyGame.MAGIC_ANT_MOVE_BEHAVIOUR);
        } else if (c == '2') {
            System.out.println("Switch ant move behaviour to AStar");
            game.switchAntsMoveBehaviour(MyGame.ASTAR_ANT_MOVE_BEHAVIOUR);
        }
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (game.isStopedUpdate()) {
            if (button == Input.Buttons.LEFT) {
                mapValue = MyGame.NO_PASS_MAP;
            } else {
                mapValue = MyGame.PASS_MAP;
            }
            game.createObstacle(x, GameManager.HEIGHT - y, mapValue);
            return false;
        }
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
    public boolean touchDragged(int x, int y, int i3) {
        if (game.isStopedUpdate()) {
            game.createObstacle(x, GameManager.HEIGHT - y, mapValue);
            return false;
        }
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
