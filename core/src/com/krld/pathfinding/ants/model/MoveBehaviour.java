package com.krld.pathfinding.ants.model;

/**
 * Created by Andrey on 5/8/2014.
 */
public interface MoveBehaviour {
    void setAnt(Ant ant);

    void update();

    void setContext(MyGame context);
}
