package com.isep.jbmo60927.game_components;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.isep.jbmo60927.enums.Wall;

public class Labyrinthe {
    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(Labyrinthe.class.getName());

    private static final int[][] labScheme = new int[][] {
        new int[] {13, 5, 3, 9, 7},
        new int[] {13, 1, 4, 6, 11},
        new int[] {11, 12, 1, 5, 2},
        new int[] {10, 13, 4, 1, 2},
        new int[] {12, 5, 5, 6, 14}
    };
    private static final int LINE_PER_CELL = 2;
    public static final Coordinates BEGINING = new Coordinates(4, 0);
    public static final Coordinates OBJECTIVE = new Coordinates(0, 4);

    private final Cell[][] lab = new Cell[labScheme.length][labScheme[0].length];

    public Labyrinthe() {
        LOGGER.setLevel(Level.INFO);
        labyrintheBuilder();
        LOGGER.log(Level.INFO, "test");
    }

    private void labyrintheBuilder() {
        for (int i = 0; i < lab.length; i++) {
            for (int j = 0; j < lab[0].length; j++) {
                boolean[] walls = new boolean[4];
                for (int k = 0; k < 4; k++) {
                    walls[k] = (((int)(labScheme[i][j]/(Math.pow(2, k)))%2) == 1);
                }
                lab[i][j] = new Cell(new Coordinates(i, j), walls);
            }
        }
    }

    public Cell getCell(Coordinates coordinates) {
        return lab[coordinates.getX()][coordinates.getY()];
    }

    public Cell getCell(Cell cellToExplore, Wall direction) {
        return getCell(new Coordinates(cellToExplore.getCoordinates(), direction));
    }

    public Cell[] getAdjacentCells(Cell cell) {
        ArrayList<Cell> adjacentCells = new ArrayList<>();
        for (int index = 0; index < cell.getWalls().length; index++)
            if (!cell.getWalls()[index])
                adjacentCells.add(getCell(cell, Wall.values()[index]));
        return adjacentCells.toArray(new Cell[adjacentCells.size()]);
    }

    public static int getDistanceToObjective(Coordinates coordinates) {
        return Math.abs(OBJECTIVE.getX() - coordinates.getX()) + Math.abs(OBJECTIVE.getY() - coordinates.getY());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Cell[] cells : lab) {
            for (int i = 0; i < LINE_PER_CELL; i++) {
                if (i == 1) {
                    for (Cell cell : cells) {
                        String inCell = " ";
                        //inCell = Integer.toString(Labyrinthe.getDistanceToObjective(cell.getCoordinates()));
                        if (cell.getCoordinates().equals(BEGINING))
                            inCell = "O";
                        if (cell.getCoordinates().equals(OBJECTIVE))
                            inCell = "X";
                        stringBuilder.append(String.format(cell.drawCell(i), inCell));
                    }
                    stringBuilder.append(Cell.VERTICAL_WALL);
                } else {
                    for (Cell cell : cells) {
                        stringBuilder.append(cell.drawCell(i));
                    }
                }
                stringBuilder.append("\n");
            }
        }
        for (int i = 0; i < lab[0].length; i++) {
            stringBuilder.append(Cell.VERTICAL_NO_WALL);
            stringBuilder.append(Cell.HORIZONTAL_WALL);
        }
        return stringBuilder.toString();
    }

    public String toString(PossibleMove moveDone) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Cell[] cells : lab) {
            for (int i = 0; i < LINE_PER_CELL; i++) {
                if (i == 1) {
                    for (Cell cell : cells) {
                        String inCell = " ";
                        //inCell = Integer.toString(Labyrinthe.getDistanceToObjective(cell.getCoordinates()));
                        for (Cell cellHistory : moveDone.getHistory())
                            if (cell.equals(cellHistory))
                                inCell = "-";
                        if (cell.equals(moveDone.getToVisit()))
                            inCell = "+";
                        if (cell.getCoordinates().equals(BEGINING))
                            inCell = "O";
                        if (cell.getCoordinates().equals(OBJECTIVE))
                            inCell = "X";
                        stringBuilder.append(String.format(cell.drawCell(i), inCell));
                    }
                    stringBuilder.append(Cell.VERTICAL_WALL);
                } else {
                    for (Cell cell : cells) {
                        stringBuilder.append(cell.drawCell(i));
                    }
                }
                stringBuilder.append("\n");
            }
        }
        for (int i = 0; i < lab[0].length; i++) {
            stringBuilder.append(Cell.VERTICAL_NO_WALL);
            stringBuilder.append(Cell.HORIZONTAL_WALL);
        }
        return stringBuilder.toString();
    }
}
