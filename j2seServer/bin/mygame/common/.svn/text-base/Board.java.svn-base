/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.common;

import constance.CProtocol;
import network.GlobalServices;
import core.common.Player;
import core.network.Message;
import java.util.Random;
import java.util.Vector;
import mygame.game.army.Archer;
import mygame.game.army.Cavalry;
import mygame.game.army.Infantry;
import mygame.game.army.Soldier;
import mygame.game.map.Map;
import mygame.game.map.MapFactory;
import mygame.network.GameServices;

/**
 *
 * @author truongps
 */
public class Board {

    /**
     * Id cua board.
     */
    public int boardID;
    public String boardName;
    /**
     * The max player can be.
     */
    public int maxPlayer = 2;
    public int ownerID = -1;
    public Vector<Player> players = new Vector<Player>();
    private Player pNull = new Player(null);
    /**
     * Map cua ban choi.
     */
    private Map map;
    private Vector mapSoldiers = new Vector();
    private int soldierID = 0;
    Turn turn1, turn2, currentTurn;
    public boolean isStartGame = false;
    /**
     * Dung de xac dinh khi nao se update time trong 1 turn.
     */
    public boolean updateTime = false;
    /**
     * Thoi gian bat dau cua 1 turn nao do.
     */
    private long timeStart;
    
    private void resetBoard() {
        ownerID = -1;
        players = new Vector<Player>();
        for (int i = maxPlayer; --i >= 0;) {
            players.addElement(pNull);
        }
        map = null;
        mapSoldiers = new Vector();
        isStartGame = false;
    }

    public Board(int boardID, String boardName) {
        this.boardID = boardID;
        this.boardName = boardName;
        for (int i = maxPlayer; --i >= 0;) {
            players.addElement(pNull);
        }
    }

    /**
     * A new user join board to play game.
     * *Note: Check player.boardID before call this function to avoid a user can be two or more board.
     * @param p
     * @return true - User duoc cho phep vap hong choi. false - khi phong da day, k the vao choi dc.
     */
    public boolean someoneJoinBoard(Player p) {
        if (countPlayer() == maxPlayer) {
            return false;
        }
        int size = players.size();
        for (int i = 0; i < size; i++) {
            if (((Player) players.elementAt(i)).playerID == -1) {
                ownerID = (i == 0 ? p.playerID : ownerID);
                p.boardID = boardID;
                players.removeElementAt(i);
                players.add(i, p);
                return true;
            }
        }
        return false;
    }

    /**
     * A player exit this board. This function remove the user from this board.
     * @param p
     * @return true - if success; otherwise.
     */
    public boolean someoneExitBoard(Player p) {
        int size = players.size();
        for (int i = 0; i < size; i++) {
            if (((Player) players.elementAt(i)).playerID == p.playerID) {
                ownerID = (((Player) players.elementAt(i)).playerID == ownerID ? -1 : ownerID);
                players.removeElementAt(i);
                players.addElement(pNull);
                if (countPlayer() == 0) {
                    resetBoard();
                }
                return true;
            }
        }
        return false;
    }
    
    

    /**
     * Look for a new board owner. Because a old owner already exit this board.
     * This function look from first to last.
     * @return the player is the new owner if can found; otherwise return null.
     */
    public Player findNewBoardOwner() {
        int size = players.size();
        for (int i = 0; i < size; i++) {
            if (((Player) players.elementAt(i)).playerID != -1) {
                return ((Player) players.elementAt(i));
            }
        }
        return null;
    }

    /**
     * Count the current players in this board.
     * @return 
     */
    public int countPlayer() {
        int count = 0;
        int size = players.size();
        for (int i = size; --i >= 0;) {
            if (((Player) players.elementAt(i)).playerID != -1) {
                count++;
            }
        }
        return count;
    }

    public void chatInBoard(Player p, String info) {
        int size = players.size();
        for (int i = size; --i >= 0;) {
            Player pp = (Player) players.elementAt(i);
            if (pp.playerID != -1 && pp.session != null) {
                GlobalServices.chatToBoard(pp, p, info);
            }
        }
    }

    /**
     * Gọi hàm này khi có 1 client do ready.
     */
    public synchronized boolean someoneReady(Player p) {
        p.isReady = true;
        int size = players.size();
        for (int i = size; --i >= 0;) {
            if (((Player) players.elementAt(i)).playerID == p.playerID) {
                ((Player) players.elementAt(i)).isReady = true;
            }
            GameServices.processSomeOneReadyMessage(((Player) players.elementAt(i)), p);
        }
        return false;
    }

    /**
     * Gọi hàm này khi có 1 người không muốn ready nữa.
     */
    public boolean someoneUnready(Player p) {
        p.isReady = false;
        int size = players.size();
        for (int i = size; --i >= 0;) {
            if (((Player) players.elementAt(i)).playerID == p.playerID) {
                ((Player) players.elementAt(i)).isReady = false;
                return true;
            }
        }
        return false;
    }

    public void resetReady() {
        int size = players.size();
        for (int i = size; --i >= 0;) {
            if (((Player) players.elementAt(i)).playerID != -1) {
                ((Player) players.elementAt(i)).isReady = false;
            }
        }
    }

