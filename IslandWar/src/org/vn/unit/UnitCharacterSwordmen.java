package org.vn.unit;

import org.vn.cache.CurrentGameInfo;
import org.vn.gl.BaseObject;
import org.vn.gl.DrawableBitmap;
import org.vn.gl.DrawableMesh;
import org.vn.gl.GameInfo;
import org.vn.gl.Priority;
import org.vn.gl.TextureLibrary;
import org.vn.gl.Vector2;
import org.vn.herowar.R;
import org.vn.model.AttackMessage;
import org.vn.model.EnemyType;
import org.vn.model.Move.TypeMove;
import org.vn.model.MoveMessage;
import org.vn.unit.AnimationCharacter.iProcessShoot;

public class UnitCharacterSwordmen extends UnitCharacterMove {
	private static int WIDTH_DRAW_HEALTH = 30;
	private DrawableMesh mDrawableBitmapChar;
	private DrawableBitmap mDrawableBitmapRangerAttack;
	private DrawableMesh mDrawableBitmapHpFull;
	private DrawableMesh mDrawableBitmapHpCurrent;
	private DrawableBitmap mDrawableBitmapMove;
	private DrawableBitmap mDrawableBitmapTouchToControl;
	// private DrawableBitmap mDrawableBitmapTouchToAttack;
	private DrawableBitmap mDark2;
	private DrawableBitmap mDark2Red;
	private Vector2 posDraw = new Vector2();
	public float mMaxHp;
	public int mCurrentHp;
	private AnimationCharacter mAnimationCharacter;
	private boolean isProcessFire = false;
	public int mMaxMana;
	public int mCurrentMana;
	private boolean isShoot = false;
	public DrawableBitmap mDrawableTileAtack;
	float size_contro = 10;
	float opacityForDrawTileSeleted = 0;
	float vO = 1;

