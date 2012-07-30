package org.vn.gl.game;

import org.vn.gl.BaseObject;
import org.vn.gl.ContextParameters;
import org.vn.gl.DrawableBitmap;
import org.vn.gl.Priority;
import org.vn.gl.RenderSystem;
import org.vn.gl.TextureLibrary;
import org.vn.gl.Vector2;
import org.vn.gl.WorldMap;
import org.vn.herowar.R;

/**
 * Manages the position of the camera based on a target game object.
 */
public class CameraSystem extends BaseObject {
	private float mX, mY, mXNext, mYNext, mXNext_ToaDoThuc, mYNext_ToaDoThuc;
	public WorldMap worldMap;
	private ContextParameters mContextParameters;
	public boolean canSetNext;
	public float timeDelayBeforeMove;
	public float mScale;
	public float mScaleNext;
	public float scaleX = 0;
	public float scaleY = 0;
	public float scaleMax;
	public float scaleMin = 4f;
	private float mHeightBackground;
	private float mWidthBackground;

	private DrawableBitmap mDrawableBitmapBackGround;
	private DrawableBitmap mDrawableBitmapBackGroundBefore;

	public CameraSystem(TextureLibrary textureLibrary, float widthWorld,
			float heightWord) {
		mContextParameters = sSystemRegistry.contextParameters;
		worldMap = new WorldMap(widthWorld, heightWord);
		mX = (mContextParameters.gameWidthAfter - mContextParameters.viewWidth) / 2;
		mY = (mContextParameters.gameHeightAfter - mContextParameters.viewHeight) / 3;
		mXNext = mX;
		mYNext = mY;
		mXNext_ToaDoThuc = mXNext + mContextParameters.gameWidthAfter / 2;
		mYNext_ToaDoThuc = mYNext + mContextParameters.gameHeightAfter / 2;
		canSetNext = true;
		timeDelayBeforeMove = 0;
		mScale = 2.8f;
		mScaleNext = 2.8f;
		scaleMax = Math.max(
				((float) mContextParameters.gameWidth) / widthWorld,
				((float) mContextParameters.gameHeight) / heightWord);
		scaleMax *= 1.1f;
		// if (mScale < scaleMax)
		{
			mScale = scaleMax;
			mScaleNext = scaleMax;
		}

		setScaleNow();

		mWidthBackground = sSystemRegistry.contextParameters.gameWidthAfter * 3 / 2;
		mHeightBackground = sSystemRegistry.contextParameters.gameHeightAfter * 3 / 2;
		mDrawableBitmapBackGround = new DrawableBitmap(
				textureLibrary.allocateTexture(R.drawable.back_ground,
						"back_ground"), (int) mWidthBackground,
				(int) mHeightBackground);

		mDrawableBitmapBackGroundBefore = new DrawableBitmap(
				textureLibrary.allocateTexture(R.drawable.batdau_ingame,
						"batdau_ingame"), (int) mWidthBackground,
				(int) mHeightBackground);
		setNext(worldMap.mWidthWord / 2, worldMap.mHeightWord / 2, 0);
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		if (mScale != mScaleNext) {
			float tocdo = timeDelta * 100;
			mScale += (mScaleNext - mScale) / tocdo;
			if (Math.abs(mScale - mScaleNext) < 0.01f) {
				mScale = mScaleNext;
			}
			setScaleNow();
		}

		if (timeDelayBeforeMove <= 0) {
			// float tocdo = timeDelta * 400;
			if (mX < mXNext) {
				mX = mX + (mXNext - mX) * 12 * timeDelta;
				if (mX > mXNext) {
					mX = mXNext;
				}
			} else {
				mX = mX + (mXNext - mX) * 12 * timeDelta;
				if (mX < mXNext) {
					mX = mXNext;
				}
			}
			if (mY < mYNext) {
				mY = mY + (mYNext - mY) * 12 * timeDelta;
				if (mY > mYNext) {
					mY = mYNext;
				}
			} else {
				mY = mY + (mYNext - mY) * 12 * timeDelta;
				if (mY < mYNext) {
					mY = mYNext;
				}
			}
		} else {
			timeDelayBeforeMove -= timeDelta;
		}
		// RenderSystem render = sSystemRegistry.renderSystem;
		// drawBackGround(render, mDrawableBitmapBackGround, 0.125f,
		// worldMap.mWidthWord, worldMap.mHeightWord, 1);
		// if (mDrawableBitmapBackGroundBefore != null) {
		// drawBackGround(render, mDrawableBitmapBackGroundBefore, 0.2f,
		// worldMap.mWidthWord, worldMap.mHeightWord, 1f);
		// }
	}

