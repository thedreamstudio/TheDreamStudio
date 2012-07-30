package org.vn.gl;

import android.view.MotionEvent;

public class MultiTouchFilter {
	private InputXY[] pointFs = new InputXY[10];

	public MultiTouchFilter() {
		for (int i = 0; i < pointFs.length; i++) {
			pointFs[i] = new InputXY();
		}
	}

	public InputXY[] getPointFTouch() {
		return pointFs;
	}

	public void onTouchEvent(MotionEvent event) {
		int pointerCount = event.getPointerCount();
		// if (event.getAction() != MotionEvent.ACTION_MOVE) {
		// Log.e("DUC", "MotionEvent " + event.getAction());
		// Log.d("DUC", "pointerCount " + pointerCount);
		// for (int i = 0; i < pointerCount; i++) {
		// float x = event.getX(i);
		// float y = event.getY(i);
		// Log.d("DUC", "pointer " + i + ": " + x + " - " + y);
		// }
		// }
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			touchMove(pointerCount, event);
			break;
		case MotionEvent.ACTION_DOWN:
			touchDown(pointerCount, event, 0);
			break;
		case MotionEvent.ACTION_UP:
			touchUp(pointerCount, event, 0);
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN:
			touchDown(pointerCount, event, 0);
			break;
		case MotionEvent.ACTION_POINTER_1_UP:
			touchUp(pointerCount, event, 0);
			break;
		case MotionEvent.ACTION_POINTER_2_DOWN:// 261
			touchDown(pointerCount, event, 1);
			break;
		case MotionEvent.ACTION_POINTER_2_UP:// 262
			touchUp(pointerCount, event, 1);
			break;
		case MotionEvent.ACTION_POINTER_3_DOWN:// 517
			touchDown(pointerCount, event, 2);
			break;
		case MotionEvent.ACTION_POINTER_3_UP:// 518
			touchUp(pointerCount, event, 2);
			break;
		default:
			int actionMash = action % 256;
			switch (actionMash) {
			case 5:// touch down
			{
				int indext = action / 256;
				if (indext < pointFs.length) {
					touchDown(pointerCount, event, indext);
				}
				break;
			}
			case 6:// touch up
			{
				int indext = action / 256;
				if (indext < pointFs.length) {
					touchUp(pointerCount, event, indext);
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
	private void touchDown(int pointerCount, MotionEvent event, int indextDown) {
		changeToLocationScreen(pointFs[indextDown], event.getX(indextDown),
				event.getY(indextDown));
	}

	/**
	 * 
	 * @param pointerCount
	 * @param event
	 * @param indextUp
	 *            là vị trí point touch trong mảng, nhưng chỉ tính những pointf
	 *            enable
	 */
	private void touchUp(int pointerCount, MotionEvent event, int indextUp) {
		int indextMash = 0;
		for (int i = 0; i < pointFs.length; i++) {
			if (!pointFs[i].isDisable()) {
				if (indextMash == indextUp) {
					indextMash = i;
					break;
				}
				indextMash++;
			}
		}
		pointFs[indextMash].disable();
	}

	/**
	 * 
	 * @param pointerCount
	 * @param event
	 */
	private void touchMove(int pointerCount, MotionEvent event) {
		int j = 0;
		for (int i = 0; i < pointFs.length; i++) {
			if (!pointFs[i].isDisable()) {
				if (j < pointerCount) {
					changeToLocationScreen(pointFs[i], event.getX(j),
							event.getY(j));
					j++;
				}
			}
		}
	}

	private static void changeToLocationScreen(InputXY inputXY, float x, float y) {
		ContextParameters params = BaseObject.sSystemRegistry.contextParameters;
		inputXY.x = x * (1.0f / params.viewScaleX);
		inputXY.y = params.gameHeight - (y * (1.0f / params.viewScaleY));
	}
}
