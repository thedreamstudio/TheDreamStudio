package org.vn.gl;

/**
 * TObjectPool is a generic version of ObjectPool that automatically casts to
 * type T on allocation.
 * 
 * @param <T>
 *            The type of object managed by the pool.
 */
public abstract class TObjectPool<T> extends ObjectPool {

	public TObjectPool() {
		super();
	}

	public TObjectPool(int size) {
		super(size);
	}

	public T allocate() {
		T object = (T) super.allocate();
		return object;
	}
}
