package com.krld.cellid;

/**
 * Created by Andrey on 5/31/2014.
 */
public class Station {
    private final double x;
    private final double y;

    public String getCellId() {
        return cellId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    private final String cellId;

    public Station(double x, double y, String cellId) {
           this.x = x;
           this.y = y;
           this.cellId = cellId;
    }
}
