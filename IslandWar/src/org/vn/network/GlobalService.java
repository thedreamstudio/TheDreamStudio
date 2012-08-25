package org.vn.network;

import java.io.IOException;
import java.util.ArrayList;

import org.vn.cache.CurrentGameInfo;
import org.vn.cache.CurrentUserInfo;
import org.vn.constant.CommandClientToServer;
import org.vn.model.Server;
import org.vn.unit.UnitCharacterSwordmen;

import vn.thedreamstudio.socket.IMobileClient;
import vn.thedreamstudio.socket.Message;
import vn.thedreamstudio.socket.MobileClient;

public class GlobalService {

	protected static GlobalService instance;
	static IMobileClient mSession = MobileClient.getInstance();
	private CurrentGameInfo mCurrentGameInfo = CurrentGameInfo.getIntance();

	private GlobalService() {
	}

	public static GlobalService getInstance() {
		if (instance == null) {
			instance = new GlobalService();
		}
		return instance;
	}

	public boolean isConnected() {
		return mSession.isConnected();
	}

	public void close() {
		mSession.close();
	}

	public boolean connect(Server server) {
		mSession.setHandler(GlobalMessageHandler.getInstance());
		if (MobileClient.CONNECT_STATUS_CONNECTED == mSession.connect(
				server.ip, server.port)) {
			// sendClientInformation();
			return true;
		}
		return false;
	}

