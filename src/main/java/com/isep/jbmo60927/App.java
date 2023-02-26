package com.isep.jbmo60927;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.isep.jbmo60927.enums.IAAlgorithms;
import com.isep.jbmo60927.game_components.Cell;
import com.isep.jbmo60927.game_components.Labyrinthe;
import com.isep.jbmo60927.game_components.PossibleMove;
import com.isep.jbmo60927.game_components.PossibleMoveComparator;
import com.isep.jbmo60927.logger.MyLogger;


public class App {

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private static final Boolean DISPLAY_SEARCH = true;
    private static final IAAlgorithms[] POSSIBLE_ALGORITHMS = {
        IAAlgorithms.DEEP_SEARCH,
        IAAlgorithms.BREATH_SEARCH,
        IAAlgorithms.A_PLUS_SEARCH
    };

    private final Labyrinthe labyrinthe = new Labyrinthe();
    private ArrayList<PossibleMove> toVisit = new ArrayList<>();
    private ArrayList<Cell> alreadyVisited = new ArrayList<>();
    private int counter = 0;
    private Scanner sc = new Scanner(System.in);

    /**
     * Start the app
     */
    public App() {
        LOGGER.setLevel(Level.INFO);
        
        toVisit.add(new PossibleMove(labyrinthe.getCell(Labyrinthe.BEGINING), new Cell[0]));

        StringBuilder stBuilder = new StringBuilder("which algorithm do you want to use?\n");
        for (int index = 0; index < POSSIBLE_ALGORITHMS.length; index++) {
            stBuilder.append(String.format("%d/ %s%n", index, POSSIBLE_ALGORITHMS[index].toString()));
        }
        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.log(Level.INFO, stBuilder.toString());
        
        int selectedAlgorithm = sc.nextInt();

        if (selectedAlgorithm >= 0 && selectedAlgorithm < POSSIBLE_ALGORITHMS.length) {
            switch (POSSIBLE_ALGORITHMS[selectedAlgorithm]) {
                case A_PLUS_SEARCH:
                    aPlusSearch();
                    break;
                case DEEP_SEARCH:
                    deepSearch();
                    break;
                case BREATH_SEARCH:
                    breathSearch();
                    break;
                default:
                    LOGGER.log(Level.SEVERE, "the selectedd algorithm is not finished");
                    break;
            }
        }

        //display the result
        if (LOGGER.isLoggable(Level.INFO)) {
            if (toVisit.isEmpty())
                LOGGER.log(Level.INFO, "no solution found");
            else
                LOGGER.log(Level.INFO, String.format("solution found in %d moves", counter));
        }
    }

    private void deepSearch() {
        while (!toVisit.isEmpty()) {
            counter++;

            //extract the first cell to visit
            PossibleMove move = toVisit.remove(0);

            //we add this cell to the array of already visited cells
            alreadyVisited.add(move.getToVisit());

            //if it is not an impass 
            if (Boolean.FALSE.equals(move.getToVisit().isAnImpasse())) {
                ArrayList<Cell> newCells = new ArrayList<>();
                Collections.addAll(newCells, labyrinthe.getAdjacentCells(move.getToVisit()));
                ArrayList<PossibleMove> toVisitBuilder = new ArrayList<>();

                //for every accessible cell from this cell
                for (Cell cell : newCells) {
                    if (move.getHistory().length == 0)
                        toVisit.add(new PossibleMove(cell, new Cell[] {move.getToVisit()}));
                    else {
                        for (int index = 0; index < move.getHistory().length; index++) {
                            if (cell.equals(move.getHistory()[index]))
                                break;

                            //if we have never been here in this history (to prevent loop and go back)
                            if (index == move.getHistory().length-1 && !alreadyVisited.contains(cell)) {
                                ArrayList<Cell> history = new ArrayList<>();
                                Collections.addAll(history, move.getHistory());
                                history.add(move.getToVisit());
                                PossibleMove moveToVisit = new PossibleMove(cell, history.toArray(new Cell[history.size()]));
                                toVisitBuilder.add(moveToVisit);
                            }
                        }
                    }
                }

                Collections.addAll(toVisitBuilder, toVisit.toArray(new PossibleMove[toVisit.size()]));
                toVisit.clear();
                Collections.addAll(toVisit, toVisitBuilder.toArray(new PossibleMove[toVisitBuilder.size()]));
            }

            //display the labyrinthe
            if (Boolean.TRUE.equals(DISPLAY_SEARCH) && LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, String.format("move number %d%n%s", counter, this.labyrinthe.toString(move)));

            //objective found (we finish here because just before to find the objective the min path to the objective is the path we will find. There can be same size path but not shortest)
            if (Labyrinthe.OBJECTIVE.equals(move.getToVisit().getCoordinates()))
                break;
        }
    }

