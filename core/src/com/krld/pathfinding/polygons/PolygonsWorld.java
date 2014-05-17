package com.krld.pathfinding.polygons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.krld.pathfinding.ants.MyInputProcessor;

/**
 * Created by Andrey on 5/17/2014.
 */
public class PolygonsWorld {
    private MyInputProcessor inputProcessor;
    private int height;

    public PolygonsWorld(int width, int height) {

    }

    public void runGameLoop() {

    }

    public void draw(SpriteBatch batch) {

    }

    public void initMap() {

    }

    public MyInputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void setInputProcessor(MyInputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
