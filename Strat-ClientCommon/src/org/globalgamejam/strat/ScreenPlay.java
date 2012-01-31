package org.globalgamejam.strat;

import com.badlogic.gdx.Gdx;

public class ScreenPlay extends ScreenBg {
	public ScreenPlay(GameStrat g) {
		super(g, "bgPhone.png");
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (Gdx.input.justTouched())
			touchInteraction();
	}

	private void touchInteraction() {
		if (Gdx.input.getX() < 100)
			Gdx.app.log("Touch", "Touch");
	}
}