    private void breathSearch() {
        while (!toVisit.isEmpty()) {
            counter++;

            //extract the first cell to visit
            PossibleMove move = toVisit.remove(0);

            //we add this cell to the array of already visited cells
            alreadyVisited.add(move.getToVisit());

            //if it is not an impass 
            if (Boolean.FALSE.equals(move.getToVisit().isAnImpasse())) {
                ArrayList<Cell> newCells = new ArrayList<>();
                Collections.addAll(newCells, labyrinthe.getAdjacentCells(move.getToVisit()));

                //for every accessible cell from this cell
                for (Cell cell : newCells) {
                    if (move.getHistory().length == 0)
                        toVisit.add(new PossibleMove(cell, new Cell[] {move.getToVisit()}));
                    else {
                        for (int index = 0; index < move.getHistory().length; index++) {
                            if (cell.equals(move.getHistory()[index]))
                                break;

                            //if we have never been here in this history (to prevent loop and go back)
                            if (index == move.getHistory().length-1 && !alreadyVisited.contains(cell)) {
                                ArrayList<Cell> history = new ArrayList<>();
                                Collections.addAll(history, move.getHistory());
                                history.add(move.getToVisit());
                                PossibleMove moveToVisit = new PossibleMove(cell, history.toArray(new Cell[history.size()]));
                                toVisit.add(moveToVisit);
                            }
                        }
                    }
                }
            }

            //display the labyrinthe
            if (Boolean.TRUE.equals(DISPLAY_SEARCH) && LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, String.format("move number %d%n%s", counter, this.labyrinthe.toString(move)));

            //objective found (we finish here because just before to find the objective the min path to the objective is the path we will find. There can be same size path but not shortest)
            if (Labyrinthe.OBJECTIVE.equals(move.getToVisit().getCoordinates()))
                break;
        }
    }

    private void aPlusSearch() {
        while (!toVisit.isEmpty()) {
            counter++;

            //sort new node
            toVisit.sort(new PossibleMoveComparator());

            //extract the first cell to visit
            PossibleMove move = toVisit.remove(0);

            //we add this cell to the array of already visited cells
            alreadyVisited.add(move.getToVisit());

            //if it is not an impass 
            if (Boolean.FALSE.equals(move.getToVisit().isAnImpasse())) {
                ArrayList<Cell> newCells = new ArrayList<>();
                Collections.addAll(newCells, labyrinthe.getAdjacentCells(move.getToVisit()));

                //for every accessible cell from this cell
                for (Cell cell : newCells) {
                    if (move.getHistory().length == 0)
                        toVisit.add(new PossibleMove(cell, new Cell[] {move.getToVisit()}));
                    else {
                        for (int index = 0; index < move.getHistory().length; index++) {
                            if (cell.equals(move.getHistory()[index]))
                                break;

                            //if we have never been here in this history (to prevent loop and go back)
                            if (index == move.getHistory().length-1 && !alreadyVisited.contains(cell)) {
                                ArrayList<Cell> history = new ArrayList<>();
                                Collections.addAll(history, move.getHistory());
                                history.add(move.getToVisit());
                                PossibleMove moveToVisit = new PossibleMove(cell, history.toArray(new Cell[history.size()]));
                                toVisit.add(moveToVisit);
                            }
                        }
                    }
                }
            }

            //display the labyrinthe
            if (Boolean.TRUE.equals(DISPLAY_SEARCH) && LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, String.format("move number %d%n%s", counter, this.labyrinthe.toString(move)));

            //objective found (we finish here because just before to find the objective the min path to the objective is the path we will find. There can be same size path but not shortest)
            if (Labyrinthe.OBJECTIVE.equals(move.getToVisit().getCoordinates()))
                break;
        }
    }

    
    /** 
     * main function
     * @param args args given at start
     * @throws IOException error that can be thrown if the log file can't be write
     */
    public static void main( String[] args ) throws IOException {
        MyLogger.setup(); //setup the logger for the app
        new App();
    }
}
