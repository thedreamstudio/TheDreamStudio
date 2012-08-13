package org.vn.unit;

public class ActionServerToClient {
	public enum ActionType {
		start_game, move, attack, next_turn, end_game
	}

	public enum ActionStatus {
		chuaXyLy, DangXuLy, XuLyXong
	}

	public ActionType mCurrentType = null;
	public ActionStatus mStatus = ActionStatus.chuaXyLy;
	public Object Obj = null;

	public void done() {
		mStatus = ActionStatus.XuLyXong;
	}
}
