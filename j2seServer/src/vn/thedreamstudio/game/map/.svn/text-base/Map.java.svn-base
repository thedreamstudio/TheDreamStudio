/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.thedreamstudio.game.map;

/**
 * Map chua nhieu cell.
 * @author truongps
 */
public class Map {

    private Cell[][] map;
    private byte[][] mapAllows;
    public int rowNum;
    public int colNum;
    public Position ownerGeneral;
    public Position visitGeneral;
    public int mapID;
    /**
     * Khoang cách từ general de dat duoc linh.
     */
    public int initRange = 6;

    public Map(final int mapID, final int rowNum, final int colNum, byte[][] mapData) {
        this.mapID = mapID;
        this.rowNum = rowNum;
        this.colNum = colNum;
        map = new Cell[rowNum][colNum];
        mapAllows = mapData;

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                map[i][j] = new Cell(i, j, mapAllows[i][j]);
                if (map[i][j].allow == Cell.ALLOW_GENERAL) {
                    if (ownerGeneral == null) {
                        ownerGeneral = new Position(i, j);
                    } else {
                        visitGeneral = new Position(i, j);
                    }
                }
            }
        }
        if (ownerGeneral == null) {
            ownerGeneral = new Position(0, 0);
        }
        if (visitGeneral == null) {
            visitGeneral = new Position(0, 0);
        }
    }

    public byte[][] getMapLayout() {
        return mapAllows;
    }
}
