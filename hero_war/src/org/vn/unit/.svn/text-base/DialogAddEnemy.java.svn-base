package org.vn.unit;

import java.util.ArrayList;

import org.vn.cache.CurrentGameInfo;
import org.vn.gl.BaseObject;
import org.vn.gl.DrawableBitmap;
import org.vn.gl.GameInfo;
import org.vn.gl.Priority;
import org.vn.gl.RenderSystem;
import org.vn.gl.Texture;
import org.vn.gl.TextureLibrary;
import org.vn.gl.Vector2;
import org.vn.gl.iBitmapInImageCache;
import org.vn.herowar.R;
import org.vn.model.EnemyType;
import org.vn.model.TouchTouch;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;

public class DialogAddEnemy extends BaseObject {
	private Tile mTileSeleted = null;
	private ButtonEnemy[] mButton;
	private ButtonEnemy mButtonSeleted = null;
	public DrawableBitmap mDrawableTile;

	/** Dialog khung the hien info ben goc trai */
	private Button mDialogInfo;
	public DrawableBitmap mDrawableCharacterInfo;
	public DrawableBitmap mDrawableInfo;

	private Button mBtAddEnemy;

	private Button mBtReady;

	public boolean isVisible = false;
	public boolean isEnableReady = false;
	public int mMoney;
	public Texture textureReady_Down;
	public Texture textureReady_Up;
	public Texture textureBuy_Down;
	public Texture textureBuy_Up;
	public Texture[] textureEnemys;

