package org.vn.unit;

import javax.microedition.khronos.opengles.GL10;

import org.vn.gl.BaseObject;
import org.vn.gl.DrawableBitmap;
import org.vn.gl.GameInfo;
import org.vn.gl.Priority;
import org.vn.gl.RenderSystem;
import org.vn.gl.TextureLibrary;
import org.vn.herowar.R;

public class UnitSreen extends BaseObject {
	private DialogChatInGame mDialogChatInGameLeft_Bot;
	private DialogChatInGame mDialogChatInGameRight_Bot;
	private float width = 200;
	private float height = 200;

	private InfoCharacter mInfoCharacter;

	private DrawableBitmap mDark1;
	private DrawableBitmap mDark2;

	public UnitSreen(TextureLibrary textureLibrary) {
		mDialogChatInGameLeft_Bot = new DialogChatInGame(textureLibrary, width,
				height);
		mDialogChatInGameRight_Bot = new DialogChatInGame(textureLibrary,
				width, height);
		mInfoCharacter = new InfoCharacter(textureLibrary);
		mDark1 = new DrawableBitmap(textureLibrary.allocateTexture(
				R.drawable.dark1, "dark1"), GameInfo.DEFAULT_WIDTH,
				GameInfo.DEFAULT_HEIGHT);
//		mDark1.setColorExpressF(0f, 0f, 0f, 0.3f);
		// mDark1.setGlBlendFun(GL10.GL_ZERO, GL10.GL_SRC_ALPHA);
		mDark2 = new DrawableBitmap(textureLibrary.allocateTexture(
				R.drawable.dark2, "dark2"), 200, 200);
		mDark2.setColorExpressF(1f, 1f, 1f, 0.3f);
		mDark2.setGlBlendFun(GL10.GL_ONE, GL10.GL_ONE);
	}

	@Override
	public void reset() {
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		RenderSystem renderSystem = sSystemRegistry.renderSystem;
		mDialogChatInGameLeft_Bot.drawChat(timeDelta, renderSystem,
				Priority.Chat);
		mDialogChatInGameRight_Bot.drawChat(timeDelta, renderSystem,
				Priority.Chat);
		mInfoCharacter.draw(renderSystem);

//		renderSystem.scheduleForDraw(mDark1, Vector2.ZERO,
//				Priority.BackGroundDark1, false);
		// renderSystem.scheduleForDraw(mDark2, Vector2.TAMP.set(220, 150),
		// Priority.BackGroundDark2, false);
		//
		// renderSystem.scheduleForDraw(mDark2, Vector2.TAMP.set(200, 200),
		// Priority.BackGroundDark2, false);
	}

	public void inputChatLeft_Bot(String content) {
		mDialogChatInGameLeft_Bot.inputChat(content, 10, GameInfo.DEFAULT_WIDTH
				- width - 3, height + 6);
	}

	public void inputChatRight_Bot(String content) {
		mDialogChatInGameRight_Bot.inputChat(content, 10,
				GameInfo.DEFAULT_WIDTH - width - 3, 3);
	}

	/**
	 * Hien thi thong tin nhan vat(hp,mana)
	 * 
	 * @param character
	 */
	public void setCharacterFocus(UnitCharacter character) {
		mInfoCharacter.setCharacterFocus(character);
	}

}
