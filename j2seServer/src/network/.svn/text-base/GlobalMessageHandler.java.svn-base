/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import com.sun.org.apache.bcel.internal.generic.L2D;
import common.DataBaseManager;
import common.GameController;
import common.L;
import common.PlayerManager;
import core.common.Player;
import constance.CProtocol;
import core.network.IMessageHandler;
import java.io.IOException;
import core.network.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.network.GameMessageHandler;
import registerAPI.RegisterManager;

/**
 *
 * @author truongps
 */
public class GlobalMessageHandler implements IMessageHandler {

    GameMessageHandler gameMessageHandler = new GameMessageHandler();
    private static int idRandom = 0;

    public static int createIDForLoginTrial() {
        return ++idRandom;
    }

    @Override
    public void processMessage(Player player, Message msg) throws IOException {
        try {
            if (player.isLogin()) {
                switch (msg.id) {
                    case CProtocol.LANGUAGE: {
                        byte lang = msg.reader().readByte();
                        switch (lang) {
                            case L.VIET:
                                player.languageType = L.VIET;
                                break;
                            case L.ENG:
                                player.languageType = L.ENG;
                                break;
                        }
                        Message m = new Message(CProtocol.LANGUAGE);
                        m.putByte(player.languageType);
                        player.session.write(m);
                        m.cleanup();
                    }
                    break;
                    case CProtocol.SET_GAME_TYPE: {
                        byte gameID = msg.reader().readByte();
                        if (GameController.isSupportGame(gameID)) {
                            player.gameID = gameID;
                            GlobalServices.processSetGameTypeMessage(player);
                            GlobalServices.sendServerMessage(player, 0, L.gl(player.languageType, 5));
                        } else {
                            //He thong k support game nay.
                            Message m = new Message(CProtocol.SERVER_MESSAGE);
                            m.putByte(1);
                            String s = player.languageType == L.VIET ? "Hệ thống không support game này." : "This system don't support this game.";
                            m.putString(s);
                            player.session.write(m);
                            m.cleanup();
                        }
                    }
                    break;
                    case CProtocol.LOGOUT:
                        doLogout(player);
                        break;
                    case CProtocol.SET_CLIENT_INFO://
                    {
                        player.platform = msg.reader().readUTF();
                        player.version = msg.reader().readUTF();
                        player.languageType = msg.reader().readByte();
                    }
                    break;
                    default:
                        processGameMessage(player, msg);
                }
            } else if (msg.id == CProtocol.LOGIN) {
//                doLogin(player, msg);
                doLoginFromFile(player, msg);
            } else if (msg.id == CProtocol.LOGIN_TRYAL) {
                doLoginTryal(player, msg);
            } else if (msg.id == CProtocol.REGISTER) {
                player.userName = msg.reader().readUTF();
                player.password = msg.reader().readUTF();
                player.phoneNumber = msg.reader().readUTF();
                if (RegisterManager.addUser(player)) {//dang ki thanh cong
                    Message m = new Message(CProtocol.REGISTER);
                    m.putByte(1);
                    m.putString(L.gl(player.languageType, 2));
                    player.session.write(m);
                    m.cleanup();
                } else {
                    Message m = new Message(CProtocol.REGISTER);
                    m.putByte(0);
                    m.putString(L.gl(player.languageType, 3));
                    player.session.write(m);
                    m.cleanup();
                }
                //Xu li tra ve server message. De user chap nhan cho minh biet so dien thoai.
//                if ("".equals(player.phoneNumber)) {
//                    GlobalServices.sendServerMessage(player, 2, null);
//                }
            } else if (msg.id == CProtocol.SET_CLIENT_INFO) {
                player.platform = msg.reader().readUTF();
                String model = msg.reader().readUTF();
                player.version = msg.reader().readUTF();
                byte lang = msg.reader().readByte();
                System.out.println("Set ngon ngu: " + lang);
                player.languageType = lang;
            } else if (msg.id == CProtocol.LANGUAGE) {
                byte lang = msg.reader().readByte();
                switch (lang) {
                    case L.VIET:
                        player.languageType = L.VIET;
                        break;
                    case L.ENG:
                        player.languageType = L.ENG;
                        break;
                }
                Message m = new Message(CProtocol.LANGUAGE);
                m.putByte(player.languageType);
                player.session.write(m);
                m.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLogout(Player p) {
        if (PlayerManager.getInstance().removePlayer(p)) {
            if (p.session == null) {
                return;
            }
            Message m = new Message(CProtocol.LOGOUT);
            p.session.write(m);
            m.cleanup();
            p.isLogin = false;
        }
    }

    private void doLoginTryal(Player user, Message message) throws IOException {
        user.playerName = user.userName = message.reader().readUTF();
        user.playerID = createIDForLoginTrial();
//        user.password = message.reader().readUTF();
        Message m = new Message(CProtocol.LOGIN);
        m.putByte(1);
        user.session.write(m);
        m.cleanup();
        PlayerManager.getInstance().addPlayer(user);
        //Goi thong tin user ve.
        m = new Message(CProtocol.GET_USER_INFO);
        m.putInt(user.playerID);
        m.putString(user.userName);
        m.putLong(user.money);
        m.putInt(user.level);
        user.session.write(m);
        m.cleanup();
        //show info.
        GlobalServices.sendServerMessage(user, 0, "You are using trial version.");
    }

    private void doLogin(Player user, Message message) throws IOException {
        user.userName = message.reader().readUTF();
        user.password = message.reader().readUTF();
        /**
         * 1-thanh cong; 2- da co may dang nhap voi nick nay roi; 3- dang nhap that bai.
         */
        int result = 1;
        //---------------------------------------------------------------------
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement p = null;
        long t1 = 0;
        try {
            String query = "SELECT userid, username, money, level FROM user where username=? && password=?";
            t1 = System.currentTimeMillis();
            System.out.println("Start login query at: " + t1);
            conn = DataBaseManager.getPoolingConnection();
            p = conn.prepareStatement(query);
            p.setString(1, user.userName);
            p.setString(2, user.password);
            rs = p.executeQuery();
            if (rs == null) {
                result = 3;
            } else {
                while (rs.next()) {
                    user.playerID = rs.getInt("userid");
                    user.playerName = rs.getString("username");
                    user.money = rs.getLong("money");
                    user.level = rs.getInt("level");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (p != null) {
                    p.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println("End login query at: " + t2);
        System.out.println("Total: " + (t2 - t1) + " ms");
        //------------------------------------------------------------
        if (!PlayerManager.getInstance().addPlayer(user)) {// Kiểm tra đã có nick đăng nhập rồi không
            result = 2;
        }
        Message m = new Message(CProtocol.LOGIN);
        m.putByte(result);
        user.session.write(m);
        m.cleanup();

        if (result == 1) {
            m = new Message(CProtocol.GET_USER_INFO);
            m.putInt(user.playerID);
            m.putString(user.playerName);
            m.putLong(user.money);
            m.putInt(user.level);
            user.session.write(m);
            m.cleanup();
        }
//        if (result == 2) {
//            session.close();
//        }
    }

    private void processGameMessage(Player p, Message m) throws IOException {
        switch (p.gameID) {
            case GameController.GAMEID_HERO_WAR:
                gameMessageHandler.processMessage(p, m);
                break;
        }
    }

    @Override
    public void onDisconnected(Player user) {
        if (gameMessageHandler != null) {
            gameMessageHandler.onDisconnected(user);
        }
        doLogout(user);
    }

    private void doLoginFromFile(Player user, Message message) throws IOException {
        user.userName = message.reader().readUTF();
        user.password = message.reader().readUTF();
        /**
         * 1-thanh cong; 2- da co may dang nhap voi nick nay roi; 3- dang nhap that bai.
         */
        int result = 1;
        Player data = RegisterManager.getPlayer(user.userName);
        if (data != null) {
            user.playerID = data.playerID;
            user.phoneNumber = data.phoneNumber;
            user.playerName = user.userName;
        }
        if (data == null || !data.password.equals(user.password)) {//nick khong ton tai trong he thong
            result = 3;
        } else if (!PlayerManager.getInstance().addPlayer(user)) {// Kiểm tra đã có nick đăng nhập rồi không
            result = 2;
        }
        Message m = new Message(CProtocol.LOGIN);
        m.putByte(result);
        user.session.write(m);
        m.cleanup();

        if (result == 1) {
            m = new Message(CProtocol.GET_USER_INFO);
            m.putInt(user.playerID);
            m.putString(user.playerName);
            m.putLong(user.money);
            m.putInt(user.level);
            user.session.write(m);
            m.cleanup();
        }
    }
}
