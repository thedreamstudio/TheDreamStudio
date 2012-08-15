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
    public static final int COST = 10;

    public Mexico() {
        typeID = Soldier.TYPE_MEXICO;
        name = "Lính cận vệ";
        hp = 100;
		mana = 54;
		damageMin = 11;
		damageMax = 22;
		rangeView = 5;
		costMove = 8;
		rangeAttack = 1;
		costAttack = 22;
		cost = 10;
    }
}
