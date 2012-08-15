package org.vn.herowar;

import java.util.ArrayList;

import org.vn.cache.CurrentGameInfo;
import org.vn.cache.CurrentUserInfo;
import org.vn.constant.CommandClientToServer;
import org.vn.custom.ImageAdapter;
import org.vn.gl.Utils;
import org.vn.model.EnemyType;
import org.vn.model.PlayerModel;
import org.vn.network.GlobalMessageHandler.LightWeightMessage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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
		updatePerson();

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
				if (CurrentUserInfo.mPlayerInfo.ID == mCurrentGameInfo.ownerId) {
					mGS.SET_MAP(mCurrentGameInfo.listMaps.get(0).mapId);
					showProgressDialog();
				}
				mBtReady.setVisibility(View.GONE);
			}
		});
		if (CurrentUserInfo.mPlayerInfo.ID != mCurrentGameInfo.ownerId) {
			mBtReady.setVisibility(View.GONE);
		}

		{
			ArrayList<String> mList_content_chat = new ArrayList<String>();
			mAdapterChat = new ArrayAdapter<String>(this,
					R.layout.array_string, mList_content_chat);
			mListviewChatInWaiting.setAdapter(mAdapterChat);
			mAdapterChat.setNotifyOnChange(true);
		}
		// {
		// final ArrayList<EnemyType> mList_enemy = new ArrayList<EnemyType>();
		// mEnemyShopAdapter = new EnemyShopAdapter(this,
		// android.R.layout.simple_list_item_1, mList_enemy);
		// mEnemyShop.setAdapter(mEnemyShopAdapter);
		// mEnemyShopAdapter.setNotifyOnChange(true);
		// mEnemyShop.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> parent, View v,
		// int position, long id) {
		// EnemyType enemyType = mEnemyShopAdapter.getItem(position);
		// updateGold();
		// if (mCurrenGold - enemyType.cost >= 0) {
		// addEnemyChossen(enemyType);
		// } else {
		// Toast.makeText(getApplicationContext(),
		// getString(R.string.Dont_enought_money),
		// Toast.LENGTH_SHORT).show();
		// }
		// }
		// });
		// }
		// {
		// mEnemyChosseAdapter = new EnemyShopAdapter(this,
		// android.R.layout.simple_list_item_1, listEnemyChossen);
		// mEnemyChosse.setAdapter(mEnemyChosseAdapter);
		// mEnemyChosseAdapter.setNotifyOnChange(true);
		// mEnemyChosse.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> parent, View v,
		// int position, long id) {
		// Toast.makeText(getApplicationContext(),
		// mEnemyChosseAdapter.getItem(position).armyName,
		// Toast.LENGTH_SHORT).show();
		// removeEnemyChossen(mEnemyChosseAdapter.getItem(position));
		// }
		// });
		// }

		processMs();

	}

	synchronized public void processMs() {
		// GET_ARMY_SHOP
		if (mCurrentGameInfo.listEnemytype.size() == 0) {
			showProgressDialog();
			mBtReady.setVisibility(View.GONE);
			mGS.GET_ARMY_SHOP();
		} else {
			dismissProgressDialog();
			updateEnemyShop();
			updateGold();
			if (CurrentUserInfo.mPlayerInfo.ID == mCurrentGameInfo.ownerId) {
				if (mCurrentGameInfo.mMapSelected != null) {
					if (mCurrentGameInfo.mMapSelected.mapType == null) {
						showProgressDialog();
						mGS.GET_MAP(mCurrentGameInfo.mMapSelected.mapId);
					} else {
						gotoInGame();
					}
				} else if (mCurrentGameInfo.listMaps.size() == 0) {
					showProgressDialog();
					mBtReady.setVisibility(View.GONE);
					showProgressDialog();
					mGS.GET_ALL_MAP();
				} else {
					mBtReady.setVisibility(View.VISIBLE);
				}
			} else {
				mBtReady.setVisibility(View.GONE);
				if (mCurrentGameInfo.mMapSelected != null) {
					if (mCurrentGameInfo.mMapSelected.mapType == null) {
						showProgressDialog();
						mGS.GET_MAP(mCurrentGameInfo.mMapSelected.mapId);
					} else {
						gotoInGame();
					}
				}
			}
		}
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
			updatePerson();
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
			dismissProgressDialog();
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mBtReady.setVisibility(View.VISIBLE);
				}
			});

			break;
		case CommandClientToServer.SET_MAP:
			if (msg.arg1 == 1) {
				processMs();
			}
			break;
		case CommandClientToServer.GET_MAP:
			processMs();
			break;
		default:
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
		ArrayList<PlayerModel> listPlayerInBoard = CurrentGameInfo.getIntance().mListPlayerInGame;
		final String[] ListPlayers = new String[listPlayerInBoard.size()];
		for (int i = 0; i < ListPlayers.length; i++) {
			ListPlayers[i] = listPlayerInBoard.get(i).ID + ":"
					+ listPlayerInBoard.get(i).name;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mListViewPlayerInBoard.setAdapter(new ImageAdapter(
						WaitingActivity.this, ListPlayers));
				if (CurrentUserInfo.mPlayerInfo.ID == mCurrentGameInfo.ownerId) {
					mBtReady.setVisibility(View.VISIBLE);
				} else {
					mBtReady.setVisibility(View.GONE);
				}
			}
		});
	}

	public void updateEnemyShop() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// mEnemyShopAdapter.clear();
				// for (EnemyType enemy :
				// CurrentGameInfo.getIntance().listEnemytype) {
				// mEnemyShopAdapter.add(enemy);
				// }
			}
		});
	}

	public void updateGold() {
		mCurrenGold = mCurrentGameInfo.gold;
		for (EnemyType enemy : listEnemyChossen) {
			mCurrenGold -= enemy.cost;
		}
	}

	public void updateGoldUi() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// mTextViewGold.setText("" + mCurrenGold);
			}
		});
	}

	public void updateEnemyChossen() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// mEnemyChosseAdapter.clear();
				// for (EnemyType enemy : listEnemyChossen) {
				// mEnemyChosseAdapter.add(enemy);
				// }
			}
		});
	}

	public void addEnemyChossen(final EnemyType enemy) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// mEnemyChosseAdapter.add(enemy);
				updateGold();
				updateGoldUi();
			}
		});
	}

	public void removeEnemyChossen(final EnemyType enemy) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// mEnemyChosseAdapter.remove(enemy);
				updateGold();
				updateGoldUi();
			}
		});
	}

	private boolean isReadyAll() {
		boolean isReadyAll = true;
		for (PlayerModel playerModel : mCurrentGameInfo.mListPlayerInGame) {
			if (playerModel.ID == -1
					|| (playerModel.ID != mCurrentGameInfo.ownerId && !playerModel.isReady)) {
				isReadyAll = false;
				break;
			}
		}
		return isReadyAll;
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
