/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.common;

/**
 * Lop nay dung de quan li ngon ngu cua server.
 * @author truongps
 */
public class L {
    
    public static final int VIET = 0;
    public static final int ENG = 1;
    public static int type = VIET;
    
    public static String gl(int playerLanguage, int ID) {
        switch(ID) {
            case 0:
                return (playerLanguage == VIET ? "Chỉ có chủ bàn mới có quyền cài đặt map." : "Only the owner can set map on the board.");
            case 1:
                return (playerLanguage == VIET ? "Bàn này không tồn tại." : "The board is not exist.");
            case 2:
                return (playerLanguage == VIET ? "Bạn đã đăng kí thành công." : "Your registration is successfully.");
            case 3:
                return (playerLanguage == VIET ? "Bạn đã đăng kí thất bại. Bạn vui lòng đăng kí với tên tài khoản khác" 
                        : "Your registration is fail. Please, register with other username.");
//            case 4:
//                return (playerLanguage == VIET ? "Số điện thoại này sẽ được dùng để xác minh tài khoản của bạn sau này." : 
//                        "Your registration is successfully.");
            case 5:
                return (playerLanguage == VIET ? "Chào mừng bạn đã đến với phiên bản dùng thử của Island War." 
                        : "Welcome to the trial version of Island War.");
            case 6:
                return (playerLanguage == VIET ? "Bạn không có đủ tiền để mua thêm lính." 
                        : "You don't have enought money.");
            case 7:
                return (playerLanguage == VIET ? "Bàn không tồn tại trong hệ thống." 
                        : "Bàn không tồn tại trong hệ thống.");
                case 8:
                return (playerLanguage == VIET ? "Bạn bị kick bởi hệ thống." 
                        : "You are kicked by system.");
            default:
                return "Unknown ID";
        }
    }
}
