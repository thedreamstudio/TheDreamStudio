/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.common;

import vn.thedreamstudio.core.common.Player;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.thedreamstudio.network.GlobalMessageHandler;
import vn.thedreamstudio.network.GlobalServices;

/**
 *
 * @author truongps
 */
public class PlayerManager extends Thread {

    /**
     * Thoi gian ma user k connect vao server se bi kickSystem.
     */
    public static final int TIME_KICK = 1000 * 30;
    private Hashtable<String, Player> players = new Hashtable<String, Player>();
    private static PlayerManager instance = new PlayerManager();

    private PlayerManager() {
        start();
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
            } else {
                if (mp.session != null) {
                    GlobalServices.sendServerMessage(mp, 1, "Bạn bị kick vì tài khoản đăng nhập từ máy khác.");
                    mp.session.close(true);
                }
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

    public Player getPlayerFromUsername(String username) {
        return players.get(username);
    }

    public Player getPlayerFromID(int id) {
        try {
            Enumeration ps = players.elements();
            while (ps.hasMoreElements()) {
                Player p = (Player) ps.nextElement();
                if (p.playerID == id) {
                    return p;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(TIME_KICK);
                kickSystem();
            } catch (InterruptedException ex) {
                Logger.getLogger(PlayerManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void kickSystem() {
        try {
            Enumeration ps = players.elements();
            long currentTime = System.currentTimeMillis();
            while (ps.hasMoreElements()) {
                Player p = (Player) ps.nextElement();
                if (currentTime - p.lastRequestTime >= TIME_KICK) {
                    if (p.session != null) {
                        GlobalServices.sendServerMessage(p, 1, L.gl(p.languageType, 8));
                        p.session.close(true);
                        p.session = null;
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
