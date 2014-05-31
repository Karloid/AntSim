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

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        font.setColor(Color.WHITE);
        font.scale(0.01f);
        initInputProcessor();
        initGeneral();
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
    }

    private void initInputProcessor() {
        MyInputProcessor inputProcessor = new MyInputProcessor();
        inputProcessor.setView(this);
        Gdx.input.setInputProcessor(inputProcessor);

    }

    private void initCellPixMap() {
        cellPixMap = new Pixmap(CellWorld.MAX_LATITUDE * cellWorld.getMultiplayer(), CellWorld.MAX_LONGITUDE * cellWorld.getMultiplayer(), Pixmap.Format.RGBA8888);
        cellPixMap.setColor(Color.WHITE);
        cellPixMap.fillRectangle(0, 0, CellWorld.MAX_LATITUDE * cellWorld.getMultiplayer(), CellWorld.MAX_LONGITUDE * cellWorld.getMultiplayer());
        cellPixMapTexture = new Texture(cellPixMap, Pixmap.Format.RGB888, false);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        drawWorld();
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 10, CellWorld.MAX_LONGITUDE * cellWorld.getMultiplayer() - 300);
        batch.end();
    }

    private void drawWorld() {
        if (cellWorld.isDownloadingCells()) {
            updateCellPixMap();
        }

        batch.draw(cellPixMapTexture, xTexture, yTexture);


    }

    private void updateCellPixMap() {
        for (int x = 0; x < CellWorld.MAX_LATITUDE * cellWorld.getMultiplayer(); x++)
            for (int y = 0; y < CellWorld.MAX_LONGITUDE * cellWorld.getMultiplayer(); y++) {
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
        if (direction == Directions.UP) {
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
        }
    }

    public CellWorld getCellWorld() {
        return cellWorld;
    }
}
