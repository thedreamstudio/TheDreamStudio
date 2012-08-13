/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import core.common.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author truongps
 */
public class FileManager {

    public static final String SEPARATOR = System.getProperty("file.separator");
    
    /**
     * Kiểm tra xem 1 file trong hệ thống có tồn tại hay không?
     * @param path
     * @return 
     */
    public static boolean checkFileExist(String path) {
        return (new File(path)).exists();
    }

    public static boolean makeDir(String path) {
        File f = new File(path);
        return f.mkdir();
    }

    public static String getcurrentDirectory() {
        String cp = new File(".").getAbsolutePath();
        if (cp.endsWith(".")) {
            return cp.substring(0, cp.length() - 1);
        }
        return cp;
    }

    public static boolean saveUserInfo(Player p, String path) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeInt(p.playerID);
            dos.writeUTF(p.userName);
            dos.writeUTF(p.password);
            dos.writeUTF(p.phoneNumber);
            dos.close();
            dos = null;
            fos.close();
            fos = null;
            System.out.println("Save user info in: " + path);
            return true;
        } catch (FileNotFoundException ex) {
            throw ex;
//            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                throw ex;
//                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        return false;
    }

    public static Player getPlayerFromFile(String path) throws IOException {
        FileInputStream fos = null;
        Player p = new Player(null);
        try {
            fos = new FileInputStream(path);
            DataInputStream dos = new DataInputStream(fos);
            p.playerID = dos.readInt();
            p.userName = dos.readUTF();
            p.password = dos.readUTF();
            p.phoneNumber = dos.readUTF();
            dos.close();
            dos = null;
            fos.close();
            fos = null;
            return p;
        } catch (FileNotFoundException ex) {
            throw ex;
//            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                throw ex;
//                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
