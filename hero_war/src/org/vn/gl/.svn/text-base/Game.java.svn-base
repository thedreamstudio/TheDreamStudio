package org.vn.gl;

import org.vn.cache.CurrentGameInfo;
import org.vn.cache.CurrentUserInfo;
import org.vn.gl.game.CameraSystem;
import org.vn.herowar.R;
import org.vn.model.EnemyType;
import org.vn.unit.CharacterManager;
import org.vn.unit.DialogAddEnemy;
import org.vn.unit.InputGameInterface;
import org.vn.unit.LogicMap;
import org.vn.unit.MapTiles;
import org.vn.unit.NumberDrawable;
import org.vn.unit.Tile;
import org.vn.unit.UnitCharacter;
import org.vn.unit.UnitCharacterSwordmen;
import org.vn.unit.UnitEffects;
import org.vn.unit.UnitSreen;

import android.content.Context;
import android.view.MotionEvent;

/**
 * High-level setup object for the AndouKun game engine. This class sets up the
 * core game engine objects and threads. It also passes events to the game
 * thread from the main UI thread.
 */
public class Game extends AllocationGuard {

	private GameThread mGameThread;
	private Thread mGame;
	public MainLoop mGameRoot;
	private GameRenderer mRenderer;
	private GLSurfaceView mSurfaceView;
	private boolean mRunning;
	private boolean mBootstrapComplete;
	private boolean mLoadingUnitComplete;
	private boolean mGLDataLoaded;
	private ContextParameters mContextParameters;
	private MultiTouchFilter2 mTouchFilter2;

	public Game() {
		super();
		if (BaseObject.sSystemRegistry == null) {
			BaseObject.sSystemRegistry = new ObjectRegistry();
		}
		mRunning = false;
		mBootstrapComplete = false;
		mContextParameters = new ContextParameters();
		mGLDataLoaded = false;
		mLoadingUnitComplete = false;
	}

	/**
	 * Creates core game objects and constructs the game engine object graph.
	 * Note that the game does not actually begin running after this function is
	 * called (see start() below). Also note that textures are not loaded from
	 * the resource pack by this function, as OpenGl isn't yet available.
	 * 
	 * @param context
	 */
	public void bootstrap(Context context, int viewWidth, int viewHeight,
			int gameWidth, int gameHeight) throws Exception {
		if (!mBootstrapComplete) {

			mRenderer = new GameRenderer(context, this, viewWidth, viewHeight);
			ContextParameters params = mContextParameters;
			params.viewWidth = viewWidth;
			params.viewHeight = viewHeight;
			params.gameWidth = gameWidth;
			params.gameHeight = gameHeight;
			params.viewScaleX = (float) viewWidth / gameWidth;
			params.viewScaleY = (float) viewHeight / gameHeight;
			params.context = context;
			BaseObject.sSystemRegistry.contextParameters = params;

			mTouchFilter2 = new MultiTouchFilter2();
			BaseObject.sSystemRegistry.multiTouchFilter2 = mTouchFilter2;

			BaseObject.sSystemRegistry.soundSystem = new SoundSystem();

			// Long-term textures persist between levels.
			TextureLibrary longTermTextureLibrary = new TextureLibrary();
			BaseObject.sSystemRegistry.longTermTextureLibrary = longTermTextureLibrary;

			MainLoop gameRoot = new MainLoop();
			mGameRoot = gameRoot;

			RenderSystem renderer = new RenderSystem();
			BaseObject.sSystemRegistry.renderSystem = renderer;

			// InputGameInterface
			{
				InputGameInterface inputInterface = new InputGameInterface(
						longTermTextureLibrary);
				BaseObject.sSystemRegistry.inputGameInterface = inputInterface;
			}
		}
	}

