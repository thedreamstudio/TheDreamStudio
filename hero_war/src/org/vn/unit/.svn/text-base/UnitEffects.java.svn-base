package org.vn.unit;

import java.util.ArrayList;

import org.vn.gl.BaseObject;
import org.vn.gl.Priority;
import org.vn.gl.Texture;
import org.vn.gl.TextureLibrary;
import org.vn.gl.Utils;
import org.vn.herowar.R;

public class UnitEffects extends BaseObject {
	private ArrayList<Effect> mListEffect;
	private EffectAnimation effectAnimationBtClick;

	public UnitEffects(TextureLibrary textureLibrary) {
		mListEffect = new ArrayList<Effect>();

		createCoiXayGio(textureLibrary);
		// createCotCo(textureLibrary);
		createCuu(textureLibrary, 2061, 900);
		createCuu(textureLibrary, 2051, 839);
		createCuu(textureLibrary, 2089, 930);
		createCuu(textureLibrary, 2110, 800);
		creatBtClick(textureLibrary);
	}

	public void createCuu(TextureLibrary textureLibrary, float x, float y) {
		// Create coi Cuu`
		int[] r = { R.drawable.image_1562, R.drawable.image_1564,
				R.drawable.image_1566, R.drawable.image_1568,
				R.drawable.image_1570, R.drawable.image_1572,
				R.drawable.image_1574, R.drawable.image_1576 };
		Texture[] texturesCoiXayGio = new Texture[r.length];
		for (int i = 0; i < texturesCoiXayGio.length; i++) {
			texturesCoiXayGio[i] = textureLibrary.allocateTexture(r[i],
					"Cot co" + i);
		}
		EffectAnimation effectAnimation = new EffectAnimation(
				texturesCoiXayGio, 0.16f, Priority.ItemAffterMap, x, y, 30, 30,
				Utils.RANDOM.nextInt(5)) {
			@Override
			public void beginLoop() {
				mTimeTick += Utils.RANDOM.nextInt(5);
			}
		};
		mListEffect.add(effectAnimation);
	}

	public void createCotCo(TextureLibrary textureLibrary, float x, float y) {
		// Create coi Co`
		int[] r = { R.drawable.image_1857, R.drawable.image_1859,
				R.drawable.image_1861, R.drawable.image_1863,
				R.drawable.image_1865, R.drawable.image_1867,
				R.drawable.image_1869, R.drawable.image_1871 };
		Texture[] texturesCoiXayGio = new Texture[r.length];
		for (int i = 0; i < texturesCoiXayGio.length; i++) {
			texturesCoiXayGio[i] = textureLibrary.allocateTexture(r[i],
					"Cot co" + i);
		}
		EffectAnimation effectAnimation = new EffectAnimation(
				texturesCoiXayGio, 0.16f, Priority.CharacterKing, x - 32, y,
				64, 136, 0) {
			@Override
			public void beginLoop() {
				mTimeTick += Utils.RANDOM.nextInt(7);
			}
		};
		mListEffect.add(effectAnimation);
	}

	public void createCoiXayGio(TextureLibrary textureLibrary) {
		// Create coi xay gio
		int[] r = { R.drawable.image_1701, R.drawable.image_1703,
				R.drawable.image_1705, R.drawable.image_1707,
				R.drawable.image_1709, R.drawable.image_1711,
				R.drawable.image_1713, R.drawable.image_1715 };
		Texture[] texturesCoiXayGio = new Texture[r.length];
		for (int i = 0; i < texturesCoiXayGio.length; i++) {
			texturesCoiXayGio[i] = textureLibrary.allocateTexture(r[i],
					"Coi xay gio" + i);
		}
		EffectAnimation effectAnimation = new EffectAnimation(
				texturesCoiXayGio, 0.16f, Priority.ItemAffterMap, 270, 642,
				170, 170, 0);
		mListEffect.add(effectAnimation);
	}

	public void creatBtClick(TextureLibrary textureLibrary) {
		if (effectAnimationBtClick == null) {
			int[] r = { R.drawable.image_6359, R.drawable.image_6361,
					R.drawable.image_6363 };
			Texture[] textures = new Texture[r.length];
			for (int i = 0; i < textures.length; i++) {
				textures[i] = textureLibrary.allocateTexture(r[i],
						"Coi xay gio" + i);
			}
			effectAnimationBtClick = new EffectAnimation(textures, 0.08f,
					Priority.BtClick, 0, 0, 200, 200, 0);
			effectAnimationBtClick.mCountLoop = 0;
		}
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		for (int i = 0; i < mListEffect.size(); i++) {
			if (mListEffect.get(i).update(timeDelta) == false) {
				mListEffect.remove(i);
				i--;
			}
		}
	}

	public void addEffect(Effect pEffect) {
		mListEffect.add(pEffect);
	}

	public void addEffectSoBayLen(NumberDrawable numberDrawable, int num,
			float x, float y, boolean isCameraRelative, int priority) {
		EffectSoBayLen effectSoBayLen = new EffectSoBayLen(numberDrawable, num,
				x, y);
		effectSoBayLen.cameraRelative = isCameraRelative;
		effectSoBayLen.priority = priority;
		mListEffect.add(effectSoBayLen);
	}

	public void addEffectRungManHinh() {
		EffectRungManHinh effectRungManHinh = new EffectRungManHinh(0.4f);
		mListEffect.add(effectRungManHinh);
	}

	public void addEffectClick(float x, float y) {
		if (effectAnimationBtClick.mCountLoop <= 0) {
			effectAnimationBtClick.mCountLoop = 2;
			effectAnimationBtClick.set(x - 20, y - 20, 40, 40);
			mListEffect.add(effectAnimationBtClick);
		} else {
			effectAnimationBtClick.mCountLoop = 2;
			effectAnimationBtClick.set(x - 20, y - 20, 40, 40);
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}
}
