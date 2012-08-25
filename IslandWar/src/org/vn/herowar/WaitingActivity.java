package org.vn.herowar;

import java.util.ArrayList;

import org.vn.cache.CurrentGameInfo;
import org.vn.cache.CurrentUserInfo;
import org.vn.constant.CommandClientToServer;
import org.vn.custom.ImageAdapter;
import org.vn.gl.Utils;
import org.vn.model.Achievement;
import org.vn.model.EnemyType;
import org.vn.model.PlayerModel;
import org.vn.network.GlobalMessageHandler.LightWeightMessage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class WaitingActivity extends CoreActiity {
	private static final int DLG_EXIT = 1;
	private CurrentGameInfo mCurrentGameInfo = CurrentGameInfo.getIntance();
	EditText editFog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.waiting);
		mListViewPlayerInBoard = (ListView) findViewById(R.id.listViewPlayerInBoard);
		mBtChat = (Button) findViewById(R.id.btChatWaiting);
		mEdChat = (EditText) findViewById(R.id.editTextChatWaiting);
		mListviewChatInWaiting = (ListView) findViewById(R.id.listViewChatInWaiting);
		// mEnemyShop = (ListView) findViewById(R.id.listViewEnemyInWaiting);
		mBtReady = (Button) findViewById(R.id.btReadyEnemyWaiting);
		// mTextViewGold = (TextView) findViewById(R.id.textInforGoldWaiting);
		// mEnemyChosse = (ListView) findViewById(R.id.listViewEnemyChossen);

		mBtChat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String content = mEdChat.getText().toString();
				if (content.length() > 0) {
					mGS.CHAT_BOARD(content);
					mEdChat.setText("");
					mBtChat.setClickable(false);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mBtChat.getWindowToken(), 0);
				}
			}
		});

		mEdChat.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				editTextChatChange("" + arg0);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				editTextChatChange("" + arg0);
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		mBtReady.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showProgressDialog();
				if (mCurrentGameInfo.isHost()) {
					mGS.START_GAME();
				} else {
					mGS.READY();
				}
				mBtReady.setTextColor(Color.WHITE);
				mBtReady.setEnabled(true);
			}
		});
		{
			ArrayList<String> mList_content_chat = new ArrayList<String>();
			mAdapterChat = new ArrayAdapter<String>(this,
					R.layout.array_string, mList_content_chat);
			mListviewChatInWaiting.setAdapter(mAdapterChat);
			mAdapterChat.setNotifyOnChange(true);
		}
		processMs();
	}

	synchronized public void processMs() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updatePerson();
				mBtReady.setTextColor(Color.BLACK);
				mBtReady.setEnabled(false);
				if (mCurrentGameInfo.isHost()) {
					mBtReady.setText("Start");
				} else {
					mBtReady.setText("Ready");
				}
				// CHECK GET_ARMY_SHOP
				if (mCurrentGameInfo.listEnemytype.size() == 0) {
					showProgressDialog();
					mGS.GET_ARMY_SHOP();
				} else {
					updateGold();
					if (mCurrentGameInfo.listMaps.size() == 0) {
						showProgressDialog();
						mGS.GET_ALL_MAP();
					} else if (mCurrentGameInfo.mMapSelected == null) {
						if (mCurrentGameInfo.isHost()) {
							showProgressDialog();
							mGS.SET_MAP(mCurrentGameInfo.listMaps.get(0).mapId);
						}
					} else {
						if (mCurrentGameInfo.mMapSelected.mapType == null) {
							showProgressDialog();
							mGS.GET_MAP(mCurrentGameInfo.mMapSelected.mapId);
						} else {
							dismissProgressDialog();
							if (mCurrentGameInfo.isHost()) {
								if (mCurrentGameInfo.isReadyAll()) {
									mBtReady.setTextColor(Color.WHITE);
									mBtReady.setEnabled(true);
								}
							} else {
								for (PlayerModel playerInBoard : mCurrentGameInfo.mListPlayerInGame) {
									if (playerInBoard.ID == CurrentUserInfo.mPlayerInfo.ID) {
										if (playerInBoard.isReady) {
											mBtReady.setTextColor(Color.BLACK);
										} else {
											mBtReady.setTextColor(Color.WHITE);
										}
										mBtReady.setEnabled(true);
										break;
									}
								}
							}
						}
					}
				}
			}
		});
	}

	private ListView mListViewPlayerInBoard;
	private Button mBtChat;
	private EditText mEdChat;
	private ListView mListviewChatInWaiting;
	private ArrayAdapter<String> mAdapterChat;
	// private ListView mEnemyShop;
	// private ListView mEnemyChosse;
	private Button mBtReady;
	// private TextView mTextViewGold;
	// private EnemyShopAdapter mEnemyShopAdapter;
	// private EnemyShopAdapter mEnemyChosseAdapter;
	public ArrayList<EnemyType> listEnemyChossen = new ArrayList<EnemyType>();
	private int mCurrenGold;

	@Override
	public void onMessageReceived(LightWeightMessage msg) {
		switch (msg.command) {
		case CommandClientToServer.LOST_CONNECTION:
			onDisconnect();
			break;
		case CommandClientToServer.SOMEONE_JOIN_BOARD:
		case CommandClientToServer.SOMEONE_LEAVE_BOARD:
			processMs();
			break;
		case CommandClientToServer.CHAT_BOARD:
			String content = (String) msg.obj;
			final String tamp = content;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mAdapterChat.add(tamp);
				}
			});
			break;
		case CommandClientToServer.GET_ARMY_SHOP:
			processMs();
			break;
		case CommandClientToServer.GET_ALL_MAP:
			processMs();
			break;
		case CommandClientToServer.SET_MAP:
			processMs();
			break;
		case CommandClientToServer.GET_MAP:
			processMs();
			break;
		case CommandClientToServer.READY:
			String s = (String) msg.obj;
			if (s != null) {
				showToast(getString(R.string.ready_in_game, s));
			}
			processMs();
			break;
		case CommandClientToServer.START_GAME:
			showToast(getString(R.string.The_game_has_start));
			gotoInGame();
			break;
		case CommandClientToServer.ACHIEVEMENT:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updatePerson();
				}
			});
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(DLG_EXIT);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DLG_EXIT:
			return new AlertDialog.Builder(this)
					.setMessage(getString(R.string.exit))
					.setPositiveButton(R.string.dialog_ok,
							new OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									gotoBoard();
								}
							}).setNegativeButton(R.string.dialog_cancel, null)
					.create();
		}
		return super.onCreateDialog(id);
	}

	private void gotoBoard() {
		// mGS.EXIT_BOARD(CurrentGameInfo.getIntance().boardId);
		mGS.LEAVE_BOARD();
		Intent intent = new Intent(this, BoardActivity.class);
		startActivity(intent);
		finish();
	}

	private void editTextChatChange(String content) {
		if (content.length() == 0) {
			mBtChat.setClickable(false);
		} else {
			mBtChat.setClickable(true);
		}
	}

	public void updatePerson() {

		ArrayList<PlayerModel> listPlayerInBoard = mCurrentGameInfo.mListPlayerInGame;
		final String[] ListPlayers = new String[listPlayerInBoard.size()];
		for (int i = 0; i < ListPlayers.length; i++) {
			ListPlayers[i] = "[" + listPlayerInBoard.get(i).ID + "]"
					+ listPlayerInBoard.get(i).name;
			Achievement achievement = mCurrentGameInfo.mHashAchievement
					.get(listPlayerInBoard.get(i).name);
			if (achievement != null) {
				ListPlayers[i] += " - Win:" + achievement.winnumber;
			}
		}
		mListViewPlayerInBoard.setAdapter(new ImageAdapter(
				WaitingActivity.this, ListPlayers));
	}

	public void updateGold() {
		mCurrenGold = mCurrentGameInfo.gold;
		for (EnemyType enemy : listEnemyChossen) {
			mCurrenGold -= enemy.cost;
		}
	}

	private void gotoInGame() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(WaitingActivity.this,
						HeroWarActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	protected int getRawBackground() {
		return R.raw.out_game1 + Utils.RANDOM.nextInt(4);
	}
}
