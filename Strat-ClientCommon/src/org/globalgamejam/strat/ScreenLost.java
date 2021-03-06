package org.globalgamejam.strat;

import com.badlogic.gdx.Gdx;

public class ScreenLost extends ScreenBg {
	public ScreenLost(GameStrat g) {
		super(g, "bgLost.png");
	}

	@Override
	public void render(float delta) {
		if (game.com.isConnected())
			game.setScreen(new ScreenPlay(game));
		if (Gdx.input.justTouched())
			game.setScreen(new ScreenCredits(game, this));
		super.render(delta);
	}
}
