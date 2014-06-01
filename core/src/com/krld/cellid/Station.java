package com.krld.cellid;

/**
 * Created by Andrey on 5/31/2014.
 */
public class Station {
    private final double x;
    private final double y;

    private final int cellId;
    private final int mobileCountryCode;
    private final int mobileNetworkCode;

    public int getLocalAreaCode() {
        return localAreaCode;
    }

    public int getMobileNetworkCode() {
        return mobileNetworkCode;
    }

    public int getMobileCountryCode() {
        return mobileCountryCode;
    }

    private final int localAreaCode;

    public int getCellId() {
        return cellId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Station(double x, double y, int cellId, int mobileCountryCode, int mobileNetworkCode, int localAreaCode) {
        this.x = x;
        this.y = y;
        this.cellId = cellId;
        this.mobileCountryCode = mobileCountryCode;
        this.mobileNetworkCode = mobileNetworkCode;
        this.localAreaCode = localAreaCode;
    }
}
