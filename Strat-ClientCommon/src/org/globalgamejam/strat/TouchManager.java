package org.globalgamejam.strat;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TouchManager {
	// Internal objects
	private List<Touchable> touchable;
	private boolean isTouched = false;
	private boolean drag = false;
	private Dragable objectToDrag;

	public TouchManager() {
		touchable = new ArrayList<Touchable>();
	}

	public void add(Touchable t) {
		touchable.add(t);
	}

	public void touchInteraction() {
		boolean continuing;
		if (!isTouched && Gdx.input.isTouched()) {
			continuing = true;
			for (int i = 0; continuing && i < touchable.size(); i++)
				continuing = touchable.get(i).touchDown(Gdx.input.getX(),
						Gdx.graphics.getHeight() - Gdx.input.getY(), this);
			isTouched = true;
		} else if (isTouched && !Gdx.input.isTouched()) {
			continuing = true;
			for (int i = 0; continuing && i < touchable.size(); i++)
				continuing = touchable.get(i).touchUp(Gdx.input.getX(),
						Gdx.graphics.getHeight() - Gdx.input.getY(), this,
						objectToDrag);
			isTouched = false;
			objectToDrag = null;
			drag = false;
		}
	}

	public void drag(Dragable objectToDrag) {
		if (!Gdx.input.isTouched())
			return;
		this.objectToDrag = objectToDrag;
		drag = true;
	}

	public void draw(SpriteBatch batch) {
		if (drag)
			objectToDrag.drawDrag(batch, Gdx.input.getX(),
					Gdx.graphics.getHeight() - Gdx.input.getY());
	}
}
