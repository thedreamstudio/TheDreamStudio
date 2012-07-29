/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import core.common.Player;
import java.util.Hashtable;

/**
 *
 * @author truongps
 */
public class PlayerManager {

    private Hashtable<String, Player> players = new Hashtable<String, Player>();
    private static PlayerManager instance = new PlayerManager();

    private PlayerManager() {
    }

    public synchronized static PlayerManager getInstance() {
        return instance;
    }

    /**
     * 
     * @param p
     * @return 
     */
    public boolean addPlayer(Player p) {
        if (p.isConnected()) {
            String key = p.userName;
            Player mp = (Player) players.get(key);
            if (mp == null) {
//                p.playerID = players.size();
                p.isLogin = true;
                players.put(key, p);
                return true;
            }
        }
        return false;
    }

    public boolean removePlayer(Player p) {
        players.remove(p.userName);
        return true;
    }
}
