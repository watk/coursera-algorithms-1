import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    // Store the tiles as chars to save space.
    private final char[][] tiles;
    private final int n;
    private final Pos blankPos;
    private final int hamming;
    private final int manhattan;

    // Create a board by taking a copy of the given tiles.
    public Board(int[][] tiles) {
        this(toCharArray(tiles));
    }

    private Board(char[][] tiles) {
        this.tiles = tiles;
        this.n = tiles.length;

        int hamming = 0;
        int manhattan = 0;
        Pos blankPos = null;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (tiles[r][c] == 0) {
                    blankPos = new Pos(r, c);
                    continue;
                }

                if (tiles[r][c] != r * n + c + 1) {
                    int expectedRow = (tiles[r][c] - 1) / n;
                    int expectedCol = (tiles[r][c] - 1) % n;
                    manhattan += Math.abs(r - expectedRow) + Math.abs(c - expectedCol);
                    hamming++;
                }
            }
        }

        this.blankPos = blankPos;
        this.hamming = hamming;
        this.manhattan = manhattan;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                s.append(String.format("%2d ", (int) tiles[r][c]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // Returns the board dimension |n|.
    public int dimension() {
        return n;
    }

    // Returns the number of tiles out of place.
    public int hamming() {
        return hamming;
    }

    // Returns the sum of the Manhattan distances between each tiles and its goal.
    public int manhattan() {
        return manhattan;
    }

    // Returns whether this board is the goal board.
    public boolean isGoal() {
        return hamming == 0;
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Board board = (Board) other;
        return Arrays.deepEquals(tiles, board.tiles);
    }

    // Returns all neighboring boards.
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();
        for (Pos p : tilesNeighboringBlank())
            neighbors.enqueue(newBoardWithTilesSwapped(blankPos, p));
        return neighbors;
    }

    // Returns a board that is obtained by exchanging any pair of tiles.
    public Board twin() {
        // Use the tiles neighboring the blank because we know there are at least two.
        Iterator<Pos> adjacents = tilesNeighboringBlank().iterator();
        return newBoardWithTilesSwapped(adjacents.next(), adjacents.next());
    }

    private Board newBoardWithTilesSwapped(Pos t1, Pos t2) {
        char[][] newTiles = Arrays.stream(tiles).map(char[]::clone).toArray(char[][]::new);
        char tmp = newTiles[t1.row][t1.col];
        newTiles[t1.row][t1.col] = newTiles[t2.row][t2.col];
        newTiles[t2.row][t2.col] = tmp;
        return new Board(newTiles);
    }

    private Iterable<Pos> tilesNeighboringBlank() {
        return Stream.of(blankPos.up(), blankPos.down(), blankPos.left(), blankPos.right())
                     .filter(p -> p.row < n && p.col < n && p.row >= 0 && p.col >= 0)
                     .collect(Collectors.toList());
    }

    private class Pos {
        private final int row;
        private final int col;

        Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Pos up() {
            return new Pos(row - 1, col);
        }

        public Pos down() {
            return new Pos(row + 1, col);
        }

        public Pos left() {
            return new Pos(row, col - 1);
        }

        public Pos right() {
            return new Pos(row, col + 1);
        }
    }

    private static char[][] toCharArray(int[][] a) {
        final int n = a.length;
        char[][] copy = new char[n][n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                copy[r][c] = (char) a[r][c];
            }
        }
        return copy;
    }
}

