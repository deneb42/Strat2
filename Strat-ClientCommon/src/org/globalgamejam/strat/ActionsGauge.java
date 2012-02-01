package org.globalgamejam.strat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ActionsGauge {
	private GameStrat game;
	private Sprite spriteNormal, spriteEnd;
	private int x, y;

	public ActionsGauge(GameStrat g) {
		game = g;

		// Assign textures
		Texture texture = new Texture("img/lifebar.png");
		spriteNormal = new Sprite(new TextureRegion(texture, 0, 0, 64, 32));
		spriteEnd = new Sprite(new TextureRegion(texture, 64, 0, 64, 32));

		// Initial position
		x = 0;
		y = 0;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(SpriteBatch batch) {
		int level = game.com.getActions();
		int i = 0;
		// Draw highlighted stones
		while (i < level - 1) {
			spriteNormal.setPosition(x + i * spriteNormal.getWidth(), y);
			spriteNormal.draw(batch);
			i++;
		}
		if (level > 0) {
			spriteEnd.setPosition(x + i * spriteNormal.getWidth(), y);
			spriteEnd.draw(batch);
		}
	}
}
