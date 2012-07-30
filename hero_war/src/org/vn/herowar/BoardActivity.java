package org.vn.herowar;

import org.vn.constant.CommandClientToServer;
import org.vn.custom.ImageAdapter;
import org.vn.model.Board;
import org.vn.network.GlobalMessageHandler.LightWeightMessage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
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
		mGS.SYS_BOARD_LIST();
		showProgressDialog();
	}

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
		final String[] ListBoard = new String[boards.length];
		for (int i = 0; i < ListBoard.length; i++) {
			ListBoard[i] = "board" + boards[i].id;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				gridViewBoard.setAdapter(new ImageAdapter(BoardActivity.this,
						ListBoard));
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
									if (mGS.isConnected()) {
										mGS.close();
									}
									finish();
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
}
