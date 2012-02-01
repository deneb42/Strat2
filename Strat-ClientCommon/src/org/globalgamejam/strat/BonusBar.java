package org.globalgamejam.strat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BonusBar implements Touchable {
	// Parameters
	private static final int MAX_BONUS = 5;
	private static final int MARGIN = 10;

	// Internal data
	private GameStrat game;
	private Sprite[] bonus;
	private int x, y, bonusPosition, bonusOld, bonusTakenId;

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
		bonusPosition = -100;
		bonusTakenId = -1;
		bonusOld = -1;
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
			if (bonusId != bonusOld) {
				bonusPosition = (int) ((Math.random() * (Gdx.graphics
						.getHeight() - 64 - 2 * MARGIN))) + MARGIN;
				bonusOld = bonusId;
			}

			// Draw
			if (bonusTakenId != bonusId) {
				bonus[bonusId].setPosition(x, y + bonusPosition);
				bonus[bonusId].draw(batch);
			}
		} else {
			bonusOld = -1;
			bonusTakenId = -1;
		}
	}

	public boolean touchDown(int x, int y, TouchManager touch) {
		// If don't hit, return
		if (x < this.x)
			return true;
		if (y > this.y + bonusPosition + 64)
			return true;
		if (y < this.y + bonusPosition)
			return true;
		if (x > this.x + 64)
			return true;

		// It is hit, launch drag
		int bonusId = game.com.getBonus();
		if (bonusId >= 0 && bonusId < bonus.length) {
			Gdx.app.log("BonusBar", "Bonus " + bonusId + " drag");
			touch.drag(new Drag(bonusId));
			bonusTakenId = bonusId;
		}
		return false;
	}

	public boolean touchUp(int x, int y, TouchManager touch, Dragable dragDrop) {
		return true; // Don't care, not handled
	}

	/**********************************************************************************************************/

	public class Drag implements Dragable {
		private int bonusId;

		public Drag(int id) {
			bonusId = id;
		}

		public int getId() {
			return bonusId;
		}

		public void drawDrag(SpriteBatch batch, int x, int y) {
			bonus[bonusId].setPosition(x - 32, y - 32);
			bonus[bonusId].draw(batch, 0.3f);
		}
	}
}
