package org.vn.unit;

import org.vn.gl.BaseObject;
import org.vn.gl.SoundSystem;
import org.vn.gl.SoundSystem.Sound;
import org.vn.gl.Utils;
import org.vn.herowar.R;
import org.vn.model.EnemyType;

public class SoundManager extends BaseObject {
	Sound[][] talk;
	Sound coin;
	Sound[] shoot;
	Sound nextTurn;

	public SoundManager() {
		talk = new Sound[6][3];
		talk[0][0] = sSystemRegistry.soundSystem.load(R.raw.p1_1);
		talk[1][0] = sSystemRegistry.soundSystem.load(R.raw.p2_1);
		talk[2][0] = sSystemRegistry.soundSystem.load(R.raw.p3_1);
		talk[3][0] = sSystemRegistry.soundSystem.load(R.raw.p4_1);
		talk[4][0] = sSystemRegistry.soundSystem.load(R.raw.p5_1);
		talk[5][0] = sSystemRegistry.soundSystem.load(R.raw.p6_1);

		talk[0][1] = sSystemRegistry.soundSystem.load(R.raw.p1_2);
		talk[1][1] = sSystemRegistry.soundSystem.load(R.raw.p2_2);
		talk[2][1] = sSystemRegistry.soundSystem.load(R.raw.p3_2);
		talk[3][1] = sSystemRegistry.soundSystem.load(R.raw.p4_2);
		talk[4][1] = sSystemRegistry.soundSystem.load(R.raw.p5_2);
		talk[5][1] = sSystemRegistry.soundSystem.load(R.raw.p6_2);

		talk[0][2] = sSystemRegistry.soundSystem.load(R.raw.p1_3);
		talk[1][2] = sSystemRegistry.soundSystem.load(R.raw.p2_3);
		talk[2][2] = sSystemRegistry.soundSystem.load(R.raw.p3_3);
		talk[3][2] = sSystemRegistry.soundSystem.load(R.raw.p4_3);
		talk[4][2] = sSystemRegistry.soundSystem.load(R.raw.p5_3);
		talk[5][2] = sSystemRegistry.soundSystem.load(R.raw.p6_3);
		coin = sSystemRegistry.soundSystem.load(R.raw.sound_coin);

		shoot = new Sound[6];
		shoot[0] = sSystemRegistry.soundSystem.load(R.raw.shoot1);
		shoot[1] = sSystemRegistry.soundSystem.load(R.raw.shoot2);
		shoot[2] = sSystemRegistry.soundSystem.load(R.raw.shoot3);
		shoot[3] = sSystemRegistry.soundSystem.load(R.raw.shoot4);
		shoot[4] = sSystemRegistry.soundSystem.load(R.raw.shoot5);
		shoot[5] = sSystemRegistry.soundSystem.load(R.raw.shoot6);

		nextTurn = sSystemRegistry.soundSystem.load(R.raw.sound_next_turn);
	}

	public void playSeleted(EnemyType enemyType) {
		int type = Math.abs(enemyType.armyType) % 6;
		int type2 = Utils.RANDOM.nextInt(talk[0].length - 1);
		sSystemRegistry.soundSystem.play(talk[type][type2], false,
				SoundSystem.PRIORITY_NORMAL, 1, 1);
	}

	public void playShoot(EnemyType enemyType) {
		int type = Math.abs(enemyType.armyType) % 6;
		int type2 = talk[0].length - 1;
		sSystemRegistry.soundSystem.play(talk[type][type2], false,
				SoundSystem.PRIORITY_NORMAL, 1, 1);
		sSystemRegistry.soundSystem.play(shoot[type], false,
				SoundSystem.PRIORITY_NORMAL, 1, 1);
	}

	public void playSoundCoin() {
		sSystemRegistry.soundSystem.play(coin, false,
				SoundSystem.PRIORITY_NORMAL, 1, 1);
	}

	public void playNextTurn() {
		sSystemRegistry.soundSystem.play(nextTurn, false,
				SoundSystem.PRIORITY_NORMAL, 1, 1);
	}
}
