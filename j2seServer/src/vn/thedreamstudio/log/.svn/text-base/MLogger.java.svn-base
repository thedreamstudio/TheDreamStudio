/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.thedreamstudio.util.FileManager;


/**
 *
 * @author truongps
 */
public class MLogger {
    private static FileHandler fileHandler = null;
    private static Logger logger = Logger.getLogger(FileManager.getcurrentDirectory() + "hehe");
    static {
        try {
            fileHandler = new FileHandler("app.log", true);
            logger.addHandler(fileHandler);
        } catch (IOException ex) {
            Logger.getLogger(MLogger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(MLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    static {
    
//        Logger logger = Logger.getLogger(WriteLogToFile.class.getName());
 
        //
        // Create an instance of FileHandler that write log to a file called
        // app.log. Each new message will be appended at the at of the log file.
        //
//        FileHandler fileHandler = new FileHandler("app.log", true);        
//        logger.addHandler(fileHandler);
//    }
    
    public static void log(String info) {
        logger.info(info);
    }
}
