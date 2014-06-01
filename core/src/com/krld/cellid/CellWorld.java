package com.krld.cellid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrey on 5/30/2014.
 */
public class CellWorld {
    public static final int MAX_LATITUDE = 360;
    public static final int MAX_LONGITUDE = 180;
    private static final int COUNT_FIELDS = 5;
    public static final int ARRAY_COUNT_STATION = 0;
    public static final int ARRAY_LAST_MCC = 1;
    public static final int ARRAY_LAST_MNC = 2;
    public static final int ARRAY_LAST_LAC = 3;
    public static final int ARRAY_LAST_CELL_ID = 4;
    //  public static final int MULTIPLAYER = 12;

    private int[][][] cells;
    private Worker worker;
    private boolean downloading;
    public static int multiplayer = 1;
    private int arrayWidth;
    private int arrayHeight;
    private double XOffset;
    private double YOffset;
    private List<Station> stations;

    public CellWorld() {
        setArrayWidth(1800);
        setArrayHeight(900);
        setXOffset(0);
        setYOffset(0);
        downloadStationFromFile();
        initCells();

    }

    private void downloadStationFromFile() {
        downloading = true;
        BufferedReader br = null;
        double x = 0;
        double y = 0;
        String[] strs = null;
        String line = null;
        int cellId = 0;
        int mobileCountryCode = 0;
        int mobileNetworkCode = 0;
        int localAreaCode = 0;
        int countErrors = 0;
        stations = new LinkedList<Station>();
        try {
            br = new BufferedReader(new FileReader("cell_towers.csv"));
            System.out.println(br.readLine());
            System.out.println(br.readLine());

            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }

                strs = line.split(",");
                try {
                    mobileCountryCode = Integer.valueOf(strs[0]);
                    mobileNetworkCode = Integer.valueOf(strs[1]);
                    localAreaCode = Integer.valueOf(strs[2]);
                    cellId = Integer.valueOf(strs[3]);
                } catch (Exception e) {
                    countErrors++;
                    /*System.out.println("error parse: ");
                    e.printStackTrace();*/
                }
                x = Double.valueOf(strs[4]);
                y = Double.valueOf(strs[5]);
                Station station = new Station(x, y, cellId, mobileCountryCode, mobileNetworkCode, localAreaCode);
                stations.add(station);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        downloading = false;
        System.out.println("downloading from file to ram end. error parse count: " + countErrors);
    }

    public void initCells() {
        cells = new int[getArrayWidth()][getArrayHeight()][COUNT_FIELDS];
    }

    public void refreshCells() {
        worker = new Worker();
        worker.start();

    }

    public int[][][] getCells() {
        return cells;
    }

    public boolean isDownloadingCells() {
        return downloading;
    }

    public int getMultiplayer() {
        return multiplayer;
    }

    public void stopDownload() {
        worker.interrupt();
    }

    public int getArrayWidth() {
        return arrayWidth;
    }

    public void setArrayWidth(int arrayWidth) {
        this.arrayWidth = arrayWidth;
    }

    public int getArrayHeight() {
        return arrayHeight;
    }

    public void setArrayHeight(int arrayHeight) {
        this.arrayHeight = arrayHeight;
    }

    public double getXOffset() {
        return XOffset;
    }

    public void setXOffset(double XOffset) {
        this.XOffset = XOffset;
    }

    public void setYOffset(double YOffset) {
        this.YOffset = YOffset;
    }

    public double getYOffset() {
        return YOffset;
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            try {
                downloadCells();
            } catch (Exception e) {
                System.out.println("stop download");
            }
        }

        private void downloadCells() {
            downloading = true;
            long x = 0;
            long y = 0;
            for (Station station : stations) {

                x = Math.round(station.getX() * getMultiplayer() - getXOffset() * getMultiplayer()) + 180 * getMultiplayer();
                y = Math.round(station.getY() * getMultiplayer() - getYOffset() * getMultiplayer()) + 90 * getMultiplayer();

                try {
                    int indexX = (int) (((int) x) - getXOffset());
                    int indexY = (int) ((getArrayHeight() - (int) y) - getYOffset());
                    cells[indexX][indexY][ARRAY_COUNT_STATION]++;
                    cells[indexX][indexY][ARRAY_LAST_MCC] = station.getMobileCountryCode();
                    cells[indexX][indexY][ARRAY_LAST_MNC] = station.getMobileNetworkCode();
                    cells[indexX][indexY][ARRAY_LAST_LAC] = station.getLocalAreaCode();
                    cells[indexX][indexY][ARRAY_LAST_CELL_ID] = station.getCellId();
                } catch (Exception e) {
                    //   e.printStackTrace();
                }
                if (Thread.interrupted()) {
                    break;
                }
            }
            downloading = false;
            System.out.println("downloading from ram to array end");
        }
    }
}
