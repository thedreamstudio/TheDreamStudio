package org.vn.gl;

public final class Lerp {

	public static float lerp(float start, float target, float duration,
			float timeSinceStart) {
		float value = start;
		if (timeSinceStart > 0.0f && timeSinceStart < duration) {
			final float range = target - start;
			final float percent = timeSinceStart / duration;
			value = start + (range * percent);
		} else if (timeSinceStart >= duration) {
			value = target;
		}
		return value;
	}

	public static float ease(float start, float target, float duration,
			float timeSinceStart) {
		float value = start;
		if (timeSinceStart > 0.0f && timeSinceStart < duration) {
			final float range = target - start;
			final float percent = timeSinceStart / (duration / 2.0f);
			if (percent < 1.0f) {
				value = start + ((range / 2.0f) * percent * percent * percent);
			} else {
				final float shiftedPercent = percent - 2.0f;
				value = start
						+ ((range / 2.0f) * ((shiftedPercent * shiftedPercent * shiftedPercent) + 2.0f));
			}
		} else if (timeSinceStart >= duration) {
			value = target;
		}
		return value;
	}
}
