package org.vn.herowar;

import org.vn.constant.CommandClientToServer;
import org.vn.custom.BoardAdapter;
import org.vn.gl.Utils;
import org.vn.model.Board;
import org.vn.network.GlobalMessageHandler.LightWeightMessage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BoardActivity extends CoreActiity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.board);
		gridViewBoard = (GridView) findViewById(R.id.gridViewBoard);
		ImageView bt_gold = (ImageView) findViewById(R.id.bt_gold);
		layout_gold = (View) findViewById(R.id.layout_gold);
		layout_gold.setVisibility(View.GONE);

		bt_refresh = (ImageView) findViewById(R.id.bt_refresh);
		bt_refresh.setVisibility(View.GONE);
		bt_refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mGS.SYS_BOARD_LIST();
				showProgressDialog();
				bt_refresh.setVisibility(View.GONE);
			}
		});

		bt_gold.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				layout_gold.setVisibility(View.VISIBLE);
				mHandlerTurnOffBtGold.sendMessageDelayed(
						mHandlerTurnOffBtGold.obtainMessage(1), 1000);
			}
		});

		layout_gold.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				closeLayoutGold();
				return true;
			}
		});
		mGS.SYS_BOARD_LIST();
		showProgressDialog();
	}

	ImageView bt_refresh;
	View layout_gold;
	Handler mHandlerTurnOffBtRefresh = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			bt_refresh.setVisibility(View.VISIBLE);
		}
	};
	Handler mHandlerTurnOffBtGold = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			closeLayoutGold();
		}
	};
	private static final int DLG_EXIT = 1;
	GridView gridViewBoard;

	@Override
	public void onMessageReceived(LightWeightMessage msg) {
		switch (msg.command) {
		case CommandClientToServer.LOST_CONNECTION:
			onDisconnect();
			break;
		case CommandClientToServer.SYS_BOARD_LIST:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dismissProgressDialog();
					mHandlerTurnOffBtRefresh.removeMessages(1);
					mHandlerTurnOffBtRefresh.sendMessageDelayed(
							mHandlerTurnOffBtRefresh.obtainMessage(1), 1000);
				}
			});

			updateBoard((Board[]) msg.obj);
			break;
		case CommandClientToServer.JOIN_BOARD:
			dismissProgressDialog();
			if (msg.arg1 == 1) {
				gotoWaitingActivity();
			} else {
				showDialogNotification(getString(R.string.JOIN_BOARD_FALL));
			}
			break;
		}
	}

	public void updateBoard(final Board[] boards) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				gridViewBoard.setAdapter(new BoardAdapter(BoardActivity.this,
						boards));
				gridViewBoard.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						Toast.makeText(
								getApplicationContext(),
								((TextView) v
										.findViewById(R.id.grid_item_label))
										.getText(), Toast.LENGTH_SHORT).show();
						mGS.JOIN_BOARD(boards[position].id);
						showProgressDialog();
					}
				});
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (layout_gold.getVisibility() == View.VISIBLE) {
				layout_gold.setVisibility(View.GONE);
			} else {
				showDialog(DLG_EXIT);
			}
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
									finish();
									mGS.LOGOUT();
								}
							}).setNegativeButton(R.string.dialog_cancel, null)
					.create();
		}
		return super.onCreateDialog(id);

	}

	private void gotoWaitingActivity() {
		Intent intent = new Intent(this, WaitingActivity.class);
		startActivity(intent);
		finish();
	}

	private void closeLayoutGold() {
		mHandlerTurnOffBtGold.removeMessages(1);
		layout_gold.setVisibility(View.GONE);
		showToast(getString(R.string.Chuc_nang_cap_nhat_sau));
	}

	@Override
	protected int getRawBackground() {
		return R.raw.out_game1 + Utils.RANDOM.nextInt(4);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
