/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.game.army;

/**
 * Quan li toan bo shop linh.
 * @author truongps
 */
public class ArmyShop {

    private static Soldier[] shopContents;
    private static ArmyShop instance = new ArmyShop();
    
    public synchronized static ArmyShop getInstance() {
        return instance;
    }
    
    private ArmyShop() {
        shopContents = new Soldier[] {
            /*new Archer(), new Infantry(), new Cavalry()*/
            new Artillery(), new Geezer(), new Cowboy(), new Indian(), new Mexico(), new Scout(), new General()
        };
    }
    
    public Soldier getArtillery() {
        return shopContents[0];
    }
    
    public Soldier[] getArmySupported() {
        return shopContents;
    }
    
    
    
//    public Soldier getArcher() {
//        return shopContents[0];
//    }
//    
//    public Soldier getInfantry() {
//        return shopContents[1];
//    }
//    
//    public Soldier getCavalry() {
//        return shopContents[2];
//    }
    
    public synchronized static int getPriceOF(final int ID) {
        switch(ID) {
//            case Soldier.TYPE_ARCHER:
//                return Archer.COST;
//            case Soldier.TYPE_CAVARLY:
//                return Cavalry.COST;
//            case Soldier.TYPE_INFANTRY:
//                return Infantry.COST;
            case Soldier.TYPE_ARTILLERY:
                return Artillery.COST;
            case Soldier.TYPE_COWBOY:
                return Cowboy.COST;
            case Soldier.TYPE_GEEZER:
                return Geezer.COST;
            case Soldier.TYPE_INDIAN:
                return Indian.COST;
            case Soldier.TYPE_MEXICO:
                return Mexico.COST;
            case Soldier.TYPE_SCOUT:
                return Scout.COST;
        }
        return 0;
    }
}
