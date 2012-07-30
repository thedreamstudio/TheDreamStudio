package org.vn.unit;

import org.vn.gl.BaseObject;
import org.vn.gl.DrawableBitmap;
import org.vn.gl.DrawableMesh;
import org.vn.gl.GameInfo;
import org.vn.gl.Priority;
import org.vn.gl.TextureLibrary;
import org.vn.gl.Utils;
import org.vn.gl.Vector2;
import org.vn.herowar.R;
import org.vn.model.AttackMessage;
import org.vn.model.EnemyType;
import org.vn.model.Move.TypeMove;
import org.vn.model.MoveMessage;
import org.vn.unit.AnimationCharacter.iProcessShoot;

public class UnitCharacterSwordmen extends UnitCharacterMove {
	private static int WIDTH_DRAW_HEALTH = 20;
	private DrawableMesh mDrawableBitmapChar;
	private DrawableBitmap mDrawableBitmapAttack;
	private DrawableBitmap mDrawableBitmapRangerAttack;
	private DrawableMesh mDrawableBitmapHpFull;
	private DrawableMesh mDrawableBitmapHpCurrent;
	private DrawableBitmap mDrawableBitmapMove;
	private DrawableBitmap mDrawableBitmapTouchToControl;
	private DrawableBitmap mDrawableBitmapTouchToAttack;
	private Vector2 posDraw = new Vector2();
	/**
	 * Thong so
	 */
	final public EnemyType mEnemyType;
	public float mMaxHp;
	public int mCurrentHp;
	private AnimationCharacter mAnimationCharacter;
	private boolean isProcessFire = false;
	public int mMaxMana;
	public int mCurrentMana;

