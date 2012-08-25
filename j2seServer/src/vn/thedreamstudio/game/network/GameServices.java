/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.game.network;

import vn.thedreamstudio.core.common.Player;
import vn.thedreamstudio.constance.CProtocol;
import vn.thedreamstudio.game.common.Board;
import vn.thedreamstudio.game.common.MGameController;
import core.network.Message;
import java.util.Vector;
import vn.thedreamstudio.game.common.MatchResult;
import vn.thedreamstudio.game.army.Soldier;
import vn.thedreamstudio.game.map.Map;
import vn.thedreamstudio.network.GlobalServices;

/**
 *
 * @author truongps
 */
public class GameServices {

    public static void processGetBoardListMessage(Player p) {
        Board[] bs = MGameController.getInstance().getBoardList();
        Message m = new Message(CProtocol.GET_BOARD_LIST);
        m.putInt(bs.length);
        for (int i = 0; i < bs.length; i++) {
            m.putInt(bs[i].boardID);
            m.putByte(bs[i].maxPlayer);
            m.putByte(bs[i].countPlayer());
        }
        p.session.write(m);
        m.cleanup();
    }

    public static void processJoinBoardMessage(Player p, boolean isOK, int boardID) {
        Message m = new Message(CProtocol.JOIN_BOARD);
        m.putInt(boardID);
        m.putByte((isOK ? 1 : 0));

        if (isOK) {
            Board b = MGameController.getInstance().getBoard(boardID);
            m.putInt(b.ownerID);
            int size = b.players.size();
            for (int i = 0; i < size; i++) {
                Player tp = ((Player) b.players.elementAt(i));
                m.putInt(tp.playerID);
                m.putString(tp.userName);
            }
        }
        p.session.write(m);
        m.cleanup();
        if (isOK) {
            sendMyInfoToBoard(boardID, p);
        }
    }

    private static void sendMyInfoToBoard(int boardID, Player myinfo) {
        Board b = MGameController.getInstance().getBoard(boardID);
        int size = b.players.size();
        for (int i = 0; i < size; i++) {
            Player p = (Player) b.players.elementAt(i);
            if (p.session != null && p.playerID != myinfo.playerID) {
                Message m = new Message(CProtocol.SOMEONE_JOIN_BOARD);
                m.putInt(boardID);
                for (int j = 0; j < size; j++) {
                    Player pp = (Player) b.players.elementAt(j);
                    if (pp.session != null) {
                        m.putInt(pp.playerID);
                        m.putString(pp.userName);
                    }
                }
                p.session.write(m);
                m.cleanup();
            }
        }
    }

    public static void processSomeOneLeaveBoardMessage(final int boardID, Player p) {
        Board b = MGameController.getInstance().getBoard(boardID);
        int size = b.players.size();
        for (int i = 0; i < size; i++) {
            Player pp = (Player) b.players.elementAt(i);
            if (pp.session != null) {
                Message m = new Message(CProtocol.SOMEONE_LEAVE_BOARD);
                m.putInt(p.playerID);
                m.putInt(b.ownerID);
                pp.session.write(m);
                m.cleanup();
            }
        }
        p.boardID = -1;
    }

    public static void processGetArmyShopContentsMessage(Player p) {
        Soldier[] temp = MGameController.getInstance().getArmyShopContents();
        Message m = new Message(CProtocol.GET_ARMY_SHOP);
        m.putInt(MGameController.TOTAL_MONEY);//cho nay de tam, sau nay sua lai
        m.putByte(temp.length);
        for (int i = 0; i < temp.length; i++) {
            m.putInt(temp[i].getID());
            m.putString(temp[i].getName());
            m.putInt(temp[i].getCost());
            m.putInt(temp[i].getHP());
            m.putInt(temp[i].getDamageMax());
            m.putInt(temp[i].getDamageMin());
            m.putInt(temp[i].getMana());
            m.putInt(temp[i].getCostMove());
            m.putInt(temp[i].getCostAttack());
            m.putInt(temp[i].getRangeAttack());
            m.putInt(temp[i].getRangeView());
        }
        p.session.write(m);
        m.cleanup();
    }

