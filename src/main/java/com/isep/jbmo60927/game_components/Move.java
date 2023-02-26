package com.isep.jbmo60927.game_components;

public class Move {

    protected final Cell[] history;

    public Move(Cell[] history) {
        this.history = history;
    }

    public Cell[] getHistory() {
        return history;
    }
}
