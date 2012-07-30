package org.vn.unit;

public class EffectSoBayLen extends Effect {
	private float mX, mY;
	private float mAlpha;
	private int mNum;
	private NumberDrawable mNumberDrawable;
	public boolean cameraRelative = true;
	public float speed = 1;
	public boolean isDraw = true;

	public EffectSoBayLen(NumberDrawable numberDrawable, int num, float x,
			float y) {
		mX = x;
		mY = y;
		mAlpha = 3;
		mNum = num;
		mNumberDrawable = numberDrawable;
	}

	public void setDrawAble(boolean b) {
		isDraw = b;
	}

	@Override
	public boolean update(float timeDelta) {
		if (mAlpha > 0) {
			if (isDraw) {
				if (mAlpha > 1) {
					mNumberDrawable.drawNumberWithAlpha(mX, mY, mNum, 1,
							cameraRelative, true);
				} else {
					mNumberDrawable.drawNumberWithAlpha(mX, mY, mNum, mAlpha,
							cameraRelative, true);
				}
			}
			mY += 0.5f / speed;
			mAlpha -= 0.08f * speed;
			return true;
		}
		return false;
	}

}
