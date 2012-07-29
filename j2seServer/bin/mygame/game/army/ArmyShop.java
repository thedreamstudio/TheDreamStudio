/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.game.army;

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
            new Archer(), new Infantry(), new Cavalry()
        };
    }
    
    public Soldier[] getArmySupported() {
        return shopContents;
    }
    
    public Soldier getArcher() {
        return shopContents[0];
    }
    
    public Soldier getInfantry() {
        return shopContents[1];
    }
    
    public Soldier getCavalry() {
        return shopContents[2];
    }
    
    public synchronized static int getPriceOF(final int ID) {
        switch(ID) {
            case Soldier.TYPE_ARCHER:
                return Archer.COST;
            case Soldier.TYPE_CAVARLY:
                return Cavalry.COST;
            case Soldier.TYPE_INFANTRY:
                return Infantry.COST;
        }
        return 0;
    }
}
