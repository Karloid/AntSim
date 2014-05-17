package com.krld.pathfinding.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.krld.pathfinding.ants.GameManager;
import com.krld.pathfinding.ants.GameView;

public class DesktopLauncherAnts {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = GameManager.WIDTH;
        config.height = GameManager.HEIGHT;


        GameView listener = GameManager.getNewGameView();
        new LwjglApplication(listener, config);
	}
}
