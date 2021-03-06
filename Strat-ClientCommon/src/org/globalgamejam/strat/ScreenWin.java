package org.globalgamejam.strat;

import com.badlogic.gdx.Gdx;

public class ScreenWin extends ScreenBg {
	public ScreenWin(GameStrat g) {
		super(g, "bgWin.png");
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
