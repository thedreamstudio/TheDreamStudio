/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.common;

import core.common.Player;
import java.util.Vector;
import mygame.game.army.ArmyShop;
import mygame.game.army.Soldier;
import mygame.game.map.Map;

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
    public static final int TOTAL_MONEY = 100;
    public Board[] boards;

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
}
