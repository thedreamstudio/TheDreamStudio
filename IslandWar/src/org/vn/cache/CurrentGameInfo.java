package org.vn.cache;

import java.util.ArrayList;
import java.util.HashMap;

import org.vn.model.Achievement;
import org.vn.model.EnemyType;
import org.vn.model.Map;
import org.vn.model.PlayerModel;
import org.vn.model.Result;

/** Save all information of game */
public class CurrentGameInfo {
	private static CurrentGameInfo intance = new CurrentGameInfo();

	private CurrentGameInfo() {
	}

	public static CurrentGameInfo getIntance() {
		return intance;
	}

	public int boardId;
	/** ID của chủ bàn */
	public int ownerId;
	public int gold;
	// --Cast--
	/** Danh sach cac enemy dc dung trong game */
	public ArrayList<EnemyType> listEnemytype = new ArrayList<EnemyType>();
	/** Danh Sach map dung cho chu phong hien thi */
	public ArrayList<Map> listMaps = new ArrayList<Map>();
	// --------
	/**
	 * Update trong JoinBoard & SOMEONE_JOIN_BOARD & leaveBoard
	 */
	public ArrayList<PlayerModel> mListPlayerInGame = new ArrayList<PlayerModel>();
	public HashMap<String, Achievement> mHashAchievement = new HashMap<String, Achievement>();
	// /**
	// * Update trong startGame
	// */
	// public ArrayList<Enemy> listIdEnemyInMap = new ArrayList<Enemy>();

	public Result result;

	public Map mMapSelected = null;
	public int xTileKing;
	public int yTileKing;
	public int rangerSetupEnemy;
	public boolean isSuongMu = false;

	public void reset() {
		mHashAchievement.clear();
		mListPlayerInGame.clear();
		mMapSelected = null;
		isSuongMu = false;
		if (CurrentUserInfo.mAchievement != null) {
			mHashAchievement.put(CurrentUserInfo.mAchievement.playerName,
					CurrentUserInfo.mAchievement);
		}
	}

	public void endGame() {
		mMapSelected = null;
	}

	public boolean isReadyAll() {
		if (mListPlayerInGame.size() < 2) {
			return false;
		}
		for (PlayerModel playerModel : mListPlayerInGame) {
			if (!playerModel.isReady
					&& playerModel.ID != CurrentUserInfo.mPlayerInfo.ID) {
				return false;
			}
		}
		return true;
	}

	public boolean isIReady() {
		for (PlayerModel playerModel : mListPlayerInGame) {
			if (playerModel.ID != CurrentUserInfo.mPlayerInfo.ID) {
				return playerModel.isReady;
			}
		}
		return false;
	}

	public boolean isHost() {
		return ownerId == CurrentUserInfo.mPlayerInfo.ID;
	}
}
