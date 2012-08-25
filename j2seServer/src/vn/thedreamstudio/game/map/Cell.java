/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.game.map;

import vn.thedreamstudio.game.army.Soldier;

/**
 * 1 cell tren ban do.
 * @author truongps
 */
public class Cell {
    /**
     * Không cho bất kì ai đặt chân lên ô này.
     */
    public static final byte ALLOW_NONE = 0;
    /**
     * Chi cho phep linh bo di wa o dat nay. Linh bo bao gom: Ki binh, Bo binh và Lính cung.
     */
    public static final byte ALLOW_INFANTRY = 1;
    /**
     * Cho phép các phuong tiện nhu xe ban da, ... (Cai nay de nang cap sau nay neu co).
     */
    public static final byte ALLOW_VEHICLES = 2;
    /**
     * Cho phep ca xe va linh deu di qua duoc cell nay.
     */
    public static final byte ALLOW_VEHICLES_INFANTRY = 3;
    /**
     * Cell này dành cho tướng. Mất ô này thì user thua.
     */
    public static final byte ALLOW_GENERAL = 4;
    public byte allow = ALLOW_NONE;
    
    /**
     * Vi tri x, y cua cell.
     */
    public int x;
    public int y;
    /**
     * Quan doi dang dong tren cell nay.
     */
    public Soldier army = null;

    public Cell(int x, int y, byte allow) {
        this.x = x;
        this.y = y;
        this.allow = allow;
    }
}
