/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.game.army;

/**
 * Tho dan.
 * @author truongps
 */
public class Indian extends Soldier {
    public static final int COST = 10;

    public Indian() {
        typeID = Soldier.TYPE_INDIAN;
        name = "Cung thủ";
        hp = 60;
		mana = 54;
		damageMin = 18;
		damageMax = 26;
		rangeView = 5;
		costMove = 8;
		rangeAttack = 4;
		costAttack = 32;
		cost = 10;
    }
}
