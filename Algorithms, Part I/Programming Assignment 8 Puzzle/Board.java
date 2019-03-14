import edu.princeton.cs.algs4.Stack;
import java.util.Arrays;


public class Board {

    private final int[][] gameBoard;
    private Stack<Board> boards;
    private int hammingP;
    private int manhattanP;
    private int zeroI, zeroJ;

    public Board(int[][] blocks) {
        // initialize state and calculate priorities
        gameBoard = new int[blocks.length][blocks.length];

        for (int i = 0; i < blocks.length; ++i) {
            for (int j = 0; j < blocks.length; ++j) {
                gameBoard[i][j] = blocks[i][j];
                if (gameBoard[i][j] != 0) {
                    int x = calculateM(i, j);
                    if (x != 0) ++hammingP;
                    manhattanP += x;
                }
                else {
                    zeroI = i;
                    zeroJ = j;
                }
            }
        }

    }

    public int dimension() {
        return gameBoard.length;
    }

    public int hamming() {
        return hammingP;
    }

    public int manhattan() {
        return manhattanP;
    }

    public boolean isGoal() {
        return manhattanP == 0;
    }

    public Board twin() {
        int size = gameBoard.length;
        exch(gameBoard, (zeroI + 1) % size, (zeroJ) % size, (zeroI + 1) % size, (zeroJ + 1) % size);
        Board b = new Board(gameBoard);
        exch(gameBoard, (zeroI + 1) % size, (zeroJ) % size, (zeroI + 1) % size, (zeroJ + 1) % size);
        return b;
    }

    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
        if (y.getClass() != this.getClass()) return false;
        Board other = (Board) y;
        return Arrays.deepEquals(this.gameBoard, other.gameBoard);
    }

    public String toString() {
        int size = gameBoard.length;
        StringBuilder s = new StringBuilder();
        s.append(size + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s.append(String.format("%2d ", gameBoard[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public Iterable<Board> neighbors() {
        if (boards == null) {
            boards = new Stack<Board>();
            fillNeighbors();
        }
        return boards;
    }

    private void fillNeighbors() {
        if (!validate(zeroI - 1)) {
            exch(gameBoard, zeroI, zeroJ, zeroI - 1, zeroJ);
            boards.push(new Board(gameBoard));
            exch(gameBoard, zeroI, zeroJ, zeroI - 1, zeroJ);
        }

        if (!validate(zeroI + 1)) {
            exch(gameBoard, zeroI, zeroJ, zeroI + 1, zeroJ);
            boards.push(new Board(gameBoard));
            exch(gameBoard, zeroI, zeroJ, zeroI + 1, zeroJ);
        }

        if (!validate(zeroJ - 1)) {
            exch(gameBoard, zeroI, zeroJ, zeroI, zeroJ - 1);
            boards.push(new Board(gameBoard));
            exch(gameBoard, zeroI, zeroJ, zeroI, zeroJ - 1);
        }

        if (!validate(zeroJ + 1)) {
            exch(gameBoard, zeroI, zeroJ, zeroI, zeroJ + 1);
            boards.push(new Board(gameBoard));
            exch(gameBoard, zeroI, zeroJ, zeroI, zeroJ + 1);
        }
    }

    private int calculateM(int i, int j) {
        int number = gameBoard[i][j];
        int n = gameBoard.length;
        int correctI = 0, correctJ = 0;

        // calculate correct position
        if (number % n != 0) correctI = (number / n);
        else correctI = ((number - 1) / n);
        correctJ = (number - correctI*n) - 1;

        // return the difference
        return Math.abs(i - correctI) + Math.abs(j - correctJ);
    }

    private boolean validate(int x) {
        return (x < 0 || (x > gameBoard.length-1));
    }

    private void exch(int[][] board1, int i1, int j1, int i2, int j2) {
        int swap = board1[i1][j1];
        board1[i1][j1] = board1[i2][j2];
        board1[i2][j2] = swap;
    }

    public static void main(String[] args) {
        int[][] x = {
            {2, 1, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
        };

        int [][] y = {
            {2, 1, 3, 4},
            {6, 5, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
        };
        Board b = new Board(x);
        Board c = new Board(y);
        if (b.equals(c))System.out.println("yup");
    }
}

