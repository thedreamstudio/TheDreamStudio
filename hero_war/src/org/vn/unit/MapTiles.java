package org.vn.unit;

import org.vn.gl.BaseObject;
import org.vn.gl.DrawableBitmap;
import org.vn.gl.Priority;
import org.vn.gl.TextureLibrary;
import org.vn.gl.Vector2;
import org.vn.herowar.R;
import org.vn.model.TouchTouch;

public class MapTiles extends BaseObject {
	public final Tile[][] arrayMap;
	private final int row;
	private final int column;
	private final float distance;
	private int countProcess = 0;
	private float posX = 300;
	private float posY = 100;
	private static float coss = (float) Math.cos(Math.toRadians(120));
	private static float sinn = (float) Math.sin(Math.toRadians(120));
	private Tile mTileForcusLast = null;
	private Tile mTileForcus = null;
	private DrawableBitmap mDrawableBitmapBackGround;

	public MapTiles(TextureLibrary textureLibrary, final int pRow, int pColumn,
			float pOffset, float pX, float pY) {
		distance = pOffset;
		arrayMap = new Tile[pRow][pColumn];
		row = pRow;
		column = pColumn;
		posX = pX;
		posY = pY;

		for (int y = 0; y < pRow; y++) {
			for (int x = 0; x < pColumn; x++) {
				arrayMap[y][x] = new Tile(textureLibrary.allocateTexture(
						org.vn.herowar.R.drawable.tile, "tile"), distance,
						distance, x, y);
				final Tile tileTaget = arrayMap[y][x];
				tileTaget.x = (coss * y + x) * distance + posX;
				tileTaget.y = (sinn * y) * distance + posY;
				tileTaget.isSetPos = true;
			}
		}
		// int yTileBegin = pRow / 2;
		// int xTileBegin = pColumn / 2;
		// long processTime = SystemClock.elapsedRealtime();
		// setPosArrayTiles(arrayMap[yTileBegin][xTileBegin], arrayMap,
		// xTileBegin, yTileBegin, 300, 300);
		// processTime = SystemClock.elapsedRealtime() - processTime;
		// DebugLog.e("DUC", "processTime map:" + processTime);
		// DebugLog.e("DUC", "processCount map:" + countProcess);
		mDrawableBitmapBackGround = new DrawableBitmap(
				textureLibrary.allocateTexture(R.drawable.back_ground,
						"back_ground"),
				sSystemRegistry.cameraSystem.worldMap.mWidthWord,
				sSystemRegistry.cameraSystem.worldMap.mHeightWord);
	}

	private Tile getTile(float x, float y) {
		y -= posY;
		int yTile = Math.round((y / distance) / sinn);
		if (yTile < 0 || yTile >= row)
			return null;
		x -= posX;
		int xYile = Math.round((x / distance) - coss * yTile);
		if (xYile < 0 || xYile >= column)
			return null;
		return arrayMap[yTile][xYile];

	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		TouchTouch touchTouch = sSystemRegistry.inputGameInterface.mTouchTouch;
		if (touchTouch.isTouch && touchTouch.isClick) {
			Tile tileForcus = getTile(touchTouch.xHasCameRa,
					touchTouch.yHasCamera);
			setTileForcus(tileForcus);
			sSystemRegistry.inputGameInterface.daXuLyTouch();
		}
		// Ve map
		sSystemRegistry.renderSystem.scheduleForDraw(mDrawableBitmapBackGround,
				Vector2.TAMP.set(0, 0), Priority.BackGround, true);
		for (int y = 0; y < row; y++) {
			for (int x = 0; x < column; x++) {
				// if (x == 0 || y == 0 || x == column - 1 || y == row - 1) {
				// arrayMap[y][x].update(timeDelta, parent);
				// }
				// if (sSystemRegistry.logicMap.mArrayMap[y][x] == true) {
				// arrayMap[y][x].update(timeDelta, parent);
				// }
			}
		}
	}