	public UnitCharacterSwordmen(TextureLibrary textureLibrary, Tile tileStart,
			boolean pIsMyTeam, EnemyType pEnemyType, int pIdEnemy) {
		super(tileStart, pIsMyTeam, pEnemyType.rangeview,
				pEnemyType.rangeattack, pIdEnemy, pEnemyType);
		if (isMyTeam) {
			sSystemRegistry.characterManager.arrayCharactersMyTeam.add(this);
		} else {
			sSystemRegistry.characterManager.arrayCharactersOtherTeam.add(this);
		}
		sSystemRegistry.characterManager.mapEnemyInGame.put(pIdEnemy, this);
		mMaxHp = mEnemyType.hp;
		mCurrentHp = mEnemyType.hp;
		mMaxMana = mEnemyType.mana;
		mCurrentMana = mEnemyType.mana;
		isMyTeam = pIsMyTeam;
		mAnimationCharacter = new AnimationCharacter(textureLibrary, mEnemyType);
		// Ranger attack
		{
			mDrawableBitmapRangerAttack = new DrawableBitmap(
					textureLibrary.allocateTexture(R.drawable.circle, "circle"),
					(pEnemyType.rangeattack * 2) * GameInfo.offset,
					(pEnemyType.rangeattack * 2) * GameInfo.offset);
			// mDrawableBitmapCircle.setOpacity(0.5f);
			mDrawableBitmapRangerAttack
					.setColorExpressF(0.1f, 0.1f, 0.1f, 0.4f);
		}
		{
			mDrawableBitmapMove = new DrawableBitmap(
					textureLibrary.allocateTextureNotHash(R.drawable.tile,
							"circle"), GameInfo.offset, GameInfo.offset);
			// mDrawableBitmapMove.setColorExpressF(0f, 1f, 0f, 1f);
		}
		{
			mDrawableBitmapTouchToControl = new DrawableBitmap(
					textureLibrary
							.allocateTexture(R.drawable.shadown, "circle"),
					GameInfo.offset - 20, (GameInfo.offset - 20) / 2);
			if (isMyTeam)
				mDrawableBitmapTouchToControl
						.setColorExpressF(0f, 0f, 1f, 0.5f);
			else
				mDrawableBitmapTouchToControl
						.setColorExpressF(1f, 0f, 0f, 0.5f);

		}
		// {
		// mDrawableBitmapTouchToAttack = new DrawableBitmap(
		// textureLibrary.allocateTexture(R.drawable.circle_while,
		// "circle"), GameInfo.offset - 10,
		// GameInfo.offset - 10);
		// mDrawableBitmapTouchToAttack.setColorExpressF(1f, 0f, 0f, 1f);
		// }
		{
			mDrawableBitmapHpCurrent = new DrawableMesh(
					textureLibrary.allocateTexture(R.drawable.hp_bar, "hp_bar"),
					WIDTH_DRAW_HEALTH, 2);
			mDrawableBitmapHpFull = new DrawableMesh(
					textureLibrary.allocateTexture(R.drawable.hp_bar, "hp_bar"),
					WIDTH_DRAW_HEALTH, 2);
			mDrawableBitmapHpFull.setColorExpress(0, 0, 0, 255);
		}
		// rangerview
		{
			mDark2 = new DrawableBitmap(textureLibrary.allocateTexture(
					R.drawable.dark2, "dark2"), (pEnemyType.rangeview * 2)
					* GameInfo.offset + GameInfo.offset,
					(pEnemyType.rangeview * 2) * GameInfo.offset
							+ GameInfo.offset);
			// mDark2.setColorExpressF(1f, 1f, 0f, 1f);
			// mDark2.setGlBlendFun(GL10.GL_ONE, GL10.GL_ONE);
			mDark2Red = new DrawableBitmap(textureLibrary.allocateTexture(
					R.drawable.dark1, "dark2"), (pEnemyType.rangeattack * 2)
					* GameInfo.offset, (pEnemyType.rangeattack * 2)
					* GameInfo.offset);
			mDark2Red.setColorExpressF(1f, 0f, 0f, 1f);
		}
		if (mEnemyType.armyType == GameInfo.idTypeKing) {
			BaseObject.sSystemRegistry.unitEffects.createCotCo(textureLibrary,
					tileStart.x - 5, tileStart.y - 10);
		}

		// Con tro?
		mDrawableTileAtack = new DrawableBitmap(textureLibrary.allocateTexture(
				R.drawable.tile_seleted_atack, "tile_seleted"), 16, 16);
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		if (isDeath()) {
			return;
		}
		super.update(timeDelta, parent);
		if (isControl()) {
			opacityForDrawTileSeleted += timeDelta * vO * 0.2f;
			if (opacityForDrawTileSeleted > 0.8f) {
				opacityForDrawTileSeleted = 0.8f;
				vO = -1;
			} else if (opacityForDrawTileSeleted < 0) {
				opacityForDrawTileSeleted = 0;
				vO = 1;
			}
			mDrawableBitmapMove.setOpacity(opacityForDrawTileSeleted + 0.2f);
			mDark2Red.setOpacity(1 - opacityForDrawTileSeleted);
		} else {
			mDrawableBitmapMove.setOpacity(0.8f);
			mDark2Red.setOpacity(0.8f);
		}
		if (isCanTaget()) {
			drawCharacter(timeDelta);
		}
	}

