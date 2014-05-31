package com.krld.cellid;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Andrey on 5/30/2014.
 */
public class MyInputProcessor implements InputProcessor {
    private CellIdView view;

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.UP) {
            view.moveCamera(Directions.UP);
        }
        if (i == Input.Keys.LEFT) {
            view.moveCamera(Directions.LEFT);
        }
        if (i == Input.Keys.RIGHT) {
            view.moveCamera(Directions.RIGHT);
        }
        if (i == Input.Keys.DOWN) {
            view.moveCamera(Directions.DOWN);
        }
        if (i == Input.Keys.SPACE) {
            CellWorld.multiplayer = Math.round(CellWorld.multiplayer * 1.5f);
            view.initGeneral();
        }
        if (i == Input.Keys.ALT_RIGHT) {
            CellWorld.multiplayer = Math.round(CellWorld.multiplayer / 1.5f);
            view.initGeneral();
        }


        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
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

    public void setView(CellIdView view) {
        this.view = view;
    }

    public CellIdView getView() {
        return view;
    }
}
