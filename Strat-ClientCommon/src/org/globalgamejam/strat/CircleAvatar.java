package org.globalgamejam.strat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CircleAvatar {
	private static final int RADIUS = 160;
	private GameStrat game;
	private Avatar[] avatars;
	private int x, y;

	public CircleAvatar(GameStrat g) {
		game = g;

		// Assign textures
		Texture texture = new Texture("img/avatar.png");
		avatars = new Avatar[game.com.getTotalId()];
		for (int i = 0; i < avatars.length; i++)
			avatars[i] = new Avatar(texture, game.com.getId(), i,
					avatars.length);

		// Initial position
		x = 0;
		y = 0;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(SpriteBatch batch) {
		for (int i = 0; i < avatars.length; i++)
			avatars[i].draw(batch);
	}

	/**********************************************************************************************************/

	private class Avatar extends Sprite {
		private int xrel, yrel, id;

		public Avatar(Texture texture, int id, int i, int total) {
			super(new TextureRegion(texture, ((id + i) % 3) * 128,
					((id + i) / 3) * 128, 128, 128));
			this.id = id;
			xrel = (int) (x + 1.3f * RADIUS * Math.sin(2 * i * Math.PI / total));
			yrel = (int) (y + RADIUS * Math.cos(2 * i * Math.PI / total));
			if (i != 0)
				setScale(0.8f);
			else
				yrel -= 40;
		}

		@Override
		public void draw(SpriteBatch spriteBatch) {
			setPosition(x + xrel, y + yrel);
			super.draw(spriteBatch);
		}
	}
}
