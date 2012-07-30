package org.vn.herowar;

import org.vn.constant.CommandClientToServer;
import org.vn.constant.Constants;
import org.vn.model.Server;
import org.vn.network.GlobalMessageHandler.LightWeightMessage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends CoreActiity {
	private static final int DLG_EXIT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);
		final EditText btIp = (EditText) findViewById(R.id.editIp);
		final EditText useName = (EditText) findViewById(R.id.editTextUserName);
		final EditText pass = (EditText) findViewById(R.id.editTextPass);
		Button btLogin = (Button) findViewById(R.id.buttonLogin);

		final SharedPreferences mCachePreferences = getSharedPreferences(
				Constants.CACHE_PREFERENCES, MODE_PRIVATE);
		final String sIp = mCachePreferences.getString(Constants.CACHE_IP,
				"localhost");
		btIp.setText(sIp);
		useName.setText("test1");
		pass.setText("t");

		btLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (sIp.compareTo(btIp.getText().toString()) != 0) {
					Editor editor = mCachePreferences.edit();
					editor.putString(Constants.CACHE_IP, btIp.getText()
							.toString());
					editor.commit();
				}
				doLogin(btIp.getText().toString(),
						useName.getText().toString(), pass.getText().toString());
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

	private void doLogin(final String ip, final String username,
			final String pass) {
		if (ip.length() == 0) {
			showDialogNotification(getString(R.string.login_ip_ko_dc_trong));
		} else if (username.length() == 0) {
			showDialogNotification(getString(R.string.login_usename_ko_dc_trong));
		} else if (pass.length() == 0) {
			showDialogNotification(getString(R.string.login_pass_ko_dc_trong));
		} else {
			showProgressDialog();
			new Thread() {
				@Override
				public void run() {
					Server server = new Server(ip, 12345, "Mặc định");
					if (!mGS.isConnected()) {
						mGS.connect(server);
					}
					if (mGS.isConnected()) {
						mGS.LOGIN_TRIAL(username);
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dismissProgressDialog();
								showDialogNotification(getString(R.string.login_ko_conect_toi_server));
							}
						});
					}
				};
			}.start();

		}
	}

	@Override
	public void onMessageReceived(LightWeightMessage msg) {
		switch (msg.command) {
		case CommandClientToServer.SYS_LOGIN:
			if (msg.arg1 == 1) {
				mGS.SET_GAME_TYPE((byte) 1);
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dismissProgressDialog();
						showDialogNotification(getString(R.string.login_saipass_hoacid));
					}
				});
			}
			break;
		case CommandClientToServer.SET_GAME_TYPE:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dismissProgressDialog();
					gotoBoard();
				}
			});
			break;
		case CommandClientToServer.LOST_CONNECTION:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dismissProgressDialog();
					// showDialogNotification(getString(R.string.login_saipass_hoacid));
				}
			});
			break;
		default:
			break;
		}
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

	private void gotoBoard() {
		Intent intent = new Intent(this, BoardActivity.class);
		startActivity(intent);
		// finish();
	}
}