	private void drawCharacter(float timeDelta) {
		mDrawableBitmapChar = mAnimationCharacter.updateColumn(timeDelta, this);
		if (mEnemyType.armyType != GameInfo.idTypeKing) {
			posDraw.x = xDraw - mDrawableBitmapChar.getWidth() * 0.5f;
			posDraw.y = yDraw - mDrawableBitmapChar.getHeight() * 0.3f;
			sSystemRegistry.renderSystem.scheduleForDraw(mDrawableBitmapChar,
					posDraw, Priority.Character, true);
		}
		if (!isMyTeam) {
			drawHinhTronMauDoLenNhanVat();
		} else {
			drawHinhTronMauXanhLenNhanVat();
		}

		if (!isProcessMove) {
			if (mTileTaget.isForcus() && isControl()) {
				if (isCanAttack()) {
					// Neu ko xu ly move thi hien thi vung co the taget
					drawAtackSight();
					if (mListCharCanAttack.size() > 0) {
						size_contro -= timeDelta * 30;
						if (size_contro < 0) {
							size_contro = 10;
						}
						float size = Math.min(1,
								sSystemRegistry.cameraSystem.mScale);
						size = GameInfo.offset / size;
						for (UnitCharacter character : mListCharCanAttack) {
							if (!character.isDeath()) {
								Tile tile = character.getTileTaget();
								mDrawableTileAtack.setWidth(size);
								mDrawableTileAtack.setHeight(size);
								sSystemRegistry.renderSystem.scheduleForDraw(
										mDrawableTileAtack,
										Vector2.TAMP.set(tile.x
												- mDrawableTileAtack.getWidth()
												/ 2, tile.y + size_contro),
										Priority.tile_atack, true);
							}
						}
					}
				}
			}
		}
		// view sight
		if (isMyTeam) {
			drawViewSight();
		}
		drawHp();
	}

	private void drawViewSight() {
		sSystemRegistry.renderSystem.scheduleForDraw(
				mDark2,
				Vector2.TAMP.set(xDraw - mDark2.getWidth() * 0.5f, yDraw
						- mDark2.getHeight() * 0.5f), Priority.BackGroundDark2,
				true);
	}

	private void drawAtackSight() {
		sSystemRegistry.renderSystem.scheduleForDraw(
				mDark2Red,
				Vector2.TAMP.set(xDraw - mDark2Red.getWidth() * 0.5f, yDraw
						- mDark2Red.getHeight() * 0.5f),
				Priority.BackGroundAtackSight, true);
	}

	private void drawHinhTronMauXanhLenNhanVat() {
		sSystemRegistry.renderSystem.scheduleForDraw(
				mDrawableBitmapTouchToControl, Vector2.TAMP.set(xDraw
						- mDrawableBitmapTouchToControl.getWidth() * 0.5f,
						yDraw - GameInfo.offset * 0.3f),
				Priority.CharacterCicler, true);
	}

	private void drawHinhTronMauDoLenNhanVat() {
		sSystemRegistry.renderSystem.scheduleForDraw(
				mDrawableBitmapTouchToControl, Vector2.TAMP.set(xDraw
						- mDrawableBitmapTouchToControl.getWidth() * 0.5f,
						yDraw - GameInfo.offset * 0.3f),
				Priority.CharacterCicler, true);
	}

	@Override
	protected void outputAttackOrtherCharacter(UnitCharacter characterBeAttack) {
		// BaseObject.sSystemRegistry.mapTiles.setTileForcus(mTileTaget);
		// Fai bat co` nay len tranh tinh trang clinet touck nhieu lan
		isProcessFire = true;
		sSystemRegistry.mGS.ATTACH(idEnemy, characterBeAttack.idEnemy);
	}

	@Override
	protected void outputMoveFromTileToTile(Tile a, Tile b, int countMove) {
		// Fai bat co` nay len tranh tinh trang clinet touck nhieu lan
		isProcessMove = true;
		sSystemRegistry.mGS.MOVE_ARMY(idEnemy, b.xTile, b.yTile);
	}

	private ActionServerToClient actionMove = null;

	@Override
	protected void doneProcessMove() {
		super.doneProcessMove();
		if (actionMove != null) {
			actionMove.done();
		}
	}

	public void processMoveInputFromServer(
			ActionServerToClient pActionServerToClientCurrent,
			MoveMessage moveMessage) {
		mCurrentMana -= controlMoveTo(moveMessage.xTypeMoveNext,
				moveMessage.yTypeMoveNext) * mEnemyType.movecost;
		actionMove = pActionServerToClientCurrent;
	}

