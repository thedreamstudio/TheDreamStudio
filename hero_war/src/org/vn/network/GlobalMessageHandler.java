package org.vn.network;

import java.util.ArrayList;

import org.vn.cache.CurrentGameInfo;
import org.vn.cache.CurrentUserInfo;
import org.vn.constant.CommandClientToServer;
import org.vn.gl.BaseObject;
import org.vn.gl.DebugLog;
import org.vn.model.AttackMessage;
import org.vn.model.Board;
import org.vn.model.Enemy;
import org.vn.model.EnemyType;
import org.vn.model.Map;
import org.vn.model.MapType;
import org.vn.model.MoveMessage;
import org.vn.model.NextTurnMessage;
import org.vn.model.PlayerModel;
import org.vn.unit.UnitCharacterSwordmen;

import vn.thedreamstudio.socket.IMessageListener;
import vn.thedreamstudio.socket.Message;

public class GlobalMessageHandler implements IMessageListener {

	protected static GlobalMessageHandler instance;

	private ArrayList<MessageListener> mMessageListeners = new ArrayList<GlobalMessageHandler.MessageListener>();

	private CurrentGameInfo mCurrentGameInfo = CurrentGameInfo.getIntance();

	private GlobalMessageHandler() {
	}

	public static GlobalMessageHandler getInstance() {
		if (instance == null) {
			instance = new GlobalMessageHandler();
		}
		return instance;
	}

	synchronized public void addMessageListener(MessageListener msgListener) {
		if (!mMessageListeners.contains(msgListener)) {
			mMessageListeners.add(0, msgListener);
		}
	}

	synchronized public void removeMessageListener(MessageListener msgListener) {
		if (mMessageListeners.contains(msgListener)) {
			mMessageListeners.remove(msgListener);
		}
	}

