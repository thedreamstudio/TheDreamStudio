/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.network;

import vn.thedreamstudio.common.L;
import vn.thedreamstudio.core.common.Player;
import vn.thedreamstudio.constance.CProtocol;
import core.network.Message;
import vn.thedreamstudio.game.common.Board;

/**
 *
 * @author truongps
 */
public class GlobalServices {
    public static void processSetGameTypeMessage(Player p) {
        Message m = new Message(CProtocol.SET_GAME_TYPE);
        m.putByte(p.gameID);
        p.session.write(m);
        m.cleanup();
    }
    
    
    public static void chatToBoard(Player p, Player pSent, String info) {
        Message m = new Message(CProtocol.CHAT_BOARD);
        m.putInt(pSent.playerID);
        m.putString(pSent.playerName);
        m.putString(info);
        p.session.write(m);
        m.cleanup();
    }
    
    /**
    * 0 - de show tren banner.
     * 1 - De cach bao tu server.
    * @param p
    * @param type
    * @param info 
    */
   public static void sendServerMessage(Player p, int type, String info) {
       Message m = new Message(CProtocol.SERVER_MESSAGE);
       m.putByte(type);
       m.putString(info);
       p.session.write(m);
       m.cleanup();
   }
   
   public static void sendBoardSuggestion(Player p, Board b) {
       Message m = new Message(CProtocol.BOARD_SUGGESTION);
       m.putByte(b.boardID);
       String s = L.type == L.VIET ? b.boardName + " còn 1 chỗ trống. Bạn muốn tham gia không?" 
               : b.boardName + " is avalable. Do you want to join this board?";
       m.putString(s);
       p.session.write(m);
       m.cleanup();
   }
}
