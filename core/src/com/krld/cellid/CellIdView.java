package com.krld.cellid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrey on 5/30/2014.
 */
public class CellIdView extends ApplicationAdapter {
    public static final float MINIMAL_BRIGHT_COLOR = 0.3f;
    public static final int MINIMAL_GREY_LEVEL = 150;
    public static final float DECIMAL_PLACES = 10000;
    private SpriteBatch batch;
    private BitmapFont font;
    private Pixmap cellPixMap;
    private Texture cellPixMapTexture;
    private CellWorld cellWorld;
    private float xTexture;
    private float yTexture;
    public float deltaMoveDegree = 3;
    private boolean updatingCellPixMap;
    private boolean paintingRandomColor;
    private Map<Integer, Color> colorsByMcc;
    private Map<Integer, Color> colorsByMnc;
    private Map<Integer, Color> colorsByLac;
    private Texture bigVoronezh;
    private Pixmap pixmapVoronezhBig;
    private TextureRegion bigVoronezhTextureRegion;

    public ColorMode getColorMode() {
        return colorMode;
    }

    public void setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
    }

    private ColorMode colorMode;

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
        setPaintingRandomColor(true);
        setColorMode(ColorMode.GREY);
    }

    public void initGeneral() {
        if (cellWorld != null) {
            cellWorld.stopDownload();
        }
        cellWorld = new CellWorld();
        cellWorld.refreshCells();
        initCellPixMap();
        xTexture = 0 - cellPixMap.getWidth() / 2;
        yTexture = 0 - cellPixMap.getHeight() / 2;
        xTexture = 10;
        yTexture = 10;
    }

    private void initInputProcessor() {
        MyInputProcessor inputProcessor = new MyInputProcessor();
        inputProcessor.setView(this);
        Gdx.input.setInputProcessor(inputProcessor);

    }

    private void initCellPixMap() {
        bigVoronezh = new Texture(Gdx.files.internal("screenVoronezhBigMap.png"));
        bigVoronezhTextureRegion = new TextureRegion(bigVoronezh);
        TextureData textureData = bigVoronezh.getTextureData();
        textureData.prepare();
        pixmapVoronezhBig = textureData.consumePixmap();
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
       // batch.draw(bigVoronezhTextureRegion, 0, 0);
        drawWorld();
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond() + "; colorMode: " + getColorMode(), 10, getCellWorld().getArrayHeight() - 30);
        font.draw(batch, "xOffset: " + myRound(cellWorld.getXOffset()) +
                "; yOffset: " + myRound(cellWorld.getYOffset()) +
                "; latOffset: " + myRound(cellWorld.getXOffset() - 180) +
                "; longOffset: " + myRound(cellWorld.getYOffset() - 90), 10, getCellWorld().getArrayHeight() - 60);
        font.draw(batch, "multiply: " + cellWorld.getMultiplayer() + " deltaMove: " + deltaMoveDegree
                + " updarting pixMap: " + isUpdatingCellPixMap(), 10, getCellWorld().getArrayHeight() - 90);
        batch.end();
    }

    private String myRound(double number) {
        return (Math.round(number * DECIMAL_PLACES) / DECIMAL_PLACES) + "";
    }

    private void drawWorld() {
        if (isUpdatingCellPixMap()) {
            updateCellPixMap();
        }

        batch.draw(cellPixMapTexture, xTexture, yTexture);


    }

    private void updateCellPixMap() {
        cellPixMap.setColor(new Color(0, 0, 0, 0f));
        cellPixMap.fill();
        // cellPixMap.drawPixmap(pixmapVoronezhBig, 0, cellPixMap.getHeight() - pixmapVoronezhBig.getHeight());
        for (int x = 0; x < cellWorld.getArrayWidth(); x++)
            for (int y = 0; y < cellWorld.getArrayHeight(); y++) {

                Color pixColor = getPixelColor(x, y);
                if (pixColor != null) {
                    cellPixMap.setColor(pixColor);
                    cellPixMap.drawPixel(x, y);
                    try {
                     /*   cellPixMap.drawPixel(x + 1, y + 1);
                        cellPixMap.drawPixel(x - 1, y + 1);
                        cellPixMap.drawPixel(x - 1, y - 1);*/
                   /*     cellPixMap.drawPixel(x + 1, y - 1);
                        cellPixMap.drawPixel(x + 1, y + 2);
                        cellPixMap.drawPixel(x + 1, y + 3);
                        cellPixMap.drawPixel(x + 1, y + 4);
                        cellPixMap.drawPixel(x + 1, y + 5);
                        cellPixMap.drawPixel(x + 2, y + 2);
                        cellPixMap.drawPixel(x + 2, y + 3);
                        cellPixMap.drawPixel(x + 2, y + 4);
                        cellPixMap.drawPixel(x + 2, y + 5);    */
                    } catch (Exception e) {

                    }
                }
            }
        cellPixMapTexture.dispose();
        cellPixMapTexture = new Texture(cellPixMap, Pixmap.Format.RGB888, false);
    }

    private Color getPixelColor(int x, int y) {
        Color color = null;
        if (colorMode == ColorMode.GREY || colorMode == ColorMode.RANDOM_BLUE) {
            float value = cellWorld.getCells()[x][y][CellWorld.ARRAY_COUNT_STATION];
            if (value == 0) {
                return null;
            }
            if (value > 255) value = 255;
            if (value < MINIMAL_GREY_LEVEL && value != 0) value = MINIMAL_GREY_LEVEL;
            value = value / 255;
            if (colorMode == ColorMode.RANDOM_BLUE) {
                color = new Color(value * (float) Math.random() / 5, value * (float) Math.random(), value * (float) Math.random(), 1f);
            } else {
                color = new Color(value, value, value, 1f);
            }
        }
        if (colorMode == ColorMode.RANDOM_MCC) {
            if (cellWorld.getCells()[x][y][CellWorld.ARRAY_COUNT_STATION] == 0) {
                return null;
            }
            int value = cellWorld.getCells()[x][y][CellWorld.ARRAY_LAST_MCC];
            if (colorsByMcc == null) {
                colorsByMcc = new HashMap<Integer, Color>();
            }
            color = getColorFromMap(value, colorsByMcc);
        }
        if (colorMode == ColorMode.RANDOM_MNC) {
            if (cellWorld.getCells()[x][y][CellWorld.ARRAY_COUNT_STATION] == 0) {
                return null;
            }
            int value = cellWorld.getCells()[x][y][CellWorld.ARRAY_LAST_MNC];
            if (colorsByMnc == null) {
                colorsByMnc = new HashMap<Integer, Color>();
            }
            color = getColorFromMap(value, colorsByMnc);
        }

        if (colorMode == ColorMode.RANDOM_LAC) {
            if (cellWorld.getCells()[x][y][CellWorld.ARRAY_COUNT_STATION] == 0) {
                return null;
            }
            int value = cellWorld.getCells()[x][y][CellWorld.ARRAY_LAST_LAC];
            if (colorsByLac == null) {
                colorsByLac = new HashMap<Integer, Color>();
            }
            color = getColorFromMap(value, colorsByLac);
        }
        return color;
    }

    private Color getColorFromMap(int value, Map<Integer, Color> colorMap) {
        Color color;
        color = colorMap.get(value);
        if (color == null) {
            float r = 1f - (float) Math.random();
            float g = 1f - (float) Math.random();
            float b = 1f - (float) Math.random();
            if (r < MINIMAL_BRIGHT_COLOR) {
                r = MINIMAL_BRIGHT_COLOR + (float) Math.random() / 3;
            }
            if (g < MINIMAL_BRIGHT_COLOR) {
                g = MINIMAL_BRIGHT_COLOR + (float) Math.random() / 3;
            }
            if (b < MINIMAL_BRIGHT_COLOR) {
                b = MINIMAL_BRIGHT_COLOR + (float) Math.random() / 3;
            }
            color = new Color(r, g, b, 1f);
            colorMap.put(value, color);
        }
        return color;
    }


    public void moveOffsetCamera(Directions direction) {
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
        getCellWorld().refreshCells();
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

    public boolean isPaintingRandomColor() {
        return paintingRandomColor;
    }

    public void setPaintingRandomColor(boolean paintingRandomColor) {
        this.paintingRandomColor = paintingRandomColor;
    }
}