	@Override
	public void onMessage(Message msg) {
		LightWeightMessage lightweightMsg = new LightWeightMessage();
		lightweightMsg.command = msg.command;
		try {
			switch (msg.command) {
			case CommandClientToServer.SYS_LOGIN: {
				byte result = msg.reader().readByte();
				if (result == 1) {
					// login thanh cong
					lightweightMsg.arg1 = 1;
				} else {
					// login loi
					lightweightMsg.arg1 = 0;
				}
			}
				break;
			case CommandClientToServer.SYS_BOARD_LIST: {
				int countBoard = msg.reader().readInt();
				Board[] boards = new Board[countBoard];
				for (int i = 0; i < boards.length; i++) {
					boards[i] = new Board();
					boards[i].id = msg.reader().readInt();
					boards[i].maxPlayer = msg.reader().readByte();
					boards[i].nPlayer = msg.reader().readByte();
				}
				lightweightMsg.obj = boards;
			}
				break;
			case CommandClientToServer.GET_USER_INFO: {
				PlayerModel playerModel = CurrentUserInfo.mPlayerInfo;
				playerModel.ID = msg.reader().readInt();
				playerModel.name = msg.reader().readUTF();
				playerModel.money = msg.reader().readLong();
				playerModel.lv = msg.reader().readInt();
			}
				break;
			case CommandClientToServer.JOIN_BOARD: {
				int boardId = msg.reader().readInt();
				byte result = msg.reader().readByte();
				lightweightMsg.arg1 = result;
				lightweightMsg.arg2 = boardId;
				if (result == 1) {
					CurrentGameInfo currentGameInfo = CurrentGameInfo
							.getIntance();
					currentGameInfo.reset();
					currentGameInfo.boardId = boardId;
					currentGameInfo.ownerId = msg.reader().readInt();
					while (msg.reader().available() != 0) {
						PlayerModel playerModel = new PlayerModel();
						playerModel.ID = msg.reader().readInt();
						playerModel.name = msg.reader().readUTF();
						currentGameInfo.mListPlayerInGame.add(playerModel);
					}
				}
			}
				break;
			case CommandClientToServer.CHAT_BOARD: {
				lightweightMsg.arg1 = msg.reader().readInt();
				String content = msg.reader().readUTF();
				content += ":" + msg.reader().readUTF();
				;
				lightweightMsg.obj = content;
			}
				break;
			case CommandClientToServer.GET_ARMY_SHOP: {
				CurrentGameInfo currentGameInfo = CurrentGameInfo.getIntance();
				currentGameInfo.gold = msg.reader().readInt();
				int countEnemy = msg.reader().readByte();
				currentGameInfo.listEnemytype.clear();
				for (int i = 0; i < countEnemy; i++) {
					EnemyType enemy = new EnemyType();
					enemy.armyType = msg.reader().readInt();
					enemy.armyName = msg.reader().readUTF();
					enemy.cost = msg.reader().readInt();
					enemy.hp = msg.reader().readInt();
					enemy.damageMax = msg.reader().readInt();
					enemy.damageMin = msg.reader().readInt();
					enemy.mana = msg.reader().readInt();
					enemy.movecost = msg.reader().readInt();
					enemy.attackcost = msg.reader().readInt();
					enemy.rangeattack = msg.reader().readInt();
					enemy.rangeview = msg.reader().readInt();
					currentGameInfo.listEnemytype.add(enemy);
				}
			}
				break;
			case CommandClientToServer.SOMEONE_JOIN_BOARD: {
				CurrentGameInfo currentGameInfo = CurrentGameInfo.getIntance();
				currentGameInfo.reset();
				currentGameInfo.boardId = msg.reader().readInt();
				while (msg.reader().available() != 0) {
					PlayerModel playerModel = new PlayerModel();
					playerModel.ID = msg.reader().readInt();
					playerModel.name = msg.reader().readUTF();
					currentGameInfo.mListPlayerInGame.add(playerModel);
				}
			}
				break;
			case CommandClientToServer.SOMEONE_LEAVE_BOARD: {
				int idPlayerLeave = msg.reader().readInt();
				CurrentGameInfo currentGameInfo = CurrentGameInfo.getIntance();
				for (PlayerModel playerModel : currentGameInfo.mListPlayerInGame) {
					if (idPlayerLeave == playerModel.ID) {
						currentGameInfo.mListPlayerInGame.remove(playerModel);
						break;
					}
				}
				int idNewOwner = msg.reader().readInt();
				currentGameInfo.ownerId = idNewOwner;
			}
				break;
			case CommandClientToServer.ARMY_SELECTION: {
				byte result = msg.reader().readByte();
				lightweightMsg.arg1 = result;
				if (result == 1) {
					while (msg.reader().available() != 0) {
						int armyId = msg.reader().readInt();
						int armyType = msg.reader().readInt();
						for (UnitCharacterSwordmen unitCharacter : BaseObject.sSystemRegistry.characterManager.arrayCharactersMyTeam) {
							if (armyType == unitCharacter.mEnemyType.armyType
									&& unitCharacter.idEnemy == -1) {
								unitCharacter.idEnemy = armyId;
								break;
							}
						}
					}
				}
			}
				break;
			case CommandClientToServer.GET_ALL_MAP: {
				CurrentGameInfo gameInfo = CurrentGameInfo.getIntance();
				gameInfo.listMaps.clear();
				byte count = msg.reader().readByte();
				for (byte i = 0; i < count; i++) {
					if (msg.reader().available() != 0) {
						Map map = new Map();
						map.mapId = msg.reader().readByte();
						map.mapName = msg.reader().readUTF();
						gameInfo.listMaps.add(map);
					}
				}
			}
				break;
			case CommandClientToServer.GET_MAP: {
				CurrentGameInfo gameInfo = CurrentGameInfo.getIntance();
				byte mapId = msg.reader().readByte();
				if (mapId == mCurrentGameInfo.mMapSelected.mapId) {
					MapType mapType = new MapType();
					mapType.row = msg.reader().readInt();
					if (mapType.row > 0) {
						mapType.column = msg.reader().readInt();
						mapType.mapType = new byte[mapType.column][mapType.row];
						int count = 0;
						for (int x = 0; x < mapType.row; x++) {
							for (int y = 0; y < mapType.column; y++) {
								mapType.mapType[y][x] = msg.reader().readByte();
								count++;
							}
						}
					}
					mCurrentGameInfo.mMapSelected.mapType = mapType;
					break;
				}
				lightweightMsg.arg1 = mapId;
			}
				break;
			case CommandClientToServer.SET_MAP: {
				byte mapId = msg.reader().readByte();
				byte result = msg.reader().readByte();
				lightweightMsg.arg1 = result;
				if (result == 1) {
					// - row: int (vi tri tuong)
					// - col: int (vi tri tuong)
					// - range: int
					mCurrentGameInfo.xTileKing = msg.reader().readInt();
					mCurrentGameInfo.yTileKing = msg.reader().readInt();
					mCurrentGameInfo.rangerSetupEnemy = msg.reader().readInt();
					mCurrentGameInfo.mMapSelected = new Map();
					mCurrentGameInfo.mMapSelected.mapId = mapId;
				}
			}
				break;
			case CommandClientToServer.LAYOUT_ARMY: {
				lightweightMsg.arg1 = msg.reader().readByte();
			}
				break;
			case CommandClientToServer.READY: {
				int boardId = msg.reader().readInt();
				if (mCurrentGameInfo.boardId == boardId) {
					int userId = msg.reader().readInt();
					lightweightMsg.arg1 = userId;
					boolean isReady = msg.reader().readByte() == 1 ? true
							: false;
					for (PlayerModel playerModel : mCurrentGameInfo.mListPlayerInGame) {
						if (playerModel.ID == userId) {
							playerModel.isReady = isReady;
							break;
						}
					}
				}
			}
				break;
			case CommandClientToServer.START_GAME: {
				ArrayList<Enemy> listEnemies = new ArrayList<Enemy>();
				while (msg.reader().available() > 0) {
					Enemy enemy = new Enemy();
					enemy.armyId = msg.reader().readInt();
					enemy.armyType = msg.reader().readInt();
					enemy.playerId = msg.reader().readInt();
					enemy.xTile = msg.reader().readInt();
					enemy.yTile = msg.reader().readInt();
					listEnemies.add(enemy);
				}
				lightweightMsg.obj = listEnemies;
			}
				break;
			case CommandClientToServer.MOVE_ARMY: {
				lightweightMsg.arg1 = msg.reader().readByte();
				if (lightweightMsg.arg1 == 1)// suscess
				{
					MoveMessage moveMessage = new MoveMessage();
					moveMessage.idMove = msg.reader().readInt();
					moveMessage.xTypeMoveNext = msg.reader().readInt();
					moveMessage.yTypeMoveNext = msg.reader().readInt();
					lightweightMsg.obj = moveMessage;
				}
			}
				break;
			case CommandClientToServer.ATTACH: {
				lightweightMsg.arg1 = msg.reader().readByte();
				if (lightweightMsg.arg1 == 1)// suscess
				{
					AttackMessage atackMessage = new AttackMessage();
					atackMessage.idAttacker = msg.reader().readInt();
					atackMessage.hpIdAttack = msg.reader().readInt();
					atackMessage.idBeAttacker = msg.reader().readInt();
					atackMessage.hpIdBeAttack = msg.reader().readInt();
					lightweightMsg.obj = atackMessage;
				}
			}
				break;
			case CommandClientToServer.NEXT_TURN: {
				NextTurnMessage nextTurnMessage = new NextTurnMessage();
				nextTurnMessage.idPlayerInTurnNext = msg.reader().readInt();
				nextTurnMessage.totaltime = msg.reader().readLong();
				nextTurnMessage.turntime = msg.reader().readLong();
				nextTurnMessage.rivalTotaltime = msg.reader().readLong();
				nextTurnMessage.rivalTurnTime = msg.reader().readLong();
				lightweightMsg.obj = nextTurnMessage;
			}
				break;
			}
			try {
				for (MessageListener listener : mMessageListeners) {
					listener.onMessageReceived(lightweightMsg);
				}
			} catch (Exception e) {
				DebugLog.e("MobileClient", "Can't process ms: " + msg.command
						+ "--/--" + e.toString());
			}
		} catch (Exception e) {
			DebugLog.e("MobileClient", "Can't read ms: " + msg.command
					+ "--/--" + e.toString());
		}
	}

	@Override
	public void onConnectionFail() {
	}

	@Override
	public void onDisconnected() {
		LightWeightMessage lightweightMsg = new LightWeightMessage();
		lightweightMsg.command = CommandClientToServer.LOST_CONNECTION;
		try {
			for (MessageListener listener : mMessageListeners) {
				listener.onMessageReceived(lightweightMsg);
			}
		} catch (Exception e) {
			DebugLog.e("MobileClient", "Can't process ms: "
					+ lightweightMsg.command + "--/--" + e.toString());
		}
	}

	@Override
	public void onConnectOK() {
	}

	public static class LightWeightMessage {
		public byte command;
		public int arg1;
		public int arg2;
		public Object obj;
	}

	public interface MessageListener {
		public void onMessageReceived(LightWeightMessage msg);
	}

}