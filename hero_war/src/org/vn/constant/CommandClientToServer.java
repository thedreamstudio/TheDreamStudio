package org.vn.constant;

public class CommandClientToServer {
	public static final byte LOST_CONNECTION = -1;
	public static final byte SYS_LOGIN = 1;
	public static final byte SYS_REGISTER = 2;
	public static final byte SYS_BOARD_LIST = 3;
	public static final byte JOIN_BOARD = 4;
	public static final byte GET_CLIENT_INFO = 5;
	public static final byte SOMEONE_JOIN_BOARD = 6;
	public static final byte EXIT_BOARD = 7;
	public static final byte SET_GAME_TYPE = 8;
	public static final byte CHAT_BOARD = 9;
	public static final byte GET_USER_INFO = 10;
	public static final byte GET_ARMY_SHOP = 11;
	public static final byte ARMY_SELECTION = 12;
	public static final byte SOMEONE_LEAVE_BOARD = 13;
	public static final byte LEAVE_BOARD = 14;
	public static final byte GET_ALL_MAP = 21;
	public static final byte GET_MAP = 16;
	public static final byte SET_MAP = 22;
	public static final byte LAYOUT_ARMY = 17;
	public static final byte READY = 19;
	public static final byte START_GAME = 20;
	public static final byte MOVE_ARMY = 18;
	public static final byte ATTACH = 23;
	public static final byte NEXT_TURN = 24;
	public static final byte LOGIN_TRIAL = 26;
}
