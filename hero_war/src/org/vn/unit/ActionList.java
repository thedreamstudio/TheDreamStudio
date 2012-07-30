package org.vn.unit;

import java.util.ArrayList;

import org.vn.unit.ActionServerToClient.ActionType;

public class ActionList {
	private ArrayList<ActionServerToClient> mListAction = new ArrayList<ActionServerToClient>();

	private ActionList() {

	}

	private static ActionList instance;

	public static ActionList getInstance() {
		if (instance == null) {
			instance = new ActionList();
		}
		return instance;
	}

	synchronized public void push(ActionType pActionType, Object pObj) {
		ActionServerToClient actionServerToClient = new ActionServerToClient();
		actionServerToClient.mCurrentType = pActionType;
		actionServerToClient.Obj = pObj;
		mListAction.add(actionServerToClient);
	}

	synchronized public ActionServerToClient pop() {
		if (mListAction.size() == 0) {
			return null;
		}
		return mListAction.remove(0);
	}

	synchronized public void clear() {
		mListAction.clear();
	}

	synchronized int size() {
		return mListAction.size();
	}

}
