package org.globalgamejam.strat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CircleAvatar implements Touchable {
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

	private int whichAvatar(int xrel, int yrel) {
		int avatar = -1;
		for (int i = 0; i < avatars.length; i++) {
			if (avatars[i].hit(xrel, yrel)) {
				avatar = i;
				break;
			}
		}
		return avatar;
	}

	public boolean touchDown(int x, int y, TouchManager touchManager) {
		// Which avatar ?
		int avatar = whichAvatar(x - this.x, y - this.y);
		if (avatar == -1)
			return true; // No avatar touched

		// Drag launch
		Gdx.app.log("CircleAvartar", "Avartar " + avatar + " drag");
		touchManager.drag(new Drag(avatars[avatar]));
		return true;
	}

	public boolean touchUp(int x, int y, TouchManager touchManager,
			Dragable dragDrop) {
		// Which avatar ?
		int avatar = whichAvatar(x - this.x, y - this.y);
		if (avatar == -1)
			return true; // No avatar touched

		// Drop processing
		if (dragDrop instanceof BonusBar.Drag) {
			int bonusId = ((BonusBar.Drag) dragDrop).getId();
			Gdx.app.log("CircleAvartar", "Avartar " + avatar + " gets bonus "
					+ bonusId);
			game.com.useBonus(bonusId, game.com.getId(), avatar);
			return false;

		} else if (dragDrop instanceof CircleAvatar.Drag) {
			int avatarId = ((CircleAvatar.Drag) dragDrop).getId();
			Gdx.app.log("CircleAvartar", "Avartar " + avatar + " gets avatar "
					+ avatarId);
			if (avatar == 0 && avatarId != 0)
				game.com.stealStone(avatarId);
			else if (avatarId == 0 && avatar != 0)
				game.com.giveStone(avatarId);
			return false;
		}
		return true;
	}

	/**********************************************************************************************************/

	private class Avatar extends Sprite {
		private int xrel, yrel, id;

		public Avatar(Texture texture, int id, int i, int total) {
			super(new TextureRegion(texture, (((id + i) % total) % 3) * 128,
					(((id + i) % total) / 3) * 128, 128, 128));
			this.id = id + i;
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

		public boolean hit(int xrelhit, int yrelhit) {
			if (xrelhit < xrel + getOriginX() - getWidth() * getScaleX() / 2)
				return false;
			if (xrelhit > xrel + getOriginX() + getWidth() * getScaleX() / 2)
				return false;
			if (yrelhit < yrel + getOriginY() - getHeight() * getScaleY() / 2)
				return false;
			if (yrelhit > yrel + getOriginY() + getHeight() * getScaleY() / 2)
				return false;
			return true;
		}

		public int getId() {
			return id;
		}
	}

	/**********************************************************************************************************/

	public class Drag implements Dragable {
		private Sprite avatar;
		private int avatarId;

		public Drag(Avatar a) {
			avatarId = a.getId();
			avatar = new Sprite(a);
		}

		public int getId() {
			return avatarId;
		}

		public void drawDrag(SpriteBatch batch, int x, int y) {
			avatar.setPosition(x - 64, y - 64);
			avatar.draw(batch, 0.3f);
		}
	}
}
