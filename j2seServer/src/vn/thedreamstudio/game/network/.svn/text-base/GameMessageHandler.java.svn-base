/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.game.network;

import vn.thedreamstudio.common.L;
import vn.thedreamstudio.common.PlayerManager;
import core.network.IMessageHandler;
import vn.thedreamstudio.core.common.Player;
import vn.thedreamstudio.constance.CProtocol;
import java.io.IOException;
import vn.thedreamstudio.game.common.Board;
import vn.thedreamstudio.game.common.MGameController;
import core.network.Message;
import java.security.spec.MGF1ParameterSpec;
import java.util.Vector;
import vn.thedreamstudio.game.common.AttackResult;
import vn.thedreamstudio.game.army.Soldier;
import vn.thedreamstudio.game.map.Map;
import vn.thedreamstudio.game.map.MapFactory;
import vn.thedreamstudio.network.GlobalServices;
import vn.thedreamstudio.registerAPI.RegisterManager;
import vn.thedreamstudio.util.FileManager;

/**
 *
 * @author truongps
 */
public class GameMessageHandler implements IMessageHandler {

    @Override
    public void processMessage(Player player, Message msg) throws IOException {
        switch (msg.id) {
            case CProtocol.GET_BOARD_LIST: {
                GameServices.processGetBoardListMessage(player);
//                Board b = MGameController.getInstance().getBoardSuggested();
//                if (b != null) {
//                    GlobalServices.sendBoardSuggestion(player, b);
//                }
            }
            break;

//            case CProtocol.EXIT_BOARD:
//                MGameController.getInstance().exitBoard(msg.reader().readInt(), player);
//                break;
            case CProtocol.CHAT_BOARD:
                MGameController.getInstance().chatInBoard(player, msg.reader().readUTF());
                break;
            case CProtocol.LEAVE_BOARD: {
                Board b = MGameController.getInstance().getBoard(player.boardID);
                if (b != null) {
                    b.someoneExitBoard(player);
                    GameServices.processSomeOneLeaveBoardMessage(player.boardID, player);
                }
            }
//                MGameController.getInstance().exitBoard(player.boardID, player);
            break;
            case CProtocol.GET_ARMY_SHOP:
                GameServices.processGetArmyShopContentsMessage(player);
                break;
            case CProtocol.SELECT_ARMY://Chọn quan.
            {
                int length = msg.reader().readInt();
                System.out.println("user name: " + player.userName + " , length: " + length);
                int[] b = new int[length];
                for (int i = 0; i < length; i++) {
                    b[i] = msg.reader().readInt();
                }
                GameServices.processSelectArmyMessage(player, b);
            }
            break;
            case CProtocol.LAYOUT_ARMY: //
            {
                Vector mm = new Vector();
                while (msg.reader().available() > 0) {
                    Soldier s = new Soldier();
                    s.id = msg.reader().readInt();
                    s.x = msg.reader().readInt();
                    s.y = msg.reader().readInt();
                    mm.addElement(s);
                }
                Board b = MGameController.getInstance().getBoard(player.boardID);
                Message m = new Message(CProtocol.LAYOUT_ARMY);
                if (b.layoutArmy(mm, player.playerID)) {
                    m.putByte(1);
                }
                m.putByte(0);
                player.session.write(m);
                m.cleanup();
//                GameServices.processLayoutArmyMessage(player, msg.reader().readInt(), msg.reader().readInt(), msg.reader().readInt());
            }
            break;
            case CProtocol.GET_ALL_MAP: {
                Message m = new Message(CProtocol.GET_ALL_MAP);
                m.putByte(MapFactory.MAP_IDs.length);
                for (int i = 0; i < MapFactory.MAP_IDs.length; i++) {
                    m.putByte(MapFactory.MAP_IDs[i]);
                    m.putString(MapFactory.MAP_NAMEs[i]);
                }
                player.session.write(m);
                m.cleanup();
            }
            break;
            case CProtocol.GET_MAP: {
                byte mapID = msg.reader().readByte();
                Map map = MapFactory.getMapFromID(mapID);
                Message m = new Message(CProtocol.GET_MAP);
                m.putByte(mapID);
                if (map == null || player.boardID < 0) {
                    m.putInt(0);
                } else {
                    m.putInt(map.rowNum);
                    m.putInt(map.colNum);
                    for (int i = 0; i < map.rowNum; i++) {
                        for (int j = 0; j < map.colNum; j++) {
                            m.putByte(map.getMapLayout()[i][j]);
                        }
                    }
                }
                player.session.write(m);
                m.cleanup();
            }
            break;
            case CProtocol.JOIN_BOARD: {
                int boardID = msg.reader().readInt();
                Board b = MGameController.getInstance().getBoard(boardID);
                if (b == null) {//Kiem tra board nay co ton tai trong he thong hay khong.
                    GlobalServices.sendServerMessage(player, 1, L.gl(player.languageType, 1));
                    return;
                }
                if (b.someoneJoinBoard(player)) {//join board success.
                    GameServices.processJoinBoardMessage(player, true, boardID);
                    Map map = b.getMap();
                    if (map != null) {
                        GameServices.sendMessageSetMap(player, b, map);
                    }
                } else {
                    GameServices.processJoinBoardMessage(player, false, boardID);
                }
            }
            break;
            case CProtocol.SET_MAP: {
                byte mapID = msg.reader().readByte();
                Board b = MGameController.getInstance().getBoard(player.boardID);
                if (b.ownerID != player.playerID) {//Kiem tra neu khong phai la chu ban thi se k cho set map.
                    GlobalServices.sendServerMessage(player, 1, L.gl(player.languageType, 0));
                    return;
                }
                Map map = b.createMap(player.playerID, mapID);
                GameServices.sendMessageSetMap(player, b, map);
                for (int i = 0; i < b.maxPlayer; i++) {
                    Player p = ((Player) b.players.elementAt(i));
                    if (p.playerID != -1) {
                        GameServices.sendMessageSetMap(p, b, map);
                    }
                }
//                Map map = MGameController.getInstance().setMapOnBoard(player, mapID);
//                for (int i = 0; i < b.maxPlayer; i++) {
//                    Player p = ((Player) b.players.elementAt(i));
//                    Message m = new Message(CProtocol.SET_MAP);
//                    m.putByte(mapID);
//                    if (map != null) {
//                        m.putByte(1);
//                        m.putInt(p.playerID);
//                        if (p.playerID == b.ownerID) {
//                            m.putInt(map.ownerGeneral.xPos);
//                            m.putInt(map.ownerGeneral.yPos);
//                        } else {
//                            m.putInt(map.visitGeneral.xPos);
//                            m.putInt(map.visitGeneral.yPos);
//                        }
//                        m.putInt(map.initRange);
//                    } else {
//                        m.putByte(0);
//                    }
//                    p.session.write(m);
//                    m.cleanup();
//                }
            }
            break;
            case CProtocol.READY: //ready - goi ve cho tat ca moi user trong ban.
            {
                Board b = MGameController.getInstance().getBoard(player.boardID);
                b.someoneReady(player);
            }
            break;
            case CProtocol.START_GAME: {
                Board b = MGameController.getInstance().getBoard(player.boardID);
                b.startGame(player);
            }
            break;
            case CProtocol.MOVE_ARMY: {
                Soldier s = new Soldier();
                s.id = msg.reader().readInt();
                s.x = msg.reader().readInt();
                s.y = msg.reader().readInt();
                Board b = MGameController.getInstance().getBoard(player.boardID);
                if (b.moveArmy(s)) {
                    int size = b.players.size();
                    for (int i = size; --i >= 0;) {
                        if (((Player) b.players.elementAt(i)).playerID != -1 && ((Player) b.players.elementAt(i)).session != null) {
                            Message m = new Message(CProtocol.MOVE_ARMY);
                            m.putByte(1);
                            m.putInt(s.id);
                            m.putInt(s.x);
                            m.putInt(s.y);
                            ((Player) b.players.elementAt(i)).session.write(m);
                            m.cleanup();
                        }
                    }
                } else {
                    s = b.getArmyFromId(s.id);
                    int size = b.players.size();
                    for (int i = size; --i >= 0;) {
                        if (((Player) b.players.elementAt(i)).playerID != -1 && ((Player) b.players.elementAt(i)).session != null) {
                            Message m = new Message(CProtocol.MOVE_ARMY);
                            m.putByte(0);
                            m.putInt(s.id);
                            m.putInt(s.x);
                            m.putInt(s.y);
                            ((Player) b.players.elementAt(i)).session.write(m);
                            m.cleanup();
                        }
                    }
                }
            }
            break;
            case CProtocol.ATTACH: {
                Board b = MGameController.getInstance().getBoard(player.boardID);
                AttackResult a = b.attack(msg.reader().readInt(), msg.reader().readInt());
                if (a == null) {
                    int size = b.players.size();
                    for (int i = size; --i >= 0;) {
                        if (((Player) b.players.elementAt(i)).playerID != -1 && ((Player) b.players.elementAt(i)).session != null) {
                            Message m = new Message(CProtocol.ATTACH);
                            m.putByte(0);
                            ((Player) b.players.elementAt(i)).session.write(m);
                            m.cleanup();
                        }
                    }
                } else {
                    int size = b.players.size();
                    for (int i = size; --i >= 0;) {
                        if (((Player) b.players.elementAt(i)).playerID != -1 && ((Player) b.players.elementAt(i)).session != null) {
                            Message m = new Message(CProtocol.ATTACH);
                            m.putByte(1);
                            m.putInt(a.myArmyID);
                            m.putInt(a.myArmyHP);
                            m.putInt(a.rivalArmyID);
                            m.putInt(a.rivalArmyHP);
                            ((Player) b.players.elementAt(i)).session.write(m);
                            m.cleanup();
                        }
                    }
                    Player loser = b.isEndGame();
                    if (loser != null) {
                        b.endGame(b.players.elementAt(0).playerID == loser.playerID
                                ? b.players.elementAt(1) : b.players.elementAt(0), loser);
                    }
                }
            }
            break;


            case CProtocol.NEXT_TURN: {
                Board b = MGameController.getInstance().getBoard(player.boardID);
                if (b.isStartGame) {
                    b.changeTurn(player);
                }
            }
            break;
            case CProtocol.GET_WAITTING_LIST://
            {
                Vector v = MGameController.getInstance().players;
                Message m = new Message(CProtocol.GET_WAITTING_LIST);
                m.putByte(0);
                int size = v.size();
                size = size > MGameController.MAX_ITEM_IN_A_PAGE ? MGameController.MAX_ITEM_IN_A_PAGE : size;
                for (int i = size; --i >= 0;) {
                    Player p = (Player) v.elementAt(i);
                    m.putInt(p.playerID);
                    m.putString(p.playerName);
                    m.putLong(p.money);
                }
                player.session.write(m);
                m.cleanup();
            }
            break;
            case CProtocol.INVITE://
            {
                int subCommand = msg.reader().readByte();
                int boardID = -1;
                int playerID = -1;
                if (subCommand == 1) {
                    boardID = msg.reader().readByte();
                } else if (subCommand == 0) {
                    playerID = msg.reader().readInt();
                    boardID = player.boardID;
                }
                Board b = MGameController.getInstance().getBoard(boardID);
                if (b == null) {
                    if (subCommand == 1) {
                        //cai nay cua thang tra loi.
                    } else if (subCommand == 0) {
                        GlobalServices.sendServerMessage(player, 1, "Ban khong duoc phep moi.");
                    }
                }

                if (subCommand == 0 && b.ownerID == player.playerID) {//chu phong
                } else if (subCommand == 1) {
                    b.someoneJoinBoard(player);
                }
            }
            break;
            case CProtocol.KICK://
            {
                int pID = msg.reader().readInt();
                Board b = MGameController.getInstance().getBoard(player.boardID);
                if (b == null) {
                    return;
                }
                if (b.ownerID == player.playerID) {//neu la chu ban thi moi dc phep duoi.
                    Player p = b.getPlayerFromId(pID);
                    b.someoneExitBoard(p);
                    GameServices.processSomeOneLeaveBoardMessage(p.boardID, p);
                    Message m = new Message(CProtocol.KICK);
                    p.session.write(m);
                    m.cleanup();
                }
            }
            break;
            case CProtocol.BUY_SOLDIER_INGAME: {
                int armyTypeID = msg.reader().readInt();
                int x = msg.reader().readInt();
                int y = msg.reader().readInt();
                Board b = MGameController.getInstance().getBoard(player.boardID);
                if (b != null && b.isStartGame) {
                    Soldier s = b.setArmyIngame(player, armyTypeID, x, y);
                    if (s == null) {
                        GlobalServices.sendServerMessage(player, 1, L.gl(player.languageType, 6));
                    } else {
                        int size = b.players.size();
                        for (int i = size; --i >= 0;) {
                            if (((Player) b.players.elementAt(i)).playerID != -1 && ((Player) b.players.elementAt(i)).session != null) {
                                sendSetArmyInGameTo(((Player) b.players.elementAt(i)), player, s);
                            }
                        }
                    }
                } else if (b == null) {
                    GlobalServices.sendServerMessage(player, 1, L.gl(player.languageType, 7));
                }
            }
            break;
            case CProtocol.PING: {
                long time = msg.reader().readLong();
                Message m = new Message(CProtocol.PING);
                m.putLong(System.currentTimeMillis());
                player.session.write(m);
                m.cleanup();
            }
            break;
            case CProtocol.ACHIEVEMENT: {
                String pName = msg.reader().readUTF();
                Player p = PlayerManager.getInstance().getPlayerFromUsername(pName);
                if (p != null) {
                    player.session.write(getMessageAchievement(p));
                } else {
                    p = RegisterManager.getPlayer(pName);
                    if (p != null) {
                        player.session.write(getMessageAchievement(p));
                    } else {
                        GlobalServices.sendServerMessage(player, 1, "Không tồn tại user này trong hệ thống");
                    }
                }
            }
            break;
        }
    }
    
    
    private Message getMessageAchievement(Player p) {
        Message m = new Message(CProtocol.ACHIEVEMENT);
        m.putInt(p.playerID);
        m.putString(p.userName);
        m.putInt(p.winNumber);
        m.putInt(p.loseNumber);
        return m;
    }

    @Override
    public void onDisconnected(Player user) {
        MGameController.getInstance().exitBoard(user.boardID, user);
        GameServices.processSomeOneLeaveBoardMessage(user.boardID, user);
    }

    public void sendSetArmyInGameTo(Player p, Player psetarmy, Soldier s) {
        Message m = new Message(CProtocol.BUY_SOLDIER_INGAME);
        m.putInt(psetarmy.playerID);
        m.putInt((int) psetarmy.money);
        m.putInt(s.typeID);
        m.putInt(s.id);
        m.putInt(s.x);
        m.putInt(s.y);
        p.session.write(m);
        m.cleanup();
    }
}
