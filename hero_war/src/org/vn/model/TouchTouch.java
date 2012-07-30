package org.vn.model;

public class TouchTouch {
	public float xNoCamera;
	public float yNoCamera;
	public float xHasCameRa;
	public float yHasCamera;

	/** cai nay do uu tien lon nhat */
	public boolean isTouch;

	public boolean isTouchUp;
	public boolean chuaXuLyTouchDownTrongVongLap;
	public boolean isWaitProccessClick;
	public boolean isClick;
	public TouchTouchType mAction = TouchTouchType.chua_lam_gi;

	public enum TouchTouchType {
		chua_lam_gi, da_touch_down, touch_up;
	}
}
