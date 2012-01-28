package org.globalgamejam.strat;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameRenderer implements ApplicationListener {

	private SpriteBatch batch;
	private Sprite[] bonus, avatars, lifeBar, blockBar;
	Texture texAvatar, texBonus, texLifeBar, texBlockBar;
	private static final int NB_JOUEURS = 6, NB_BONUS = 5,
			NB_SPRITE_LIFEBAR = 2, NB_SPRITE_BLOCKBAR = 7;
	private static final int NB_BLOCK_MAX = 10;
	private static int[] posiX = { -84, +128, +64, -80, -228, -292 }, posiY = {
			-184, -248, -372, -460, -372, -248 };
	public static int halfW, h;
	public static String PATH_IMG = "img/";

	private Communication com;

	public GameRenderer(String host, int port) {
		com = new Communication(host, port);
		com.start();
	}

	public void create() {
		// TODO Auto-generated method stub
		halfW = Gdx.graphics.getWidth() / 2;
		h = Gdx.graphics.getHeight();

		batch = new SpriteBatch();

		texAvatar = new Texture(PATH_IMG + "avatar.png");
		texBonus = new Texture(PATH_IMG + "bonus.png");
		texLifeBar = new Texture(PATH_IMG + "lifebar.png");
		texBlockBar = new Texture(PATH_IMG + "blockbar.png");

		loadTextures();

		for (int i = 0; i < 6; i++) {
			posiX[i] += halfW;
			posiY[i] += h;
		}
	}

	public void render() {
		int nbPa = com.getActions();
		int nbBlock = com.getStones();
		int id = com.getId();
		int i;

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin();
		/* LIFEBAR */
		for (i = 0; i < nbPa - 1; i++) {
			lifeBar[0].setPosition(i * (lifeBar[0].getWidth()),
					Gdx.graphics.getHeight() - lifeBar[0].getHeight());
			lifeBar[0].draw(batch);
		}
		lifeBar[1].setPosition((nbPa - 1) * (lifeBar[1].getWidth()),
				Gdx.graphics.getHeight() - lifeBar[1].getHeight());
		lifeBar[1].draw(batch);
		/* LIFEBAR */

		/* BLOCKS */
		for (i = 0; i < nbBlock; i++) {
			blockBar[id]
					.setPosition(10, 5 + i * (blockBar[id].getHeight() + 5));
			blockBar[id].draw(batch);
		}
		for (; i < NB_BLOCK_MAX; i++) {
			blockBar[0].setPosition(10, 5 + i * (blockBar[0].getHeight() + 5));
			blockBar[0].draw(batch);
		}
		/* FIN BLOCK */

		avatars[id].setPosition(posiX[0], posiY[0]);
		avatars[id].draw(batch);
		for (i = 1; i < NB_JOUEURS; i++) {
			avatars[(id + i) % NB_JOUEURS].setPosition(posiX[i], posiY[i]);
			avatars[(id + i) % NB_JOUEURS].setScale(0.8f);
			avatars[(id + i) % NB_JOUEURS].draw(batch);
		}

		batch.end();

	}

	private void loadTextures() {
		bonus = new Sprite[NB_BONUS];
		avatars = new Sprite[NB_JOUEURS];
		lifeBar = new Sprite[NB_SPRITE_LIFEBAR];
		blockBar = new Sprite[NB_SPRITE_BLOCKBAR];

		for (int i = 0; i < NB_JOUEURS; i++)
			avatars[i] = new Sprite(new TextureRegion(texAvatar, (i % 4) * 128,
					(i / 4) * 128, 128, 128));

		for (int i = 0; i < NB_BONUS; i++)
			bonus[i] = new Sprite(new TextureRegion(texBonus, (i % 2) * 64,
					(i / 2) * 64, 64, 64));

		for (int i = 0; i < NB_SPRITE_LIFEBAR; i++)
			lifeBar[i] = new Sprite(new TextureRegion(texLifeBar, i * 64, 0,
					64, 32));

		for (int i = 0; i < NB_SPRITE_BLOCKBAR; i++)
			blockBar[i] = new Sprite(new TextureRegion(texBlockBar,
					(i % 4) * 64, (i / 4) * 32, 64, 32));
	}

	public void dispose() {
	}

	public void pause() {
	}

	public void resize(int arg0, int arg1) {
	}

	public void resume() {
		render();
	}
}
