package org.globalgamejam.strat;

import java.io.IOException;

import android.util.Log;

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
	private Sprite bg;
	Texture texAvatar, texBonus, texLifeBar, texBlockBar;
	private static final int NB_JOUEURS = 7, NB_BONUS = 5,
			NB_SPRITE_LIFEBAR = 2, NB_SPRITE_BLOCKBAR = 7;
	private static final int NB_BLOCK_MAX = 10;
	private static int[] posiX = { -84, +128, +64, -80, -228, -292 }, posiY = {
			-184, -248, -372, -460, -372, -248 };
	public static int halfW, h;
	private boolean firstLaunch = true;
	public static String PATH_IMG = "img/";

	private Communication com;
	
	//Interface
		private int selected = -1;
		

	public GameRenderer(String host, int port) throws IOException {
		com = new Communication(host, port);
		com.start();
	}

	public void create() {
		
		halfW = Gdx.graphics.getWidth() / 2;
		h = Gdx.graphics.getHeight(); // helps the placement of the avatars

		batch = new SpriteBatch();

		texAvatar = new Texture(PATH_IMG + "avatar.png");
		texBonus = new Texture(PATH_IMG + "bonus.png");
		texLifeBar = new Texture(PATH_IMG + "lifebar.png");
		texBlockBar = new Texture(PATH_IMG + "blockbar.png");
		bg = new Sprite(new Texture(PATH_IMG + "bgPhone.png"));

		loadTextures();
		
		//if(firstLaunch)
		{
			for (int i = 0; i < 6; i++) {
				posiX[i] += halfW;
				posiY[i] += h;
			}
			//firstLaunch=false;
		}
	}

	public void render() {
		int nbPa = com.getActions();
		int nbBlock = com.getStones();
		int id = com.getId();
		int i;
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if (!com.isConnected()) return;

		if(com.getStatus()!=0) // beark caca !
			return;
		
		
		if(Gdx.input.isTouched())
		{
			int x = Gdx.input.getX(), y = Gdx.input.getY();
			int select = -1;
			//Log.i("colision", "collision au " + "x " + x + " y : " + y);
			
			for(i=1; i< NB_JOUEURS;i++)
			{
				if(collision(x, y, avatars[i].getX(), avatars[i].getY(),avatars[i].getWidth(), avatars[i].getHeight()))
				{
					select = i;
					Log.i("colision", "collision avec  " + select);
					//Log.i("coordonnees", "x : " + avatars[select].getX() + "y : " + avatars[select].getY() +
					//					 "w : "+ avatars[select].getWidth() + "h : " + avatars[select].getHeight());
					
				}
			}
			
			if(select != -1 && select != selected)
			{
				if(selected == -1)
				{
					selected=select;
					Log.i("setSelected", " new selection : " + selected);
				}
				else
				{
					Log.i("setSelect", " new selection : " + selected);
					
					//if(select != selected)
					{
						if(selected == id)
						{
							Log.i("action", "transfert de piere du joueur vers " + select);
							com.giveStone(select);
						}
						else if(select == id)
						{
							Log.i("action", "transfert de piere vers le joueur de la part de " + select);
							com.stealStone(select);
						}
						else
						{
							Log.i("action", "action spéciale de " + selected + "vers " + select);
						}
						selected=-1;
					}
					
				}
			}
			
		}
		
		batch.begin();
		bg.draw(batch);
		
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
		for (i = 0; i < NB_JOUEURS-2; i++) {
			avatars[(id+i) % (NB_JOUEURS-1) + 1].setPosition(posiX[i+1], posiY[i+1]);
			avatars[(id+i) % (NB_JOUEURS-1) + 1].setScale(0.8f);
			avatars[(id+i) % (NB_JOUEURS-1) + 1].draw(batch);
		}

		batch.end();

	}

	private void loadTextures() {
		bonus = new Sprite[NB_BONUS];
		avatars = new Sprite[NB_JOUEURS];
		lifeBar = new Sprite[NB_SPRITE_LIFEBAR];
		blockBar = new Sprite[NB_SPRITE_BLOCKBAR];

		for (int i = 1; i < NB_JOUEURS; i++)
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

	private boolean collision(int pX, int pY, float cX, float cY, float cW, float cH) {
		if(pX<cX)
			return false;
		if(pX > cX+cW)
			return false;
		if(pY<cY)
			return false;
		if(pY > cY+cH)
			return false;
		
		return true;
	}

	public void dispose() {}
	public void pause()  {}
	public void resize(int arg0, int arg1)  {}
	public void resume()  {}
}
