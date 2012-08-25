/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.common;

import vn.thedreamstudio.game.common.MGameController;

/**
 *
 * @author truongps
 */
public class GameController {
    public final static byte GAMEID_COMMON = 0;
    public final static byte GAMEID_HERO_WAR = 1;
    
    private static GameController instance = new GameController();
    
    public synchronized static GameController getInstance() {
        return instance;
    }

    public GameController() {
    }
    
    public synchronized static boolean isSupportGame(byte gameID) {
        switch(gameID) {
            case GAMEID_COMMON:
            case GAMEID_HERO_WAR:
                return true;
        }
        return false;
    }
}