    public synchronized static void processSelectArmyMessage(Player p, int[] ids) {
        Message m = new Message(CProtocol.SELECT_ARMY);
        if (MGameController.getInstance().checkUserSelectArmy(ids)) {
            m.putByte(1);
            Board b = MGameController.getInstance().getBoard(p.boardID);
            Vector v = b.setArmyOnMap(ids, p.playerID);
            int size = v.size();
            System.out.println("So linh da set vao map: " + size);
            for (int i = 0; i < size; i++) {
                Soldier s = (Soldier) v.elementAt(i);
                m.putInt(s.id);
                m.putInt(s.typeID);
            }
        } else {
            m.putByte(0);
        }
        p.session.write(m);
        m.cleanup();
    }

    public static void processGetMapMessage(Player p) {
        Message m = new Message(CProtocol.GET_MAP);
        int row = 50;
        int col = 50;
        m.putInt(row);
        m.putInt(col);
        int c = row * col;
        for (int i = 0; i < c; i++) {
            m.putInt(0);
        }
        p.session.write(m);
        m.cleanup();
    }

    public static void processLayoutArmyMessage(Player p, int armyID, int xPos, int yPos) {
        byte status = 1;//cai nay ve sau se tinh lai.
        Message m = new Message(CProtocol.LAYOUT_ARMY);
        m.putInt(armyID);
        m.putInt(xPos);
        m.putInt(yPos);
        m.putByte(status);
        p.session.write(m);
        m.cleanup();
    }

    public static void processSomeOneReadyMessage(Player p, Player pReady) {
        if (p == null || p.playerID == -1) {
            return;
        }
        Message m = new Message(CProtocol.READY);
        m.putInt(pReady.boardID);
        m.putInt(pReady.playerID);
        m.putByte(pReady.isReady ? 1 : 0);
        p.session.write(m);
    }

    public static void processStartGame(Player p, Vector soldier) {
        Message m = new Message(CProtocol.START_GAME);
        int size = soldier.size();
        for (int i = size; --i >= 0;) {
            Soldier s = (Soldier) soldier.elementAt(i);
            m.putInt(s.id);
            m.putInt(s.typeID);
            m.putInt(s.owner.playerID);
            m.putInt(s.x);
            m.putInt(s.y);
        }
        p.session.write(m);
    }

    public static void processMoveArmyMessage(Player p, Soldier s) {
        Message m = new Message(CProtocol.MOVE_ARMY);
        m.putInt(s.id);
        m.putInt(s.x);
        m.putInt(s.y);
        p.session.write(m);
        m.cleanup();
    }

    public static void sendMessageSetMap(Player p, Board b, Map map) {
        if (map == null) {
            GlobalServices.sendServerMessage(p, 1, "Trên server chua co map.");
            return;
        }
        Message m = new Message(CProtocol.SET_MAP);
        m.putByte(map.mapID);
        m.putByte(1);
        int size = b.players.size();
        for (int i = size; --i >= 0;) {
            m.putInt(b.players.elementAt(i).playerID);
            if (b.players.elementAt(i).playerID == b.ownerID) {
                m.putInt(map.ownerGeneral.xPos);
                m.putInt(map.ownerGeneral.yPos);
            } else {
                m.putInt(map.visitGeneral.xPos);
                m.putInt(map.visitGeneral.yPos);
            }
            m.putInt(map.initRange);
        }
        p.session.write(m);
        m.cleanup();
    }

    public static void sendMatchResult(Player p, MatchResult ma) {
        if (p.session == null || ma == null) {
            return;
        }
        Message m = new Message(CProtocol.END_GAME);
        m.putInt(ma.pWin.playerID);
        m.putString(ma.pWin.playerName);
        m.putLong(ma.pWin.money);
        m.putInt(ma.pWin.level);
        m.putInt(ma.pLose.playerID);
        m.putString(ma.pLose.playerName);
        m.putLong(ma.pLose.money);
        m.putInt(ma.pLose.level);
        p.session.write(m);
        m.cleanup();
    }

    /**
     * Chi can xac dinh duoc ban thì goi het cho nhung thang trong ban.
     * @param b 
     */
    public static Message getMessageUpdateMoney(Board b) {
        Message m = new Message(CProtocol.UPDATE_MONEY_INGAME);
        m.putByte(0);//sub command 0.
        for (int j = b.players.size(); --j >= 0;) {
            m.putInt(b.players.elementAt(j).playerID);
            m.putInt((int) b.players.elementAt(j).money);
        }
        return m;
    }

}
