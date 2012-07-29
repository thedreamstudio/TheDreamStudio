/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 * Lop nay dung de quan li ngon ngu cua server.
 * @author truongps
 */
public class L {
    
    public static final int VIET = 0;
    public static final int ENG = 1;
    public static int type = VIET;
    
    public static String gl(int ID) {
        switch(type) {
            case 0:
                return (type == VIET ? "Chỉ có chủ bàn mới có quyền cài đặt map." : "Only the owner can set map on the board.");
            case 1:
                return (type == VIET ? "Bàn này không tồn tại." : "The board is not exist.");
            default:
                return "Unknown ID";
        }
    }
}
