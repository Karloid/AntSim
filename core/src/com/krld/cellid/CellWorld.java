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

    public CellWorld() {
        cells = new int[getCurrentLatitude()][getCurrentLongitude()];

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

    private class Worker extends Thread {
        @Override
        public void run() {
            try {
                downloadCells();
            }catch ( Exception e) {
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
                    if (x < 0 || y < 0 || x > 359 * getMultiplayer() || y > 179 * getMultiplayer()) {
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
