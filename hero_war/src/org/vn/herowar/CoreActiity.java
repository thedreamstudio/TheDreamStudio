package org.vn.herowar;

import org.vn.network.GlobalMessageHandler;
import org.vn.network.GlobalMessageHandler.MessageListener;
import org.vn.network.GlobalService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public abstract class CoreActiity extends Activity implements MessageListener {
	protected GlobalService mGS = GlobalService.getInstance();
	protected ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalMessageHandler.getInstance().addMessageListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GlobalMessageHandler.getInstance().removeMessageListener(this);
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	protected void showDialogNotification(String content) {
		new AlertDialog.Builder(this).setMessage(content)
				.setPositiveButton(R.string.dialog_ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				}).show();
	}

	protected void showProgressDialog() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mDialog == null) {
					mDialog = new ProgressDialog(CoreActiity.this);
					mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					mDialog.setMessage(getText(R.string.loading));
					mDialog.setIndeterminate(true);
					mDialog.show();
				} else if (!mDialog.isShowing()) {
					mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					mDialog.setMessage(getText(R.string.loading));
					mDialog.setIndeterminate(true);
					mDialog.show();
				}
			}
		});
	}

	protected void dismissProgressDialog() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
			}
		});
	}

	public void onDisconnect() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(CoreActiity.this)
						.setMessage("Connection lost")
						.setCancelable(false)
						.setPositiveButton(R.string.dialog_ok,
								new OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										finish();
									}
								}).show();
			}
		});

	}
}
