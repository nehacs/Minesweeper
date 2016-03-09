package minesweeper.model;

import java.util.List;

/**
 * Representation of a cell on the minesweeper board.
 */
public class Cell {

  // Represents the index of the cell on the board.
  private Index index;

  // Enum that will be used to keep track of the status of the cell -- whether it is open or closed
  public enum Status {
    OPEN, CLOSED;
  }

  // Status of the current cell at any given point in time
  private Status status;

  // Whether this cell is a mine or not
  private boolean isMine;

  // Number of adjacent mines for this cell.
  private int numberOfAdjacentMines;

  // List of the adjacent cells for this cell.
  private List<Cell> adjacentCells;

  /**
   * Prints out the cell. If it is a mine, print "M". Otherwise prints out the nubmer of adjacent
   * mines. If there are none, then it prints "."
   */
  public void print() {
    if (this.isMine()) {
      System.out.print("M ");
    } else {
      if (this.getNumberOfAdjacentMines() > 0) {
        System.out.print(this.getNumberOfAdjacentMines() + " ");
      } else {
        System.out.print(". ");
      }
    }
  }

  //-------------------------------
  // Getters and Setters
  //-------------------------------
  public void setIndex(int i, int j) {
    this.index = new Index(i, j);
  }

  public Index getIndex() {
    return index;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public boolean isOpened() {
    return this.status == Status.OPEN;
  }

  // Make sure this is accessible only within the models and not outside.
  boolean isMine() {
    return isMine;
  }

  public void setIsMine(boolean isMine) {
    this.isMine = isMine;
  }

  public int getNumberOfAdjacentMines() {
    return numberOfAdjacentMines;
  }

  public void setNumberOfAdjacentMines(int numberOfAdjacentMines) {
    this.numberOfAdjacentMines = numberOfAdjacentMines;
  }

  public List<Cell> getAdjacentCells() {
    return adjacentCells;
  }

  public void setAdjacentCell(List<Cell> adjacentCells) {
    this.adjacentCells = adjacentCells;
  }
}
