/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.game.map;

import constance.CProtocol;
import core.network.Message;

/**
 *
 * @author truongps
 */
public class MapFactory {

    public final static int MAP_ROW = 50;
    public final static int MAP_COL = 50;
    public static final byte[] MAP_IDs = new byte[]{0};
    public static final String[] MAP_NAMEs = new String[]{"Hoang mạc"};
    public static final byte[][] MAP_ONE;

    static {
        MAP_ONE = new byte[24][24];
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 24; j++) {
                if (i == 0 && j == 0) {
                    MAP_ONE[i][j] = Cell.ALLOW_GENERAL;
                } else if (i == 15 && j == 15) {
                    MAP_ONE[i][j] = Cell.ALLOW_GENERAL;
                } else {
                    MAP_ONE[i][j] = Cell.ALLOW_VEHICLES_INFANTRY;
                }
            }
        }
    }

    public synchronized static Map getMapFromID(final int mapID) {
        switch (mapID) {
            case 0:
                return new Map(0, 24, 24, MAP_ONE);
        }
        return null;
    }
}