package org.vn.gl;


public class InputTouchScreen extends BaseObject {
//	public InputXY findPointerInRegion(float regionX, float regionY,
//			float regionWidth, float regionHeight) {
//		InputXY[] arrayInput = sSystemRegistry.multiTouchFilter
//				.getPointFTouch();
//		float regionX2 = regionX + regionWidth;
//		float regionY2 = regionY + regionHeight;
//		for (int i = 0; i < arrayInput.length; i++) {
//			InputXY inputXYTamp = arrayInput[i];
//			if (!inputXYTamp.isDisable()
//					&& checkIn(inputXYTamp.x, inputXYTamp.y, regionX, regionY,
//							regionX2, regionY2)) {
//				return inputXYTamp;
//			}
//		}
//		return null;
//	}

	private static boolean checkIn(float x, float y, float regionX,
			float regionY, float regionX2, float regionY2) {
		if (x < regionX || y < regionY || x > regionX2 || y > regionY2)
			return false;
		return true;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}
}
