package com.krld.cellid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Andrey on 5/30/2014.
 */
public class CellIdView extends ApplicationAdapter {
    private SpriteBatch batch;
    private BitmapFont font;
    private Pixmap cellPixMap;
    private Texture cellPixMapTexture;
    private CellWorld cellWorld;
    private float xTexture;
    private float yTexture;
    public float deltaMoveDegree = 3;
    private boolean updatingCellPixMap;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        font.setColor(Color.WHITE);
        font.scale(0.01f);
        initInputProcessor();
        initGeneral();
        setUpdatingCellPixMap(true);
    }

    public void initGeneral() {
        if (cellWorld != null) {
            cellWorld.stopDownload();
        }
        cellWorld = new CellWorld();
        cellWorld.runDownloadCells();
        initCellPixMap();
        xTexture = 0 - cellPixMap.getWidth() / 2;
        yTexture = 0 - cellPixMap.getHeight() / 2;
        xTexture = 0;
        yTexture = 0;
    }

    private void initInputProcessor() {
        MyInputProcessor inputProcessor = new MyInputProcessor();
        inputProcessor.setView(this);
        Gdx.input.setInputProcessor(inputProcessor);

    }

    private void initCellPixMap() {
        cellPixMap = new Pixmap(cellWorld.getArrayWidth(), cellWorld.getArrayHeight(), Pixmap.Format.RGBA8888);
        cellPixMap.setColor(Color.WHITE);
        cellPixMap.fillRectangle(0, 0, cellWorld.getArrayWidth(), cellWorld.getArrayHeight());
        cellPixMapTexture = new Texture(cellPixMap, Pixmap.Format.RGB888, false);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        drawWorld();
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 10, 1000 - 100);
        font.draw(batch, "xOffset: " + cellWorld.getXOffset() + "; yOffset: " + cellWorld.getYOffset(), 10, 1000 - 200);
        font.draw(batch, "multiply: " + cellWorld.getMultiplayer() + " deltaZoom: " + deltaMoveDegree
                + " updarting pixMap: " + isUpdatingCellPixMap(), 10, 1000 - 300);
        batch.end();
    }

    private void drawWorld() {
        if (isUpdatingCellPixMap()) {
            updateCellPixMap();
        }

        batch.draw(cellPixMapTexture, xTexture, yTexture);


    }

    private void updateCellPixMap() {
        for (int x = 0; x < cellWorld.getArrayWidth(); x++)
            for (int y = 0; y < cellWorld.getArrayHeight(); y++) {
                float value = cellWorld.getCells()[x][y];
                if (value > 255) value = 255;
                if (value < 100 && value != 0) value = 100;
                value = value / 255;

                cellPixMap.setColor(new Color(value, value, value, 1f));
                cellPixMap.drawPixel(x, y);
            }
        cellPixMapTexture.dispose();
        cellPixMapTexture = new Texture(cellPixMap, Pixmap.Format.RGB888, false);
    }

    public void moveCamera(Directions direction) {
  /*      if (direction == Directions.UP) {
            yTexture -= 100;
        }
        if (direction == Directions.DOWN) {
            yTexture += 100;
        }
        if (direction == Directions.RIGHT) {
            xTexture -= 100;
        }
        if (direction == Directions.LEFT) {
            xTexture += 100;
        }*/
        if (direction == Directions.UP) {

            cellWorld.setYOffset(cellWorld.getYOffset() + deltaMoveDegree);
        }
        if (direction == Directions.DOWN) {
            cellWorld.setYOffset(cellWorld.getYOffset() - deltaMoveDegree);
        }
        if (direction == Directions.RIGHT) {
            cellWorld.setXOffset(cellWorld.getXOffset() + deltaMoveDegree);
        }
        if (direction == Directions.LEFT) {
            cellWorld.setXOffset(cellWorld.getXOffset() - deltaMoveDegree);
        }
        getCellWorld().initCells();
        getCellWorld().stopDownload();
        getCellWorld().runDownloadCells();
    }

    public CellWorld getCellWorld() {
        return cellWorld;
    }

    public boolean isUpdatingCellPixMap() {
        return updatingCellPixMap;
    }

    public void setUpdatingCellPixMap(boolean updatingCellPixMap) {
        this.updatingCellPixMap = updatingCellPixMap;
    }
}