	public UnitCharacterSwordmen(TextureLibrary textureLibrary, Tile tileStart,
			boolean pIsMyTeam, EnemyType pEnemyType, int pIdEnemy) {
		super(tileStart, pIsMyTeam, pEnemyType.rangeview,
				pEnemyType.rangeattack, pIdEnemy);
		if (isMyTeam) {
			sSystemRegistry.characterManager.arrayCharactersMyTeam.add(this);
		} else {
			sSystemRegistry.characterManager.arrayCharactersOtherTeam.add(this);
		}
		// sSystemRegistry.characterManager.mapEnemyInGame.put(pIdEnemy, this);
		mEnemyType = pEnemyType;
		mMaxHp = mEnemyType.hp;
		mCurrentHp = mEnemyType.hp;
		mMaxMana = mEnemyType.mana;
		mCurrentMana = mEnemyType.mana;
		isMyTeam = pIsMyTeam;
		mAnimationCharacter = new AnimationCharacter(textureLibrary, mEnemyType);
		{
			mDrawableBitmapAttack = new DrawableBitmap(
					textureLibrary.allocateTexture(R.drawable.sword, "sword"),
					26, 26);
		}
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
					textureLibrary.allocateTexture(R.drawable.circle_while,
							"circle"), GameInfo.offset - 24,
					GameInfo.offset - 24);
			mDrawableBitmapMove.setColorExpressF(0f, 1f, 0f, 1f);
		}
		{
			mDrawableBitmapTouchToControl = new DrawableBitmap(
					textureLibrary.allocateTexture(R.drawable.circle_while,
							"circle"), GameInfo.offset - 10,
					GameInfo.offset - 10);
			mDrawableBitmapTouchToControl.setColorExpressF(0f, 0f, 1f, 1f);
		}
		{
			mDrawableBitmapTouchToAttack = new DrawableBitmap(
					textureLibrary.allocateTexture(R.drawable.circle_while,
							"circle"), GameInfo.offset - 10,
					GameInfo.offset - 10);
			mDrawableBitmapTouchToAttack.setColorExpressF(1f, 0f, 0f, 1f);
		}
		{
			mDrawableBitmapHpCurrent = new DrawableMesh(
					textureLibrary.allocateTexture(R.drawable.hp_bar, "hp_bar"),
					WIDTH_DRAW_HEALTH, 2);
			mDrawableBitmapHpFull = new DrawableMesh(
					textureLibrary.allocateTexture(R.drawable.hp_bar, "hp_bar"),
					WIDTH_DRAW_HEALTH, 2);
			mDrawableBitmapHpFull.setColorExpress(0, 0, 0, 255);
		}
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		if (isDeath()) {
			return;
		}
		super.update(timeDelta, parent);

		mDrawableBitmapChar = mAnimationCharacter.updateColumn(timeDelta, this);
		posDraw.x = xDraw - mDrawableBitmapChar.getWidth() * 0.5f;
		posDraw.y = yDraw - mDrawableBitmapChar.getHeight() * 0.5f;
		sSystemRegistry.renderSystem.scheduleForDraw(mDrawableBitmapChar,
				posDraw, Priority.Character, true);
		if (!isMyTeam) {
			sSystemRegistry.renderSystem.scheduleForDraw(
					mDrawableBitmapAttack,
					Vector2.TAMP.set(xDraw - mDrawableBitmapAttack.getWidth()
							* 0.5f, yDraw + mDrawableBitmapAttack.getHeight()),
					Priority.CharacterCicler, true);
		} else {
			if (sSystemRegistry.inputGameInterface.isControl()) {
				sSystemRegistry.renderSystem.scheduleForDraw(
						mDrawableBitmapTouchToControl,
						Vector2.TAMP.set(
								xDraw
										- mDrawableBitmapTouchToControl
												.getWidth() * 0.5f,
								yDraw
										- mDrawableBitmapTouchToControl
												.getHeight() * 0.5f),
						Priority.CharacterCicler, true);
			}
		}

		if (!isProcessMove) {
			if (mTileTaget.isForcus()) {
				if (isCanAttack()) {
					// Neu ko xu ly move thi hien thi vung co the taget
					for (UnitCharacter character : mListCharCanAttack) {
						Tile tile = character.getTileTaget();
						sSystemRegistry.renderSystem.scheduleForDraw(
								mDrawableBitmapTouchToAttack, Vector2.TAMP.set(
										tile.x
												- mDrawableBitmapTouchToAttack
														.getWidth() * 0.5f,
										tile.y
												- mDrawableBitmapTouchToAttack
														.getHeight() * 0.5f),
								Priority.CharacterCicler, true);
					}
				}
				sSystemRegistry.renderSystem.scheduleForDraw(
						mDrawableBitmapRangerAttack, Vector2.TAMP.set(
								mTileTaget.x
										- mDrawableBitmapRangerAttack
												.getWidth() * 0.5f,
								mTileTaget.y
										- mDrawableBitmapRangerAttack
												.getHeight() * 0.5f),
						Priority.CiclerCharacterAttack, true);
			}
		}
		drawHp();
	}

	@Override
	protected void outputAttackOrtherCharacter(UnitCharacter characterBeAttack) {
		// BaseObject.sSystemRegistry.mapTiles.setTileForcus(mTileTaget);
		if (GameInfo.isOnline) {
			sSystemRegistry.mGS.ATTACH(idEnemy, characterBeAttack.idEnemy);
		} else {
			ActionServerToClient actionServerToClient = new ActionServerToClient();
			AttackMessage attackMessage = new AttackMessage();
			attackMessage.idBeAttacker = characterBeAttack.idEnemy;
			UnitCharacterSwordmen characterSwordmenBeAttack = (UnitCharacterSwordmen) characterBeAttack;
			attackMessage.hpIdBeAttack = characterSwordmenBeAttack.mCurrentHp
					- mEnemyType.damageMin
					- Utils.RANDOM.nextInt(mEnemyType.damageMax
							- mEnemyType.damageMin);
			processAttackInputFromServer(actionServerToClient, attackMessage);
		}
	}

	@Override
	protected void outputMoveFromTileToTile(Tile a, Tile b, int countMove) {
		if (GameInfo.isOnline) {
			sSystemRegistry.mGS.MOVE_ARMY(idEnemy, b.xTile, b.yTile);
		} else {
			ActionServerToClient actionServerToClient = new ActionServerToClient();
			MoveMessage moveMessage = new MoveMessage();
			moveMessage.idMove = idEnemy;
			moveMessage.xTypeMoveNext = b.xTile;
			moveMessage.yTypeMoveNext = b.yTile;
			processMoveInputFromServer(actionServerToClient, moveMessage);
		}
	}

	public void processMoveInputFromServer(
			ActionServerToClient pActionServerToClientCurrent,
			MoveMessage moveMessage) {
		mCurrentMana -= controlMoveTo(moveMessage.xTypeMoveNext,
				moveMessage.yTypeMoveNext) * mEnemyType.movecost;
		pActionServerToClientCurrent.done();
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
		mCurrentMana -= mEnemyType.attackcost;
		mAnimationCharacter.shoot(new iProcessShoot() {
			@Override
			public void processShoot() {
				UnitCharacterSwordmen UnitBeAtack = sSystemRegistry.characterManager.mapEnemyInGame
						.get(attackMessage.idBeAttacker);
				UnitBeAtack.takeDamage(UnitBeAtack.mCurrentHp
						- attackMessage.hpIdBeAttack);
				pActionServerToClientCurrent.done();
				isProcessFire = false;
			}
		});
	}

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
		float vi_tri_y_ve_ten_va_mau = yDraw - 20;
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
				mTileTaget.y + 30);
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
		if (so_nuoc_can_de_di_chuyen_toi <= mCurrentMana / mEnemyType.movecost) {
			return true;
		}
		return false;
		// return true;
	}

	@Override
	protected boolean isCanAttack() {
		if (mCurrentMana >= mEnemyType.attackcost) {
			return true;
		}
		return false;
		// return true;
	}

	public void nextTurn() {
		mCurrentMana = mEnemyType.mana;
	}
}
