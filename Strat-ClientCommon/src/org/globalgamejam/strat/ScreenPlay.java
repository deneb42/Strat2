package org.globalgamejam.strat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenPlay extends ScreenBg {
	private GameStrat game;
	private StonesGauge stonesGauge;
	private ActionsGauge actionsGauge;

	public ScreenPlay(GameStrat g) {
		super(g, "bgPhone.png");
		game = g;

		stonesGauge = new StonesGauge(game);
		stonesGauge.setPosition(20, 40);
		
		actionsGauge = new ActionsGauge(game);
		actionsGauge.setPosition(0, Gdx.graphics.getHeight() - 32);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		if (Gdx.input.justTouched())
			touchInteraction();

		SpriteBatch batch = game.batch;
		batch.begin();
		stonesGauge.draw(batch);
		actionsGauge.draw(batch);
		batch.end();
	}

	private void touchInteraction() {
		if (Gdx.input.getX() < 100)
			Gdx.app.log("Touch", "Touch");
	}
}
