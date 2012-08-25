package org.vn.unit;

import org.vn.gl.DrawableMesh;
import org.vn.gl.GameInfo;
import org.vn.gl.Texture;
import org.vn.gl.TextureLibrary;
import org.vn.herowar.R;
import org.vn.model.EnemyType;
import org.vn.model.Move.TypeMove;

/**
 * class dung de xu ly Animation<br>
 * Luu y: chi xu ly logic, khong lien quan den viec hien thi
 * 
 * @author duchq
 * 
 */
public class AnimationCharacter {
	private EnemyType mEnemyType;
	private Texture mTKing;

	public AnimationCharacter(TextureLibrary textureLibrary,
			EnemyType pEnemyType) {
		mEnemyType = pEnemyType;
		int[] id = { R.drawable.player_cannon, R.drawable.player_geezer,
				R.drawable.player_gunslinger, R.drawable.player_indian,
				R.drawable.player_mexican, R.drawable.player_scout };
		int[] idShoot = { R.drawable.player_cannon_shot,
				R.drawable.player_geezer_shot,
				R.drawable.player_gunslinger_shot,
				R.drawable.player_indian_shot, R.drawable.player_mexican_shot,
				R.drawable.player_scout_shot };

		mTextures = new Texture[2];
		mTextures[0] = textureLibrary.allocateTexture(
				id[Math.abs(pEnemyType.armyType) % id.length], "player_geezer");
		mTextures[1] = textureLibrary.allocateTexture(
				idShoot[Math.abs(pEnemyType.armyType) % idShoot.length],
				"player_geezer_shot");
		mBitmapAnimation = new DrawableMesh(mTextures[0], GameInfo.offset,
				GameInfo.offset);
		mTKing = textureLibrary.allocateTexture(R.drawable.image_1857,
				"image_1857");
	}

	public enum DirectionType {
		HOLD, MOVE, FIRE
	}

	public DrawableMesh updateColumn(float timeDelta,
			UnitCharacterSwordmen pCharacterSwordmen) {
		DirectionType mActionTypeCurrent;
		if (pCharacterSwordmen.isProcessMove) {
			// Ve luc di chuyen
			mActionTypeCurrent = DirectionType.MOVE;
		} else if (timeProcessShoot > 0) {
			mActionTypeCurrent = DirectionType.FIRE;
		} else {
			// Hold
			mActionTypeCurrent = DirectionType.HOLD;
		}
		if (mDirectionTypeLast != mActionTypeCurrent
				|| mTypeMoveLast != pCharacterSwordmen.mTypeMoveCurrent) {
			setupIndextDraw(pCharacterSwordmen.mTypeMoveCurrent,
					mActionTypeCurrent);
			mTypeMoveLast = pCharacterSwordmen.mTypeMoveCurrent;
			mDirectionTypeLast = mActionTypeCurrent;
		}

		switch (mActionTypeCurrent) {
		case MOVE:
			x = (int) ((pCharacterSwordmen.mTimeTickMove % 0.032f) / 0.032f * x_size)
					+ x_begin;
			break;
		case FIRE:
			// 1.5s -> 1s la chuan bi ban
			if (timeProcessShoot > 1) {
				x = x_begin;
			} else {
				x = x_begin + 1;
				if (mProcessShoot != null) {
					mProcessShoot.processShoot();
					mProcessShoot = null;
				}
			}
			timeProcessShoot -= timeDelta;
			break;
		default:
			x = x_begin;
			break;
		}
		mBitmapAnimation.setTexture(mTextures[currentDrawAble]);
		mBitmapAnimation.setCoordinates(getScaleX1(), getScaleX2(),
				getScaleY1(), getScaleY2());
		return mBitmapAnimation;
	}

	private DrawableMesh mBitmapAnimation;
	private Texture[] mTextures;
	private DirectionType mDirectionTypeLast = null;
	private TypeMove mTypeMoveLast = null;
	private int x, y;
	private int currentDrawAble = 0;
	private int x_begin;
	private int x_size;
	private int mMaxColumn;
	private int mMaxRow;
	private boolean isFlip;

	private float timeProcessShoot = 0;

	public void setupIndextDraw(TypeMove typeMoveCurrent,
			DirectionType actionType) {
		if (actionType == DirectionType.HOLD) {
			x_begin = 0;
			x_size = 1;
			currentDrawAble = 0;
			mMaxColumn = 3;
			mMaxRow = 5;
		} else if (actionType == DirectionType.MOVE) {
			x_begin = 1;
			x_size = 2;
			currentDrawAble = 0;
			mMaxColumn = 3;
			mMaxRow = 5;
		} else if (actionType == DirectionType.FIRE) {
			x_begin = 0;
			x_size = 2;
			currentDrawAble = 1;
			mMaxColumn = 2;
			mMaxRow = 5;
		}
		switch (typeMoveCurrent) {
		case left:
			y = 1;
			isFlip = false;
			break;
		case right:
			y = 1;
			isFlip = true;
			break;
		case left_top:
			y = 2;
			isFlip = true;
			break;
		case left_bot:
			y = 0;
			isFlip = false;
			break;
		case right_top:
			y = 2;
			isFlip = false;
			break;
		case right_bot:
			y = 0;
			isFlip = true;
			break;
		}
	}

	public float getScaleX1() {
		if (!isFlip)
			return ((float) x) / mMaxColumn;
		else
			return ((float) x + 1) / mMaxColumn;
	}

	public float getScaleX2() {
		if (!isFlip)
			return ((float) x + 1) / mMaxColumn;
		else
			return ((float) x) / mMaxColumn;
	}

	public float getScaleY1() {
		return ((float) y) / mMaxRow;
	}

	public float getScaleY2() {
		return ((float) y + 1) / mMaxRow;
	}

	private iProcessShoot mProcessShoot;

	public void shoot(iProcessShoot processShoot) {
		timeProcessShoot = 1.5f;
		mProcessShoot = processShoot;
	}

	public interface iProcessShoot {
		public void processShoot();
	}

	public void setIcon(DrawableMesh drawableMesh) {
		if (mEnemyType.armyType == GameInfo.idTypeKing) {
			drawableMesh.setTexture(mTKing);
			drawableMesh.setCoordinates(0, 1, 0, 1);
		} else {
			drawableMesh.setTexture(mTextures[0]);
			drawableMesh.setCoordinates(1f / 3, 0, 0, 1f / 5);
		}
	}
}
