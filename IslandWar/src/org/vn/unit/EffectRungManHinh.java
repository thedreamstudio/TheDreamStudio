package org.vn.unit;

import org.vn.gl.BaseObject;
import org.vn.gl.Utils;


public class EffectRungManHinh extends Effect {
	private float xCameraBefore, yCameraBefore;
	private float mTimes, mTimesMax;

	public EffectRungManHinh(float times) {
		mTimes = times;
		mTimesMax = times;
		xCameraBefore = BaseObject.sSystemRegistry.cameraSystem.getX();
		yCameraBefore = BaseObject.sSystemRegistry.cameraSystem.getY();
	}

	@Override
	public boolean update(float timeDelta) {
		// TODO Auto-generated method stub
		if (mTimes > 0) {
			mTimes -= timeDelta;
			float offset = mTimes / mTimesMax * 8;
			BaseObject.sSystemRegistry.cameraSystem.setY(yCameraBefore - offset
					+ (offset * 2 + 1) * Utils.RANDOM.nextFloat());
			return true;
		} else {
			// BaseObject.sSystemRegistry.cameraSystem.setY(yCameraBefore);
			return false;
		}
	}
}
