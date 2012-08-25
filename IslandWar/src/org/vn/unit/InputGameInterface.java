package org.vn.unit;

import java.util.ArrayList;

import org.vn.cache.CurrentGameInfo;
import org.vn.cache.CurrentUserInfo;
import org.vn.gl.BaseObject;
import org.vn.gl.ContextParameters;
import org.vn.gl.DebugLog;
import org.vn.gl.DrawableBitmap;
import org.vn.gl.GameInfo;
import org.vn.gl.MultiTouchFilter2;
import org.vn.gl.Priority;
import org.vn.gl.RenderSystem;
import org.vn.gl.SingerTouchDetector.ITouchDetectorListener;
import org.vn.gl.TextureLibrary;
import org.vn.gl.Vector2;
import org.vn.herowar.R;
import org.vn.model.AttackMessage;
import org.vn.model.BuyEnemy;
import org.vn.model.EnemyType;
import org.vn.model.Money;
import org.vn.model.MoveMessage;
import org.vn.model.NextTurnMessage;
import org.vn.model.TouchTouch;
import org.vn.model.TouchTouch.TouchTouchType;
import org.vn.unit.ActionServerToClient.ActionStatus;

import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * class dung de xu ly tat ca cac input vao game client<br>
 * <b>input_touch input_server
 * 
 * @author duchq
 * 
 */
public class InputGameInterface extends BaseObject {

	public boolean isChoPhepTouchDiChuyenCamera = true;
	private ContextParameters mContextParameters;
	private boolean isEndGame = false;
	public boolean isXuLyShoot = false;
	private float scaleTouch = -1;
	private GestureDetector gd = null;
	public Vector2 mPositionTouchLast = new Vector2(-1, -1);
	// private Vector2 mPositionTouch = new Vector2(-1, -1);

	// public Vector2 mPositionClick_HasCamera = new Vector2(-1, -1);
	// public Vector2 mPositionClick_NoCamera = new Vector2(-1, -1);
	// private Vector2 mPositionClickTamp = new Vector2(-1, -1);
	public TouchTouch mTouchTouch = new TouchTouch();

	private boolean mSendLoadingUnitComplete;

	private Vector2 mPosBtChat;

	private boolean isTurnOnChat = false;
	public float xOffset = 0, yOffset = 0;
	private float xBefore = -1, yBefore = -1;
	private DrawableBitmap mDrawableBitmapTouch;
	private DrawableBitmap mDrawableBitmapChat;

	private ActionList mActionList;
	private ActionServerToClient mActionServerToClientCurrent = null;
	public float mTimeTickInTurn = 0;
	private int mIdPlayerTurn = -1;
	private CurrentGameInfo mCurrentGameInfo = CurrentGameInfo.getIntance();

