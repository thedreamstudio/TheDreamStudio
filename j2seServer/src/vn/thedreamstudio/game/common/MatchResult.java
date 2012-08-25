/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.game.common;

import vn.thedreamstudio.core.common.Player;

/**
 *
 * @author truongps
 */
public class MatchResult {
    public Player pWin;
    public Player pLose;

    public MatchResult(Player pWin, Player pLose) {
        this.pWin = pWin;
        this.pLose = pLose;
    }
}
