package com.krld.ant.model;

/**
 * Created by Andrey on 5/14/2014.
 */
public class AntAStarMoveBehaviour implements MoveBehaviour {
    private Ant ant;
    private MyGame context;

    @Override
    public void setAnt(Ant ant) {
        this.ant = ant;
    }

    @Override
    public void update() {
        System.out.println("AStar update");
    }

    @Override
    public void setContext(MyGame context) {
        this.context = context;
    }
}
