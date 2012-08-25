package org.vn.herowar;

import org.vn.cache.CurrentGameInfo;
import org.vn.constant.CommandClientToServer;
import org.vn.custom.ResuftAdapter;
import org.vn.network.GlobalMessageHandler.LightWeightMessage;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;

public class ResuftActivity extends CoreActiity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resuft);
		ListView listResuft = (ListView) findViewById(R.id.resuft_item_list);

		listResuft.setAdapter(new ResuftAdapter(ResuftActivity.this,
				CurrentGameInfo.getIntance().result));

	}

	@Override
	public void onMessageReceived(LightWeightMessage msg) {
		switch (msg.command) {
		case CommandClientToServer.LOST_CONNECTION:
			onDisconnect();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		runOnUiThread(new Runnable() {
			public void run() {
				Intent intent = new Intent(ResuftActivity.this,
						WaitingActivity.class);
				startActivity(intent);
				finish();
			}
		});
		return false;
	}

	@Override
	protected int getRawBackground() {
		return R.raw.sound_resuft;
	}
}