	public void processAttackInputFromServer(
			final ActionServerToClient pActionServerToClientCurrent,
			final AttackMessage attackMessage) {
		UnitCharacterSwordmen _UnitBeAtack = sSystemRegistry.characterManager.mapEnemyInGame
				.get(attackMessage.idBeAttacker);
		float offsetXFire = _UnitBeAtack.mTileTaget.x - mTileTaget.x;
		float offsetYFire = _UnitBeAtack.mTileTaget.y - mTileTaget.y;
		boolean isRight = offsetXFire > 0;
		int abc = 0;
		if (offsetYFire > 0) {
			if (Math.abs(offsetXFire) > offsetYFire) {
				abc = 0;
			} else {
				abc = 1;
			}
		} else {
			if (Math.abs(offsetXFire) > -offsetYFire) {
				abc = 0;
			} else {
				abc = -1;
			}
		}
		if (isRight) {
			switch (abc) {
			case 1:
				mTypeMoveCurrent = TypeMove.left_top;
				break;
			case 0:
				mTypeMoveCurrent = TypeMove.right;
				break;
			case -1:
				mTypeMoveCurrent = TypeMove.right_bot;
				break;
			}
		} else {
			switch (abc) {
			case 1:
				mTypeMoveCurrent = TypeMove.right_top;
				break;
			case 0:
				mTypeMoveCurrent = TypeMove.left;
				break;
			case -1:
				mTypeMoveCurrent = TypeMove.left_bot;
				break;
			}
		}
		isProcessFire = true;
		isShoot = false;
		// mCurrentMana -= mEnemyType.attackcost;
		mAnimationCharacter.shoot(new iProcessShoot() {
			@Override
			public void processShoot() {
				UnitCharacterSwordmen UnitBeAtack = sSystemRegistry.characterManager.mapEnemyInGame
						.get(attackMessage.idBeAttacker);
				UnitBeAtack.takeDamage(UnitBeAtack.mCurrentHp
						- attackMessage.hpIdBeAttack);
				pActionServerToClientCurrent.done();
				isProcessFire = false;
				BaseObject.sSystemRegistry.soundManager.playShoot(mEnemyType);
			}
		});
	}

	// Ve tile co the di chuyen dc
	@Override
	protected void drawTileSeleted(Tile tile, byte total) {
		sSystemRegistry.renderSystem.scheduleForDraw(mDrawableBitmapMove,
				Vector2.TAMP.set(
						tile.x - mDrawableBitmapMove.getWidth() * 0.5f, tile.y
								- mDrawableBitmapMove.getHeight() * 0.5f),
				Priority.CiclerCharacterMove, true);
	}

	private void drawHp() {
		float vi_tri_x_ve_ten_va_mau = xDraw - mDrawableBitmapHpFull.getWidth()
				* 0.5f;
		float vi_tri_y_ve_ten_va_mau = yDraw - 25;
		{
			sSystemRegistry.renderSystem
					.scheduleForDraw(mDrawableBitmapHpFull, new Vector2(
							vi_tri_x_ve_ten_va_mau, vi_tri_y_ve_ten_va_mau),
							Priority.Character, true);
			float fanTramSoMauConLai = (float) mCurrentHp
					/ (mMaxHp == 0 ? 1 : mMaxHp);
			mDrawableBitmapHpCurrent.setWidth(WIDTH_DRAW_HEALTH
					* fanTramSoMauConLai);
			mDrawableBitmapHpCurrent
					.setCoordinates(0, fanTramSoMauConLai, 0, 1);
			if (fanTramSoMauConLai >= 0.5) {
				fanTramSoMauConLai = (fanTramSoMauConLai - 0.5f) * 2;
				mDrawableBitmapHpCurrent.setColorExpress(
						1 - fanTramSoMauConLai, 1, 0.3f, 1);
			} else {
				fanTramSoMauConLai = fanTramSoMauConLai * 2;
				mDrawableBitmapHpCurrent.setColorExpress(1, fanTramSoMauConLai,
						0.3f, 1);
			}

			sSystemRegistry.renderSystem.scheduleForDraw(
					mDrawableBitmapHpCurrent, new Vector2(
							vi_tri_x_ve_ten_va_mau, vi_tri_y_ve_ten_va_mau),
					Priority.Character, true);
		}
	}

