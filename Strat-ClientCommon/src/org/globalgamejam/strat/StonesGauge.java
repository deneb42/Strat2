package org.globalgamejam.strat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StonesGauge {
	private final static int MAX_LEVEL = 10;
	private final static int MARGIN = 5;
	private GameStrat game;
	private Sprite empty, full;
	private int x, y;

	public StonesGauge(GameStrat g) {
		game = g;

		// Assign textures
		Texture texture = new Texture("img/blockbar.png");
		int id = game.com.getId();
		full = new Sprite(new TextureRegion(texture, (id % 4) * 64,
				(id / 4) * 32, 64, 32));
		empty = new Sprite(new TextureRegion(texture, 128, 32, 64, 32));

		// Initial position
		x = 0;
		y = 0;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(SpriteBatch batch) {
		int level = game.com.getStones();
		int i = 0;
		// Draw highlighted stones
		while (i < level) {
			full.setPosition(x, y + i * (full.getHeight() + MARGIN));
			full.draw(batch);
			i++;
		}
		// Draw dark stones
		while (i < MAX_LEVEL) {
			empty.setPosition(x, y + i * (empty.getHeight() + MARGIN));
			empty.draw(batch);
			i++;
		}
	}

}
