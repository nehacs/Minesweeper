package minesweeper.model;

/**
 * Represents the index of a cell on the minesweeper board
 */
public class Index {

  // Row index of a cell in a 2-d matrix[i][j]
  private int i;

  // Column index of a cell in a 2-d matrix[i][j]
  private int j;

  public Index(int i, int j) {
    this.i = i;
    this.j = j;
  }

  //-------------------------------
  // Getters
  //-------------------------------
  public int i() {
    return i;
  }

  public int j() {
    return j;
  }
}
