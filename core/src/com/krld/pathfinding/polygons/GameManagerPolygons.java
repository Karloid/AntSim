package com.krld.pathfinding.polygons;

import com.krld.pathfinding.ants.GameView;
import com.krld.pathfinding.ants.model.MyGame;

/**
 * Created by Andrey on 5/17/2014.
 */
public class GameManagerPolygons {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static PolygonsView getNewGameView() {
        PolygonsView polygonsView = new PolygonsView();
        PolygonsWorld polygonWorld = new PolygonsWorld(WIDTH, HEIGHT);
        polygonWorld.initMap();
        polygonsView.setPolygonsWorld(polygonWorld);
        return polygonsView;

    }
}
