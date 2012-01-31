package org.globalgamejam.strat;

public class ScreenWait extends ScreenBg {
	public ScreenWait(GameStrat g) {
		super(g, "bgWait.png");
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (game.com.isConnected())
			game.setScreen(new ScreenPlay(game));
	}
}
