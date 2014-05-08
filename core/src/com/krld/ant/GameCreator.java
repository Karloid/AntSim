package com.krld.ant;

/**
 * Created by Andrey on 5/8/2014.
 */
public class GameCreator {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 780;

    public static GameView getNewAntView() {
        GameView gameView = new GameView();
        MyGame game = new MyGame();
        gameView.setGame(game);
        gameView.setInputProcessor(game.getInputProcessor());
        gameView.initInputProcessor();
        game.getInputProcessor().setGameView(gameView);
        return gameView;
    }
}
