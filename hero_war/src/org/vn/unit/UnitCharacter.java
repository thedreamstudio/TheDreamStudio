package org.vn.unit;

import java.util.ArrayList;

import org.vn.gl.BaseObject;
import org.vn.gl.DrawableMesh;
import org.vn.model.Move;
import org.vn.unit.DirectionMap.doSomeThingWithTile;

public abstract class UnitCharacter extends BaseObject {
	/**
	 * Vi tri
	 */
	public Tile mTileTaget = null;
	/**
	 * Cong cu tim duong
	 */
	private DirectionMap directionMap;
	/**
	 * List nay luu dau viet di chuyen
	 */
	protected ArrayList<Move> mListMove = new ArrayList<Move>();
	/**
	 * List character can attack
	 */
	protected ArrayList<UnitCharacter> mListCharCanAttack = new ArrayList<UnitCharacter>();

	public boolean isMapChange = false;
	private final int mRangerMove;
	private final int mRangerAttack;
	protected boolean isProcessMove = false;
	protected boolean isMyTeam;
	public int idEnemy;

	public UnitCharacter(Tile tileStart, boolean pIsMyTeam, int pRangerMove,
			int pRangerAttack, int pIdEnemy) {
		mRangerMove = pRangerMove;
		mRangerAttack = pRangerAttack;
		isMyTeam = pIsMyTeam;
		mTileTaget = tileStart;
		mTileTaget.setCharacterTaget(this);
		idEnemy = pIdEnemy;
		directionMap = new DirectionMap(sSystemRegistry.logicMap.mArrayMap,
				sSystemRegistry.mapTiles.arrayMap, mRangerMove);
		// directionMap.setPosStart(mTileTaget.xTile, mTileTaget.yTile);
		sSystemRegistry.logicMap.setIsCanMove(mTileTaget.xTile,
				mTileTaget.yTile, true);
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		// if (isMapChange || mTileTaget.xTile != directionMap.getPosStartX()
		// || mTileTaget.yTile != directionMap.getPosStartY()) {
		// directionMap.setPosStart(mTileTaget.xTile, mTileTaget.yTile);
		// updateListCharacterCanAttack();
		// isMapChange = false;
		// }
		if (isControl()) {
			checkMapChange(mTileTaget.xTile, mTileTaget.yTile);
			// Hien thi cac duong co the di
			directionMap.doSomeThingWithAll(someThingWithTile);
		}
	}

	public void checkMapChange(int xTileCurrent, int yTileCurrent) {
		if (isMapChange || xTileCurrent != directionMap.getPosStartX()
				|| yTileCurrent != directionMap.getPosStartY()) {
			directionMap.setPosStart(xTileCurrent, yTileCurrent);
			updateListCharacterCanAttack();
			isMapChange = false;
		}
	}

	/**
	 * 
	 * @param tileForcus
	 * @return 0:ko control dc<br>
	 *         1:move<br>
	 *         2:attack<br>
	 */
	public int onClick(Tile tileForcus) {
		if (!isControl()) {
			return 0;
		}
		byte moveCount = directionMap.getDirection(tileForcus.xTile,
				tileForcus.yTile);
		if (moveCount != -1) {
			if (isCanMove(mRangerMove - moveCount)) {
				// neu co the di chuyen dc
				// thi luu lai vet di chuyen
				// beginMoveAt(mTileTaget);
				// recordMove(mListMove, directionMap, tileForcus);
				// // roi moi cho di chuyen tren ban do
				// moveTo(tileForcus.xTile, tileForcus.yTile);
				// // Bat co` xu ly move
				// isProcessMove = true;

				outputMoveFromTileToTile(mTileTaget, tileForcus, mRangerMove
						- moveCount);
				return 1;
			}
		} else {
			if (isCanAttack()) {
				// neu taget vao character orther team
				for (UnitCharacter character : mListCharCanAttack) {
					if (tileForcus == character.getTileTaget()) {
						outputAttackOrtherCharacter(character);
						return 2;
					}
				}
			}
		}
		return 0;
	}

	@Override
	public void reset() {
	}

	private doSomeThingWithTile someThingWithTile = new doSomeThingWithTile() {

		@Override
		public void doSomeThing(Tile tile, byte total) {
			if (tile != null) {
				// sSystemRegistry.numberDrawableTime.drawNumberWithAlpha(tile.x,
				// tile.y, mRangerMove - total, 1, true, false,
				// Priority.CharacterBarAngle);
				if (isCanMove(mRangerMove - total)) {
					drawTileSeleted(tile, total);
				}
			}
		}
	};

