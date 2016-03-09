package minesweeper.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Representation of a Board for Minesweeper.
 */
public class Board {
  // The cells on the minesweeper board, represented as a 2-d array.
  private Cell[][] cells;

  // The matrix size of the board.
  private int matrixSize;

  // The number of mines on the board.
  private int numberOfMines;

  // The locations of the mines on the board. These are stored as a flattened list of indices,
  // such that (i*matrixSize)+j = mine location index.
  private HashSet<Integer> mineLocations;

  // List of the unopened cells. This will be used in keeping track of the board state while solving.
  List<Cell> unopenedCells = new ArrayList<Cell>();

  // List of the opened cells. This will be used in keeping track of the board state while solving.
  List<Cell> openedCells = new ArrayList<Cell>();

  public Board(int matrixSize, int numberOfMines) {
    this.matrixSize = matrixSize;
    this.numberOfMines = numberOfMines;
    this.unopenedCells = new ArrayList<Cell>();
    this.openedCells = new ArrayList<Cell>();
    this.cells = new Cell[matrixSize][matrixSize];
  }

  /**
   * Initializes the board. Does 2 things:
   * 1. Generates random mine locations and places them on the board.
   * 2. Sets up the cells with the appropriate values on their status, mine status, number of
   *    adjacent mines etc.
   */
  public void initBoard() {
    this.mineLocations = generateMineLocations();

    initializeCells();
  }

  /**
   * Prints out the board in its current state.
   */
  public void print() {
    for (int i=0; i<getMatrixSize(); i++) {
      for (int j=0; j<getMatrixSize(); j++) {
        if (unopenedCells.contains(cells[i][j])) {
          System.out.print("x ");
        } else {
          cells[i][j].print();
        }
      }
      System.out.println();
    }
  }

  public void printSolution() {
    for (int i=0; i<getMatrixSize(); i++) {
      for (int j=0; j<getMatrixSize(); j++) {
        cells[i][j].print();
      }
      System.out.println();
    }
  }

  /**
   * Opens the cell in the given board. It will also open up all adjacent spaces that are empty.
   * It will mark the cell (or cells) as having status open. It will also add them to the opened
   * cells array and remove them from the unopened cells array.
   * If the cell picked happens to be a mine, it will return false and signify loss of game.
   * If not, it will return true.
   * @param board
   * @param cell
   * @return false if the cell was a mine, true otherwise.
   */
  public static boolean openCell(Board board, Cell cell) {
    // Blew up a mine. You lose!
    if (cell.isMine()) {
      return false;
    }

    // Maintain a queue of the cells to open up.
    Queue<Cell> allCellsToOpen = new LinkedList<Cell>();
    allCellsToOpen.add(cell);
    while (!allCellsToOpen.isEmpty()) {
      Cell cellToOpen= allCellsToOpen.poll();

      // Open the cell
      cellToOpen.setStatus(Cell.Status.OPEN);
      board.openedCells.add(cellToOpen);
      board.unopenedCells.remove(cellToOpen);

      // Go through all the adjacentCells
      for (Cell adjCell : cellToOpen.getAdjacentCells()) {
        // If the adjCell is not yet opened and it is not a mine then either:
        if (!adjCell.isOpened() && !allCellsToOpen.contains(adjCell) && !adjCell.isMine()) {
          // 1. If it has no neighboring mines, add it to the queue of cells the board will need to open OR
          if (adjCell.getNumberOfAdjacentMines() == 0) {
            allCellsToOpen.add(adjCell);
          } else {

            // 2. If the current cell has no neighboring mines, then just open the adjCell,
            //    instead of adding it to the queue.
            if (cellToOpen.getNumberOfAdjacentMines() == 0) {
              adjCell.setStatus(Cell.Status.OPEN);
              board.openedCells.add(adjCell);
              board.unopenedCells.remove(adjCell);
            }
          }
        }
      }
    }
    return true;
  }
  //-------------------------------
  // Getters and Setters
  //-------------------------------
  public int getMatrixSize() {
    return matrixSize;
  }

  public int getNumberOfMines() {
    return numberOfMines;
  }

  public Cell[][] getCells() {
    return cells;
  }

  public List<Cell> getUnopenedCells() {
    return unopenedCells;
  }

  public List<Cell> getOpenedCells() {
    return openedCells;
  }
  //-------------------------------
  // Helper methods
  //-------------------------------

  /**
   * Generates the mine locations using a random number generator.
   * The locations are generated as an integer value and correspond to a cell's flattened position
   * in the 2-d board. For e.g. in a 2-d array with [i][j] indices, the flattened position
   * would be (i*matrixSize) + j
   * @return list of mine locations as integers
   */
  private HashSet<Integer> generateMineLocations() {
    HashSet<Integer> mineLocations = new HashSet<Integer>();
    // Generate the random placements of the mines.
    Random randomGenerator = new Random();
    // Number of mines to generate = numberOfMines
    for (int idx = 1; idx <= numberOfMines; ++idx){
      // Will generate an integer between 0..NxN, where N=matrixSize
      int randomInt = randomGenerator.nextInt(matrixSize*matrixSize);
      while (mineLocations.contains(randomInt)) {
        randomInt = randomGenerator.nextInt(matrixSize*matrixSize);
      }
      mineLocations.add(randomInt);
    }
    return mineLocations;
  }

  /**
   * Initialize the cells. Does the following things:
   * 1. Create a new cell
   * 2. Set the Index on it
   * 3. Set initial status as CLOSED
   * 4. Look up mine locations and if it is in a minelocation, set it to be a mine.
   * 5. Add each cell to the unopened cells list.
   * 6. Set the list of the adjacent cells for each cell.
   * 7. Set the number of adjacent mines on each cell.
   * 8. Add the cell to the board.
   */
  private void initializeCells() {
    for (int i=0; i<matrixSize; i++) {
      for (int j=0; j<matrixSize; j++) {
        Cell cell = new Cell();
        cell.setIndex(i, j);
        cell.setStatus(Cell.Status.CLOSED);
        if (mineLocations.contains(i*matrixSize + j)) {
          cell.setIsMine(true);
        } else {
          cell.setIsMine(false);
        }
        cells[i][j] = cell;
        unopenedCells.add(cell);
      }
    }

    // Set the values of the adjacent number of mines
    for (int i=0; i<matrixSize; i++) {
      for (int j=0; j<matrixSize; j++) {
        setAdjacentCells(cells[i][j]);
        setAdjacentMines(cells[i][j]);
      }
    }
  }

  /**
   * Set list of adjacent cells on each cell.
   * @param cell
   */
  private void setAdjacentCells(Cell cell) {
    List<Cell> adjacentCells = new ArrayList<Cell>();
    for(int i=-1; i<=1; i++) {
      for(int j=-1; j<=1; j++) {
        if(i == 0 && j == 0) {
          continue;
        }
        int x = cell.getIndex().i();
        int y = cell.getIndex().j();
        if(i + x >= 0 && i + x < getMatrixSize() && j + y >= 0 && j + y < getMatrixSize()) {
          adjacentCells.add(this.getCells()[i+x][j+y]);
        }
      }
    }
    cell.setAdjacentCell(adjacentCells);
  }

  /**
   * Looks up the adjacent cells for a cell and sets the number of adjacentMines for it based on that.
   * @param cell
   */
  private void setAdjacentMines(Cell cell) {
    int adjacentMines = 0;

    for (Cell adjacentCell : cell.getAdjacentCells()) {
      if (adjacentCell.isMine()) {
        adjacentMines++;
      }
    }
    cell.setNumberOfAdjacentMines(adjacentMines);
  }
}
