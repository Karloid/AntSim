package com.krld.pathfinding.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.krld.pathfinding.ants.GameManager;
import com.krld.pathfinding.ants.GameView;
import com.krld.pathfinding.polygons.GameManagerPolygons;
import com.krld.pathfinding.polygons.PolygonsView;

public class DesktopLauncherPolygons {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = GameManagerPolygons.WIDTH;
        config.height = GameManagerPolygons.HEIGHT;


        PolygonsView listener = GameManagerPolygons.getNewGameView();
        new LwjglApplication(listener, config);
	}
}
