package org.vn.gl;

public class Touch {
	public float x, y;
	public ActionTouch actionTouch;
	public long timeCurrent;
	public boolean touchDown = false;
	public int indext;
	public enum ActionTouch {
		DOWN, MOVE, UP
	}
}
