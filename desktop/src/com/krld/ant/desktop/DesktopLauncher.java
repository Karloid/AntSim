package com.krld.ant.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.krld.ant.GameManager;
import com.krld.ant.GameView;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = GameManager.WIDTH;
        config.height = GameManager.HEIGHT;


        GameView listener = GameManager.getNewGameView();
        new LwjglApplication(listener, config);
	}
}