	private void LoadingUnit(Context context,
			final TextureLibrary longTermTextureLibrary, final MainLoop gameRoot) {
		// WindowManager windowMgr = (WindowManager) context
		// .getSystemService(Context.WINDOW_SERVICE);
		// int rotationIndex = windowMgr.getDefaultDisplay().getOrientation();

		// InputGameInterface
		{
			gameRoot.add(BaseObject.sSystemRegistry.inputGameInterface);
		}
		// CameraSystem
		final CurrentGameInfo mCurrentGameInfo = CurrentGameInfo.getIntance();
		int widthtTileMap;
		int heightTileMap;
		if (GameInfo.isOnline) {
			widthtTileMap = mCurrentGameInfo.mMapSelected.mapType.row;
			heightTileMap = mCurrentGameInfo.mMapSelected.mapType.column;
		} else {
			widthtTileMap = 24;
			heightTileMap = 24;
		}
		float offset = GameInfo.offset;
		float offsetMap = GameInfo.offsetMap;
		float xMap = -MapTiles.getPosXUnit(0, heightTileMap, offset);
		float widthMap = offsetMap * 2 + xMap + widthtTileMap * offset - offset
				/ 2;
		float heightMap = offsetMap * 2 + (heightTileMap - 1) * offset - offset
				* 3 / 4;

		{
			CameraSystem camera = new CameraSystem(longTermTextureLibrary,
					widthMap, heightMap);
			BaseObject.sSystemRegistry.cameraSystem = camera;
			gameRoot.add(camera);
		}
		{
			DialogAddEnemy dialogAddEnemy = new DialogAddEnemy(
					longTermTextureLibrary) {
				@Override
				public void addEnemyAt(Tile tile, EnemyType enemyType) {
					if (mMoney - enemyType.cost > 0) {
						if (!BaseObject.sSystemRegistry.logicMap.mArrayMap[tile.yTile][tile.xTile]) {
							UnitCharacter character = new UnitCharacterSwordmen(
									longTermTextureLibrary,
									BaseObject.sSystemRegistry.mapTiles.arrayMap[tile.yTile][tile.xTile],
									true, enemyType, -1);
							BaseObject.sSystemRegistry.unitSreen
									.setCharacterFocus(character);
							mMoney -= enemyType.cost;
							BaseObject.sSystemRegistry.unitEffects
									.addEffectSoBayLen(
											BaseObject.sSystemRegistry.numberDrawableTakeDame,
											-enemyType.cost, 150, 465, false,
											Priority.InfoCharacter2);
						} else {
							DebugLog.e("DUC",
									"Khoi tao character tai 1 vi tri ko di  chuyen dc");
						}
					}
				}

				@Override
				public void ready() {
					// ready
					BaseObject.sSystemRegistry.mGS
							.ARMY_SELECTION(BaseObject.sSystemRegistry.characterManager.arrayCharactersMyTeam);
					isVisible = false;
					clearAllBitmapcast();
				}
			};
			BaseObject.sSystemRegistry.dialogAddEnemy = dialogAddEnemy;
			dialogAddEnemy.isVisible = true;
			dialogAddEnemy
					.setIsEnableReady(mCurrentGameInfo.ownerId != CurrentUserInfo.mPlayerInfo.ID);
			gameRoot.add(dialogAddEnemy);
		}
		// Map
		{
			// Khoi tao vi tri hien thi cua map
			BaseObject.sSystemRegistry.mapTiles = new MapTiles(
					longTermTextureLibrary, widthtTileMap, heightTileMap,
					offset, offsetMap + xMap, offsetMap + offset / 2);
			gameRoot.add(BaseObject.sSystemRegistry.mapTiles);

			// Khoi tao viec cho phep di hay khong tren logic map
			if (GameInfo.isOnline) {
				BaseObject.sSystemRegistry.logicMap = new LogicMap(
						widthtTileMap, heightTileMap,
						mCurrentGameInfo.mMapSelected.mapType.mapType,
						R.drawable.back_ground_alpha);
			} else {
				BaseObject.sSystemRegistry.logicMap = new LogicMap(
						widthtTileMap, heightTileMap, null,
						R.drawable.back_ground_alpha);
			}
		}
		// Effect
		{
			UnitEffects unitEffects = new UnitEffects(longTermTextureLibrary);
			BaseObject.sSystemRegistry.unitEffects = unitEffects;
			gameRoot.add(unitEffects);
		}
		// Character
		BaseObject.sSystemRegistry.characterManager = new CharacterManager();
		gameRoot.add(BaseObject.sSystemRegistry.characterManager);

		if (GameInfo.isOnline) {
			// for (Enemy enemy : mCurrentGameInfo.listIdEnemyInMap) {
			// if
			// (!BaseObject.sSystemRegistry.logicMap.mArrayMap[enemy.yTile][enemy.xTile])
			// {
			// UnitCharacter character = new UnitCharacterSwordmen(
			// longTermTextureLibrary,
			// BaseObject.sSystemRegistry.mapTiles.arrayMap[enemy.yTile][enemy.xTile],
			// enemy.playerId == CurrentUserInfo.mPlayerInfo.ID,
			// enemy.getEnemyType(), enemy.armyId);
			// } else {
			// DebugLog.e("DUC",
			// "Khoi tao character tai 1 vi tri ko di  chuyen dc");
			// }
			// }
			// Add King
			for (EnemyType enemyType : mCurrentGameInfo.listEnemytype) {
				if (enemyType.armyType == GameInfo.idTypeKing) {
					addKing(longTermTextureLibrary, mCurrentGameInfo.xTileKing,
							mCurrentGameInfo.yTileKing, enemyType);
					break;
				}
			}
		} else {
			// For test
			for (int i = 0; i < 14; i++) {
				EnemyType enemyType = new EnemyType();
				enemyType.hp = 120;
				enemyType.mana = 78;
				enemyType.movecost = 8;
				enemyType.attackcost = 35;
				enemyType.damageMin = 28;
				enemyType.damageMax = 32;
				enemyType.rangeattack = 2;
				enemyType.rangeview = 5;

				int xTile = Utils.RANDOM.nextInt(14);
				int yTile = i;
				if (!BaseObject.sSystemRegistry.logicMap.mArrayMap[yTile][xTile]) {
					UnitCharacter character = new UnitCharacterSwordmen(
							longTermTextureLibrary,
							BaseObject.sSystemRegistry.mapTiles.arrayMap[yTile][xTile],
							Utils.RANDOM.nextInt(2) == 0 ? true : false,
							enemyType, i);
				}
			}
		}
		{
			BaseObject.sSystemRegistry.numberDrawableTime = new NumberDrawable(
					longTermTextureLibrary, 256, 256, 256, 256, 30, 30,
					NumberDrawable.idDrawableNumber2);
			BaseObject.sSystemRegistry.numberDrawableTakeDame = new NumberDrawable(
					longTermTextureLibrary, 256, 256, 256, 256, 24, 24,
					NumberDrawable.idDrawableNumber2);
			BaseObject.sSystemRegistry.numberDrawableCostInDialogAddEnemy = new NumberDrawable(
					longTermTextureLibrary, 256, 256, 256, 256, 20, 20,
					NumberDrawable.idDrawableNumber2);
		}
		// Sreen
		{
			UnitSreen unitSreen = new UnitSreen(longTermTextureLibrary);
			BaseObject.sSystemRegistry.unitSreen = unitSreen;
			gameRoot.add(unitSreen);
		}
		// GameThread
		{
			mGameThread = new GameThread(mRenderer);
			mGameThread.setGameRoot(mGameRoot);
		}

		mBootstrapComplete = true;
	}

