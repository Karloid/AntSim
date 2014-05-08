package com.krld.ant.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.krld.ant.GameView;
import com.krld.ant.GameCreator;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = GameCreator.WIDTH;
        config.height = GameCreator.HEIGHT;


        GameView listener = GameCreator.getNewAntView();
        new LwjglApplication(listener, config);
	}
}
