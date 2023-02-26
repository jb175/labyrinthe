package com.isep.jbmo60927.game_components;

import java.util.Comparator;

public class PossibleMoveComparator implements Comparator<PossibleMove> {

    @Override
    public int compare(PossibleMove arg0, PossibleMove arg1) {
        if (arg0.getMinPathToSolution() == arg1.getMinPathToSolution())
            return 0;
        if (arg0.getMinPathToSolution() > arg1.getMinPathToSolution())
            return 1;
        else
            return -1;
    }
    
}
