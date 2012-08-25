package org.vn.gl;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * Draws a screen-aligned bitmap to the screen.
 */
public class DrawableBitmap extends DrawableObject {

	private Texture mTexture;
	private float mWidth;
	private float mHeight;
	private int mCrop[];
	private int mViewWidth;
	private int mViewHeight;
	private iBitmapInImageCache mBitmapInImageCacheeBitmapChange;
	private boolean isChangeBitmap;
	private boolean isRemoveBitmapAffterChange;
	private boolean isSetColorExpress;
	private float red;
	private float green;
	private float blue;
	private float alpha;
	private boolean ONE_MINUS_SRC_ALPHA = false;
	private boolean IsEnableGlBlend = false;
	private int glBlend1 = GL10.GL_ONE;
	private int glBlend2 = GL10.GL_ONE_MINUS_SRC_ALPHA;

	public void setGlBlendFun(int scrColour, int destColour) {
		glBlend1 = scrColour;
		glBlend2 = destColour;
		IsEnableGlBlend = true;
	}

	public DrawableBitmap(Texture texture, float width, float height) {
		super();
		mTexture = texture;
		mWidth = width;
		mHeight = height;
		mCrop = new int[4];
		mViewWidth = 0;
		mViewHeight = 0;
		isSetColorExpress = false;
		red = 1;
		green = 1;
		blue = 1;
		alpha = 1;
		setCrop(0, 0, 1, 1);
		isChangeBitmap = false;
		isRemoveBitmapAffterChange = false;
	}

	public void reset() {
		mTexture = null;
		mViewWidth = 0;
		mViewHeight = 0;
	}

	public void setViewSize(int width, int height) {
		mViewHeight = height;
		mViewWidth = width;
	}

	public void setOpacity(float opacity) {
		alpha = opacity;
		isSetColorExpress = true;
	}

	public float getOpacity() {
		return alpha;
	}

	/**
	 * Begins drawing bitmaps. Sets the OpenGL state for rapid drawing.
	 * 
	 * @param gl
	 *            A pointer to the OpenGL context.
	 * @param viewWidth
	 *            The width of the screen.
	 * @param viewHeight
	 *            The height of the screen.
	 */
	public static void beginDrawing(GL10 gl, float viewWidth, float viewHeight) {
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, viewWidth, 0.0f, viewHeight, 0.0f, 1.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		// TODO: nho xoa
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}

	/**
	 * Draw the bitmap at a given x,y position, expressed in pixels, with the
	 * lower-left-hand-corner of the view being (0,0).
	 * 
	 * @param gl
	 *            A pointer to the OpenGL context
	 * @param x
	 *            The number of pixels to offset this drawable's origin in the
	 *            x-axis.
	 * @param y
	 *            The number of pixels to offset this drawable's origin in the
	 *            y-axis
	 * @param scaleX
	 *            The horizontal scale factor between the bitmap resolution and
	 *            the display resolution.
	 * @param scaleY
	 *            The vertical scale factor between the bitmap resolution and
	 *            the display resolution.
	 */
	@Override
	public void draw(float x, float y, float scaleX, float scaleY) {
		final Texture texture = mTexture;
		if (texture != null) {
			GL10 gl = OpenGLSystem.getGL();
			if (isChangeBitmap) {
				setBitmapChange(gl);
			}
			if (gl != null) {
				if (texture.resource != -1 && texture.loaded == false) {
					BaseObject.sSystemRegistry.longTermTextureLibrary
							.loadBitmap(
									BaseObject.sSystemRegistry.contextParameters.context,
									gl, texture);
				}
				assert texture.loaded;

				final float snappedX = x;
				final float snappedY = y;

				final float opacity = alpha;
				final float width = mWidth;
				final float height = mHeight;
				final float viewWidth = mViewWidth;
				final float viewHeight = mViewHeight;

				boolean cull = false;
				if (viewWidth > 0) {
					if (snappedX + width < 0.0f || snappedX > viewWidth
							|| snappedY + height < 0.0f
							|| snappedY > viewHeight || opacity == 0.0f
							|| !texture.loaded) {
						cull = true;
					}
				}
				if (!cull) {
					OpenGLSystem.bindTexture(GL10.GL_TEXTURE_2D, texture.name);

					// This is necessary because we could be drawing the same
					// texture with different
					// crop (say, flipped horizontally) on the same frame.
					OpenGLSystem.setTextureCrop(mCrop);

					if (isSetColorExpress) {
						if (!ONE_MINUS_SRC_ALPHA) {
							gl.glColor4f(red * alpha, green * alpha, blue
									* alpha, alpha);
						} else {
							gl.glColor4f(red, green, blue, alpha);
						}
					}
					if (IsEnableGlBlend) {
						gl.glBlendFunc(glBlend1, glBlend2);
						// gl.glEnable(GL10.GL_STENCIL_TEST);
						// gl.glStencilFunc(GL10.GL_ALWAYS, 0x1, 0x1);
						// gl.glStencilFunc(GL10.GL_EQUAL, 0x0, 0x1);
						// IntBuffer intBuffer = IntBuffer.allocate(1);

					}

					((GL11Ext) gl).glDrawTexfOES(snappedX * scaleX, snappedY
							* scaleY, getPriority(), width * scaleX, height
							* scaleY);
					if (isSetColorExpress) {
						gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
					}
					if (IsEnableGlBlend) {
						gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
						// gl.glDisable(GL10.GL_STENCIL_TEST);
					}
				}
			}
		}
	}

