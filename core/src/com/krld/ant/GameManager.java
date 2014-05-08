package com.krld.ant;

import com.krld.ant.model.MyGame;

/**
 * Created by Andrey on 5/8/2014.
 */
public class GameManager {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 780;

    public static GameView getNewGameView() {
        GameView gameView = new GameView();
        MyGame game = new MyGame();
        game.setWidth(WIDTH);
        game.setHeight(HEIGHT);
        game.initMap();
        gameView.setGame(game);
        return gameView;
    }
}
