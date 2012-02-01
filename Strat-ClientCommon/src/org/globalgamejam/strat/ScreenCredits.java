package org.globalgamejam.strat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class ScreenCredits extends ScreenBg {
	private Screen scrOld;
	public ScreenCredits(GameStrat g, Screen scr) {
		super(g, "bgEnd.png");
		scrOld = scr;
	}

	@Override
	public void render(float delta) {
		if (game.com.isConnected())
			game.setScreen(new ScreenPlay(game));
		if (Gdx.input.justTouched())
			game.setScreen(scrOld);
		super.render(delta);
	}
}
