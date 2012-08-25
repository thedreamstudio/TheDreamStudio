package org.vn.gl;


/**
 * Main game loop. Updates the time system and passes the result down to the
 * rest of the game graph. This object is effectively the root of the game
 * graph.
 */
public class MainLoop extends ObjectManager {

	// Ensures that time updates before everything else.
	public MainLoop() {
		super();
		mTimeSystem = new TimeSystem();
		sSystemRegistry.timeSystem = mTimeSystem;
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		mTimeSystem.update(timeDelta, parent);
		final float newTimeDelta = mTimeSystem.getFrameDelta(); // The time
																// system may
																// warp time.
		super.update(newTimeDelta, parent);
	}

	private TimeSystem mTimeSystem;
}