	/**
	 * Ends the drawing and restores the OpenGL state.
	 * 
	 * @param gl
	 *            A pointer to the OpenGL context.
	 */
	public static void endDrawing(GL10 gl) {
		gl.glDisable(GL10.GL_BLEND);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPopMatrix();
	}

	public void resize(int width, int height) {
		mWidth = width;
		mHeight = height;
		setCrop(0, height, width, height);
	}

	public float getWidth() {
		return mWidth;
	}

	public void setWidth(float width) {
		mWidth = width;
	}

	public float getHeight() {
		return mHeight;
	}

	public void setHeight(float height) {
		mHeight = height;
	}

	/**
	 * Changes the crop parameters of this bitmap. Note that the underlying
	 * OpenGL texture's parameters are not changed immediately The crop is
	 * updated on the next call to draw(). Note that the image may be flipped by
	 * providing a negative width or height.
	 * 
	 * @param left
	 * @param bottom
	 * @param width
	 * @param height
	 */
	public void setCrop(int left, int bottom, int width, int height) {
		// Negative width and height values will flip the image.
		mCrop[0] = left;
		mCrop[1] = bottom;
		mCrop[2] = width;
		mCrop[3] = -height;
	}

	public int[] getCrop() {
		return mCrop;
	}

	public void setTexture(Texture texture) {
		mTexture = texture;
	}

	@Override
	public Texture getTexture() {
		return mTexture;
	}

	@Override
	public boolean visibleAtPosition(Vector2 position) {
		boolean cull = false;
		if (mViewWidth > 0) {
			if (position.x + mWidth < 0 || position.x > mViewWidth
					|| position.y + mHeight < 0 || position.y > mViewHeight) {
				cull = true;
			}
		}
		return !cull;
	}

	public final void setFlip(boolean horzFlip, boolean vertFlip) {
		setCrop(horzFlip ? (int) mWidth : 0, vertFlip ? 0 : (int) mHeight,
				horzFlip ? -(int) mWidth : (int) mWidth,
				vertFlip ? -(int) mHeight : (int) mHeight);
	}

	public void changeBitmap(iBitmapInImageCache bitmap, boolean isRemove) {
		mBitmapInImageCacheeBitmapChange = bitmap;
		isRemoveBitmapAffterChange = isRemove;
		isChangeBitmap = true;
	}

	private void setBitmapChange(GL10 gl) {
		int[] mTextureNameWorkspace = new int[1];
		mTextureNameWorkspace[0] = mTexture.name;
		gl.glDeleteTextures(1, mTextureNameWorkspace, 0);
		Bitmap mBitmapChange = null;
		if (mBitmapInImageCacheeBitmapChange != null) {
			mBitmapChange = mBitmapInImageCacheeBitmapChange
					.getBitMapResourcesItem();
		}
		if (mBitmapChange != null) {
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture.name);

			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_LINEAR);

			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
					GL10.GL_CLAMP_TO_EDGE);

			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
					GL10.GL_MODULATE); // GL10.GL_REPLACE);

			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmapChange, 0);

			int[] mCropWorkspace = new int[4];
			mCropWorkspace[0] = 0;
			mCropWorkspace[1] = mBitmapChange.getHeight();
			mCropWorkspace[2] = mBitmapChange.getWidth();
			mCropWorkspace[3] = -mBitmapChange.getHeight();

			((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
					GL11Ext.GL_TEXTURE_CROP_RECT_OES, mCropWorkspace, 0);

			mTexture.width = mBitmapChange.getWidth();
			mTexture.height = mBitmapChange.getHeight();

			if (isRemoveBitmapAffterChange) {
				mBitmapChange.recycle();
			}
			mBitmapChange = null;
			isChangeBitmap = false;
		} else {
			// Delete
			mTexture.reset();
			mTexture = null;
		}
	}

	public void setCoordinates(float x1, float y1, float x2, float y2) {
		setCrop((int) (x1 * mTexture.width),//
				(int) (y2 * mTexture.height)//
				, (int) (mTexture.width * (x2 - x1)), //
				(int) (mTexture.height * (y2 - y1)));
	}

	public void setColorExpress(int r, int g, int b, int a) {
		float tamp = 1.0f / 255;
		red = tamp * r;
		green = tamp * g;
		blue = tamp * b;
		alpha = tamp * a;
		isSetColorExpress = true;
	}

	public void setColorExpressF(float r, float g, float b, float a) {
		red = r;
		green = g;
		blue = b;
		alpha = a;
		isSetColorExpress = true;
	}

	public void setDefaultColour() {
		isSetColorExpress = false;
	}

	public void setONE_MINUS_SRC_ALPHA(boolean b) {
		ONE_MINUS_SRC_ALPHA = b;
	}
}
