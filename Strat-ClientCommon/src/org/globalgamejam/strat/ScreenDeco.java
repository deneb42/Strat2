package org.globalgamejam.strat;

import com.badlogic.gdx.Gdx;

public class ScreenDeco extends ScreenBg {
	public ScreenDeco(GameStrat g) {
		super(g, "bgDeco.png");
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.justTouched())
			game.setScreen(new ScreenCredits(game, this));
		super.render(delta);
	}
}
