package org.globalgamejam.strat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class ScreenBg implements Screen {
	protected GameStrat game;
	private Sprite bg;
	private static boolean viewedDisconnect = false;

	public ScreenBg(GameStrat g, String bgimg) {
		game = g;
		bg = new Sprite(new Texture("img/" + bgimg));
	}

	public void render(float delta) {
		// Clear screen
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (game.com.getStatus() == Communication.STATUS_DECO && !viewedDisconnect) {
			viewedDisconnect = true;
			game.setScreen(new ScreenDeco(game));
		}

		// Draw background
		game.batch.begin();
		bg.draw(game.batch);
		game.batch.end();
	}

	public void resize(int width, int height) {
	}

	public void show() {
	}

	public void hide() {
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
	}
}
