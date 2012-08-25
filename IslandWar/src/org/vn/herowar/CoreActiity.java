package org.vn.herowar;

import org.vn.network.GlobalMessageHandler;
import org.vn.network.GlobalMessageHandler.MessageListener;
import org.vn.network.GlobalService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public abstract class CoreActiity extends Activity implements MessageListener {
	protected GlobalService mGS = GlobalService.getInstance();
	protected ProgressDialog mDialog;
	protected boolean isLoginScreen = false;
	MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalMessageHandler.getInstance().addMessageListener(this);
		playSound();
	}

	Handler playSoundHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			playSound();
		};
	};

	private void playSound() {
		try {
			if (mMediaPlayer != null) {
				mMediaPlayer.stop();
			}
			mMediaPlayer = MediaPlayer.create(CoreActiity.this,
					getRawBackground());
			mMediaPlayer.setLooping(false);
			mMediaPlayer.start();
			playSoundHandler.removeMessages(1);
			playSoundHandler.sendMessageDelayed(
					playSoundHandler.obtainMessage(1),
					mMediaPlayer.getDuration());
		} catch (Exception e) {
		}
	}

	abstract protected int getRawBackground();

	@Override
	protected void onResume() {
		super.onResume();
		try {
			if (!isLoginScreen && !mGS.isConnected()) {
				onDisconnect();
				if (mMediaPlayer.isPlaying()) {
					mMediaPlayer.stop();
					playSoundHandler.removeMessages(1);
				}
			} else {
				if (!mMediaPlayer.isPlaying()) {
					playSoundHandler.removeMessages(1);
					mMediaPlayer.start();
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			playSoundHandler.removeMessages(1);
			mMediaPlayer.stop();
			GlobalMessageHandler.getInstance().removeMessageListener(this);
			if (mDialog != null) {
				mDialog.dismiss();
			}
		} catch (Exception e) {
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

	public void showToast(final String content) {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), content,
							Toast.LENGTH_SHORT).show();
				}
			});
		} catch (Exception e) {
		}

	}

	protected void showProgressDialog() {
		try {
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
		} catch (Exception e) {
		}

	}

	protected void dismissProgressDialog() {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mDialog != null && mDialog.isShowing()) {
						mDialog.dismiss();
					}
				}
			});
		} catch (Exception e) {
		}
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

	@Override
	protected void onPause() {
		try {
			playSoundHandler.removeMessages(1);
			mMediaPlayer.pause();
		} catch (Exception e) {
		}

		super.onPause();
	}

}
