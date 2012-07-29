/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.game.army;

import core.common.Player;

/**
 *
 * @author truongps
 */
public class Soldier {

    /**
     * Linh cung.
     */
    public static final int TYPE_ARCHER = 0;
    /**
     * Linh bo.
     */
    public static final int TYPE_INFANTRY = 1;
    /**
     * Ki binh.
     */
    public static final int TYPE_CAVARLY = 2;
    /**
     * Player là chủ cua doi quan nay.
     */
    public Player owner;
    /**
     * Vi tri cua doi quan dung.
     */
    public int x;
    public int y;
    /**
     * Id cua doi quan tren ban.
     */
    public int id;
    /**Id cua binh chủng.*/
    public int typeID;
    public String name;
    public int cost;
    public int hp;
    public int damageMax;
    public int damageMin;
    public int mana;
    public int costMove;
    public int costAttack;
    public int rangeAttack;
    public int rangeView;
    

    public int getID() {
        return typeID;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getHP() {
        return hp;
    }

    public int getDamageMax() {
        return damageMax;
    }
    
    public int getDamageMin() {
        return damageMin;
    }
    
    public int getMana() {
        return mana;
    }
    
    public int getCostMove() {
        return costMove;
    }
    
    public int getCostAttack() {
        return costAttack;
    }
    
    public int getRangeAttack() {
        return rangeAttack;
    }
    
    public int getRangeView() {
        return rangeView;
    }
}
