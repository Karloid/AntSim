package com.krld.cellid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 5/30/2014.
 */
public class CellWorld {
    public static final int MAX_LATITUDE = 360;
    public static final int MAX_LONGITUDE = 180;
    //  public static final int MULTIPLAYER = 12;

    private int[][] cells;
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
        stations = new ArrayList<Station>();
        try {
            br = new BufferedReader(new FileReader("cell_towers.csv"));
            System.out.println(br.readLine());

            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                strs = line.split(",");
                String cellId = strs[0];
                x = Double.valueOf(strs[4]);
                y = Double.valueOf(strs[5]);
                Station station = new Station(x, y, cellId);
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
        System.out.println("downloading from file to ram end");
    }

    public void initCells() {
        cells = new int[getArrayWidth()][getArrayHeight()];
    }

    public void runDownloadCells() {
        worker = new Worker();
        worker.start();

    }

    public int[][] getCells() {
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
                    cells[((int) (((int) x) - getXOffset()))][((int) ((getArrayHeight() - (int) y) - getYOffset()))]++;
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
