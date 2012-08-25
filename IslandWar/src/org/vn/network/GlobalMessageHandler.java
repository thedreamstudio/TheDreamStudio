package org.vn.network;

import java.util.ArrayList;

import org.vn.cache.CurrentGameInfo;
import org.vn.cache.CurrentUserInfo;
import org.vn.constant.CommandClientToServer;
import org.vn.gl.BaseObject;
import org.vn.gl.DebugLog;
import org.vn.model.Achievement;
import org.vn.model.AttackMessage;
import org.vn.model.Board;
import org.vn.model.BuyEnemy;
import org.vn.model.Enemy;
import org.vn.model.EnemyType;
import org.vn.model.Map;
import org.vn.model.MapType;
import org.vn.model.Money;
import org.vn.model.MoveMessage;
import org.vn.model.NextTurnMessage;
import org.vn.model.PlayerModel;
import org.vn.model.Result;
import org.vn.unit.ActionList;
import org.vn.unit.ActionServerToClient.ActionType;
import org.vn.unit.UnitCharacterSwordmen;

import vn.thedreamstudio.socket.IMessageListener;
import vn.thedreamstudio.socket.Message;

public class GlobalMessageHandler implements IMessageListener {

	protected static GlobalMessageHandler instance;

	private ArrayList<MessageListener> mMessageListeners = new ArrayList<GlobalMessageHandler.MessageListener>();

	private CurrentGameInfo mCurrentGameInfo = CurrentGameInfo.getIntance();

	private ActionList mActionList = ActionList.getInstance();

