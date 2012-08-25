package org.vn.unit;

public class ActionServerToClient {
	public enum ActionType {
		move, attack, next_turn, end_game, buy_enemy,update_money
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
