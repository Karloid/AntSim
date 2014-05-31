package com.krld.cellid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Andrey on 5/30/2014.
 */
public class CellWorld {
    public static final int MAX_LATITUDE = 360;
    public static final int MAX_LONGITUDE = 180;
    //  public static final int MULTIPLAYER = 12;

    private final int[][] cells;
    private Worker worker;
    private boolean downloading;
    public static int multiplayer = 12;
    private int arrayWidth;
    private int arrayHeight;
    private long XOffset;
    private int YOffset;

    public CellWorld() {
        setArrayWidth(1000);
        setArrayHeight(700);
        setXOffset(0);
        setYOffset(0);
        cells = new int[getArrayWidth()][getArrayHeight()];

    }

    private int getCurrentLongitude() {
        return MAX_LONGITUDE * multiplayer;
    }

    private int getCurrentLatitude() {
        return MAX_LATITUDE * multiplayer;
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

    public long getXOffset() {
        return XOffset;
    }

    public void setXOffset(long XOffset) {
        this.XOffset = XOffset;
    }

    public void setYOffset(int YOffset) {
        this.YOffset = YOffset;
    }

    public int getYOffset() {
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
            BufferedReader br = null;
            long x = 0;
            long y = 0;
            String[] strs = null;
            String line = null;
            try {
                br = new BufferedReader(new FileReader("cell_towers.csv"));
                br.readLine();

                while (true) {
                    line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    strs = line.split(",");
                    x = Math.round(Double.valueOf(strs[4]) * getMultiplayer()) + 180 * getMultiplayer();
                    y = Math.round(Double.valueOf(strs[5]) * getMultiplayer()) + 90 * getMultiplayer();
                    if (x < getXOffset() || y < getYOffset() || x > 359 * getMultiplayer() || y > 179 * getMultiplayer()) {
                        continue;
                    }
                    cells[((int) x)][(MAX_LONGITUDE * getMultiplayer() - (int) y)]++;
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
            System.out.println("downloading end");
        }
    }
}
