/* *****************************************************************************
 *  Name:              Nicholas French
 *  Created On:        19/7/2023
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int gridSize;
    private boolean[][] grid;
    private WeightedQuickUnionUF uf;
    private int count;
    private int virtualTopIndex;
    private int virtualBottomIndex;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Percolation N size must be greater than 0.");

        gridSize = n;

        grid = new boolean[gridSize][gridSize]; // row * n + col
        uf = new WeightedQuickUnionUF(gridSize * gridSize + 2); // n^2 plus two virtual nodes.

        virtualTopIndex = gridSize * gridSize;
        virtualBottomIndex = gridSize * gridSize + 1;

        count = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!inBounds(row, col))
            throw new IllegalArgumentException("Row or Column out of bounds.");

        if (isOpen(row, col))
            // Do nothing if already open.
            return;

        grid[row][col] = true;
        count += 1;

        unionVirtualNodes(row, col);
        unionNeighbourNodes(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!inBounds(row, col))
            throw new IllegalArgumentException("Row or Column out of bounds.");
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!inBounds(row, col))
            throw new IllegalArgumentException("Row or Column out of bounds.");

        return uf.find(gridToIndex(row, col)) == uf.find(virtualTopIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(virtualBottomIndex) == uf.find(virtualTopIndex);
    }

    private void unionVirtualNodes(int row, int col) {
        // Connect (row, col) to top/bottom virtual node, if first or last row.
        if (!inBounds(row, col))
            throw new IllegalArgumentException("Row or Column out of bounds.");

        if (row > 0 && row < gridSize - 1) // Middle Rows
            return;

        if (row == 0) // Top Row
            uf.union(uf.find(gridToIndex(row, col)), uf.find(virtualTopIndex));

        if (row == gridSize - 1) // Bottom Row
            uf.union(uf.find(gridToIndex(row, col)), uf.find(virtualBottomIndex));
    }

    private void unionNeighbourNodes(int row, int col) {
        // Connect (row, col) to open neighbouring nodes.

        if (!inBounds(row, col))
            throw new IllegalArgumentException("Row or Column out of bounds.");

        int[] offsetRows = { -1, 1, 0, 0 }; // Offsets for row to explore neighboring cells
        int[] offsetCols = { 0, 0, -1, 1 }; // Offsets for column to explore neighboring cells

        for (int i = 0; i < 4; i++) {
            int newRow = row + offsetRows[i];
            int newCol = col + offsetCols[i];

            if (inBounds(newRow, newCol) && isOpen(newRow, newCol)) {
                uf.union(gridToIndex(row, col), gridToIndex(newRow, newCol));
            }
        }
    }

    public int gridToIndex(int row, int col) {
        if (!inBounds(row, col))
            throw new IllegalArgumentException("Row or Column out of bounds.");

        return row * gridSize + col;
    }

    public boolean inBounds(int row, int col) {
        // Check row and col are within bounds.
        // Not responsible for throwing error.
        if (row < 0 || row > gridSize - 1)
            return false;
        if (col < 0 || col > gridSize - 1)
            return false;

        return true;
    }

    public void printGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j])
                    if (uf.find(gridToIndex(i, j)) == uf.find(virtualTopIndex))
                        System.out.print("▣" + "\t");
                    else
                        System.out.print("▪" + "\t");
                else
                    System.out.print("□" + "\t");
                // System.out.print(grid[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    // public WeightedQuickUnionUF getUF() {
    //     return uf;
    // }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(Integer.parseInt(args[0]));

        while (!percolation.percolates()) {
            int x = StdRandom.uniformInt(0, percolation.gridSize);
            int y = StdRandom.uniformInt(0, percolation.gridSize);
            percolation.open(x, y);
        }

        percolation.printGrid();
        System.out.println(
                "Percolated in " + percolation.numberOfOpenSites() + "/" + (percolation.gridSize
                        * percolation.gridSize) + " cells.");

        boolean testCases = false;
        if (testCases) {
            Percolation testPercolation = new Percolation(Integer.parseInt(args[0]));

            System.out.println("InBounds() Test Cases:");
            System.out.println("Needs to be true (0, 0): " +
                                       testPercolation.inBounds(0, 0));
            System.out.println("Needs to be false (-1, 0): " +
                                       testPercolation.inBounds(-1, 0));
            System.out.println("Needs to be false (0, -1): " +
                                       testPercolation.inBounds(0, -1));
            System.out.println("Needs to be false (-1, -1): " +
                                       testPercolation.inBounds(-1, -1));
            System.out.println(
                    "Needs to be false (n, 0): " +
                            testPercolation.inBounds(testPercolation.gridSize, 0));
            System.out.println(
                    "Needs to be false (0, n): " +
                            testPercolation.inBounds(0, testPercolation.gridSize));
            System.out.println("Needs to be false (n, n): " +
                                       testPercolation.inBounds(testPercolation.gridSize,
                                                                testPercolation.gridSize));
            System.out.println("Needs to be true (n-1, n-1): " +
                                       testPercolation.inBounds(testPercolation.gridSize - 1,
                                                                testPercolation.gridSize - 1));
            System.out.println();

            System.out.println("Empty Grid Test:");
            testPercolation.printGrid();
            System.out.println();

            System.out.println("Open() Test Cases:");
            testPercolation.open(0, 3);
            // System.out.println("Top row connected to virtual top? " + (percolation.getUF().find(
            //         percolation.gridToIndex(0, 3)) == percolation.getUF().find(
            //         percolation.virtualTopIndex)));
            testPercolation.open(2, 3);
            testPercolation.printGrid();
            System.out.println();
            testPercolation.open(1, 3);
            testPercolation.printGrid();

            System.out.println();
            System.out.println("Percolates() Test Case:");
            System.out.println(
                    "Needs to be false (no percolation): " + testPercolation.percolates());
            testPercolation.open(3, 3);
            testPercolation.open(4, 3);
            testPercolation.open(5, 3);
            testPercolation.open(6, 3);
            testPercolation.open(7, 3);
            testPercolation.open(7, 0);
            testPercolation.open(8, 3);
            testPercolation.open(8, 6);
            testPercolation.open(9, 3);
            testPercolation.open(9, 4);
            testPercolation.printGrid();
            System.out.println();
            System.out.println("Needs to be true (percolation): " + testPercolation.percolates());
            System.out.println();

            System.out.println(
                    "Number of Open Sites needs to equal 10: "
                            + testPercolation.numberOfOpenSites());
            System.out.println("Is 0,1 open (needs to be false)? " + testPercolation.isOpen(0, 1));
            System.out.println("Is 0,3 open (needs to be true)? " + testPercolation.isOpen(0, 3));
            System.out.println("Is 0,3 full (needs to be true)? " + testPercolation.isFull(0, 3));
            System.out.println("Is 8,6 full (needs to be false)? " + testPercolation.isFull(8, 6));
        }
    }
}