	/** Starts the game running. */
	public void start() {
		if (!mRunning) {
			assert mGame == null;
			// 11072011, ChinhQT: reclaim the memory
			System.gc();
			mGame = new Thread(mGameThread);
			mGame.setName("Game");
			mGame.start();
			mRunning = true;
			AllocationGuard.sGuardActive = false;
		} else {
			mGameThread.resumeGame();
		}
	}

	public void stop() {
		try {
			BaseObject.sSystemRegistry.soundSystem.stopAll();
			if (mGameThread != null) {
				mGameThread.stopGame();
			}
			mRunning = false;
			AllocationGuard.sGuardActive = false;
			BaseObject.sSystemRegistry.longTermTextureLibrary.removeAll();
			mSurfaceView.flushAll();
			mSurfaceView.destroyDrawingCache();
			mSurfaceView = null;
			BaseObject.sSystemRegistry.onDestroy();
			BaseObject.sSystemRegistry = null;
			// if (mGame != null) {
			// try {
			// mGame.join();
			// mGame.interrupt();
			// } catch (InterruptedException e) {
			// mGame.interrupt();
			// }
			// }
			mGame = null;
		} catch (Exception error) {
		} finally {
			System.gc();
		}
	}

	public void CallbackRequested() {
		mSurfaceView
				.flushTextures(BaseObject.sSystemRegistry.longTermTextureLibrary);
	}

