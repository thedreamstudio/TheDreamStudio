package org.vn.gl;

/**
 * AllocationGuard is a utility class for tracking down memory leaks. It
 * implements a "checkpoint" memory scheme. After the static sGuardActive flag
 * has been set, any further allocation of AllocationGuard or its derivatives
 * will cause an error log entry. Note that AllocationGuard requires all of its
 * derivatives to call super() in their constructor.
 */
public class AllocationGuard {
	public static boolean sGuardActive = false;

	public AllocationGuard() {
		if (sGuardActive) {
			// An allocation has occurred while the guard is active! Report it.
			DebugLog.e("AllocGuard", "An allocation of type "
					+ this.getClass().getName()
					+ " occurred while the AllocGuard is active.");

		}
	}
}