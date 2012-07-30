package org.vn.unit;

import org.vn.gl.BaseObject;
import org.vn.gl.DrawableBitmap;
import org.vn.gl.Texture;
import org.vn.gl.Vector2;

public class EffectAnimation extends Effect {
	protected float mTimeTick;
	private float mDeltaTime;
	private Texture[] mTextures;
	private int mIndextTexture;
	private Vector2 pos = new Vector2();
	private DrawableBitmap mDrawableBitmap;
	private int mPriority;
	public int mCountLoop = 9999;

	public EffectAnimation(Texture[] pTextures, float pDeltaTime,
			int pPriority, float x, float y, float width, float height,
			float waitTimeInfist) {
		mTextures = pTextures;
		mDeltaTime = pDeltaTime;
		mTimeTick = mDeltaTime + waitTimeInfist;
		pos.set(x, y);
		mDrawableBitmap = new DrawableBitmap(pTextures[mIndextTexture], width,
				height);
		mPriority = pPriority;
	}

	public void set(float x, float y, float width, float height) {
		pos.set(x, y);
		mDrawableBitmap.setWidth(width);
		mDrawableBitmap.setHeight(height);
	}

	@Override
	public boolean update(float timeDelta) {
		mTimeTick -= timeDelta;
		if (mTimeTick < 0) {
			mTimeTick = mDeltaTime;
			mIndextTexture++;
			if (mIndextTexture >= mTextures.length) {
				mIndextTexture = 0;
				if (mCountLoop != 9999) {
					mCountLoop--;
				}
				beginLoop();
			}
			mDrawableBitmap.setTexture(mTextures[mIndextTexture]);
		}
		BaseObject.sSystemRegistry.renderSystem.scheduleForDraw(
				mDrawableBitmap, pos, mPriority, true);
		if (mCountLoop <= 0) {
			return false;
		} else {
			return true;
		}
	}

	public void beginLoop() {

	}
}
