package com.krld.pathfinding.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.krld.pathfinding.ants.GameManager;
import com.krld.pathfinding.ants.GameView;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        GameView gameView = GameManager.getNewGameView();
        initialize(gameView, config);
	}
}