    /**
     * 
     * @param p
     * @return 
     */
    public boolean startGame(Player p) {
        int size = players.size();
        for (int i = size; --i >= 0;) {
            Player pp = ((Player) players.elementAt(i));
            if (pp.playerID != ownerID && (pp.playerID == -1 || !pp.isReady)) {
                return false;
            }
        }
        for (int i = size; --i >= 0;) {
            Player pp = ((Player) players.elementAt(i));
            GameServices.processStartGame(pp, mapSoldiers);
        }
        //Khoi tao turn va chon nguoi di truoc.
        turn1 = new Turn(((Player) players.elementAt(0)).playerID);
        turn2 = new Turn(((Player) players.elementAt(1)).playerID);
        currentTurn = turn1;
        for (int i = size; --i >= 0;) {
            Player pp = ((Player) players.elementAt(i));
            if (pp.playerID != -1 && pp.session != null) {
                pp.session.write(getMessageNextTurn());
            }
        }
        isStartGame = true;
        updateTime = true;
        timeStart = System.currentTimeMillis();
        new Thread() {

            @Override
            public void run() {
                while (isStartGame) {
                    if (updateTime) {
                        long cTime = System.currentTimeMillis();
                        if (cTime - timeStart >= Turn.TURN_TIME) {
                            endGame(currentTurn);
                        }
                    }
                }
            }
        }.start();
        return true;
    }

    /**
     * Chi co chủ phòng mới khởi tạo được map.
     * @param playeriD
     * @param mapID
     * @return 
     */
    public Map createMap(final int playeriD, final int mapID) {
        if (playeriD != ownerID) {
            return map;
        }
        map = MapFactory.getMapFromID(mapID);
        return map;
    }

    public synchronized Vector setArmyOnMap(int[] armies, int ownerID) {
        //remove nhung army cu
        int size = mapSoldiers.size();
        for (int i = size; --i >= 0;) {
            if (((Soldier) mapSoldiers.elementAt(i)).owner.playerID == ownerID) {
                mapSoldiers.removeElementAt(i);
            }
        }
        //add army moi
        Vector v = new Vector();
        Player p = getPlayerFromId(ownerID);
        for (int i = 0; i < armies.length; i++) {
            Soldier a = createSodier(armies[i]);
            a.owner = p;
            a.id = soldierID;
            soldierID++;
            mapSoldiers.addElement(a);
            v.addElement(a);
        }
        return v;
    }

    private Soldier createSodier(int typeID) {
        switch (typeID) {
            case Soldier.TYPE_ARCHER:
                return (new Archer());
            case Soldier.TYPE_CAVARLY:
                return (new Cavalry());
            case Soldier.TYPE_INFANTRY:
                return (new Infantry());
        }
        return null;
    }

    public Player getPlayerFromId(final int playerID) {
        if (playerID < 0) {
            return pNull;
        }
        int size = players.size();
        for (int i = size; --i >= 0;) {
            if (((Player) players.elementAt(i)).playerID == playerID) {
                return ((Player) players.elementAt(i));
            }
        }
        return pNull;
    }

    /**
     * Layout army on map.
     * @param army
     * @param ownerID
     * @return 
     */
    public synchronized boolean layoutArmy(Vector army, int ownerID) {
        if (map == null) {
            return false;
        }
        int size = army.size();
        for (int i = size; --i >= 0;) {
            Soldier s = (Soldier) army.elementAt(i);
            if (s.x < 0 || s.x > map.rowNum
                    || s.y < 0 || s.y > map.colNum) {
                return false;
            }
        }
        for (int i = size; --i >= 0;) {
            Soldier s = (Soldier) army.elementAt(i);
            int length = mapSoldiers.size();
            for (int j = length; --j >= 0;) {
                Soldier ms = (Soldier) mapSoldiers.elementAt(j);
                if (ms.id == s.id) {
                    ms.x = s.x;
                    ms.y = s.y;
                }
            }
        }
        return true;
    }

    public synchronized boolean moveArmy(Soldier s) {
        if (s.x < 0 || s.x > map.rowNum
                || s.y < 0 || s.y > map.colNum) {
            return false;
        }
        int length = mapSoldiers.size();
        for (int j = length; --j >= 0;) {
            Soldier ms = (Soldier) mapSoldiers.elementAt(j);
            if (ms.id == s.id) {
                ms.x = s.x;
                ms.y = s.y;
            }
        }
        return true;
    }

    public synchronized AttackResult attack(int myArmyID, int rivalArmyID) {
        AttackResult a = new AttackResult();
        a.myArmyID = myArmyID;
        a.rivalArmyID = rivalArmyID;
        Random r = new Random();
        Soldier s = getArmyFromId(rivalArmyID);
        a.myArmyHP = s.hp;
        s = getArmyFromId(myArmyID);
        a.rivalArmyHP -= s.damageMin + r.nextInt(s.damageMax - s.damageMin);
        return a;
    }

    public synchronized Soldier getArmyFromId(int id) {
        int size = mapSoldiers.size();
        for (int i = size; --i >= 0;) {
            if (((Soldier) mapSoldiers.elementAt(i)).id == id) {
                return (Soldier) mapSoldiers.elementAt(i);
            }
        }
        return null;
    }

    /**
     * Doi luot di
     */
    public synchronized void changeTurn() {
        if (currentTurn == turn1) {
            currentTurn = turn2;
        } else {
            currentTurn = turn1;
        }
    }

    public synchronized Message getMessageNextTurn() {
        Message m = new Message(CProtocol.NEXT_TURN);
        m.putInt(currentTurn.playerID);
        m.putLong(currentTurn.total);
        m.putLong(currentTurn.turn);
        if (currentTurn == turn1) {
            m.putInt(turn2.playerID);
            m.putLong(turn2.total);
            m.putLong(turn2.turn);
        } else {
            m.putInt(turn1.playerID);
            m.putLong(turn1.total);
            m.putLong(turn1.turn);
        }
        return m;
    }

    /**
     * Nguoi thua vi het gio.
     * @param t 
     */
    public synchronized void endGame(Turn t) {
    }
    
    
    public synchronized Map getMap() {
        return map;
    }
}
