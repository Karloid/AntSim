package com.krld.pathfinding.polygons;

import com.krld.pathfinding.polygons.model.PolygonsWorld;

/**
 * Created by Andrey on 5/17/2014.
 */
public interface PathCalcer {
    void setContext(PolygonsWorld polygonsWorld);

    void calcPath();
}
