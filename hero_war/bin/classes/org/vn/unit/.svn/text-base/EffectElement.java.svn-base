package org.vn.unit;

import java.util.ArrayList;

import org.vn.gl.BaseObject;
import org.vn.gl.DrawableBitmap;
import org.vn.gl.Priority;
import org.vn.gl.Texture;
import org.vn.gl.Vector2;

public class EffectElement extends Effect {
	private Vector2 pos;
	private float mTimeShowTick = 5;
	private float mTimeShow = 5;
	private float mTimeFaceOut = 2;
	public DrawableBitmap mDrawableBitmap;
	private boolean cameraRelative = true;
	private float mOpacity = 1;
	private float mx = 0, my = 0;
	private float mVx = 0, mVy = 0;
	private float mAx = 0, mAy = 0;
	private float mWidth = 0, mHeight = 0;
	private float mTimeDelay = 0;
	private boolean mIsScale = false;
	private boolean mIsOpacity = false;
	private ArrayList<EffectElement> arrayEffectElementCanUsing = null;
	private int mPriority = Priority.Effect1;

	private void reset() {
		mIsScale = false;
		mIsOpacity = false;
		mDrawableBitmap.setONE_MINUS_SRC_ALPHA(false);
		mOpacity = 1;
		mx = 0;
		my = 0;
		mVx = 0;
		mVy = 0;
		mAx = 0;
		mAy = 0;
	}

	public EffectElement(ArrayList<EffectElement> pArrayEffectElementCanUsing) {
		pos = new Vector2(0, 0);
		mTimeShowTick = 0;
		mTimeShow = 0;
		mTimeFaceOut = 1;
		mDrawableBitmap = new DrawableBitmap(null, 0, 0);
		arrayEffectElementCanUsing = pArrayEffectElementCanUsing;
	}

	public EffectElement setDelayTime(float pTimeDelay) {
		mTimeDelay = pTimeDelay;
		return this;
	}

	public EffectElement setTimeShow(float pTimeShow) {
		mTimeShowTick = pTimeShow;
		mTimeShow = pTimeShow;
		return this;
	}

	public EffectElement setTimeFaceOut(float pTimeShow) {
		mTimeFaceOut = pTimeShow;
		return this;
	}

	public EffectElement setCameraRelative(Boolean isCameraRelative) {
		cameraRelative = isCameraRelative;
		return this;
	}

	public EffectElement setPostion(float pX, float pY) {
		mx = pX;
		my = pY;
		return this;
	}

	public EffectElement setVelocity(float pVx, float pVy) {
		mVx = pVx;
		mVy = pVy;
		return this;
	}

	public EffectElement setAcceleration(float pAx, float pAy) {
		mAx = pAx;
		mAy = pAy;
		return this;
	}

	public EffectElement setSize(float w, float h) {
		mWidth = w;
		mHeight = h;
		mDrawableBitmap.setWidth(mWidth);
		mDrawableBitmap.setHeight(mHeight);
		return this;
	}

	public EffectElement setIsScale(boolean isScale) {
		mIsScale = isScale;
		return this;
	}

	public EffectElement setIsOpacity(boolean isOpacity) {
		mIsOpacity = isOpacity;
		return this;
	}

	public EffectElement setTexture(Texture pTexture) {
		mDrawableBitmap.setTexture(pTexture);
		return this;
	}

	public EffectElement setONE_MINUS_SRC_ALPHA(boolean b) {
		mDrawableBitmap.setONE_MINUS_SRC_ALPHA(b);
		return this;
	}

	public EffectElement setPriority(int p) {
		mPriority = p;
		return this;
	}

	@Override
	public boolean update(float timeDelta) {
		if (mTimeDelay > 0) {
			mTimeDelay -= timeDelta;
			return true;
		}
		if (mTimeShowTick > 0 || mTimeShowTick == 9999) {
			if (!mIsOpacity || mTimeShowTick > mTimeFaceOut) {
				mOpacity = 1;
			} else {
				mOpacity = mTimeShowTick / mTimeShow;
			}
			if (mIsScale) {
				mDrawableBitmap.setWidth(mWidth * mOpacity);
				mDrawableBitmap.setHeight(mHeight * mOpacity);
			}
			mDrawableBitmap.setOpacity(mOpacity);
			float t = mTimeShow - mTimeShowTick;
			float tmu2d2 = t * t * 0.5f;
			pos.x = t * mVx + mAx * tmu2d2 + mx - mDrawableBitmap.getWidth()
					/ 2;
			pos.y = t * mVy + mAy * tmu2d2 + my - mDrawableBitmap.getHeight()
					/ 2;
			BaseObject.sSystemRegistry.renderSystem.scheduleForDraw(
					mDrawableBitmap, pos, mPriority, cameraRelative);
			mTimeShowTick -= timeDelta;
		}
		if (isTurnOff()) {
			synchronized (arrayEffectElementCanUsing) {
				arrayEffectElementCanUsing.add(this);
			}
			reset();
			return false;
		} else {
			return true;
		}
	}

	public boolean isTurnOff() {
		if (mTimeShowTick < 0 || my < -mHeight) {
			return true;
		} else {
			return false;
		}
	}
}
