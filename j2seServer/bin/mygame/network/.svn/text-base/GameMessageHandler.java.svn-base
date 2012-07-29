/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.network;

import common.L;
import core.network.IMessageHandler;
import core.common.Player;
import constance.CProtocol;
import java.io.IOException;
import mygame.common.Board;
import mygame.common.MGameController;
import core.network.Message;
import java.util.Vector;
import mygame.common.AttackResult;
import mygame.game.army.Soldier;
import mygame.game.map.Map;
import mygame.game.map.MapFactory;
import network.GlobalServices;

/**
 *
 * @author truongps
 */
public class GameMessageHandler implements IMessageHandler {

    @Override
    public void processMessage(Player player, Message msg) throws IOException {
        switch (msg.id) {
            case CProtocol.GET_BOARD_LIST:
                GameServices.processGetBoardListMessage(player);
                break;

//            case CProtocol.EXIT_BOARD:
//                MGameController.getInstance().exitBoard(msg.reader().readInt(), player);
//                break;
            case CProtocol.CHAT_BOARD:
                MGameController.getInstance().chatInBoard(player, msg.reader().readUTF());
                break;
            case CProtocol.LEAVE_BOARD:
                MGameController.getInstance().exitBoard(player.boardID, player);
                GameServices.processSomeOneLeaveBoardMessage(player.boardID, player);
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
                    GlobalServices.sendServerMessage(player, 1, L.gl(1));
                    return;
                }
                if (b.someoneJoinBoard(player)) {//join board success.
                    GameServices.processJoinBoardMessage(player, true, boardID);
                    Map map = b.getMap();
                    if (map != null) {
                        Message m = new Message(CProtocol.SET_MAP);
                        m.putByte(map.mapID);
                        m.putByte(1);
                        if (player.playerID == b.ownerID) {
                            m.putInt(map.ownerGeneral.xPos);
                            m.putInt(map.ownerGeneral.yPos);
                        } else {
                            m.putInt(map.visitGeneral.xPos);
                            m.putInt(map.visitGeneral.yPos);
                        }
                        m.putInt(map.initRange);
                        if (player.session != null) {
                            player.session.write(m);
                        }
                        m.cleanup();
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
                    GlobalServices.sendServerMessage(player, 1, L.gl(0));
                    return;
                }
                Map map = b.createMap(player.playerID, mapID);
//                Map map = MGameController.getInstance().setMapOnBoard(player, mapID);
                for (int i = 0; i < b.maxPlayer; i++) {
                    Player p = ((Player) b.players.elementAt(i));
                    if (p.playerID == -1) {
                        return;
                    }
                    Message m = new Message(CProtocol.SET_MAP);
                    m.putByte(mapID);
                    if (map != null) {
                        m.putByte(1);
                        if (p.playerID == b.ownerID) {
                            m.putInt(map.ownerGeneral.xPos);
                            m.putInt(map.ownerGeneral.yPos);
                        } else {
                            m.putInt(map.visitGeneral.xPos);
                            m.putInt(map.visitGeneral.yPos);
                        }
                        m.putInt(map.initRange);
                    } else {
                        m.putByte(0);
                    }
                    p.session.write(m);
                    m.cleanup();
                }
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
                }
            }
            break;
            case CProtocol.NEXT_TURN: {
                Board b = MGameController.getInstance().getBoard(player.boardID);
                if (b.isStartGame) {
                    b.changeTurn();
                    int size = b.players.size();
                    for (int i = size; --i >= 0;) {
                        if (((Player) b.players.elementAt(i)).playerID != -1 && ((Player) b.players.elementAt(i)).session != null) {
                            ((Player) b.players.elementAt(i)).session.write(b.getMessageNextTurn());
                        }
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onDisconnected(Player user) {
        MGameController.getInstance().exitBoard(user.boardID, user);
    }
}
