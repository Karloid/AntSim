package com.krld.pathfinding.polygons;

/**
 * Created by Andrey on 5/17/2014.
 */
public interface PathCalcer {
    void setContext(PolygonsWorld polygonsWorld);

    void calcPath();
}
