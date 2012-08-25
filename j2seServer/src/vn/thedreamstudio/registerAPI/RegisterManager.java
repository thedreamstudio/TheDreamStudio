/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.registerAPI;

import vn.thedreamstudio.core.common.Player;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.thedreamstudio.util.FileManager;

/**
 * 
 * @author truongps
 */
public class RegisterManager {

//    public static final int USER_EXIST = 0;
//    public static final int USER_DONT_EXIST = 1;
//    public static final int USER_REGISTER_OK = 2;
//    public static final int USER_REGISTER_FAIL = 3;
    private static int currentID;
    public static final String dir = FileManager.getcurrentDirectory() + "USER" + FileManager.SEPARATOR;
    public static final String[] registerParttern = new String[]{
        "userid=",
        "username=",
        "password=",
        "phonenumber"
    };

    static {
        System.out.println("Current user info save in: " + dir);
        FileManager.makeDir(dir);
        currentID = new File(dir).list().length;
        System.out.println("Current user: " + currentID);
    }

    /**
     * Kiem tra xem ten nay co duoc phep them vao database.
     * @param username
     * @return 
     */
    public static boolean checkUsername(String username) {
        return FileManager.checkFileExist(dir + username);
    }

    /**
     * Them 1 user vào database.
     * @param p
     * @return
     */
    public static boolean addUser(Player p) {
        try {
            if (!checkUsername(p.userName)) {
                currentID = new File(dir).list().length;
                System.out.println("Current user: " + currentID);
                p.playerID = currentID;
                FileManager.saveUserInfo(p, dir + p.userName);
                return true;
            }
        } catch (IOException ex) {
            Logger.getLogger(RegisterManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static Player getPlayer(String userName) {
        if (!checkUsername(userName)) {
            return null;
        }
        Player p = null;
        try {
            p = FileManager.getPlayerFromFile(dir + userName);
        } catch (IOException ex) {
            Logger.getLogger(RegisterManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
}
