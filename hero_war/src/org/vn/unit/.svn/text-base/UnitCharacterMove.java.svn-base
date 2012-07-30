package org.vn.unit;

import org.vn.gl.BaseObject;
import org.vn.model.Move;
import org.vn.model.Move.TypeMove;

public abstract class UnitCharacterMove extends UnitCharacter {
	protected float xDraw, yDraw;
	protected float mTimeTickMove, mDurationMove;
	protected Tile tileCurrentDraw;
	private Tile tileNextDraw;
	/**
	 * Huong cua nhan vat luc nay
	 */
	protected TypeMove mTypeMoveCurrent = TypeMove.right_bot;

	public UnitCharacterMove(Tile tileStart, boolean pIsMyTeam,
			int pRangerMove, int pRangerAttack, int pIdEnemy) {
		super(tileStart, pIsMyTeam, pRangerMove, pRangerAttack, pIdEnemy);
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		if (isProcessMove) {
			if (!processMove(timeDelta)) {
				if (mListMove.size() != 0) {
					if (tileNextDraw != null) {
						tileCurrentDraw = tileNextDraw;
					}
					Move move = mListMove.remove(mListMove.size() - 1);
					tileNextDraw = move.tileNext;
					mTypeMoveCurrent = move.typeMove;
					moveTo(tileCurrentDraw, tileNextDraw, 0.5f);
				} else {
					doneProcessMove();
				}
			}
		} else {
			xDraw = mTileTaget.x;
			yDraw = mTileTaget.y;
		}
		super.update(timeDelta, parent);

	}

	@Override
	protected void beginMoveAt(Tile tileBegin) {
		tileCurrentDraw = tileBegin;
		tileNextDraw = null;
	}

	private void moveTo(Tile tileA, Tile tileB, float pDurationTime) {
		mDurationMove = pDurationTime;
		mTimeTickMove = mDurationMove;
		tileCurrentDraw = tileA;
		tileNextDraw = tileB;
	}

	private boolean processMove(float timeDelta) {
		mTimeTickMove -= timeDelta;
		if (mTimeTickMove > 0) {
			float mu = 1 - mTimeTickMove / mDurationMove;
			xDraw = tileCurrentDraw.x + (tileNextDraw.x - tileCurrentDraw.x)
					* mu;
			yDraw = tileCurrentDraw.y + (tileNextDraw.y - tileCurrentDraw.y)
					* mu;
			return true;
		}
		return false;
	}

}
