package com.isep.jbmo60927.game_components;

import com.isep.jbmo60927.enums.Wall;

public class Coordinates {

    private static final String DISPLAY_COORDINATES = "[%d, %d]";

    private final int x;
    private final int y;

    public Coordinates(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(Coordinates coordinates, Wall direction) {
        switch (direction) {
            case NORTH:
                this.x = coordinates.x - 1;
                this.y = coordinates.y;
                break;
            case SOUTH:
                this.x = coordinates.x + 1;
                this.y = coordinates.y;
                break;
            case EAST:
                this.x = coordinates.x;
                this.y = coordinates.y + 1;
                break;
            case WEST:
                this.x = coordinates.x;
                this.y = coordinates.y - 1;
                break;
            default:
                this.x = coordinates.x;
                this.y = coordinates.y;
                break;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    @Override
    public String toString() {
        return String.format(DISPLAY_COORDINATES, x, y);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Coordinates other = (Coordinates) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }
}
