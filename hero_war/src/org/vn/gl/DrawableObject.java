package org.vn.gl;


/**
 * DrawableObject is the base object interface for objects that can be rendered
 * to the screen. Note that objects derived from DrawableObject are passed
 * between threads, and that care must be taken when modifying drawable
 * parameters to avoid side-effects (for example, the DrawableFactory class can
 * be used to generate fire-and-forget drawables).
 */
public abstract class DrawableObject extends AllocationGuard {
	private float mPriority;
	private ObjectPool mParentPool;

	public abstract void draw(float x, float y, float scaleX, float scaleY);

	public DrawableObject() {
		super();
	}

	public void setPriority(float f) {
		mPriority = f;
	}

	public float getPriority() {
		return mPriority;
	}

	public void setParentPool(ObjectPool pool) {
		mParentPool = pool;
	}

	public ObjectPool getParentPool() {
		return mParentPool;
	}

	// Override to allow drawables to be sorted by texture.
	public Texture getTexture() {
		return null;
	}

	// Function to allow drawables to specify culling rules.
	public boolean visibleAtPosition(Vector2 position) {
		return true;
	}

}