	public void setTileForcus(Tile tileForcus) {
		if (mTileForcus == tileForcus) {
			return;
		}
		if (mTileForcus != null && mTileForcus.getCharacterTaget() != null
				&& tileForcus != null) {
			switch (mTileForcus.getCharacterTaget().onClick(tileForcus)) {
			case 1:// Move
				mTileForcus = tileForcus;
				sSystemRegistry.unitEffects.addEffectClick(tileForcus.x,
						mTileForcus.y);
				BaseObject.sSystemRegistry.dialogAddEnemy.setTileSeleted(null);
				break;
			case 2:// Attack
				BaseObject.sSystemRegistry.dialogAddEnemy.setTileSeleted(null);
				break;
			case 0:// Notthing
				mTileForcus = tileForcus;
				changeCharacterInfo(tileForcus);
				BaseObject.sSystemRegistry.dialogAddEnemy
						.setTileSeleted(tileForcus);
				break;
			}
		} else {
			mTileForcus = tileForcus;
			changeCharacterInfo(tileForcus);
			BaseObject.sSystemRegistry.dialogAddEnemy
					.setTileSeleted(tileForcus);
		}
		if (mTileForcusLast == mTileForcus) {
			return;
		}
		if (mTileForcusLast != null) {
			mTileForcusLast.disableForcus();
		}
		if (mTileForcus != null) {
			mTileForcus.enableForcus();
		}
		mTileForcusLast = tileForcus;
	}

	public void changeCharacterInfo(Tile mTileForcus) {
		if (mTileForcus != null) {
			sSystemRegistry.unitSreen.setCharacterFocus(mTileForcus
					.getCharacterTaget());
		} else {
			sSystemRegistry.unitSreen.setCharacterFocus(null);
		}
	}

	@Override
	public void reset() {
	}

	public void setPosArrayTiles(Tile tileTaget, Tile[][] arrayTiles,
			int xTile, int yTile, final float posX, final float posY) {
		countProcess++;
		tileTaget.x = posX;
		tileTaget.y = posY;
		tileTaget.isSetPos = true;

		processPosArrayTiles(arrayTiles, xTile, yTile, posX, posY,
				(float) Math.cos(Math.toRadians(0)) * distance,
				(float) Math.sin(Math.toRadians(0)) * distance, 1, 0);
		processPosArrayTiles(arrayTiles, xTile, yTile, posX, posY,
				(float) Math.cos(Math.toRadians(180)) * distance,
				(float) Math.sin(Math.toRadians(180)) * distance, -1, 0);
		processPosArrayTiles(arrayTiles, xTile, yTile, posX, posY,
				(float) Math.cos(Math.toRadians(120)) * distance,
				(float) Math.sin(Math.toRadians(120)) * distance, 0, 1);
		processPosArrayTiles(arrayTiles, xTile, yTile, posX, posY,
				(float) Math.cos(Math.toRadians(300)) * distance,
				(float) Math.sin(Math.toRadians(300)) * distance, 0, -1);
		processPosArrayTiles(arrayTiles, xTile, yTile, posX, posY,
				(float) Math.cos(Math.toRadians(240)) * distance,
				(float) Math.sin(Math.toRadians(240)) * distance, -1, -1);
		processPosArrayTiles(arrayTiles, xTile, yTile, posX, posY,
				(float) Math.cos(Math.toRadians(60)) * distance,
				(float) Math.sin(Math.toRadians(60)) * distance, 1, 1);
	}

	public void processPosArrayTiles(Tile[][] arrayTiles, int xTile, int yTile,
			float posX, float posY, float offsetPosX, float offsetPosY,
			int offsetTileX, int offsetTileY) {
		xTile += offsetTileX;
		yTile += offsetTileY;
		if (xTile >= 0 && xTile < column && yTile >= 0 && yTile < row) {
			Tile tileTaget = arrayTiles[yTile][xTile];
			if (!tileTaget.isSetPos) {
				posX += offsetPosX;
				posY += offsetPosY;
				setPosArrayTiles(tileTaget, arrayTiles, xTile, yTile, posX,
						posY);
			}
		}
	}

	private interface IProcessTile {
		public void processTile(Tile tileTaget, Tile[][] arrayTiles, int x,
				int y);
	}

	public Tile getTileForcus() {
		return mTileForcus;
	}

	/**
	 * Lay vi tri x ve tren ban do voi he truc toa do (0,0)
	 * 
	 * @param xTile
	 * @param yTile
	 * @param offset
	 * @return
	 */
	public static float getPosXUnit(int xTile, int yTile, float offset) {
		return (coss * yTile + xTile) * offset;
	}

	/**
	 * Lay vi tri y ve tren ban do voi he truc toa do (0,0)
	 * 
	 * @param xTile
	 * @param yTile
	 * @param offset
	 * @return
	 */
	public static float getPosYUnit(int xTile, int yTile, float offset) {
		return (sinn * yTile) * offset;
	}
}
