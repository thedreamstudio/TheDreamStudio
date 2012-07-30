package org.vn.gl;

import android.content.Context;

/**
 * Contains global (but typically constant) parameters about the current
 * operating context
 */
public class ContextParameters extends BaseObject {
	public int viewWidth;
	public int viewHeight;
	public Context context;
	public int gameWidth;
	public int gameHeight;
	public float viewScaleX;
	public float viewScaleY;
	//
	public float gameWidthAfter;
	public float gameHeightAfter;
	public float viewScaleXAfter;
	public float viewScaleYAfter;
	//
	public boolean supportsDrawTexture;
	public boolean supportsVBOs;

	//

	public ContextParameters() {
		super();
	}

	@Override
	public void reset() {
	}

	public void setScale(float value) {
		gameWidthAfter = gameWidth / value;
		gameHeightAfter = gameHeight / value;
		viewScaleXAfter = viewWidth / gameWidthAfter;
		viewScaleYAfter = viewHeight / gameHeightAfter;
	}
}
