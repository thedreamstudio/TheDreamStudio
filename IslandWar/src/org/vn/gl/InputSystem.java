package org.vn.gl;


/**
 * Manages input from a roller wheel and touch screen. Reduces frequent UI
 * messages to an average direction over a short period of time.
 */
public class InputSystem extends BaseObject {
	private InputTouchScreen mTouchScreen = new InputTouchScreen();

	public InputSystem() {
		super();
		reset();
	}

	public InputTouchScreen getTouchScreen() {
		return mTouchScreen;
	}

	// Thanks to NVIDIA for this useful canonical-to-screen orientation
	// function.
	// More here:
	// http://developer.download.nvidia.com/tegra/docs/tegra_android_accelerometer_v5f.pdf
	static void canonicalOrientationToScreenOrientation(int displayRotation,
			float[] canVec, float[] screenVec) {
		final int axisSwap[][] = { { 1, -1, 0, 1 }, // ROTATION_0
				{ -1, -1, 1, 0 }, // ROTATION_90
				{ -1, 1, 0, 1 }, // ROTATION_180
				{ 1, 1, 1, 0 } }; // ROTATION_270

		final int[] as = axisSwap[displayRotation];
		screenVec[0] = (float) as[0] * canVec[as[2]];
		screenVec[1] = (float) as[1] * canVec[as[3]];
		screenVec[2] = canVec[2];
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
