package com.krld.pathfinding.ants;

import com.krld.pathfinding.ants.model.MyGame;

/**
 * Created by Andrey on 5/8/2014.
 */
public class GameManager {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static GameView getNewGameView() {
        GameView gameView = new GameView();
        MyGame game = new MyGame(WIDTH, HEIGHT);
        game.initMap();
        gameView.setGame(game);
        return gameView;
    }
}