	private void moveTo(int x, int y) {
		sSystemRegistry.logicMap.setIsCanMove(mTileTaget.xTile,
				mTileTaget.yTile, false);
		mTileTaget.setCharacterTaget(null);
		mTileTaget = sSystemRegistry.mapTiles.arrayMap[y][x];
		mTileTaget.setCharacterTaget(this);
		sSystemRegistry.logicMap.setIsCanMove(mTileTaget.xTile,
				mTileTaget.yTile, true);
		for (UnitCharacter character : sSystemRegistry.characterManager.arrayCharactersMyTeam) {
			character.isMapChange = true;
		}
		for (UnitCharacter character : sSystemRegistry.characterManager.arrayCharactersOtherTeam) {
			character.isMapChange = true;
		}
	}

	protected void doneProcessMove() {
		isProcessMove = false;
	}

	protected void recordMove(ArrayList<Move> pListMove,
			DirectionMap pDirectionMap, final Tile pTileTo) {
		pListMove.clear();
		Tile tileCheck = pTileTo;

		Move moveLast = new Move();
		moveLast.tileNext = pTileTo;
		pListMove.add(moveLast);

		while (tileCheck != null) {
			tileCheck = directionMap.getTileAroundTile(tileCheck.xTile,
					tileCheck.yTile, 1, moveLast);
			if (tileCheck != null) {
				Move move = new Move();
				move.tileNext = tileCheck;
				pListMove.add(move);
				moveLast = move;
			} else {
				break;
			}
		}
		// getTileAroundTile
	}

	// abstract
	protected void beginMoveAt(Tile tileBegin) {
	}

	public Tile getTileTaget() {
		return mTileTaget;
	}

	public void updateListCharacterCanAttack() {
		mListCharCanAttack.clear();
		ArrayList<UnitCharacterSwordmen> arrayCharacters = sSystemRegistry.characterManager.arrayCharactersOtherTeam;
		if (!isMyTeam) {
			arrayCharacters = sSystemRegistry.characterManager.arrayCharactersMyTeam;
		}
		for (UnitCharacter character : arrayCharacters) {
			Tile tile_forcus = character.getTileTaget();
			int xTileOffSet = tile_forcus.xTile - mTileTaget.xTile;
			int yTileOffset = tile_forcus.yTile - mTileTaget.yTile;
			int asbX = Math.abs(xTileOffSet);
			int asbY = Math.abs(yTileOffset);

			if (asbX <= mRangerAttack && asbY <= mRangerAttack) {
				if (xTileOffSet * yTileOffset >= 0) {
					// neu cung dau
					mListCharCanAttack.add(character);
				} else {
					if (asbX + asbY <= mRangerAttack) {
						mListCharCanAttack.add(character);
					}
				}
			}
		}
	}

	/**
	 * Ham dc viet cho other team
	 */
	public int controlMoveTo(int xTile, int yTile) {
		// Setup viec tim duong
		checkMapChange(mTileTaget.xTile, mTileTaget.yTile);
		//
		int moveCount = directionMap.getDirection(xTile, yTile);
		if (moveCount != -1) {
			// neu co the di chuyen dc
			// thi luu lai vet di chuyen
			beginMoveAt(mTileTaget);
			recordMove(mListMove, directionMap,
					sSystemRegistry.mapTiles.arrayMap[yTile][xTile]);
			// roi moi cho di chuyen tren ban do
			moveTo(xTile, yTile);
			// Bat co` xu ly move
			isProcessMove = true;
		} else {
			return 0;
		}
		return mRangerMove - moveCount;
	}

	abstract protected void outputAttackOrtherCharacter(
			UnitCharacter characterBeAttack);

	abstract protected void outputMoveFromTileToTile(Tile a, Tile b,
			int countMove);

	/**
	 * For Move
	 * 
	 * @param tile
	 * @param total
	 */
	abstract protected void drawTileSeleted(Tile tile, byte total);

	/**
	 * Check co nam trong view taget khong
	 * 
	 * @return
	 */
	abstract protected boolean isCanTaget();

	/**
	 * Check xu ly move va fire
	 * 
	 * @return
	 */
	abstract protected boolean isControl();

	abstract protected boolean isDeath();

	abstract protected boolean isCanAttack();

	/**
	 * Check xu ly di chuyen
	 * 
	 * @param so_nuoc_can_de_di_chuyen_toi
	 * @return
	 */
	abstract protected boolean isCanMove(int so_nuoc_can_de_di_chuyen_toi);

	/**
	 * Lay icon de hien thi
	 * 
	 * @param drawableMesh
	 */
	abstract void getIcon(DrawableMesh drawableMesh);

	protected void removeInPhysic() {
		sSystemRegistry.logicMap.setIsCanMove(mTileTaget.xTile,
				mTileTaget.yTile, false);
		if (isMyTeam) {
			sSystemRegistry.characterManager.arrayCharactersMyTeam.remove(this);
		} else {
			sSystemRegistry.characterManager.arrayCharactersOtherTeam
					.remove(this);
		}
		for (UnitCharacter character : sSystemRegistry.characterManager.arrayCharactersMyTeam) {
			character.isMapChange = true;
		}
	}

}
