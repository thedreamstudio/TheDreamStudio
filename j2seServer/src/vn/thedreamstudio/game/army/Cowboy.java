/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.game.army;

/**
 * Linh cao boi.
 * @author truongps
 */
public class Cowboy extends Soldier {
    public static final int COST = 8;

    public Cowboy() {
        typeID = Soldier.TYPE_COWBOY;
        name = "Lính Tốc Độ";
        hp = 20;
		mana = 54;
		damageMin = 18;
		damageMax = 26;
		rangeView = 2;
		costMove = 5;
		rangeAttack = 1;
		costAttack = 24;
		cost = 8;
    }
}