	public void takeDamage(int damage) {
		mCurrentHp -= damage;
		if (mCurrentHp <= 0) {
			mCurrentHp = 0;
			removeInPhysic();
		}
		sSystemRegistry.unitEffects.addEffectRungManHinh();
		sSystemRegistry.unitEffects.addEffectSoBayLen(
				sSystemRegistry.numberDrawableTakeDame, -damage, mTileTaget.x,
				mTileTaget.y + 30, true, Priority.CharacterTakeDamage);
	}

	@Override
	protected boolean isControl() {
		if (mTileTaget.isForcus() && isMyTeam && !isProcessMove
				&& !isProcessFire && !isDeath()
				&& sSystemRegistry.inputGameInterface.isControl()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isDeath() {
		if (mCurrentHp > 0) {
			return false;
		}
		return true;
	}

	@Override
	public void getIcon(DrawableMesh drawableMesh) {
		mAnimationCharacter.setIcon(drawableMesh);
	}

	@Override
	protected boolean isCanMove(int so_nuoc_can_de_di_chuyen_toi) {
		if (mEnemyType.movecost == 0) {
			return false;
		}
		if (so_nuoc_can_de_di_chuyen_toi <= mCurrentMana / mEnemyType.movecost) {
			return true;
		}
		return false;
		// return true;
	}

	@Override
	protected boolean isCanAttack() {
		return isShoot;
		// if (mCurrentMana >= mEnemyType.attackcost) {
		// return true;
		// }
		// return false;
	}

	public void nextTurn() {
		mCurrentMana = mEnemyType.mana;
		isShoot = true;
	}

	@Override
	protected boolean isCanTaget() {
		if (!CurrentGameInfo.getIntance().isSuongMu) {
			return true;
		}
		if (isMyTeam)
			return true;

		for (UnitCharacterSwordmen character : sSystemRegistry.characterManager.arrayCharactersMyTeam) {
			Tile tile_forcus = character.getTileTaget();
			int rangerCheck = character.mEnemyType.rangeview;
			int x1 = tile_forcus.xTile;
			int y1 = tile_forcus.yTile;
			int x2 = mTileTaget.xTile;
			int y2 = mTileTaget.yTile;

			if (isInSight(x1, y1, x2, y2, rangerCheck)) {
				return true;
			}
			// int xTileOffSet = x1 - x2;
			// int yTileOffset = y1 - y2;
			// int asbX = Math.abs(xTileOffSet);
			// int asbY = Math.abs(yTileOffset);
			// if (asbX <= rangerCheck && asbY <= rangerCheck) {
			// if (xTileOffSet * yTileOffset >= 0) {
			// // neu cung dau
			// return true;
			// } else {
			// if (asbX + asbY <= rangerCheck) {
			// return true;
			// }
			// }
			// }
		}

		return false;
	}

	public static boolean isInSight(int x1, int y1, int x2, int y2,
			int rangerCheck) {
		int xTileOffSet = x1 - x2;
		int yTileOffset = y1 - y2;
		int asbX = Math.abs(xTileOffSet);
		int asbY = Math.abs(yTileOffset);
		if (asbX <= rangerCheck && asbY <= rangerCheck) {
			if (xTileOffSet * yTileOffset >= 0) {
				// neu cung dau
				return true;
			} else {
				if (asbX + asbY <= rangerCheck) {
					return true;
				}
			}
		}
		return false;
	}
}
