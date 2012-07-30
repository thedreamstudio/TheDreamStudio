package org.vn.unit;

import org.vn.gl.BaseObject;
import org.vn.gl.GameInfo;
import org.vn.gl.Priority;
import org.vn.gl.RenderSystem;
import org.vn.gl.TextureLibrary;

public class UnitSreen extends BaseObject {
	private DialogChatInGame mDialogChatInGameLeft_Bot;
	private DialogChatInGame mDialogChatInGameRight_Bot;
	private float width = 324;
	private float height = 162;

	private InfoCharacter mInfoCharacter;

	public UnitSreen(TextureLibrary textureLibrary) {
		mDialogChatInGameLeft_Bot = new DialogChatInGame(textureLibrary, width,
				height);
		mDialogChatInGameRight_Bot = new DialogChatInGame(textureLibrary,
				width, height);
		mInfoCharacter = new InfoCharacter(textureLibrary);
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
	}

	public void inputChatLeft_Bot(String content) {
		mDialogChatInGameLeft_Bot.inputChat(content, 10, 0, 0);
	}

	public void inputChatRight_Bot(String content) {
		mDialogChatInGameRight_Bot.inputChat(content, 10,
				GameInfo.DEFAULT_WIDTH - width, 0);
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
