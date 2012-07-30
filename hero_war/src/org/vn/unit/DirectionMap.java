package org.vn.unit;

import org.vn.model.Move;
import org.vn.model.Move.TypeMove;

public class DirectionMap {
	private boolean[][] mArrayLogicMap;
	private Tile[][] mArrayTileMap;
	/**
	 * luu gia tri di chuyen
	 */
	private final byte[][] mArrayDirectionMap;
	private int mRange;
	private int mXStart;
	private int mYStart;

	public DirectionMap(boolean[][] arrayLogicMap, Tile[][] arrayTileMap,
			int range) {
		mArrayTileMap = arrayTileMap;
		mArrayLogicMap = arrayLogicMap;
		mRange = range;
		mArrayDirectionMap = new byte[range * 2 + 1][range * 2 + 1];
	}

	public void setPosStart(int x, int y) {
		resetInTo((byte) -1);
		mXStart = x - mRange;
		mYStart = y - mRange;
		boolean b = mArrayLogicMap[y][x];
		mArrayLogicMap[y][x] = false;
		setDirection(x, y, 0, 0, (byte) mRange);
		mArrayLogicMap[y][x] = b;
	}

	public int getPosStartX() {
		return mXStart + mRange;
	}

	public int getPosStartY() {
		return mYStart + mRange;
	}

	private void resetInTo(byte num) {
		for (int y = 0; y < mArrayDirectionMap.length; y++) {
			for (int x = 0; x < mArrayDirectionMap[0].length; x++) {
				mArrayDirectionMap[y][x] = num;
			}
		}
	}

	private void setDirection(int centerX, int centerY, int offsetX,
			int offsetY, byte foot) {
		int xInDirection = mRange + offsetX;
		int yInDirection = mRange + offsetY;
		if (mArrayDirectionMap[yInDirection][xInDirection] == -1) {
			int xInLogic = centerX + offsetX;
			int yInLogic = centerY + offsetY;
			if (xInLogic < 0 || yInLogic < 0
					|| xInLogic >= mArrayLogicMap[0].length
					|| yInLogic >= mArrayLogicMap.length
					|| mArrayLogicMap[yInLogic][xInLogic] == true) {
				return;
			}
		} else if (mArrayDirectionMap[yInDirection][xInDirection] > foot) {
			return;
		}
		mArrayDirectionMap[yInDirection][xInDirection] = foot;
		foot--;
		if (foot >= 0) {
			setDirection(centerX, centerY, offsetX + 1, offsetY + 1, foot);
			setDirection(centerX, centerY, offsetX - 1, offsetY - 1, foot);
			setDirection(centerX, centerY, offsetX + 1, offsetY, foot);
			setDirection(centerX, centerY, offsetX - 1, offsetY, foot);
			setDirection(centerX, centerY, offsetX, offsetY + 1, foot);
			setDirection(centerX, centerY, offsetX, offsetY - 1, foot);
		}
	}

	public byte getDirection(int xTile, int yTile) {
		yTile -= mYStart;
		xTile -= mXStart;
		if (xTile < 0 || yTile < 0 || yTile >= mArrayDirectionMap.length
				|| xTile >= mArrayDirectionMap[0].length) {
			return -1;
		}
		return mArrayDirectionMap[yTile][xTile];
	}

	public void doSomeThingWithAll(doSomeThingWithTile action) {
		byte b;
		for (int y = 0; y < mArrayDirectionMap.length; y++) {
			for (int x = 0; x < mArrayDirectionMap[0].length; x++) {
				b = mArrayDirectionMap[y][x];
				if (b == -1) {
					action.doSomeThing(null, b);
				} else {
					action.doSomeThing(mArrayTileMap[mYStart + y][mXStart + x],
							b);
				}
			}
		}
	}

	public interface doSomeThingWithTile {
		public void doSomeThing(Tile tile, byte total);
	}

	public Tile getTileAroundTile(int xTile, int yTile, int offset,
			Move moveSetDirection) {
		byte directionNext = (byte) (offset + getDirection(xTile, yTile));
		// check
		if (directionNext == -1) {
			return null;
		}
		if (directionNext == getDirection(xTile + 1, yTile + 1)) {
			moveSetDirection.typeMove = TypeMove.left_bot;
			return directionNext == mRange ? null
					: mArrayTileMap[yTile + 1][xTile + 1];
		} else if (directionNext == getDirection(xTile - 1, yTile - 1)) {
			moveSetDirection.typeMove = TypeMove.right_top;
			return directionNext == mRange ? null
					: mArrayTileMap[yTile - 1][xTile - 1];
		} else if (directionNext == getDirection(xTile + 1, yTile)) {
			moveSetDirection.typeMove = TypeMove.left;
			return directionNext == mRange ? null
					: mArrayTileMap[yTile][xTile + 1];
		} else if (directionNext == getDirection(xTile - 1, yTile)) {
			moveSetDirection.typeMove = TypeMove.right;
			return directionNext == mRange ? null
					: mArrayTileMap[yTile][xTile - 1];
		} else if (directionNext == getDirection(xTile, yTile + 1)) {
			moveSetDirection.typeMove = TypeMove.right_bot;
			return directionNext == mRange ? null
					: mArrayTileMap[yTile + 1][xTile];
		} else if (directionNext == getDirection(xTile, yTile - 1)) {
			moveSetDirection.typeMove = TypeMove.left_top;
			return directionNext == mRange ? null
					: mArrayTileMap[yTile - 1][xTile];
		}
		return null;
	}

}
