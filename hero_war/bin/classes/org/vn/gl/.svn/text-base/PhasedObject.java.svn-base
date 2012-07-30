package org.vn.gl;

/**
 * A basic object that adds an execution phase. When PhasedObjects are combined
 * with PhasedObjectManagers, objects within the manager will be updated by
 * phase.
 */
public class PhasedObject extends BaseObject {

	public int phase; // This is public because the phased is accessed extremely
						// often, so much
						// so that the function overhead of an getter is
						// non-trivial.

	public PhasedObject() {
		super();
	}

	@Override
	public void reset() {

	}

	public void setPhase(int phaseValue) {
		phase = phaseValue;
	}
}