	public InputGameInterface(TextureLibrary textureLibrary) {
		super();
		mActionList = ActionList.getInstance();
		mContextParameters = sSystemRegistry.contextParameters;
		isEndGame = false;
		gd = new GestureDetector(mContextParameters.context,
				new LearnGestureListener());

		mSendLoadingUnitComplete = false;
		{
			sSystemRegistry.multiTouchFilter2
					.setTouchDetectorListener(new ITouchDetectorListener() {
						@Override
						public void touchDown(float x, float y, int indext) {
							// DebugLog.d("DUC", "touchDown");
							if (x > mPosBtChat.x && y > mPosBtChat.y) {
								turnOnChat();
							} else {
								mTouchTouch.mAction = TouchTouchType.da_touch_down;
								mTouchTouch.xNoCamera = x;
								mTouchTouch.yNoCamera = y;
								mTouchTouch.isTouch = true;
								// mPositionTouch.set(x, y);
							}
						}

						@Override
						public void touchMove(float x, float y, int indext) {
							// DebugLog.d("DUC", "touchMove");
							mTouchTouch.mAction = TouchTouchType.da_touch_down;
							mTouchTouch.xNoCamera = x;
							mTouchTouch.yNoCamera = y;

							// if (isTurnOnChat && mPositionTouch.x != -1) {
							// mPositionTouch.set(x, y);
							// }

						}

						@Override
						public void onScroll(float xOffset, float yOffset,
								int indext) {
							// mArrayItemCoSan.onScroll(0, yOffset, indext);
						}

						@Override
						public void touchUp(float x, float y, int indext) {
							mTouchTouch.mAction = TouchTouchType.touch_up;
							mTouchTouch.xNoCamera = x;
							mTouchTouch.yNoCamera = y;
						}

						@Override
						public void onFling(float Vx, float Vy,
								float deltaTime, int indext) {
							// DebugLog.d("DUC", "onFling//Vx:" + Vx + "//Vy:"
							// + Vy + "//deltaTime:" + deltaTime);
							// mArrayItemCoSan.onFling(0, Vy, deltaTime,
							// indext);
						}

						@Override
						public void onClick(float x, float y, int indext) {
							// DebugLog.d("DUC", "onClick");
							// mArrayItemCoSan.onClick(x, y, indext);
							// mPositionClickTamp.set(x, y);
							mTouchTouch.isWaitProccessClick = true;
						}

					});
		}
		{
			mDrawableBitmapChat = new DrawableBitmap(
					textureLibrary.allocateTextureNotHash(R.drawable.bt_chat,
							"bt_chat"), 64, 50);
			mPosBtChat = new Vector2(GameInfo.DEFAULT_WIDTH
					- mDrawableBitmapChat.getWidth() - 17,
					GameInfo.DEFAULT_HEIGHT - mDrawableBitmapChat.getHeight()
							- 30);
			// mDrawableBitmapChat.setGlBlendFun(GL10.GL_ONE, GL10.GL_ONE);
		}
		mDrawableBitmapTouch = new DrawableBitmap(
				textureLibrary.allocateTexture(R.drawable.bt_chat, ""), 10, 10);

		// Trick
		// if (CurrentUserInfo.mPlayerInfo.ID ==
		// CurrentGameInfo.getIntance().ownerId) {
		// if (GameInfo.isOnline) {
		// sSystemRegistry.mGS.NEXT_TURN();
		// }
		// }
	}

	@Override
	public void reset() {
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		try {
			ProcessingActionFromServer();
		} catch (Exception e) {
			mActionServerToClientCurrent = null;
			DebugLog.e("ProcessingActionFromServer", e.toString());
		}
		RenderSystem render = sSystemRegistry.renderSystem;
		sendLoadingDone();
		if (mTimeTickInTurn > 0) {
			mTimeTickInTurn -= timeDelta;
			if (mTimeTickInTurn < 0) {
				mTimeTickInTurn = 0;
			}
			if (mIdPlayerTurn == CurrentUserInfo.mPlayerInfo.ID) {
				sSystemRegistry.numberDrawableTimeInTurn.drawNumberWithAlpha(
						400, GameInfo.DEFAULT_HEIGHT - 80,
						(int) mTimeTickInTurn, 1, false, false, Priority.Hind);
			} else {
				sSystemRegistry.numberDrawableTime.drawNumberWithAlpha(400,
						GameInfo.DEFAULT_HEIGHT - 40, (int) mTimeTickInTurn, 1,
						false, false, Priority.Hind);
			}
		}

		if (mTouchTouch.mAction != TouchTouchType.chua_lam_gi
				&& mTouchTouch.isTouch) {
			mTouchTouch.xHasCameRa = (mTouchTouch.xNoCamera
					/ sSystemRegistry.contextParameters.gameWidth * sSystemRegistry.contextParameters.gameWidthAfter)
					+ sSystemRegistry.cameraSystem.getX();
			mTouchTouch.yHasCamera = (mTouchTouch.yNoCamera
					/ sSystemRegistry.contextParameters.gameHeight * sSystemRegistry.contextParameters.gameHeightAfter)
					+ sSystemRegistry.cameraSystem.getY();
			mTouchTouch.chuaXuLyTouchDownTrongVongLap = true;
			if (mTouchTouch.mAction == TouchTouchType.touch_up) {
				mTouchTouch.isTouchUp = true;
			} else {
				mTouchTouch.isTouchUp = false;
			}
			if (mTouchTouch.isWaitProccessClick) {
				mTouchTouch.isClick = true;
			}
			// mPositionClick_HasCamera.x = (mPositionClickTamp.x
			// / sSystemRegistry.contextParameters.gameWidth *
			// sSystemRegistry.contextParameters.gameWidthAfter)
			// + sSystemRegistry.cameraSystem.getX();
			// mPositionClick_HasCamera.y = (mPositionClickTamp.y
			// / sSystemRegistry.contextParameters.gameHeight *
			// sSystemRegistry.contextParameters.gameHeightAfter)
			// + sSystemRegistry.cameraSystem.getY();
			// mPositionClick_NoCamera.set(mPositionClickTamp);
			// mPositionClickTamp.x = -1;
		}

		render.scheduleForDraw(mDrawableBitmapChat, mPosBtChat, Priority.Hind,
				false);
	}