	private void drawBackGround(RenderSystem render,
			DrawableBitmap drawableBitmapBackGround, float a,
			final float widthWord, final float heightWord, float scaleY) {
		float xScale = widthWord
				/ sSystemRegistry.contextParameters.gameWidthAfter;
		float yScale = heightWord
				/ sSystemRegistry.contextParameters.gameHeightAfter;
		float scaleMin = Math.min(xScale, yScale);
		scaleMin = a * scaleMin * scaleMin + (1 - a);
		mWidthBackground = sSystemRegistry.contextParameters.gameWidth
				* scaleMin;
		mHeightBackground = sSystemRegistry.contextParameters.gameHeight
				* scaleMin;

		drawableBitmapBackGround.setWidth((int) mWidthBackground);
		drawableBitmapBackGround.setHeight((int) (scaleY * mHeightBackground));
		float TiLeX;
		if (widthWord - sSystemRegistry.contextParameters.gameWidthAfter == 0) {
			TiLeX = 0;
		} else {
			TiLeX = getX()
					/ (widthWord - sSystemRegistry.contextParameters.gameWidthAfter);
		}
		float TiLeY = 0;
		if (heightWord - sSystemRegistry.contextParameters.gameHeightAfter == 0) {
			TiLeY = 0;
		} else {
			TiLeY = getY()
					/ (heightWord - sSystemRegistry.contextParameters.gameHeightAfter);
		}
		float x = TiLeX
				* (mWidthBackground - sSystemRegistry.contextParameters.gameWidth);
		float y = TiLeY
				* (mHeightBackground - sSystemRegistry.contextParameters.gameHeight);

		render.scheduleForDraw(drawableBitmapBackGround, new Vector2(-x, -y),
				Priority.BackGround, false);
	}

	public void setScaleNow() {
		BaseObject.sSystemRegistry.contextParameters.setScale(mScale);
		mXNext = mXNext_ToaDoThuc - mContextParameters.gameWidthAfter * scaleX;
		mYNext = mYNext_ToaDoThuc - mContextParameters.gameHeightAfter * scaleY;
		canhLe();
		mXNext_ToaDoThuc = mXNext + mContextParameters.gameWidthAfter / 2;
		mYNext_ToaDoThuc = mYNext + mContextParameters.gameHeightAfter / 2;
		mX = mXNext;
		mY = mYNext;
		// mWidthBackground = sSystemRegistry.contextParameters.gameWidthAfter *
		// 3 / 2;
		// mHeightBackground = sSystemRegistry.contextParameters.gameHeightAfter
		// * 3 / 2;
		// mDrawableBitmapBackGround.setWidth((int) (mWidthBackground));
		// mDrawableBitmapBackGround.setHeight((int) (mHeightBackground));
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	public float getX() {
		return mX;
	}

	public float getY() {
		return mY;
	}

	public void setX(float x) {
		mX = x;
	}

	public void setY(float y) {
		mY = y;
	}

	public void touchDown() {
		mXNext = mX;
		mYNext = mY;
		timeDelayBeforeMove = 0;
		canSetNext = false;
	}

	public void touchUp() {
		canSetNext = true;
	}

	public void scale(float scaleOffset, float x, float y) {
		mScaleNext *= scaleOffset;
		scaleX = x;
		scaleY = y;
		if (mScaleNext > scaleMin) {
			mScaleNext = scaleMin;
		}
		if (mScaleNext < scaleMax) {
			mScaleNext = scaleMax;
		}
	}

	public void offset(float xOffset, float yOffset) {
		if (mScale == mScaleNext) {
			mXNext += xOffset / mContextParameters.gameWidth
					* mContextParameters.gameWidthAfter;
			mYNext += yOffset / mContextParameters.gameHeight
					* mContextParameters.gameHeightAfter;
			canhLe();
			mXNext_ToaDoThuc = mXNext + mContextParameters.gameWidthAfter / 2;
			mYNext_ToaDoThuc = mYNext + mContextParameters.gameHeightAfter / 2;
		}
	}

	private void canhLe() {
		if (mXNext + mContextParameters.gameWidthAfter > worldMap.mWidthWord) {
			mXNext = worldMap.mWidthWord - mContextParameters.gameWidthAfter;
		}
		if (mXNext < 0) {
			mXNext = 0;
		}
		if (mYNext + mContextParameters.gameHeightAfter > worldMap.mHeightWord) {
			mYNext = worldMap.mHeightWord - mContextParameters.gameHeightAfter;
		}
		if (mYNext < 0) {
			mYNext = 0;
		}
	}

	public void setNext(float x, float y, float timeDelay) {
		if (canSetNext && timeDelayBeforeMove <= 0 && mScale == mScaleNext) {
			mXNext_ToaDoThuc = x;
			mYNext_ToaDoThuc = y;
			mXNext = x - mContextParameters.gameWidthAfter / 2;
			mYNext = y - mContextParameters.gameHeightAfter / 2;
			timeDelayBeforeMove = timeDelay;
			canhLe();
		}
	}
}
