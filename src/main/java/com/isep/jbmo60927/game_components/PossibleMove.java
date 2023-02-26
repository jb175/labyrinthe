package com.isep.jbmo60927.game_components;

public class PossibleMove extends Move {

    private final Cell toVisit;

    public PossibleMove(Cell toVisit, Cell[] history) {
        super(history);
        this.toVisit = toVisit;
    }
    
    public int getMinPathToSolution() {
        return this.history.length + Labyrinthe.getDistanceToObjective(this.toVisit.getCoordinates());
    }

    public Cell getToVisit() {
        return toVisit;
    }
}