	public DialogAddEnemy(TextureLibrary textureLibrary) {
		{
			int[] idTextureEnemy = { R.drawable.player_cannon_sleep,
					R.drawable.player_geezer_sleep,
					R.drawable.player_gunslinger_sleep,
					R.drawable.player_indian_sleep,
					R.drawable.player_mexican_sleep,
					R.drawable.player_scout_sleep };
			textureEnemys = new Texture[idTextureEnemy.length];
			for (int i = 0; i < idTextureEnemy.length; i++) {
				textureEnemys[i] = textureLibrary.allocateTexture(
						idTextureEnemy[i], "idTextureEnemy" + i);
			}
		}
		textureReady_Down = textureLibrary.allocateTextureNotHash(
				R.drawable.bt_start_down, "bt_start_down");
		textureReady_Up = textureLibrary.allocateTextureNotHash(
				R.drawable.bt_start_up, "bt_start_up");
		textureBuy_Down = textureLibrary.allocateTextureNotHash(
				R.drawable.bt_buy_down, "bt_buy_down");
		textureBuy_Up = textureLibrary.allocateTextureNotHash(
				R.drawable.bt_buy_up, "bt_buy_up");

		// Khoi tao list enemy
		ArrayList<EnemyType> listEnemytype = CurrentGameInfo.getIntance().listEnemytype;
		mButton = new ButtonEnemy[listEnemytype.size()];
		for (int i = 0; i < mButton.length; i++) {
			mButton[i] = new ButtonEnemy(new DrawableBitmap(textureEnemys[i
					% textureEnemys.length], 80, 80), listEnemytype.get(i)) {
				public boolean onClick() {
					if (mButtonSeleted != this) {
						if (mButtonSeleted != null) {
							mButtonSeleted.mDrawableBitmap.setColorExpressF(1,
									1, 1, 0.5f);
						}
						mButtonSeleted = this;
						mButtonSeleted.mDrawableBitmap.setColorExpressF(1, 1,
								1, 1);
						{
							// Chang bitmap info
							iBitmapInImageCache bmInfor = new iBitmapInImageCache() {

								@Override
								public Bitmap getBitMapResourcesItem() {
									Bitmap bitmap = Bitmap.createBitmap(128,
											128, Config.ARGB_8888);
									Canvas canvas = new Canvas(bitmap);
									canvas.drawCircle(0, 0,
											mEnemyType.cost * 3, new Paint());
									return bitmap;
								}
							};
							if (mDrawableInfo.getTexture() == null) {
								mDrawableInfo
										.setTexture(sSystemRegistry.longTermTextureLibrary
												.allocateBitmapCache(bmInfor,
														true, "BmInfor"));
							} else {
								mDrawableInfo.changeBitmap(bmInfor, true);
							}

							mDrawableCharacterInfo
									.setTexture(textureEnemys[mEnemyType.armyType
											% textureEnemys.length]);
						}
					}
					return true;
				};

				@Override
				public boolean onFocus() {
					return true;
				}
			};
			mButton[i].pos.set(200 + i * 85, 10);
			mButton[i].mDrawableBitmap.setColorExpressF(1, 1, 1, 0.5f);
		}

		// Khoi tao infor cho enemy
		mDialogInfo = new Button(new DrawableBitmap(
				textureLibrary.allocateTexture(R.drawable.dialog_back,
						"dialog_back"), 208, 422)) {
			@Override
			public boolean onClick() {
				mBtAddEnemy.checkInTouch();
				return true;
			}

			public boolean onFocus() {
				if (!mBtAddEnemy.checkInTouch()) {
					mBtAddEnemy.setDrawOfffocus();
				} else {
					mBtAddEnemy.setDrawOnfocus();
				}
				return true;
			};
		};
		// Bt andEnemy
		mBtAddEnemy = new Button(new DrawableBitmap(textureBuy_Down, 60, 60)) {
			@Override
			public boolean onClick() {
				addEnemyAt(mTileSeleted, mButtonSeleted.mEnemyType);
				return true;
			}

			public boolean onFocus() {
				return true;
			};

			public void setDrawOfffocus() {
				mDrawableBitmap.setTexture(textureBuy_Down);
			};

			public void setDrawOnfocus() {
				mDrawableBitmap.setTexture(textureBuy_Up);
			};
		};
		mBtAddEnemy.pos
				.set((mDialogInfo.mDrawableBitmap.getWidth() - mBtAddEnemy.mDrawableBitmap
						.getWidth()) - 30, 30);

		mBtReady = new Button(new DrawableBitmap(textureReady_Down, 150, 78)) {
			@Override
			public boolean onClick() {
				ready();
				return true;
			}

			public void setDrawOfffocus() {
				mDrawableBitmap.setTexture(textureReady_Down);
			};

			public void setDrawOnfocus() {
				mDrawableBitmap.setTexture(textureReady_Up);
			};
		};
		mBtReady.pos.set(790 - mBtReady.mDrawableBitmap.getWidth(), 10);
		mMoney = CurrentGameInfo.getIntance().gold;
		// Bitmap ve~ info
		mDrawableInfo = new DrawableBitmap(null, 180, 180);
		// Con tro?
		mDrawableTile = new DrawableBitmap(textureLibrary.allocateTexture(
				R.drawable.image_6359, "image_6359"), GameInfo.offset / 2,
				GameInfo.offset / 2);
		// Bitmap ve~ character tren info
		mDrawableCharacterInfo = new DrawableBitmap(textureEnemys[0], 150, 150);
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		if (isVisible) {
			RenderSystem render = sSystemRegistry.renderSystem;
			if (isEnableReady) {
				mBtReady.checkInTouch();
				mBtReady.draw(render, Priority.DialogAddEnemy);
			}
			if (mTileSeleted != null
					&& mTileSeleted.getCharacterTaget() == null
					&& !mTileSeleted.isColition()) {
				render.scheduleForDraw(mDrawableTile, Vector2.TAMP.set(
						mTileSeleted.x - mDrawableTile.getWidth() / 2,
						mTileSeleted.y - mDrawableTile.getHeight() / 2),
						Priority.CiclerCharacterMove, true);
				for (Button button : mButton) {
					button.checkInTouch();
					button.draw(render, Priority.DialogAddEnemy);
				}
				if (mButtonSeleted != null) {
					showEnemyselect(render, mButtonSeleted.mEnemyType);
				}
			}

		}
	}

