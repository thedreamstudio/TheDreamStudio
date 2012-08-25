/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.game.common;

/**
 *
 * @author truongps
 */
public class Turn {
    /**
     * Thoi gian cua 1 tran dau. Tinh theo minute
     */
    public static final long MATCH_TIME = 20 * 60 * 1000;
    /**
     * Thoi gian cua 1 tran dau. Tinh theo second.
     */
    public static final long TURN_TIME = 20 * 1000;
    public int playerID = -1;
    /**
     * Tong time 1 van choi, ai het truoc se thua.
     */
    public long total;
    /**
     * Tong thoi gian di cua 1 turn, if k di kip se thua.
     */
    public long turn;

    public Turn(int pID) {
        playerID = pID;
        this.total = MATCH_TIME;
        this.turn = TURN_TIME;
    }
}
