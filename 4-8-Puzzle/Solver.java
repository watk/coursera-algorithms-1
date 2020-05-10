import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class Solver {
    private final Board[] solution;

    // Finds a solution to the initial board (using the A* algorithm).
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("board must be non-null");

        Comparator<SearchNode> cmp = Comparator.comparingInt(p -> p.priority);
        MinPQ<SearchNode> movesFromInitial = new MinPQ<>(cmp);
        MinPQ<SearchNode> movesFromTwin = new MinPQ<>(cmp);
        movesFromInitial.insert(new SearchNode(initial, null, 0));
        movesFromTwin.insert(new SearchNode(initial.twin(), null, 0));

        while (true) {
            SearchNode initialSolution = makeOneMove(movesFromInitial);
            // If the solution is found, populate |solution| in reverse by walking
            // the parent pointers.
            if (initialSolution != null) {
                solution = new Board[initialSolution.numMovesMade + 1];
                for (int i = solution.length - 1; i >= 0; i--) {
                    solution[i] = initialSolution.board;
                    initialSolution = initialSolution.parent;
                }
                return;
            }

            // If the twin is solved then the initial board is unsolvable.
            if (makeOneMove(movesFromTwin) != null) {
                solution = null;
                return;
            }
        }
    }

    // Makes one move in A* and returns the goal node if found and null otherwise.
    private SearchNode makeOneMove(MinPQ<SearchNode> pq) {
        SearchNode node = pq.delMin();
        if (node.board.isGoal())
            return node;

        for (Board neighbor : node.board.neighbors()) {
            if (node.parent == null || !neighbor.equals(node.parent.board))
                pq.insert(new SearchNode(neighbor, node, node.numMovesMade + 1));
        }

        return null;
    }

    // Whether the initial board is solvable.
    public boolean isSolvable() {
        return solution != null;
    }

    // The minimum number of moves to solve the initial board.
    public int moves() {
        return solution != null ? solution.length - 1 : -1;
    }

    // The sequence of boards in a shortest solution.
    public Iterable<Board> solution() {
        return solution == null ? null : Arrays.asList(solution);
    }

    private class SearchNode {
        final Board board;
        final SearchNode parent;
        final int numMovesMade;
        final int priority;

        private SearchNode(Board board, SearchNode parentNode, int numMovesMade) {
            this.board = board;
            this.parent = parentNode;
            this.numMovesMade = numMovesMade;
            this.priority = numMovesMade + board.manhattan();
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
