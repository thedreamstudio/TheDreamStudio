/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.game.army;

/**
 * Linh mexico.
 * @author truongps
 */
public class Mexico extends Soldier {
    public static final int COST = 30;

    public Mexico() {
        typeID = Soldier.TYPE_MEXICO;
        name = "Mexico";
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
