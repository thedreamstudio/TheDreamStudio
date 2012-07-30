package org.vn.gl;

import org.vn.gl.game.CameraSystem;

import android.os.SystemClock;

/**
 * The GameThread contains the main loop for the game engine logic. It invokes
 * the game graph, manages synchronization of input events, and handles the draw
 * queue swap with the rendering thread.
 */
public class GameThread implements Runnable {
	private long mLastTime;

	private ObjectManager mGameRoot;
	private GameRenderer mRenderer;
	private Object mPauseLock;
	private boolean mFinished;
	private boolean mPaused = false;
	private int mProfileFrames;
	private long mProfileTime;

	private static final float PROFILE_REPORT_DELAY = 2.0f;

	public GameThread(GameRenderer renderer) {
		mLastTime = SystemClock.uptimeMillis();
		mRenderer = renderer;
		mPauseLock = new Object();
		mFinished = false;
		mPaused = false;
	}

	public void run() {
		mLastTime = SystemClock.elapsedRealtime();
		mFinished = false;
		long averageFrameTime = 1;
		while (!mFinished) {
			if (mGameRoot != null) {
				mRenderer.waitDrawingComplete();
				if (GameRenderer.isDeleteGLDone) {
					// Make sure our dependence on the render system is cleaned
					// up.
					if (BaseObject.sSystemRegistry != null) {
						BaseObject.sSystemRegistry.renderSystem
								.emptyQueues(mRenderer);
						BaseObject.sSystemRegistry.inputGameInterface
								.glDeleteGLDone();
					}
					mFinished = true;
				} else {
					final long time = SystemClock.elapsedRealtime();
					final long timeDelta = time - mLastTime;
					if (timeDelta > 32) {
						float secondsDelta = 0.001f * timeDelta;
						// if (secondsDelta > 0.1f) {
						// secondsDelta = 0.1f;
						// }
						mLastTime = time;
						mGameRoot.update(secondsDelta, null);

						CameraSystem camera = BaseObject.sSystemRegistry.cameraSystem;
						float x = 0.0f;
						float y = 0.0f;
						if (camera != null) {
							x = camera.getX();
							y = camera.getY();
						}
						BaseObject.sSystemRegistry.renderSystem.swap(mRenderer,
								x, y);
						mProfileTime += timeDelta;
						mProfileFrames++;
						if (mProfileTime > PROFILE_REPORT_DELAY * 1000) {
							averageFrameTime = mProfileTime / mProfileFrames;
							DebugLog.d("Game Profile", "Average: "
									+ averageFrameTime);
							mProfileTime = 0;
							mProfileFrames = 0;
							// mGameRoot.sSystemRegistry.hudSystem
							// .setFPS(1000 / (int) averageFrameTime);
						}
						if (BaseObject.sSystemRegistry.numberDrawableTime != null) {
							BaseObject.sSystemRegistry.numberDrawableTime
									.drawNumberWithAlpha(30, 20,
											(int) GameRenderer.averageWaitTime,
											1, false, false, Priority.Help);
						}
					} else {
						// If the game logic completed in less than 16ms, that
						// means
						// it's running
						// faster than 60fps, which is our target frame rate. In
						// that
						// case we should
						// yield to the rendering thread, at least for the
						// remaining
						// frame.
						try {
							Thread.sleep(32 - timeDelta);
						} catch (InterruptedException e) {
							// Interruptions here are no big deal.
						}
					}
					synchronized (mPauseLock) {
						if (mPaused) {
							// SoundSystem sound =
							// BaseObject.sSystemRegistry.soundSystem;
							// if (sound != null) {
							// sound.pauseAll();
							// BaseObject.sSystemRegistry.inputSystem
							// .releaseAllKeys();
							// }
							while (mPaused) {
								try {
									mPauseLock.wait();
								} catch (InterruptedException e) {
									// No big deal if this wait is interrupted.
								}
							}
						}
					}
				}
			}
			if (mFinished) {
				DebugLog.d("DUC", "END GameThread :)");
			}
		}
		// Make sure our dependence on the render system is cleaned up.
		if (BaseObject.sSystemRegistry != null
				&& BaseObject.sSystemRegistry.renderSystem != null)
			BaseObject.sSystemRegistry.renderSystem.emptyQueues(mRenderer);
	}

	public void stopGame() {
		synchronized (mPauseLock) {
			mPaused = false;
			mFinished = true;
			mPauseLock.notifyAll();
		}
	}

	public void pauseGame() {
		synchronized (mPauseLock) {
			mPaused = true;
		}
	}

	public void resumeGame() {
		synchronized (mPauseLock) {
			mPaused = false;
			mPauseLock.notifyAll();
		}
	}

	public boolean getPaused() {
		return mPaused;
	}

	public void setGameRoot(ObjectManager gameRoot) {
		mGameRoot = gameRoot;
	}

}
