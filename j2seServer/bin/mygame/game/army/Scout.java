/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.game.army;

/**
 * Linh ban tia.
 * @author truongps
 */
public class Scout extends Soldier {
    public static final int COST = 30;

    public Scout() {
        typeID = Soldier.TYPE_SCOUT;
        name = "Lính bắn tỉa";
        cost = 30;
        hp = 60;
        damageMax = 20;
        damageMin = 12;
        mana = 54;
        costMove = 6;
        costAttack = 24;
        rangeAttack = 4;
        rangeView = 5;
    }
}
