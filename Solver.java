package minesweeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import minesweeper.model.Board;
import minesweeper.model.Cell;

/**
 * Run a solver for the Minesweeper game.
 * It creates random Minesweeper board with MATRIX_SIZE, NUMER_OF_MINES
 * and then runs the solver over them NUMBER_OF_RUNS times.
 * It outputs, the number of wins out of the NUMBER_OF_RUNS, win % and the time it took to run.
 */
public class Solver {

  // Constants associated with the runs
  private static final int NUMBER_OF_RUNS = 100000;
  private static final int MATRIX_SIZE = 10;
  private static final int NUMBER_OF_MINES = 10;

  public static void main(String[] args) {
    int numberOfWins = 0;
    System.out.println("Running minesweeper solver over " + NUMBER_OF_RUNS + " runs...");
    final long startTime = System.currentTimeMillis();
    for (int i=0; i<NUMBER_OF_RUNS; i++) {
      Board board = new Board(MATRIX_SIZE, NUMBER_OF_MINES);
      board.initBoard();

      if (Solver.solve(board)) {
        numberOfWins++;
      }
    }
    final long finishTime = System.currentTimeMillis();
    final double winRate = (numberOfWins*100.0)/NUMBER_OF_RUNS;
    final double timeTaken = (finishTime - startTime)/1000.0;

    System.out.println("Numer of wins out of " + NUMBER_OF_RUNS + ": " + numberOfWins);
    System.out.println("Win rate " + winRate + "%");
    System.out.println("Time taken for " + NUMBER_OF_RUNS + " runs: " + timeTaken + " s");
  }

  /**
   * Solves the given board and returns true or false based on whether we won or lost.
   * @param board
   * @return true, if we win, false if we lose.
   */
  public static boolean solve(Board board) {
    boolean win = false;

    Queue<Cell> choosenCells = new LinkedList<Cell>();

    // Start with a random cell from all the unopened ones.
    Random randomGenerator = new Random();
    int randomInt = randomGenerator.nextInt(board.getUnopenedCells().size());

    // We maintain a queue of the choosen cells that we want to open.
    // This queue will get populated using the chooseCells() method.
    // When this queue becomes empty that means we do not have anything to click on any more,
    // i.e. the board is solved.
    choosenCells.add(board.getUnopenedCells().get(randomInt));
    while(!choosenCells.isEmpty()) {
      Cell cell = choosenCells.poll();
      if (!Board.openCell(board, cell)) {
        // openCell() returns false if we stepped on a mine, that means we lost.
        // BOOM, you lose!
        break;
      } else {
        choosenCells.addAll(chooseCells(board));
        // If we found nothing new to add, that means we won.
        if (choosenCells.isEmpty()) {
          // You win!
          win = true;
        }
      }
    }

    return win;
  }

  //-------------------------------
  // Helper methods
  //-------------------------------

  /**
   * Chooses which cells to click on next. It returns a list of cells that it thinks are
   * safe to click on. If no cells are found safe, it returns a random cell from among the
   * unopened ones. If it returns no cells, that means the board is solved.
   *
   * Note: Way to flag mines: If we suspect a cell to be a mine, we remove it from the list of unopened
   * cells. So all cells left unopened at the end, means those are the mines.
   *
   * Logic for picking a new cell:
   * Each time, look at only unopened cells so far.
   * Calculate the number of mines already flagged adjacent to this cell = X
   * Calculate the number of adjacent cells left unopened = Y
   *
   * If this cells's numberOfAdjMines == X, then return all Y as safe cells to open.
   * (because this means that we have already correctly flagged possible mines for this cell. So anything
   * left unopened in adjacent list can be safely opened)
   * If this cell's numberAdjMines == X + Y, then all Y are mines (because that is the only
   * way that numberOfAdjMines can be true). So we flag all Y as mines.
   *
   * @param board
   * @return list of cells that might be safe to click on, or random cell from the list of
   *         unopened cells so far.
   */
  private static List<Cell> chooseCells(Board board) {
    for (int i=0; i<board.getMatrixSize(); i++) {
      for (int j=0; j<board.getMatrixSize(); j++) {
        Cell cell = board.getCells()[i][j];

        // If the cell is already opened, it will not get picked to be opened again. Skip it.
        if (cell.isOpened()) {
          continue;
        }

        List<Cell> unopenedAdjacentCells = new ArrayList<Cell>();
        int numberAdjacentFlaggedMines = 0;
        for (Cell adjCell : cell.getAdjacentCells()) {
          if (!adjCell.isOpened()) {
            if (board.getUnopenedCells().contains(adjCell)) {
              unopenedAdjacentCells.add(adjCell);
            } else {
              numberAdjacentFlaggedMines++;
            }
          }
        }

        if (cell.getNumberOfAdjacentMines() != 0 &&
            cell.getNumberOfAdjacentMines() == numberAdjacentFlaggedMines) {
          return unopenedAdjacentCells;
        }

        if (cell.getNumberOfAdjacentMines() ==
            (unopenedAdjacentCells.size() + numberAdjacentFlaggedMines)) {
          board.getUnopenedCells().removeAll(unopenedAdjacentCells);
        }
      }
    }

    // If we did not yet return with a list of safe cells, it means we need to pick one at random
    // from the list of the boards unopened cells. Run a random number generator over the size of
    // the unopened cells so far and return a cell at random.
    // Note: We do not need to worry about cells we are aware are mines (flagged cells), because
    // we remove them from the list of unopened cells whenever we are sure they are a mine.
    if (board.getUnopenedCells().size() > 0) {
      Random randomGenerator = new Random();
      int randomInt = randomGenerator.nextInt(board.getUnopenedCells().size());
      Cell cell = board.getUnopenedCells().get(randomInt);
      return Arrays.asList(cell);
    } else {
      return new ArrayList<Cell>();
    }
  }
}