	public void stopGL() {
		mRenderer.requestCallback();
		BaseObject.sSystemRegistry.soundSystem.stopAll();
	}

	public GameRenderer getRenderer() {
		return mRenderer;
	}

	public void onPause() {
		if (mRunning) {
			mGameThread.pauseGame();
		}
	}

	public void onResume(Context context, boolean force) {
		if (force && mRunning) {
			mGameThread.resumeGame();
		} else {
			mRenderer.setContext(context);
			// Don't explicitly resume the game here. We'll do that in
			// the SurfaceReady() callback, which will prevent the game
			// starting before the render thread is ready to go.
			BaseObject.sSystemRegistry.contextParameters.context = context;
		}
	}

	public void onSurfaceReady() {
		DebugLog.d("DUC", "onSurfaceReady");
		if (mLoadingUnitComplete == false) {
			LoadingUnit(BaseObject.sSystemRegistry.contextParameters.context,
					BaseObject.sSystemRegistry.longTermTextureLibrary,
					mGameRoot);
			mLoadingUnitComplete = true;
		}
		start();
		mGameThread.pauseGame();
		if (!mGLDataLoaded && mGameThread.getPaused() && mRunning) {
			mRenderer.setContext(mContextParameters.context);
			mSurfaceView
					.loadTextures(BaseObject.sSystemRegistry.longTermTextureLibrary);

			TimeSystem time = BaseObject.sSystemRegistry.timeSystem;
			time.reset();
			mGLDataLoaded = true;
		}
		mGameThread.resumeGame();
		ContextParameters params = mContextParameters;
		params.viewWidth = mSurfaceView.getWidth();
		params.viewHeight = mSurfaceView.getHeight();
		params.viewScaleX = (float) params.viewWidth / params.gameWidth;
		params.viewScaleY = (float) params.viewHeight / params.gameHeight;
		BaseObject.sSystemRegistry.cameraSystem.setScaleNow();
		mRenderer.setWidthHeight(params.viewWidth, params.viewHeight);
		BaseObject.sSystemRegistry.inputGameInterface.sendLoadingDone();
	}

	public void setSurfaceView(GLSurfaceView view) {
		mSurfaceView = view;
	}

	public void onSurfaceLost() {
		mSurfaceView
				.flushTextures(BaseObject.sSystemRegistry.longTermTextureLibrary);
		BaseObject.sSystemRegistry.longTermTextureLibrary.invalidateAll();
		mGLDataLoaded = false;
	}

	public void onSurfaceCreated() {
	}

	public void setSoundEnabled(boolean soundEnabled) {
		BaseObject.sSystemRegistry.soundSystem.setSoundEnabled(soundEnabled);
	}

	public void setSafeMode(boolean safe) {
		mSurfaceView.setSafeMode(safe);
	}

	public float getGameTime() {
		return BaseObject.sSystemRegistry.timeSystem.getGameTime();
	}

	public boolean isPaused() {
		return (mRunning && mGameThread != null && mGameThread.getPaused());
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (mRunning) {
			mTouchFilter2.onTouchEvent(event);
		}
		return true;
	}

	public boolean isBootstrapComplete() {
		return mBootstrapComplete;
	}

	private void addKing(TextureLibrary textureLibrary, int xTile, int yTile,
			EnemyType enemyType) {
		if (!BaseObject.sSystemRegistry.logicMap.mArrayMap[yTile][xTile]) {
			UnitCharacter character = new UnitCharacterSwordmen(textureLibrary,
					BaseObject.sSystemRegistry.mapTiles.arrayMap[yTile][xTile],
					true, enemyType, -1);
		} else {
			DebugLog.e("DUC",
					"Khoi tao character tai 1 vi tri ko di  chuyen dc");
		}
	}
}