	public void SYS_LOGIN(String username, String pass) {
		Message m = new Message(CommandClientToServer.SYS_LOGIN);
		try {
			CurrentUserInfo.mUsername = username;
			CurrentUserInfo.mUsername = pass;
			m.writer().writeUTF(username);
			m.writer().writeUTF(pass);
			mSession.sendMessage(m);
			m.cleanup();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Đăng ký
	 * 
	 * @param username
	 * @param password
	 */
	public void SYS_REGISTER(String username, String password, String phone_num) {
		Message m = new Message(CommandClientToServer.SYS_REGISTER);
		try {
			m.writer().writeUTF(username);
			m.writer().writeUTF(password);
			m.writer().writeUTF(phone_num);
		} catch (IOException e) {
		}
		mSession.sendMessage(m);
		m.cleanup();
	}

	/**
	 * Lấy danh sách các bàn chơi trong một phòng
	 */
	public void SYS_BOARD_LIST() {
		Message m = new Message(CommandClientToServer.SYS_BOARD_LIST);
		mSession.sendMessage(m);
		m.cleanup();
	}

	/**
	 * Tham gia vào một bàn chơi game
	 * 
	 * @param roomID
	 * @param boardID
	 */
	public void JOIN_BOARD(int boardID) {
		Message m = new Message(CommandClientToServer.JOIN_BOARD);
		try {
			m.writer().writeInt(boardID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSession.sendMessage(m);
		m.cleanup();
	}

	/**
	 * 
	 * @param platform
	 * @param model
	 * @param version
	 * @param language
	 *            0:vn ; 1:eng
	 */
	public void GET_CLIENT_INFO(String platform, String model, String version,
			byte language) {
		Message m = new Message(CommandClientToServer.GET_CLIENT_INFO);
		try {
			m.writer().writeUTF(platform);
			m.writer().writeUTF(model);
			m.writer().writeUTF(version);
			m.writer().writeByte(language);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void EXIT_BOARD(int boardID) {
		Message m = new Message(CommandClientToServer.EXIT_BOARD);
		try {
			m.writer().writeInt(boardID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void SET_GAME_TYPE(byte idGame) {
		Message m = new Message(CommandClientToServer.SET_GAME_TYPE);
		try {
			m.writer().writeByte(idGame);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void CHAT_BOARD(String content) {
		Message m = new Message(CommandClientToServer.CHAT_BOARD);
		try {
			m.writer().writeUTF(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void LEAVE_BOARD() {
		Message m = new Message(CommandClientToServer.LEAVE_BOARD);
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void GET_ARMY_SHOP() {
		Message m = new Message(CommandClientToServer.GET_ARMY_SHOP);
		mSession.sendMessage(m);
		m.cleanup();
	}

	// public void ARMY_SELECTION(ArrayList<Integer> enemyIDs) {
	// Message m = new Message(CommandClientToServer.ARMY_SELECTION);
	// try {
	// m.writer().writeInt(enemyIDs.size());
	// for (Integer i : enemyIDs) {
	// m.writer().writeInt(i);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// mSession.sendMessage(m);
	// m.cleanup();
	// }

	public void ARMY_SELECTION(
			ArrayList<UnitCharacterSwordmen> arrayCharactersMyTeam) {
		Message m = new Message(CommandClientToServer.ARMY_SELECTION);
		try {
			m.writer().writeInt(arrayCharactersMyTeam.size());
			for (UnitCharacterSwordmen unitCharacter : arrayCharactersMyTeam) {
				m.writer().writeInt(unitCharacter.mEnemyType.armyType);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void GET_ALL_MAP() {
		Message m = new Message(CommandClientToServer.GET_ALL_MAP);
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void GET_MAP(byte mapId) {
		Message m = new Message(CommandClientToServer.GET_MAP);
		try {
			m.writer().writeByte(mapId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void SET_MAP(byte mapId) {
		Message m = new Message(CommandClientToServer.SET_MAP);
		try {
			m.writer().writeByte(mapId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void LAYOUT_ARMY(
			ArrayList<UnitCharacterSwordmen> arrayCharactersMyTeam) {
		Message m = new Message(CommandClientToServer.LAYOUT_ARMY);
		try {
			for (UnitCharacterSwordmen unitCharacterSwordmen : arrayCharactersMyTeam) {
				m.writer().writeInt(unitCharacterSwordmen.idEnemy);
				m.writer().writeInt(unitCharacterSwordmen.mTileTaget.xTile);
				m.writer().writeInt(unitCharacterSwordmen.mTileTaget.yTile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSession.sendMessage(m);
		m.cleanup();

	}

	public void READY() {
		Message m = new Message(CommandClientToServer.READY);
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void START_GAME() {
		Message m = new Message(CommandClientToServer.START_GAME);
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void MOVE_ARMY(int idEnemyMove, int xTileMoveNext, int yTileMoveNext) {
		Message m = new Message(CommandClientToServer.MOVE_ARMY);
		try {
			m.writer().writeInt(idEnemyMove);
			m.writer().writeInt(xTileMoveNext);
			m.writer().writeInt(yTileMoveNext);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void ATTACH(int idAtack, int idBeAtack) {
		Message m = new Message(CommandClientToServer.ATTACH);
		try {
			m.writer().writeInt(idAtack);
			m.writer().writeInt(idBeAtack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void NEXT_TURN() {
		Message m = new Message(CommandClientToServer.NEXT_TURN);
		mSession.sendMessage(m);
		m.cleanup();
	}

	public void LOGIN_TRIAL(String username) {
		Message m = new Message(CommandClientToServer.LOGIN_TRIAL);
		try {
			m.writer().writeUTF(username);
			mSession.sendMessage(m);
			m.cleanup();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void BUY_SOLDIER_INGAME(int armyType, int x_tile, int y_tile) {
		Message m = new Message(CommandClientToServer.BUY_SOLDIER_INGAME);
		try {
			m.writer().writeInt(armyType);
			m.writer().writeInt(x_tile);
			m.writer().writeInt(y_tile);
			mSession.sendMessage(m);
			m.cleanup();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ACHIEVEMENT(String playerName) {
		Message m = new Message(CommandClientToServer.ACHIEVEMENT);
		try {
			m.writer().writeUTF(playerName);
			mSession.sendMessage(m);
			m.cleanup();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void LOGOUT() {
		Message m = new Message(CommandClientToServer.LOGOUT);
		mSession.sendMessage(m);
		m.cleanup();
	}
}