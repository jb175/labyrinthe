package com.isep.jbmo60927.game_components;

import java.util.Arrays;

public class Cell {

    public static final String HORIZONTAL_WALL = "___";
    public static final String HORIZONTAL_NO_WALL = "   ";
    public static final String HORIZONTAL_NO_WALL_CENTER = " %s ";
    public static final String VERTICAL_WALL = "|";
    public static final String VERTICAL_NO_WALL = " ";

    private final boolean[] walls;
    private final Coordinates coordinates;

    public Cell(Coordinates coordinates, boolean[] walls) {
        this.coordinates = coordinates;
        this.walls = walls;
    }

    public boolean[] getWalls() {
        return walls;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return "Case [walls=" + Arrays.toString(walls) + ", coordinates=" + coordinates + "]";
    }

    public String drawCell(int line) {
        final Object[] characterToDraw;
        switch (line) {
            // case 0:
            //     characterToDraw = new Object[] {VERTICAL_NO_WALL, Boolean.TRUE.equals(walls[0]) ? HORIZONTAL_WALL : HORIZONTAL_NO_WALL , VERTICAL_NO_WALL};
            //     break;
            // case 1:
            //     characterToDraw = new Object[] {Boolean.TRUE.equals(walls[3]) ? VERTICAL_WALL : VERTICAL_NO_WALL , HORIZONTAL_NO_WALL, Boolean.TRUE.equals(walls[1]) ? VERTICAL_WALL : VERTICAL_NO_WALL};
            //     break;
            // case 2:
            //     characterToDraw = new Object[] {VERTICAL_NO_WALL, Boolean.TRUE.equals(walls[2]) ? HORIZONTAL_WALL : HORIZONTAL_NO_WALL , VERTICAL_NO_WALL};
            //     break;
            // default:
            //     characterToDraw = new Object[] {VERTICAL_NO_WALL, HORIZONTAL_NO_WALL, VERTICAL_NO_WALL};
            //     break;
            case 0:
                characterToDraw = new Object[] {VERTICAL_NO_WALL, Boolean.TRUE.equals(walls[0]) ? HORIZONTAL_WALL : HORIZONTAL_NO_WALL};
                break;
            case 1:
                characterToDraw = new Object[] {Boolean.TRUE.equals(walls[3]) ? VERTICAL_WALL : VERTICAL_NO_WALL , HORIZONTAL_NO_WALL_CENTER};
                break;
            default:
                characterToDraw = new Object[] {VERTICAL_NO_WALL, HORIZONTAL_NO_WALL};
                break;
        }
        return String.format("%s%s", characterToDraw);
    }

    private int wallCounter() {
        int wallCounter = 0;
        for (boolean b : walls)
            if (b)
                wallCounter++;
        return wallCounter;
    }

    public Boolean isAnIntersection() {
        int wallCounter = wallCounter();
        return (wallCounter < 2);
    }

    public Boolean isAnImpasse() {
        int wallCounter = wallCounter();
        return (wallCounter > 2);
    }
}
