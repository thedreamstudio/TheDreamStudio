package org.vn.unit;

import org.vn.gl.BaseObject;
import org.vn.gl.DrawableBitmap;
import org.vn.gl.Priority;
import org.vn.gl.RenderSystem;
import org.vn.gl.TextureLibrary;
import org.vn.gl.Vector2;
import org.vn.herowar.R;

/**
 * class dung de hien thi So len Man Hinh<br>
 * Luu y: class chi de hien thi
 * 
 * @author duchq
 * 
 */
public class NumberDrawable {
	public int width;
	public int height;

	private DrawableBitmap[] mDrawableNumber;
	public static int[] idDrawableNumber2 = { R.drawable.num_0,
			R.drawable.num_1, R.drawable.num_2, R.drawable.num_3,
			R.drawable.num_4, R.drawable.num_5, R.drawable.num_6,
			R.drawable.num_7, R.drawable.num_8, R.drawable.num_9,
			R.drawable.ui_tru, R.drawable.ui_cong };

	public NumberDrawable(TextureLibrary textureLibrary, int r, int g, int b,
			int a, int _width, int _height, int[] idDrawableNumber) {
		mDrawableNumber = new DrawableBitmap[idDrawableNumber.length];
		for (int i = 0; i < idDrawableNumber.length; i++) {
			mDrawableNumber[i] = new DrawableBitmap(
					textureLibrary.allocateTexture(idDrawableNumber[i],
							"DrawableNumber"), _width, _height);
			mDrawableNumber[i].setPriority(Priority.CharacterBarAngle);
			if (r != 255 || g != 255 || b != 255 || a != 255) {
				mDrawableNumber[i].setColorExpress(r, g, b, a);
			}
		}
		width = _width;
		height = _height;
	}

	// public void drawNumber(float x, float y, int number,
	// boolean cameraRelative, int priority, int offset) {
	// String string = "" + number;
	// int size = string.length();
	// x -= size * (width + offset) / 2;
	// y -= height / 2;
	// RenderSystem render = BaseObject.sSystemRegistry.renderSystem;
	// if (number < 0) {
	// size--;
	// render.scheduleForDraw(mDrawableNumber[10], new Vector2(x, y),
	// Priority.CharacterBarAngle, cameraRelative);
	// number = -number;
	// x = x + (width + offset);
	// }
	// for (int i = size - 1; i >= 0; i--) {
	// int pow10 = (int) Math.pow(10, i);
	// int numberShow = number / pow10;
	// number -= numberShow * pow10;
	// render.scheduleForDraw(mDrawableNumber[numberShow], new Vector2(x,
	// y), priority, cameraRelative);
	// x = x + (width + offset);
	// }
	// }

	public void drawNumberWithAlpha(float x, float y, int number, float alpha,
			boolean cameraRelative, boolean theHienDau) {
		drawNumberWithAlpha(x, y, number, alpha, cameraRelative, theHienDau,
				Priority.CharacterBarAngle);
	}

	public void drawNumberWithAlpha(float x, float y, int number, float alpha,
			boolean cameraRelative, boolean theHienDau, int priority) {
		String string = "" + number;
		int size = string.length();
		int numberAbs = Math.abs(number);
		if (number >= Math.pow(10, size)) {
			return;
		}
		while (size > 1 && numberAbs < Math.pow(10, size - 1)) {
			size--;
		}
		if (number == 0 && theHienDau == true) {
			theHienDau = false;
		}
		RenderSystem render = BaseObject.sSystemRegistry.renderSystem;
		if (theHienDau) {
			if (number < 0) {
				x -= (size + 1) * width / 2;
				y -= height / 2;
				mDrawableNumber[10].setOpacity(alpha);
				render.scheduleForDraw(mDrawableNumber[10], new Vector2(x, y),
						priority, cameraRelative);
				number = -number;
				x = x + width;
			} else {
				x -= (size + 1) * width / 2;
				y -= height / 2;
				mDrawableNumber[11].setOpacity(alpha);
				render.scheduleForDraw(mDrawableNumber[11], new Vector2(x, y),
						priority, cameraRelative);
				x = x + width;
			}
		} else {
			x -= size * width / 2;
		}
		for (int i = size - 1; i >= 0; i--) {
			int pow10 = (int) Math.pow(10, i);
			int numberShow = number / pow10;
			number -= numberShow * pow10;
			mDrawableNumber[numberShow % 10].setOpacity(alpha);
			render.scheduleForDraw(mDrawableNumber[numberShow], new Vector2(x,
					y), priority, cameraRelative);
			x = x + width;
		}
	}
}