	public void setTileSeleted(Tile pTile) {
		if (isVisible) {
			mTileSeleted = pTile;
			// Tat seleted
			if (mButtonSeleted != null) {
				mButtonSeleted.mDrawableBitmap.setColorExpressF(1, 1, 1, 0.5f);
				mButtonSeleted = null;
			}
		}
	}

	private void showEnemyselect(RenderSystem render, EnemyType enemyType) {
		mDialogInfo.checkInTouch();
		mDialogInfo.draw(render, Priority.DialogAddEnemy);
		mBtAddEnemy.draw(render, Priority.ButtonOnDialogAddEnemy);
		sSystemRegistry.numberDrawableTime.drawNumberWithAlpha(50, 430, mMoney,
				1, false, false, Priority.ButtonOnDialogAddEnemy);
		sSystemRegistry.renderSystem.scheduleForDraw(mDrawableInfo,
				Vector2.TAMP.set(
						(mDialogInfo.mDrawableBitmap.getWidth() - mDrawableInfo
								.getWidth()) / 2, 100),
				Priority.ButtonOnDialogAddEnemy, false);
		sSystemRegistry.renderSystem
				.scheduleForDraw(
						mDrawableCharacterInfo,
						Vector2.TAMP.set(
								(mDialogInfo.mDrawableBitmap.getWidth() - mDrawableCharacterInfo
										.getWidth()) / 2, 260),
						Priority.ButtonOnDialogAddEnemy, false);
	}

	public void addEnemyAt(Tile tile, EnemyType enemyType) {

	}

	public void ready() {

	}

	private class ButtonEnemy extends Button {
		public EnemyType mEnemyType;

		public ButtonEnemy(DrawableBitmap pDrawableBitmap, EnemyType pEnemyType) {
			super(pDrawableBitmap);
			mEnemyType = pEnemyType;
		}

	}

	public void clearAllBitmapcast() {
		mDrawableInfo.changeBitmap(null, true);
	}

	private class Button {
		public Button(DrawableBitmap pDrawableBitmap) {
			mDrawableBitmap = pDrawableBitmap;
		}

		public final DrawableBitmap mDrawableBitmap;

		public Vector2 pos = new Vector2();

		public void draw(RenderSystem render, int priority) {
			render.scheduleForDraw(mDrawableBitmap, pos, priority, false);
		}

		final public boolean checkInTouch() {
			TouchTouch touchTouch = sSystemRegistry.inputGameInterface.mTouchTouch;

			if (touchTouch.isTouch) {
				float x = touchTouch.xNoCamera;
				float y = touchTouch.yNoCamera;
				if (x < pos.x || y < pos.y
						|| x > pos.x + mDrawableBitmap.getWidth()
						|| y > pos.y + mDrawableBitmap.getHeight()) {
					setDrawOfffocus();
				} else {
					if (touchTouch.isTouchUp) {
						setDrawOfffocus();
						if (onClick()) {
							sSystemRegistry.inputGameInterface.daXuLyTouch();
							return true;
						} else {
							return false;
						}
					} else {
						if (onFocus()
								&& touchTouch.chuaXuLyTouchDownTrongVongLap) {
							sSystemRegistry.inputGameInterface
									.daXuLyTouchTrongVongLap();
							setDrawOnfocus();
							return true;
						} else {
							setDrawOfffocus();
							return false;
						}
					}
				}
			}
			setDrawOfffocus();
			return false;
		}

		/**
		 * Co xu ly thi tra ve true
		 * 
		 * @return
		 */
		public boolean onClick() {
			return false;
		}

		public boolean onFocus() {
			return true;
		}

		public void setDrawOnfocus() {

		}

		public void setDrawOfffocus() {

		}
	}
}
