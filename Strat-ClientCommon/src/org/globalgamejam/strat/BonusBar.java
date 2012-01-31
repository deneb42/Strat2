package org.globalgamejam.strat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BonusBar {
	private static final int MAX_BONUS = 5;
	private static final int MARGIN = 10;
	private GameStrat game;
	private Sprite[] bonus;
	private int x, y, bonusposition;

	public BonusBar(GameStrat g) {
		game = g;

		// Assign textures
		Texture texture = new Texture("img/bonus.png");
		bonus = new Sprite[MAX_BONUS];
		for (int i = 0; i < MAX_BONUS; i++)
			bonus[i] = new Sprite(new TextureRegion(texture, (i % 2) * 64,
					(i / 2) * 64, 64, 64));

		// Initial position
		x = 0;
		y = 0;
		bonusposition = -100;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(SpriteBatch batch) {
		int bonusId = game.com.getBonus();
		// If bonus available
		if (bonusId >= 0 && bonusId < bonus.length) {
			// Random position
			if (bonusposition < 0)
				bonusposition = (int) ((Math.random() * (Gdx.graphics
						.getHeight() - 64 - 2 * MARGIN))) + MARGIN;
			// Draw
			bonus[bonusId].setPosition(x, y + bonusposition);
			bonus[bonusId].draw(batch);
		} else
			// Special position to force random new one for next bonus
			bonusposition = -100;
	}
}
