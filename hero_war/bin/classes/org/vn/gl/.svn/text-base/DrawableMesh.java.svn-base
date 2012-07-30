package org.vn.gl;

import javax.microedition.khronos.opengles.GL10;

import org.vn.unit.AnimationCharacter;

import android.graphics.PointF;

public class DrawableMesh extends DrawableObject {
	private Mesh mMesh;
	private float mAngle, mXRotate, mYRotate;
	private float mWidth, mHeight;

	public DrawableMesh(Texture texture, float width, float height) {
		mMesh = new Mesh(texture, width, height);
		mAngle = 0;
		mXRotate = width / 2;
		mYRotate = height / 2;
		mWidth = width;
		mHeight = height;
	}

	public float getWidth() {
		return mWidth;
	}

	public float getHeight() {
		return mHeight;
	}

	public void setWidth(float width) {
		mWidth = width;
		mMesh.setWidthHeight(mWidth, mHeight);
	}

	public void setHeight(float height) {
		mHeight = height;
		mMesh.setWidthHeight(mWidth, mHeight);
	}

	public void setangle(float angle) {
		mAngle = angle;
	}

	public void setPositionRotateInBitmap(float xRotate, float yRotate) {
		mXRotate = xRotate;
		mYRotate = yRotate;
	}

	public void setCoordinates(AnimationCharacter animation) {
		mMesh.setTextureCoordinates(animation.getScaleX1(),
				animation.getScaleX2(), animation.getScaleY1(),
				animation.getScaleY2());
	}

	public void setCoordinates(float scaleX1, float scaleX2, float scaleY1,
			float scaleY2) {
		mMesh.setTextureCoordinates(scaleX1, scaleX2, scaleY1, scaleY2);
	}

	@Override
	public void draw(float x, float y, float scaleX, float scaleY) {
		GL10 gl = OpenGLSystem.getGL();
		mMesh.draw(gl, x, y, mAngle, mXRotate, mYRotate, getPriority(), scaleX,
				scaleY);
	}

	public void setOpacity(float _opacity) {
		mMesh.setOpacity(_opacity);
	}

	public void setColorExpress(float r, float g, float b, float a) {
		mMesh.setColorExpress(r, g, b, a);
	}

	/**
	 * C-D<br>
	 * |---|<br>
	 * A-B
	 * 
	 * @param A
	 * @param B
	 * @param C
	 * @param D
	 */
	public void setVertices(PointF A, PointF B, PointF C, PointF D) {
		mMesh.setVertices(A, B, C, D);
	}

	public void setVerticesFloat(float[][] vertices) {
		mMesh.setVertices(vertices);
	}

	public void setTextureCoordinates(float[][] pTextureCoordinates) {
		mMesh.setTextureCoordinates(pTextureCoordinates);
	}

	public void setONE_MINUS_SRC_ALPHA(boolean b) {
		mMesh.setONE_MINUS_SRC_ALPHA(b);
	}
	
	public void setTexture(Texture pTexture) {
		mMesh.setTexture(pTexture);
	}
}
