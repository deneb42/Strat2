package org.globalgamejam.strat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenPlay extends ScreenBg {
	private GameStrat game;
	private StonesGauge stonesGauge;
	private ActionsGauge actionsGauge;
	private BonusBar bonusBar;
	private CircleAvatar circleAvatar;
	private TouchManager touchManager;

	public ScreenPlay(GameStrat g) {
		super(g, "bgPhone.png");
		game = g;

		stonesGauge = new StonesGauge(game);
		stonesGauge.setPosition(20, 40);

		actionsGauge = new ActionsGauge(game);
		actionsGauge.setPosition(0, Gdx.graphics.getHeight() - 32);

		bonusBar = new BonusBar(game);
		bonusBar.setPosition(Gdx.graphics.getWidth() - 74, 0);

		circleAvatar = new CircleAvatar(game);
		circleAvatar.setPosition(350, 160);

		touchManager = new TouchManager();
		touchManager.add(bonusBar);
		touchManager.add(circleAvatar);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		// Change screen to display status ?
		int status = game.com.getStatus();
		if (status == Communication.STATUS_WIN)
			game.setScreen(new ScreenWin(game));
		else if (status == Communication.STATUS_LOST)
			game.setScreen(new ScreenLost(game));
		else if (status == Communication.STATUS_DECO)
			game.setScreen(new ScreenDeco(game));
		if (status != Communication.STATUS_IN_PROGRESS)
			return;

		// Process touch interaction
		touchManager.touchInteraction();

		// Draw all
		SpriteBatch batch = game.batch;
		batch.begin();
		stonesGauge.draw(batch);
		actionsGauge.draw(batch);
		bonusBar.draw(batch);
		circleAvatar.draw(batch);
		touchManager.draw(batch);
		batch.end();
	}
}