	private GlobalService mGs = GlobalService.getInstance();

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
			case CommandClientToServer.LOGOUT:
				if (mGs.isConnected()) {
					mGs.close();
				}
				break;
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
			case CommandClientToServer.SYS_REGISTER: {
				byte result = msg.reader().readByte();
				if (result == 1) {
					// login thanh cong
					lightweightMsg.arg1 = 1;
					lightweightMsg.obj = msg.reader().readUTF();
				} else {
					// login loi
					lightweightMsg.arg1 = 0;
					lightweightMsg.obj = msg.reader().readUTF();
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
				mCurrentGameInfo.mListPlayerInGame.clear();
				int boardId = msg.reader().readInt();
				byte result = msg.reader().readByte();
				lightweightMsg.arg1 = result;
				lightweightMsg.arg2 = boardId;
				if (result == 1) {
					mCurrentGameInfo.reset();
					mCurrentGameInfo.boardId = boardId;
					mCurrentGameInfo.ownerId = msg.reader().readInt();
					while (msg.reader().available() != 0) {
						PlayerModel playerModel = new PlayerModel();
						playerModel.ID = msg.reader().readInt();
						playerModel.name = msg.reader().readUTF();
						if (playerModel.ID != -1) {
							mCurrentGameInfo.mListPlayerInGame.add(playerModel);
							if (mCurrentGameInfo.mHashAchievement
									.get(playerModel.name) == null) {
								mGs.ACHIEVEMENT(playerModel.name);
							}
						}
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
				mCurrentGameInfo.gold = msg.reader().readInt();
				int countEnemy = msg.reader().readByte();
				mCurrentGameInfo.listEnemytype.clear();
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
					mCurrentGameInfo.listEnemytype.add(enemy);
				}
			}
				break;
			case CommandClientToServer.SOMEONE_JOIN_BOARD: {
				mCurrentGameInfo.mListPlayerInGame.clear();
				mCurrentGameInfo.boardId = msg.reader().readInt();
				while (msg.reader().available() != 0) {
					PlayerModel playerModel = new PlayerModel();
					playerModel.ID = msg.reader().readInt();
					playerModel.name = msg.reader().readUTF();
					if (playerModel.ID != -1) {
						mCurrentGameInfo.mListPlayerInGame.add(playerModel);
						lightweightMsg.obj = playerModel.name;

						if (mCurrentGameInfo.mHashAchievement
								.get(playerModel.name) == null) {
							mGs.ACHIEVEMENT(playerModel.name);
						}
					}
				}
			}
				break;
			case CommandClientToServer.SOMEONE_LEAVE_BOARD: {
				boolean isBackToWaiting = false;
				int idPlayerLeave = msg.reader().readInt();
				int idNewOwner = msg.reader().readInt();
				// Neu chuyen? quyen` host va` da~ lo~ chon linh thi cho chon
				// lai
				if (idNewOwner == CurrentUserInfo.mPlayerInfo.ID) {
					for (PlayerModel playerModel : mCurrentGameInfo.mListPlayerInGame) {
						if (playerModel.ID == CurrentUserInfo.mPlayerInfo.ID
								&& playerModel.isReady) {
							isBackToWaiting = true;
							break;
						}
					}
				}

				for (PlayerModel playerModel : mCurrentGameInfo.mListPlayerInGame) {
					if (idPlayerLeave == playerModel.ID) {
						mCurrentGameInfo.mListPlayerInGame.remove(playerModel);
						lightweightMsg.obj = playerModel.name;
						break;
					}
				}

				mCurrentGameInfo.ownerId = idNewOwner;
				lightweightMsg.arg1 = isBackToWaiting ? 1 : 0;
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
					mCurrentGameInfo.mMapSelected = new Map();
					mCurrentGameInfo.mMapSelected.mapId = mapId;
					while (msg.reader().available() > 0) {
						int idUser = msg.reader().readInt();
						int xTileKing = msg.reader().readInt();
						int yTileKing = msg.reader().readInt();
						int rangerSetupEnemy = msg.reader().readInt();
						if (idUser == CurrentUserInfo.mPlayerInfo.ID) {
							mCurrentGameInfo.xTileKing = xTileKing;
							mCurrentGameInfo.yTileKing = yTileKing;
							mCurrentGameInfo.rangerSetupEnemy = rangerSetupEnemy;
						}
					}

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
							lightweightMsg.obj = playerModel.name;
							playerModel.isReady = isReady;
							break;
						}
					}
				}
			}
				break;
			case CommandClientToServer.START_GAME: {
				mActionList.clear();
				for (PlayerModel playerModel : mCurrentGameInfo.mListPlayerInGame) {
					playerModel.isReady = false;
				}
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
					mActionList.push(ActionType.move, moveMessage);
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
					mActionList.push(ActionType.attack, atackMessage);
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
				mActionList.push(ActionType.next_turn, nextTurnMessage);
			}
				break;
			case CommandClientToServer.SERVER_MESSAGE: {
				int type = msg.reader().readByte();
				String content = msg.reader().readUTF();
				lightweightMsg.obj = content;
			}
				break;
			case CommandClientToServer.END_GAME:
				mCurrentGameInfo.endGame();
				Result result = new Result();
				result.winnerID = msg.reader().readInt();
				result.winnerName = msg.reader().readUTF();
				result.winnerMoney = msg.reader().readLong();
				result.winnerLevel = msg.reader().readInt();
				result.loserID = msg.reader().readInt();
				result.loserName = msg.reader().readUTF();
				result.loserMoney = msg.reader().readLong();
				result.loserLevel = msg.reader().readInt();
				lightweightMsg.obj = result;
				mCurrentGameInfo.result = result;
				Achievement achievement = mCurrentGameInfo.mHashAchievement
						.get(result.winnerName);
				if (achievement != null) {
					achievement.winnumber++;
				}
				achievement = mCurrentGameInfo.mHashAchievement
						.get(result.loserName);
				if (achievement != null) {
					achievement.losenumber++;
				}
				mActionList.push(ActionType.end_game, result);
				break;
			case CommandClientToServer.SET_FOG:
				byte b = msg.reader().readByte();
				mCurrentGameInfo.isSuongMu = b == 1;
				break;
			case CommandClientToServer.BUY_SOLDIER_INGAME:
				BuyEnemy buyEnemy = new BuyEnemy();
				buyEnemy.buyerId = msg.reader().readInt();
				buyEnemy.money = msg.reader().readInt();
				buyEnemy.armytype = msg.reader().readInt();
				buyEnemy.armyid = msg.reader().readInt();
				buyEnemy.x_tile = msg.reader().readInt();
				buyEnemy.y_tile = msg.reader().readInt();
				mActionList.push(ActionType.buy_enemy, buyEnemy);
				break;
			case CommandClientToServer.MONEY:
				byte subCommand = msg.reader().readByte();
				lightweightMsg.arg1 = subCommand;
				switch (subCommand) {
				case CommandClientToServer.MONEY_UPDATE:
					ArrayList<Money> listMoney = new ArrayList<Money>();
					while (msg.reader().available() > 0) {
						Money money = new Money();
						money.playerID = msg.reader().readInt();
						money.money = msg.reader().readInt();
						listMoney.add(money);
					}
					mActionList.push(ActionType.update_money, listMoney);
					break;
				}
			case CommandClientToServer.ACHIEVEMENT:
				achievement = new Achievement();
				achievement.playerID = msg.reader().readInt();
				achievement.playerName = msg.reader().readUTF();
				achievement.winnumber = msg.reader().readInt();
				achievement.losenumber = msg.reader().readInt();
				Achievement achievement2 = mCurrentGameInfo.mHashAchievement
						.get(achievement.playerName);
				if (achievement2 == null) {
					mCurrentGameInfo.mHashAchievement.put(
							achievement.playerName, achievement);
				} else {
					achievement2.winnumber = achievement.winnumber;
					achievement2.losenumber = achievement.losenumber;
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