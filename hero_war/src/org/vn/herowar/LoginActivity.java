package org.vn.herowar;

import org.vn.constant.CommandClientToServer;
import org.vn.constant.Constants;
import org.vn.gl.Utils;
import org.vn.model.Server;
import org.vn.network.GlobalMessageHandler.LightWeightMessage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

public class LoginActivity extends CoreActiity {
	private static final int DLG_EXIT = 1;
	private TableLayout tableLayoutTaget;
	TableLayout tableLayoutRegiter;
	TableLayout tableLayoutLogin;
	EditText useName;
	EditText pass;
	EditText useName_re;
	EditText pass_re;
	EditText pass_re2;
	EditText btIp;
	SharedPreferences mCachePreferences;
	String sUsername;
	String sPass;
	String sIp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);
		btIp = (EditText) findViewById(R.id.editIp);
		useName = (EditText) findViewById(R.id.editTextUserName);
		pass = (EditText) findViewById(R.id.editTextPass);
		useName_re = (EditText) findViewById(R.id.regiterUserName);
		pass_re = (EditText) findViewById(R.id.regiterPass);
		pass_re2 = (EditText) findViewById(R.id.regiterPass2);
		final EditText btPhone_Number_re = (EditText) findViewById(R.id.regiterPhone);
		final View bt_facebook = (View) findViewById(R.id.bt_facebook);
		final View bt_call = (View) findViewById(R.id.bt_call);
		tableLayoutRegiter = (TableLayout) findViewById(R.id.tableRe);
		tableLayoutLogin = (TableLayout) findViewById(R.id.tableLogin);
		tableLayoutTaget = tableLayoutLogin;
		tableLayoutRegiter.setVisibility(View.GONE);

		final Button btLogin = (Button) findViewById(R.id.buttonLogin);
		final Button btRegiter = (Button) findViewById(R.id.buttonRegiter);
		mCachePreferences = getSharedPreferences(Constants.CACHE_PREFERENCES,
				MODE_PRIVATE);
		sIp = mCachePreferences.getString(Constants.CACHE_IP, "localhost");
		btIp.setText(sIp);

		sUsername = mCachePreferences.getString(Constants.CACHE_USER_NAME,
				"test1");
		useName.setText(sUsername);
		sPass = mCachePreferences.getString(Constants.CACHE_PASS, "t");
		pass.setText(sPass);

		btLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!checkInternet()) {
					return;
				}
				if (tableLayoutTaget != tableLayoutLogin) {
					tableLayoutTaget = tableLayoutLogin;
					tableLayoutLogin.setVisibility(View.VISIBLE);
					tableLayoutRegiter.setVisibility(View.GONE);
					return;
				}
				if (sIp.compareTo(btIp.getText().toString()) != 0) {
					Editor editor = mCachePreferences.edit();
					editor.putString(Constants.CACHE_IP, btIp.getText()
							.toString());
					editor.commit();
					sIp = btIp.getText().toString();
				}

				doLogin(btIp.getText().toString(),
						useName.getText().toString(), pass.getText().toString());
			}
		});

		btRegiter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!checkInternet()) {
					return;
				}
				if (tableLayoutTaget != tableLayoutRegiter) {
					tableLayoutTaget = tableLayoutRegiter;
					tableLayoutRegiter.setVisibility(View.VISIBLE);
					tableLayoutLogin.setVisibility(View.GONE);
					return;
				}
				doRegiter(btIp.getText().toString(), useName_re.getText()
						.toString(), pass_re.getText().toString(),
						btPhone_Number_re.getText().toString());
			}
		});

		bt_facebook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showProgressDialog();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("https://www.google.com.vn/"));
				startActivity(intent);
			}
		});
		bt_call.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showProgressDialog();
				Utils.callSupport(LoginActivity.this, "01218816208");
			}
		});

		checkInternet();
	}

	public boolean checkInternet() {
		boolean isInternetConnection = Utils
				.checkInternetConnection(LoginActivity.this);
		if (!isInternetConnection) {
			AlertDialog alertDialog = new AlertDialog.Builder(
					LoginActivity.this)
					.setTitle(R.string.notification)
					.setMessage(R.string.network_settings)
					.setPositiveButton(R.string.dialog_yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									startActivity(new Intent(
											Settings.ACTION_WIRELESS_SETTINGS));
								}
							}).setNegativeButton(R.string.dialog_no, null)
					.create();
			alertDialog.show();
		}
		return isInternetConnection;
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
			// Luu lai thong tin
			if (sUsername.compareTo(username) != 0) {
				Editor editor = mCachePreferences.edit();
				editor.putString(Constants.CACHE_USER_NAME, username);
				editor.commit();
				sUsername = username;
			}
			if (sPass.compareTo(pass) != 0) {
				Editor editor = mCachePreferences.edit();
				editor.putString(Constants.CACHE_PASS, pass);
				editor.commit();
				sPass = pass;
			}

			showProgressDialog();
			new Thread() {
				@Override
				public void run() {
					Server server = new Server(ip, 12345, "Mặc định");
					if (!mGS.isConnected()) {
						mGS.connect(server);
					}
					if (mGS.isConnected()) {
						// mGS.LOGIN_TRIAL(username);
						mGS.SYS_LOGIN(username, pass);
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

	private void doRegiter(final String ip, final String username,
			final String pass, final String phone_num) {
		if (username.length() == 0) {
			showDialogNotification(getString(R.string.login_usename_ko_dc_trong));
		} else if (pass.length() == 0) {
			showDialogNotification(getString(R.string.login_pass_ko_dc_trong));
		} else if (pass_re2.getText().toString()
				.compareTo(pass_re.getText().toString()) != 0) {
			showDialogNotification(getString(R.string.login_pass_re_pass_fail));
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
						mGS.SYS_REGISTER(username, pass, phone_num);
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
	public void onMessageReceived(final LightWeightMessage msg) {
		switch (msg.command) {
		case CommandClientToServer.SYS_REGISTER:
			final String info = (String) msg.obj;
			if (msg.arg1 == 1) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dismissProgressDialog();
						// Chuyen tab thanh login lai
						if (tableLayoutTaget != tableLayoutLogin) {
							tableLayoutTaget = tableLayoutLogin;
							tableLayoutLogin.setVisibility(View.VISIBLE);
							tableLayoutRegiter.setVisibility(View.GONE);
						}
						// Luu lai thong tin login
						{
							useName.setText(useName_re.getText().toString());
							pass.setText(pass_re.getText().toString());
						}
						new AlertDialog.Builder(LoginActivity.this)
								.setMessage(info)
								.setPositiveButton(R.string.dialog_ok,
										new OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												doLogin(btIp.getText()
														.toString(), useName_re
														.getText().toString(),
														pass_re.getText()
																.toString());
											}
										}).show();
					}
				});
			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						dismissProgressDialog();
						new AlertDialog.Builder(LoginActivity.this)
								.setMessage(info)
								.setPositiveButton(R.string.dialog_ok,
										new OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
											}
										}).show();
					}
				});

			}
			break;
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
		case CommandClientToServer.SERVER_MESSAGE:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), (String) msg.obj,
							Toast.LENGTH_SHORT).show();
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

	@Override
	protected void onResume() {
		super.onResume();
		dismissProgressDialog();
	}
}
