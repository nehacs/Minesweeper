package minesweeper;

import java.util.Scanner;

import minesweeper.model.Board;

/**
 * Run the minesweeper game. It will ask you for 2 pieces of input,
 * 1. Matrix size N (for a NxN board)
 * 2. Number of mines to be places on the board.
 * such that number of mines <= N
 *
 * After that it will ask for your input as a 0-based index in the 2-d board
 * for the cell that the user wants to open. Mines are left unopened. When
 * the number of unopened cells is the same as the number of wines, you are declared
 * a winner. If you step on a mine you are declared a loser.
 */
public class Minesweeper {
  public static void main(String[] args) {

    Scanner reader = new Scanner(System.in);  // Reading from System.in
    System.out.print("Enter (NxN) board size where N>1: ");
    int matrixSize = reader.nextInt();
    System.out.print("Enter number of mines < N (board size): ");
    int numberOfMines = reader.nextInt();
    if (numberOfMines > matrixSize) {
      System.out.println("Incorrect number of mines. Number of mines should not be greater than " + matrixSize);
      System.exit(1);
    }

    if (matrixSize < 2) {
      System.out.println("Matrix size should be > 1: " + matrixSize);
      System.exit(1);
    }

    if (numberOfMines == 0) {
      System.out.println("Number of mines should be > 0: " + numberOfMines);
      System.exit(1);
    }

    Board board = new Board(matrixSize, numberOfMines);
    board.initBoard();

    System.out.println("To play the game, enter an i,j 0-based index of the cell that you want to open");
    boolean solved = false;
    final long startTime = System.currentTimeMillis();
    while(!solved) {
      board.print();
      System.out.println();
      System.out.print("i: ");
      int i = reader.nextInt();
      System.out.print("j: ");
      int j = reader.nextInt();

      // If the indices chosen fall outside of the matrixSize, then inform the user and continue.
      if (i < 0 || i > (matrixSize-1) || j < 0 || j > (matrixSize-1)) {
        System.out.println("The indices fall outside of the matrix. Please pick another one.");
        continue;
      }

      // If we have already opened up this cell, then inform the user and continue.
      if (!board.getUnopenedCells().contains(board.getCells()[i][j])) {
        System.out.println("This cell has already been opened. Please pick another one.");
        continue;
      }

      // Open the cell based on the i, j coordinate given by the user.
      if (!Board.openCell(board, board.getCells()[i][j])) {
        // We stepped on a mine, we lose. But board is solved.
        solved = true;
        System.out.println("BOOM! You lose!! Board solution:");
      } else if (board.getUnopenedCells().size() == board.getNumberOfMines()) {
        // We have opened all cells, except the mines, we win. Board is solved.
        final long finishTime = System.currentTimeMillis();
        final double timeTaken = (finishTime - startTime)/1000.0;
        solved = true;
        System.out.println("You win!! Solved in " + timeTaken + "s.");
      }
    }
    board.printSolution();
    reader.close();
    System.exit(0);
  }
}
