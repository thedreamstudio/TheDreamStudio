package org.vn.unit;

import org.vn.gl.DrawableMesh;
import org.vn.gl.Priority;
import org.vn.gl.RenderSystem;
import org.vn.gl.TextureLibrary;
import org.vn.gl.Vector2;
import org.vn.herowar.R;

public class InfoCharacter {
	private DrawableMesh drawableObj;
	private Vector2 posObj = new Vector2(200, 10);
	private boolean isVisible = false;
	private Vector2 posHp = new Vector2(260, 30);
	private Vector2 posMana = new Vector2(260, 17);
	private float WIDTH_DRAW_HEALTH = 200;
	private DrawableMesh mDrawableBitmapHpCurrent;
	private DrawableMesh mDrawableBitmapManaCurrent;
	private UnitCharacterSwordmen characterSwordmenTaget;

	public InfoCharacter(TextureLibrary textureLibrary) {
		drawableObj = new DrawableMesh(null, 60, 60);
		mDrawableBitmapHpCurrent = new DrawableMesh(
				textureLibrary.allocateTexture(R.drawable.hp_bar, "hp_bar"),
				WIDTH_DRAW_HEALTH, 10);
		mDrawableBitmapManaCurrent = new DrawableMesh(
				textureLibrary.allocateTexture(R.drawable.hp_bar, "hp_bar"),
				WIDTH_DRAW_HEALTH, 10);
		mDrawableBitmapManaCurrent.setColorExpress(0.3f, 0.3f, 0.7f, 1);
	}

	public void draw(RenderSystem renderSystem) {
		if (isVisible) {
			renderSystem.scheduleForDraw(drawableObj, posObj,
					Priority.InfoCharacter, false);
			drawHp(renderSystem, characterSwordmenTaget.mMaxHp,
					characterSwordmenTaget.mCurrentHp);
			drawMana(renderSystem, characterSwordmenTaget.mMaxMana,
					characterSwordmenTaget.mCurrentMana);
		}
	}

	public void setCharacterFocus(UnitCharacter character) {
		if (character == null) {
			isVisible = false;
		} else {
			isVisible = true;
			character.getIcon(drawableObj);
		}
		characterSwordmenTaget = (UnitCharacterSwordmen) character;
	}

	private void drawHp(RenderSystem renderSystem, float mMaxHp,
			float mCurrentHp) {
		// renderSystem
		// .scheduleForDraw(mDrawableBitmapHpFull, posHp,
		// Priority.Character, false);
		float fanTramSoMauConLai = mCurrentHp / (mMaxHp == 0 ? 1 : mMaxHp);
		mDrawableBitmapHpCurrent.setWidth(WIDTH_DRAW_HEALTH
				* fanTramSoMauConLai);
		mDrawableBitmapHpCurrent.setCoordinates(0, fanTramSoMauConLai, 0, 1);
		if (fanTramSoMauConLai >= 0.5) {
			fanTramSoMauConLai = (fanTramSoMauConLai - 0.5f) * 2;
			mDrawableBitmapHpCurrent.setColorExpress(1 - fanTramSoMauConLai, 1,
					0.3f, 1);
		} else {
			fanTramSoMauConLai = fanTramSoMauConLai * 2;
			mDrawableBitmapHpCurrent.setColorExpress(1, fanTramSoMauConLai,
					0.3f, 1);
		}
		renderSystem.scheduleForDraw(mDrawableBitmapHpCurrent, posHp,
				Priority.InfoCharacter, false);
	}

	private void drawMana(RenderSystem renderSystem, float mMaxMana,
			float mCurrentMana) {
		// renderSystem
		// .scheduleForDraw(mDrawableBitmapHpFull, posHp,
		// Priority.Character, false);
		float fanTramSoMauConLai = mCurrentMana
				/ (mMaxMana == 0 ? 1 : mMaxMana);
		mDrawableBitmapManaCurrent.setWidth(WIDTH_DRAW_HEALTH
				* fanTramSoMauConLai);
		mDrawableBitmapManaCurrent.setCoordinates(0, fanTramSoMauConLai, 0, 1);
		renderSystem.scheduleForDraw(mDrawableBitmapManaCurrent, posMana,
				Priority.InfoCharacter, false);
	}
}
