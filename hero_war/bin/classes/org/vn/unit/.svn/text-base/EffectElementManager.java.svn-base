package org.vn.unit;


import java.util.ArrayList;

public class EffectElementManager {
	private EffectElement[] arrayEffectParticles = null;
	private ArrayList<EffectElement> arrayEffectElementCanUsing = null;

	public EffectElementManager() {
		if (arrayEffectParticles == null) {
			arrayEffectParticles = new EffectElement[128];
			arrayEffectElementCanUsing = new ArrayList<EffectElement>();
			for (int i = 0; i < arrayEffectParticles.length; i++) {
				arrayEffectParticles[i] = new EffectElement(
						arrayEffectElementCanUsing);
				arrayEffectElementCanUsing.add(arrayEffectParticles[i]);
			}
		}
	}

	public EffectElement getEffectElementAndRemove() {
		synchronized (arrayEffectElementCanUsing) {
			if (arrayEffectElementCanUsing.size() > 0) {
				return arrayEffectElementCanUsing.remove(0);
			} else {
				return null;
			}
		}
	}
}
