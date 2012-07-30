package org.vn.gl;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * An object wrapper for a pointer to the OpenGL context. Note that the context
 * is only valid in certain threads at certain times (namely, in the Rendering
 * thread during draw time), and at other times getGL() will return null.
 */
public class OpenGLSystem extends BaseObject {

	private static GL10 sGL;
	private static int sLastBoundTexture;
	private static int sLastSetCropSignature;

	public OpenGLSystem() {
		super();
		sGL = null;
	}

	public OpenGLSystem(GL10 gl) {
		sGL = gl;
	}

	public static final void setGL(GL10 gl) {
		sGL = gl;
		sLastBoundTexture = 0;
		sLastSetCropSignature = 0;
	}

	public static final GL10 getGL() {
		return sGL;
	}

	public static final void bindTexture(int target, int texture) {
		if (sLastBoundTexture != texture) {
			sGL.glBindTexture(target, texture);
			sLastBoundTexture = texture;
			sLastSetCropSignature = 0;
		}
	}

	public static final void setTextureCrop(int[] crop) {
		int cropSignature = 0;
		cropSignature = (crop[0] + crop[1]) << 16;
		cropSignature |= crop[2] + crop[3];

		if (cropSignature != sLastSetCropSignature) {
			((GL11) sGL).glTexParameteriv(GL10.GL_TEXTURE_2D,
					GL11Ext.GL_TEXTURE_CROP_RECT_OES, crop, 0);
			sLastSetCropSignature = cropSignature;
		}
	}

	@Override
	public void reset() {
		
	}

}
