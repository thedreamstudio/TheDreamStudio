package org.vn.model;

import org.vn.cache.CurrentGameInfo;

public class Enemy {
	public int armyId;
	public int armyType;
	public int playerId;
	public int xTile = -1;
	public int yTile = -1;
	public EnemyType mEnemyType = null;

	public EnemyType getEnemyType() {
		if (mEnemyType != null) {
			return mEnemyType;
		}
		for (EnemyType enemyType : CurrentGameInfo.getIntance().listEnemytype) {
			if (armyType == enemyType.armyType) {
				mEnemyType = enemyType;
				return mEnemyType;
			}
		}
		return null;
	}
}
