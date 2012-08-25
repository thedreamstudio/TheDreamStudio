/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.game.common;

import vn.thedreamstudio.common.PlayerManager;
import vn.thedreamstudio.core.common.Player;
import java.util.Vector;
import vn.thedreamstudio.game.army.ArmyShop;
import vn.thedreamstudio.game.army.Soldier;
import vn.thedreamstudio.game.map.Map;

/**
 *
 * @author truongps
 */
public class MGameController {

    private static MGameController instance = new MGameController();

    public synchronized static MGameController getInstance() {
        return instance;
    }
    public static final int MAXBOARD = 10;
    /**
     * Total money that a user can have to select army.
     * It use only in a board.
     */
    public static final int TOTAL_MONEY = 24;
    public Board[] boards;
    /**
     * Vector này chứa các player đang trong game.
     */
    public Vector players = new Vector();
    public static final int MAX_ITEM_IN_A_PAGE = 20;

    private MGameController() {
        boards = new Board[MAXBOARD];
        for (int i = 0; i < MAXBOARD; i++) {
            boards[i] = new Board(i, "Board " + i);
        }
    }

    public boolean checkUserSelectArmy(final int[] values) {
        int total = 0;
        for (int i = values.length; --i >= 0;) {
            total += ArmyShop.getPriceOF(values[i]);
        }
        return (total <= TOTAL_MONEY);
    }

    public boolean joinBoard(final int boardID, Player p) {
        if (p.boardID != -1) {//is in another board.
            boards[p.boardID].someoneExitBoard(p);
        }
        if (boardID > -1 && boardID < MAXBOARD) {
            return boards[boardID].someoneJoinBoard(p);
        }
        return false;
    }

    public void chatInBoard(Player p, String info) {
        if (p.boardID > -1 && p.boardID < MAXBOARD) {
            boards[p.boardID].chatInBoard(p, info);
        }
    }

    public boolean exitBoard(final int boardID, Player p) {
        if (boardID > -1 && boardID < MAXBOARD) {
            return boards[boardID].someoneExitBoard(p);
        }
        return false;
    }

    public Board[] getBoardList() {
        return boards;
    }

    public Soldier[] getArmyShopContents() {
        return ArmyShop.getInstance().getArmySupported();
    }

    /**
     * Get the players in a board.
     * @param boardID
     * @return 
     */
    public Vector getPlayerInBoard(final int boardID) {
        return boards[boardID].players;
    }

    /**
     * Get the board.
     * @param boardID
     * @return 
     */
    public Board getBoard(final int boardID) {
        if (boardID > -1 && boardID < MAXBOARD) {
            return boards[boardID];
        }
        return null;
    }

    /**
     * Khoi tao map trong 1 ban choi.
     * Chi co chu ban moi tao duoc map.
     * @param boardID
     * @param p
     * @param mapID
     * @return 
     */
    public Map setMapOnBoard(Player p, final int mapID) {
        Board b = getBoard(p.boardID);
        if (b != null) {
            return b.createMap(p.playerID, mapID);
        }
        return null;
    }

    public void addPlayerIntoGame(Player p) {
        players.addElement(p);
    }

    public void removePlayerInGame(Player p) {
        boolean r = players.removeElement(p);
        System.out.println("Da remove" + (r ? " Thanh cong " : " That bai ") + p.playerName);
    }

    public Board getBoardSuggested() {
        for (int i = 0; i < boards.length; i++) {
            if (!boards[i].isSuggested && boards[i].countPlayer() != 2) {
                boards[i].isSuggested = true;
                return boards[i];
            }
        }
        return null;
    }
}
