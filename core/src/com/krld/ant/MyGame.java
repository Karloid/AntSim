package com.krld.ant;

import com.badlogic.gdx.InputProcessor;

/**
 * Created by Andrey on 5/8/2014.
 */
public class MyGame {
    private MyInputProcessor inputProcessor;


    public MyGame() {
        inputProcessor = new MyInputProcessor();
        inputProcessor.setGame(this);
    }

    public MyInputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void setInputProcessor(MyInputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }
}
