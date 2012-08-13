package org.vn.unit;

import org.vn.gl.BaseObject;
import org.vn.gl.DrawableBitmap;
import org.vn.gl.RenderSystem;
import org.vn.gl.TextureLibrary;
import org.vn.gl.Utils;
import org.vn.gl.Vector2;
import org.vn.gl.iBitmapInImageCache;
import org.vn.herowar.R;

import android.graphics.Bitmap;
import android.graphics.Color;

public class DialogChatInGame {
	private DrawableBitmap mChatOfChar;
	private float mTimeShowChat = 0;
	private Vector2 drawChat = new Vector2();

	public DialogChatInGame(TextureLibrary textureLibrary, float width,
			float height) {
		mChatOfChar = new DrawableBitmap(textureLibrary.allocateTextureNotHash(
				R.drawable.dialog_back, "khungchat"), width, height);
	}

	public void inputChat(final String content, int time, float x, float y) {
		iBitmapInImageCache bitmapInImageCache = new iBitmapInImageCache() {

			@Override
			public Bitmap getBitMapResourcesItem() {
				Bitmap bitmapKhungChat = Utils.decodeRawResource(
						BaseObject.sSystemRegistry.contextParameters.context
								.getResources(), R.drawable.dialog_back);
				Utils.setText(bitmapKhungChat, content, 30,
						Color.argb(255, 255, 255, 255), 10);
				return bitmapKhungChat;
			}
		};

		mChatOfChar.changeBitmap(bitmapInImageCache, true);
		mTimeShowChat = time;
		drawChat.set(x, y);
	}

	public void drawChat(float timeDelta, RenderSystem render, int priority) {
		if (mTimeShowChat > 0) {
			if (mTimeShowChat > 1) {
				mChatOfChar.setColorExpressF(1, 1, 1, 1);
			} else {
				mChatOfChar.setColorExpressF(mTimeShowChat, mTimeShowChat,
						mTimeShowChat, mTimeShowChat);
			}
			render.scheduleForDraw(mChatOfChar, drawChat, priority, false);
			mTimeShowChat -= timeDelta;
		}
	}
}
