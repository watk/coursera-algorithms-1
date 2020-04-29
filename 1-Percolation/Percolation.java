import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // The size of the grid.
    private final int n;

    // isOpen[row - 1][col - 1] is true if the site at (row, col) is open.
    private final boolean[][] isOpen;

    // For tracking the connected sites.
    private final WeightedQuickUnionUF uf;

    // For identifying filled sites. A separate UF datastructure is used to avoid backwash, which
    // occurs when sites are only connected to the top virtual node via a path through the bottom
    // virtual node.
    private final WeightedQuickUnionUF fillUf;

    // Virtual nodes that are connected to the top and bottom rows.
    private final int virtualTopNode;
    private final int virtualBottomNode;

    private int numOpenSites = 0;

    // Creates an n-by-n grid with all sites initially blocked.
    public Percolation(int n) {
        if (n < 1)
            throw new IllegalArgumentException("n < 1");

        this.n = n;
        isOpen = new boolean[n][n];
        virtualTopNode = n * n;
        virtualBottomNode = n * n + 1;
        uf = new WeightedQuickUnionUF(virtualBottomNode + 1);
        fillUf = new WeightedQuickUnionUF(virtualTopNode + 1);
    }

    // Whether the system percolates.
    public boolean percolates() {
        return uf.find(virtualTopNode) == uf.find(virtualBottomNode);
    }

    // Opens the site at (row, col) if it is not open already.
    public void open(int row, int col) {
        validatePos(row, col);
        if (isOpen(row, col))
            return;
        isOpen[row - 1][col - 1] = true;
        numOpenSites++;

        // Connect to virtual nodes if necessary.
        if (row == 1) {
            uf.union(posToNode(row, col), virtualTopNode);
            fillUf.union(posToNode(row, col), virtualTopNode);
        }
        if (row == n)
            uf.union(posToNode(row, col), virtualBottomNode);

        // Union this site with each open neighbour.
        for (int[] deltas : new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }) {
            int neighbourRow = row + deltas[0];
            int neighbourCol = col + deltas[1];
            if (isValidPos(neighbourRow, neighbourCol) && isOpen(neighbourRow, neighbourCol)) {
                uf.union(posToNode(row, col), posToNode(neighbourRow, neighbourCol));
                fillUf.union(posToNode(row, col), posToNode(neighbourRow, neighbourCol));
            }
        }
    }

    // Whether the site at (row, col) is open.
    public boolean isOpen(int row, int col) {
        validatePos(row, col);
        return isOpen[row - 1][col - 1];
    }

    // Whether the site at (row, col) is filled.
    public boolean isFull(int row, int col) {
        validatePos(row, col);
        return isOpen(row, col) && fillUf.find(virtualTopNode) == fillUf.find(posToNode(row, col));
    }

    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // Whether (row, col) identifies a valid site.
    private boolean isValidPos(int row, int col) {
        return row >= 1 && col >= 1 && row <= n && col <= n;
    }

    // Throws if (row, col) does not identify a valid site.
    private void validatePos(int row, int col) {
        if (!isValidPos(row, col))
            throw new IllegalArgumentException("row or col out of range");
    }

    // Converts a (row, col) position to a Union-Find node ID.
    private int posToNode(int row, int col) {
        return (row - 1) * n + col - 1;
    }
}
