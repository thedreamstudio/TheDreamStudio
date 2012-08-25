package org.vn.gl;

import org.vn.gl.SingerTouchDetector.ITouchDetectorListener;
import org.vn.gl.Touch.ActionTouch;

import android.view.MotionEvent;

public class MultiTouchFilter2 {
	public SingerTouchDetector[] pointFs = new SingerTouchDetector[1];
	private int countPoint = 0;
	private ITouchDetectorListener mTouchDetectorListener = null;

	public MultiTouchFilter2() {
		for (int i = 0; i < pointFs.length; i++) {
			pointFs[i] = new SingerTouchDetector();
		}
	}

	public void setTouchDetectorListener(
			ITouchDetectorListener pTouchDetectorListener) {
		mTouchDetectorListener = pTouchDetectorListener;
		for (int i = 0; i < pointFs.length; i++) {
			pointFs[i] = new SingerTouchDetector();
			pointFs[i].setTouchDetectorListener(pTouchDetectorListener);
		}
	}

	public SingerTouchDetector[] getPointFTouch() {
		return pointFs;
	}

	synchronized public void onTouchEvent(MotionEvent event) {
		int pointerCount = event.getPointerCount();
		if (pointerCount > countPoint)
			countPoint = pointerCount > pointFs.length ? pointFs.length - 1
					: pointerCount;
		ContextParameters params = BaseObject.sSystemRegistry.contextParameters;
		float xInGame = event.getX() * (1.0f / params.viewScaleX);
		float yInGame = params.gameHeight
				- (event.getY() * (1.0f / params.viewScaleY));

		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			touchMove(pointerCount, xInGame, yInGame, event.getActionIndex());
			break;
		case MotionEvent.ACTION_DOWN:
			touchDown(pointerCount, xInGame, yInGame, 0);
			break;
		case MotionEvent.ACTION_UP:
			touchUp(pointerCount, xInGame, yInGame, 0);
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN:
			touchDown(pointerCount, xInGame, yInGame, 0);
			break;
		case MotionEvent.ACTION_POINTER_1_UP:
			touchUp(pointerCount, xInGame, yInGame, 0);
			break;
		case MotionEvent.ACTION_POINTER_2_DOWN:// 261
			touchDown(pointerCount, xInGame, yInGame, 1);
			break;
		case MotionEvent.ACTION_POINTER_2_UP:// 262
			touchUp(pointerCount, xInGame, yInGame, 1);
			break;
		case MotionEvent.ACTION_POINTER_3_DOWN:// 517
			touchDown(pointerCount, xInGame, yInGame, 2);
			break;
		case MotionEvent.ACTION_POINTER_3_UP:// 518
			touchUp(pointerCount, xInGame, yInGame, 2);
			break;
		default:
			int actionMash = action % 256;
			switch (actionMash) {
			case 5:// touch down
			{
				int indext = action / 256;
				if (indext < pointFs.length) {
					touchDown(pointerCount, xInGame, yInGame, indext);
				}
				break;
			}
			case 6:// touch up
			{
				int indext = action / 256;
				if (indext < pointFs.length) {
					touchUp(pointerCount, xInGame, yInGame, indext);
				}
				break;
			}
			default:
				break;
			}
			break;
		}
	}

	/**
	 * 
	 * @param pointerCount
	 * @param event
	 * @param indextDown
	 *            là vị trí point touch trong mảng
	 */
	private void touchDown(int pointerCount, float x, float y, int indextDown) {
//		DebugLog.d("DUC", "touchDown//pointerCount = " + pointerCount
//				+ "//indextDown =" + indextDown);
		if (pointerCount == 1 && indextDown == 0) {
			if (mTouchDetectorListener != null)
				mTouchDetectorListener.touchDown(x, y, indextDown);
			if (indextDown < pointFs.length)
				pointFs[indextDown].touch(ActionTouch.DOWN, x, y, indextDown);
		}
	}

	/**
	 * 
	 * @param pointerCount
	 * @param event
	 * @param indextUp
	 *            là vị trí point touch trong mảng, nhưng chỉ tính những pointf
	 *            enable
	 */
	private void touchUp(int pointerCount, float x, float y, int indextUp) {
//		DebugLog.d("DUC", "touchUp//pointerCount = " + pointerCount
//				+ "//indextUp =" + indextUp);
		if (indextUp == 0 && pointFs[indextUp].getCurrentTouch().touchDown) {
			pointFs[indextUp].touch(ActionTouch.UP, x, y, indextUp);
			if (mTouchDetectorListener != null)
				mTouchDetectorListener.touchUp(x, y, indextUp);
		}
	}

	/**
	 * 
	 * @param pointerCount
	 * @param event
	 */
	private void touchMove(int pointerCount, float x, float y, int indextMove) {
//		DebugLog.d("DUC", "touchMove//pointerCount = " + pointerCount);
		if (indextMove == 0 && pointFs[indextMove].getCurrentTouch().touchDown) {
			if (mTouchDetectorListener != null)
				mTouchDetectorListener.touchMove(x, y, indextMove);
			pointFs[indextMove].touch(ActionTouch.MOVE, x, y, indextMove);
		}
	}

	synchronized public Touch findPointerInRegion(float regionX, float regionY,
			float regionWidth, float regionHeight) {
		float regionX2 = regionX + regionWidth;
		float regionY2 = regionY + regionHeight;
		for (int i = 0; i < countPoint; i++) {
			Touch touch = pointFs[i].getCurrentTouch();
			if (touch != null
					&& touch.touchDown
					&& checkIn(touch.x, touch.y, regionX, regionY, regionX2,
							regionY2)) {
				return touch;
			}
		}
		return null;
	}

	private static boolean checkIn(float x, float y, float regionX,
			float regionY, float regionX2, float regionY2) {
		if (x < regionX || y < regionY || x > regionX2 || y > regionY2)
			return false;
		return true;
	}
}