	public void sendLoadingDone() {
		if (mSendLoadingUnitComplete == false && mGameListener != null) {
			mGameListener.LoadingUnitComplete();
			mSendLoadingUnitComplete = true;
		}
	}

	public void xuLyViecDiChuyenCameraManHinh(final MultiTouchFilter2 touch) {
		if (xBefore == -1) {
			xOffset = 0;
			yOffset = 0;
			xBefore = mTouchTouch.xNoCamera;
			yBefore = mTouchTouch.yNoCamera;
			sSystemRegistry.cameraSystem.touchDown();
		} else {
			xOffset = mTouchTouch.xNoCamera - xBefore;
			yOffset = mTouchTouch.yNoCamera - yBefore;
			xBefore = mTouchTouch.xNoCamera;
			yBefore = mTouchTouch.yNoCamera;
			sSystemRegistry.cameraSystem.offset(-xOffset, -yOffset);
		}
	}

	private void touchUpTrongLucDiChuyenCamera() {
		xOffset = 0;
		yOffset = 0;
		xBefore = -1;
		yBefore = -1;
		sSystemRegistry.cameraSystem.touchUp();
	}

	//
	public boolean dumpEvent(MotionEvent event) {
		final int action = event.getAction();
		final int actualEvent = action & MotionEvent.ACTION_MASK;
		if (actualEvent == MotionEvent.ACTION_POINTER_UP
				|| actualEvent == MotionEvent.ACTION_UP
				|| actualEvent == MotionEvent.ACTION_CANCEL) {
			touchUpTrongLucDiChuyenCamera();
			scaleTouch = -1;
			return false;
		}
		int countTouch = event.getPointerCount();
		if (countTouch >= 2) {
			// float x0 = event.getX(0)*(1.0f /
			// sSystemRegistry.contextParameters.viewScaleX);
			// float y0 = event.getY(0)*(1.0f /
			// sSystemRegistry.contextParameters.viewScaleY);
			// float x1 = event.getX(1)*(1.0f /
			// sSystemRegistry.contextParameters.viewScaleX);
			// float y1 = event.getY(1)*(1.0f /
			// sSystemRegistry.contextParameters.viewScaleY);
			float x0 = event.getX(0);
			float y0 = event.getY(0);
			float x1 = event.getX(1);
			float y1 = event.getY(1);
			float scale = PointF.length(x1 - x0, y1 - y0);
			if (scaleTouch == -1) {
				scaleTouch = scale;
			} else {
				sSystemRegistry.cameraSystem.scale(scale / scaleTouch, 0.5f,
						0.5f);
				scaleTouch = scale;
			}
			touchUpTrongLucDiChuyenCamera();
			return true;
		} else {
			scaleTouch = -1;
		}
		return false;
	}

	//
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			break;

