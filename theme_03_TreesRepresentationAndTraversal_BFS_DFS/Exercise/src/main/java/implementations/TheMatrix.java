package implementations;

import java.util.ArrayDeque;
import java.util.Deque;

public class TheMatrix {
    private char[][] matrix;
    private char fillChar;
    private char startChar;
    private int startRow;
    private int startCol;

    public TheMatrix(char[][] matrix, char fillChar, int startRow, int startCol) {
        this.matrix = matrix;
        this.fillChar = fillChar;
        this.startRow = startRow;
        this.startCol = startCol;
        this.startChar = this.matrix[this.startRow][this.startCol];
    }

    public void solve() {
        Deque<int[]> coordinates = new ArrayDeque<>();

        coordinates.offer(new int[]{startRow, startCol});

        while (!coordinates.isEmpty()) {
            int[] position = coordinates.poll();

            int row = position[0];
            int col = position[1];

            this.matrix[row][col] = this.fillChar;

            if (isInBounds(row + 1, col) && this.matrix[row + 1][col] == this.startChar) {
                coordinates.offer(new int[]{row + 1, col});
            }

            if (isInBounds(row, col + 1) && this.matrix[row][col + 1] == this.startChar) {
                coordinates.offer(new int[]{row, col + 1});
            }

            if (isInBounds(row -1, col) && this.matrix[row - 1][col] == this.startChar) {
                coordinates.offer(new int[]{row - 1, col});
            }

            if (isInBounds(row, col - 1) && this.matrix[row][col - 1] == this.startChar) {
                coordinates.offer(new int[]{row, col - 1});
            }
        }
    }

    private boolean isInBounds(int row, int col) {
        return !isOutOfBounds(row, col);
    }

//    public void solve() {
//        fillMatrix(this.startRow, this.startCol);
//    }
//
//    private void fillMatrix(int row, int col) {
//
//        if (isOutOfBounds(row, col) || this.matrix[row][col] != startChar){
//            return;
//        }
//        this.matrix[row][col] = fillChar;
//
//        this.fillMatrix(row + 1, col);
//        this.fillMatrix(row, col + 1);
//        this.fillMatrix(row - 1, col);
//        this.fillMatrix(row, col - 1);
//    }

    private boolean isOutOfBounds(int r, int c) {
        return r < 0 || r >= this.matrix.length || c < 0 || c >= this.matrix[r].length;
    }

    public String toOutputString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < this.matrix.length; row++) {
            for (int col = 0; col < this.matrix[row].length; col++) {
                builder.append(this.matrix[row][col]);
            }
            builder.append(System.lineSeparator());
        }

        return builder.toString().trim();
    }
}
