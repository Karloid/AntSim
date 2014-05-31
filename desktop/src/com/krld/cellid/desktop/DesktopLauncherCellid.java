package com.krld.cellid.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.krld.cellid.CellIdView;
import com.krld.cellid.CellWorld;
import com.krld.pathfinding.ants.GameManager;

public class DesktopLauncherCellid {

    public static final int WIDTH = 1820;
    private static final int HEIGHT = 980;

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
/*        config.width = CellWorld.MAX_LATITUDE * CellWorld.getMultiplayer();
        config.height = CellWorld.MAX_LONGITUDE * cellWorld.getMultiplayer();*/

        config.width = WIDTH;
        config.height = HEIGHT;

        CellIdView listener = new CellIdView();
        new LwjglApplication(listener, config);
    }
}