		default:
			break;
		}
		// gd.onTouchEvent(event);
		return false;
	}

	class LearnGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onSingleTapUp(MotionEvent ev) {
			// DebugLog.d("onSingleTapUp", ev.toString());
			return true;
		}

		@Override
		public void onShowPress(MotionEvent ev) {
			// DebugLog.d("onShowPress", ev.toString());
		}

		@Override
		public void onLongPress(MotionEvent ev) {
			// DebugLog.d("onLongPress", ev.toString());
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return true;
		}

		@Override
		public boolean onDown(MotionEvent ev) {
			// DebugLog.d("onDownd", ev.toString());
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			return true;
		}
	}

	/*
	 * Goi ra khi server tra ve finish
	 */
	public void endGameInput() {
		DebugLog.d("DUC", "EndGameInput");
		isEndGame = true;
	}

	private void endGameOutput() {
		DebugLog.d("DUC", "EndGameOutput");
		mGameListener.onEndgameListener();
		sSystemRegistry.soundSystem.stopAll();
		isEndGame = false;
	}

	private OnEndgameListener mGameListener = null;

	public void setOnSelectedItemListener(OnEndgameListener endgameListener) {
		mGameListener = endgameListener;
	}

	public interface OnEndgameListener {
		/**
		 * Goi ra luc loading GL xong
		 */
		public void LoadingUnitComplete();

		/**
		 * Goi ra luc deleted GL
		 */
		public void onGLDeletedListener();

		/**
		 * Goi ra luc GL
		 */
		public void onEndgameListener();

		public void turnOnchat();
	}

	public void glDeleteGLDone() {
		mGameListener.onGLDeletedListener();
	}

	synchronized public void turnOnChat() {
		isTurnOnChat = true;
		mGameListener.turnOnchat();
	}

	synchronized public void turnOffChat() {
		isTurnOnChat = false;
	}

	public void nextTurnIput(int idInTurn, Long timeTick) {
		mIdPlayerTurn = idInTurn;
		// if (mIdPlayerTurn == CurrentUserInfo.mPlayerInfo.ID)
		{
			mTimeTickInTurn = (float) timeTick / 1000;
		}
		// else {
		// mTimeTickInTurn = 0;
		// }
		sSystemRegistry.characterManager.nextTurn();
		sSystemRegistry.soundManager.playNextTurn();
	}

	public boolean isControl() {
		if (mTimeTickInTurn > 0
				&& mIdPlayerTurn == CurrentUserInfo.mPlayerInfo.ID) {
			return true;
		}
		return false;
	}

	private void ProcessingActionFromServer() throws Exception {
		if ((mActionList.size() > 0)
				&& (mActionServerToClientCurrent == null || mActionServerToClientCurrent.mStatus == ActionStatus.XuLyXong)) {
			mActionServerToClientCurrent = null;
			mActionServerToClientCurrent = mActionList.pop();
			mActionServerToClientCurrent.mStatus = ActionStatus.DangXuLy;
			Object obj = mActionServerToClientCurrent.Obj;
			switch (mActionServerToClientCurrent.mCurrentType) {
			case move:
				MoveMessage moveMessage = (MoveMessage) obj;
				sSystemRegistry.characterManager.mapEnemyInGame.get(
						moveMessage.idMove).processMoveInputFromServer(
						mActionServerToClientCurrent, moveMessage);
				break;
			case attack:
				AttackMessage attackMessage = (AttackMessage) obj;
				sSystemRegistry.characterManager.mapEnemyInGame.get(
						attackMessage.idAttacker).processAttackInputFromServer(
						mActionServerToClientCurrent, attackMessage);
				break;
			case next_turn:
				NextTurnMessage nextTurnMessage = (NextTurnMessage) obj;
				nextTurnIput(nextTurnMessage.idPlayerInTurnNext,
						nextTurnMessage.turntime);
				mActionServerToClientCurrent.done();
				break;
			case end_game:
				endGameOutput();
				mActionServerToClientCurrent.done();
				break;
			case buy_enemy:
				BuyEnemy buyEnemy = (BuyEnemy) obj;
				processBuyEnemy(buyEnemy);
				mActionServerToClientCurrent.done();
				break;
			case update_money:
				ArrayList<Money> listMoney = (ArrayList<Money>) obj;
				for (Money money : listMoney) {
					if (money.playerID == CurrentUserInfo.mPlayerInfo.ID) {
						int moneyOffset = money.money
								- sSystemRegistry.dialogAddEnemy.mMoney;
						if (moneyOffset != 0) {
							BaseObject.sSystemRegistry.unitEffects
									.addEffectSoBayLen(
											BaseObject.sSystemRegistry.numberDrawableTakeDame,
											moneyOffset, 143, 430, false,
											Priority.InfoCharacter2);
						}
						sSystemRegistry.dialogAddEnemy.mMoney = money.money;
					}
				}
				mActionServerToClientCurrent.done();
				break;
			}
		}
	}

	private void processBuyEnemy(BuyEnemy buyEnemy) {
		EnemyType enemyTypeBuy = null;
		for (EnemyType enemyType : mCurrentGameInfo.listEnemytype) {
			if (enemyType.armyType == buyEnemy.armytype) {
				enemyTypeBuy = enemyType;
				break;
			}
		}
		if (enemyTypeBuy != null) {
			boolean isMyTeam = buyEnemy.buyerId == CurrentUserInfo.mPlayerInfo.ID;
			UnitCharacterSwordmen character = new UnitCharacterSwordmen(
					sSystemRegistry.longTermTextureLibrary,
					sSystemRegistry.mapTiles.arrayMap[buyEnemy.y_tile][buyEnemy.x_tile],
					isMyTeam, enemyTypeBuy, buyEnemy.armyid);

			if (isMyTeam) {
				if (enemyTypeBuy.armyType == GameInfo.idTypeKing) {
					BaseObject.sSystemRegistry.characterManager.myKing = character;
				} else {
					BaseObject.sSystemRegistry.unitSreen
							.setCharacterFocus(character);
					if (enemyTypeBuy.cost != 0) {
						BaseObject.sSystemRegistry.unitEffects
								.addEffectSoBayLen(
										BaseObject.sSystemRegistry.numberDrawableTakeDame,
										-enemyTypeBuy.cost, 143, 430, false,
										Priority.InfoCharacter2);
					}
					BaseObject.sSystemRegistry.soundManager.playSoundCoin();
					BaseObject.sSystemRegistry.soundManager
							.playSeleted(enemyTypeBuy);
				}
				sSystemRegistry.dialogAddEnemy.mMoney = buyEnemy.money;
				sSystemRegistry.dialogAddEnemy.setVisible(true);

			}

		}
	}

	public void daXuLyTouch() {
		// mTouchTouch.isTouch = false;
		// mTouchTouch.isTouchUp = false;
		endLoop();
	}

	public void daXuLyTouchTrongVongLap() {
		mTouchTouch.chuaXuLyTouchDownTrongVongLap = false;
	}

	public void endLoop() {
		if (mTouchTouch.isTouch) {
			if (mTouchTouch.chuaXuLyTouchDownTrongVongLap) {
				xuLyViecDiChuyenCameraManHinh(sSystemRegistry.multiTouchFilter2);
			}
			mTouchTouch.chuaXuLyTouchDownTrongVongLap = false;
			if (mTouchTouch.isTouchUp) {
				mTouchTouch.isTouch = false;
				mTouchTouch.isTouchUp = false;
				touchUpTrongLucDiChuyenCamera();
				if (mTouchTouch.mAction == TouchTouchType.touch_up) {
					mTouchTouch.mAction = TouchTouchType.chua_lam_gi;
				}
			}
		}
		if (mTouchTouch.isClick) {
			mTouchTouch.isClick = false;
			mTouchTouch.isWaitProccessClick = false;
		}
	}
}
