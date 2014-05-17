package com.krld.pathfinding.polygons;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Andrey on 5/17/2014.
 */
public class PolygonInputProcessor implements InputProcessor {
    private PolygonsWorld context;
    private PolygonsView polygonView;

    public PolygonInputProcessor() {
        state = InputState.ADD_POINTS_TO_OBSTACLES;
    }

    private InputState state;

    @Override
    public boolean keyDown(int c) {
        if (c == Input.Keys.NUM_1) {
            state = InputState.ADD_POINTS_TO_OBSTACLES;
            context.cleanLinks();
            context.cleanPathCalcer();
        } else if (c == Input.Keys.NUM_2) {
            state = InputState.ADD_START_OR_END_POINTS;
            context.cleanLinks();
            context.cleanPathCalcer();
        } else if (c == Input.Keys.NUM_3) {
            state = InputState.CALC_VIEW_GRAPH;
            context.calcViewGraph();
        }
        if (c == Input.Keys.F1) {
            polygonView.setShowHelp(!polygonView.isShowHelp());
        }
        if (state == InputState.ADD_POINTS_TO_OBSTACLES) {
            addPointsToObstaclesKeyDown(c);
        }
        return false;
    }

    private void addPointsToObstaclesKeyDown(int c) {
        if (c == Input.Keys.LEFT || c == Input.Keys.TAB) {
            context.previusCurrentObstacle();
        } else if (c == Input.Keys.RIGHT || c == Input.Keys.SPACE) {
            context.nextCurrentObstacle();
        }
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
    public boolean touchDown(int x, int y, int pointner, int button) {
        if (state == InputState.ADD_POINTS_TO_OBSTACLES) {
            addPointsToObstaclesTouchDown(x, y, button);
        } else if (state == InputState.ADD_START_OR_END_POINTS) {
            addStartOrEndPointTouchDown(x, y, button);
        }
        return false;
    }

    private void addPointsToObstaclesTouchDown(int x, int y, int button) {
        if (button == Input.Buttons.LEFT) {
            context.addNewPointToCurrentObstacle(x, context.getHeight() - y);
        } else if (button == Input.Buttons.RIGHT) {
            context.removeLastPointFromCurrentObstacle();
        }

    }

    private void addStartOrEndPointTouchDown(int x, int y, int button) {
        if (button == Input.Buttons.LEFT) {
            context.addNewStartPoint(x, context.getHeight() - y);
        } else if (button == Input.Buttons.RIGHT) {
            context.addNewEndPoint(x, context.getHeight() - y);
        }
        context.calcViewGraph();
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

    public void setContext(PolygonsWorld context) {
        this.context = context;
    }

    public PolygonsWorld getContext() {
        return context;
    }

    public void setPolygonView(PolygonsView polygonView) {
        this.polygonView = polygonView;
    }

    public PolygonsView getPolygonView() {
        return polygonView;
    }

    public InputState getState() {
        return state;
    }
}
