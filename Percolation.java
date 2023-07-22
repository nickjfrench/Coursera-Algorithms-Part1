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
    private boolean percolated;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException(
                    "Percolation N size must be greater than 0. N = " + n);

        gridSize = n;

        grid = new boolean[gridSize][gridSize]; // row * n + col
        uf = new WeightedQuickUnionUF(gridSize * gridSize + 1); // n^2 plus top virtual node.

        virtualTopIndex = gridSize * gridSize;

        count = 0;

        percolated = false;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // Update specified API input to match index.
        int internalRow = convertToInternalIndex(row);
        int internalCol = convertToInternalIndex(col);

        if (!inBounds(internalRow, internalCol))
            throw new IllegalArgumentException(
                    "Row or Column out of bounds. Row = " + internalRow + " Col = " + internalCol);

        if (isOpen(row, col))
            // Do nothing if already open.
            return;

        grid[internalRow][internalCol] = true;
        count += 1;

        unionVirtualTop(internalRow, internalCol);
        unionNeighbourNodes(internalRow, internalCol);

        checkBottomToTop(internalRow, internalCol);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        // Update specified API input to match index.
        int internalRow = convertToInternalIndex(row);
        int internalCol = convertToInternalIndex(col);

        if (!inBounds(internalRow, internalCol))
            throw new IllegalArgumentException(
                    "Row or Column out of bounds. Row = " + internalRow + " Col = " + internalCol);

        return grid[internalRow][internalCol];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // Update specified API input to match index.
        int internalRow = convertToInternalIndex(row);
        int internalCol = convertToInternalIndex(col);

        if (!inBounds(internalRow, internalCol))
            throw new IllegalArgumentException(
                    "Row or Column out of bounds. Row = " + internalRow + " Col = " + internalCol);

        return uf.find(gridToIndex(internalRow, internalCol)) == uf.find(virtualTopIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        // To prevent backwash, bottom virtual node was removed.
        // Now checked in checkBottomToTop()
        return percolated;
        // return uf.find(virtualBottomIndex) == uf.find(virtualTopIndex);
    }

    private void unionVirtualTop(int row, int col) {
        if (row == 0) // Top Row
            uf.union(gridToIndex(row, col), virtualTopIndex);
    }

    private void checkBottomToTop(int row, int col) {
        // To prevent backwash, don't implement virtual bottom. Check percolation if on bottom row.
        if (row == gridSize - 1)
            percolated = uf.find(gridToIndex(row, col)) == uf.find(virtualTopIndex);
    }

    private void unionNeighbourNodes(int row, int col) {
        int[] offsetRows = { -1, 1, 0, 0 }; // Offsets for row to explore neighboring cells
        int[] offsetCols = { 0, 0, -1, 1 }; // Offsets for column to explore neighboring cells

        for (int i = 0; i < offsetRows.length; i++) {
            int newRow = row + offsetRows[i];
            int newCol = col + offsetCols[i];

            // isOpen needs to be called with external index
            if (inBounds(newRow, newCol) && isOpen(convertToExternalIndex(newRow),
                                                   convertToExternalIndex(newCol))) {
                uf.union(gridToIndex(row, col), gridToIndex(newRow, newCol));
            }
        }
    }

    private int gridToIndex(int row, int col) {
        return row * gridSize + col;
    }

    private boolean inBounds(int row, int col) {
        // Check row and col are within bounds.
        // Not responsible for throwing error.
        boolean check = true;

        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize)
            check = false;

        return check;
    }

    private int convertToInternalIndex(int externalIndex) {
        return externalIndex - 1;
    }

    private int convertToExternalIndex(int internalIndex) {
        return internalIndex + 1;
    }

    private void printGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j])
                    if (uf.find(gridToIndex(i, j)) == uf.find(virtualTopIndex))
                        System.out.print("▣" + "  ");
                    else
                        System.out.print("▪" + "  ");
                else
                    System.out.print("□" + "  ");
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

        System.out.println("Starting Monte Carlo Simulation...");
        while (!percolation.percolates()) {
            int x = StdRandom.uniformInt(0, percolation.gridSize);
            int y = StdRandom.uniformInt(0, percolation.gridSize);
            percolation.open(x + 1,
                             y + 1); // Convert to match external API index calls starting at 1-n.
        }

        percolation.printGrid();
        System.out.println(
                "Monte Carlo Sim percolated in " + percolation.numberOfOpenSites() + "/" + (
                        percolation.gridSize
                                * percolation.gridSize) + " cells.");

        boolean testCases = false;
        if (testCases) {
            System.out.println();
            System.out.println("Starting Percolation Test Cases...");
            Percolation testPercolation = new Percolation(10);

            // System.out.println("InBounds() Test Cases:");
            // System.out.println("Needs to be true (0, 0): " +
            //                            testPercolation.inBounds(0, 0));
            // System.out.println("Needs to be false (-1, 0): " +
            //                            testPercolation.inBounds(-1, 0));
            // System.out.println("Needs to be false (0, -1): " +
            //                            testPercolation.inBounds(0, -1));
            // System.out.println("Needs to be false (-1, -1): " +
            //                            testPercolation.inBounds(-1, -1));
            // System.out.println(
            //         "Needs to be false (n, 0): " +
            //                 testPercolation.inBounds(testPercolation.gridSize, 0));
            // System.out.println(
            //         "Needs to be false (0, n): " +
            //                 testPercolation.inBounds(0, testPercolation.gridSize));
            // System.out.println("Needs to be false (n, n): " +
            //                            testPercolation.inBounds(testPercolation.gridSize,
            //                                                     testPercolation.gridSize));
            // System.out.println("Needs to be true (n-1, n-1): " +
            //                            testPercolation.inBounds(testPercolation.gridSize - 1,
            //                                                     testPercolation.gridSize - 1));
            // System.out.println();

            System.out.println("Empty Grid Test:");
            testPercolation.printGrid();
            System.out.println();

            System.out.println("Open() Test Cases:");
            testPercolation.open(1, 4);
            // System.out.println("Top row connected to virtual top? " + (percolation.getUF().find(
            //         percolation.gridToIndex(0, 3)) == percolation.getUF().find(
            //         percolation.virtualTopIndex)));
            testPercolation.open(3, 4);
            testPercolation.printGrid();
            System.out.println();
            testPercolation.open(2, 4);
            testPercolation.printGrid();

            System.out.println();
            System.out.println("Percolates() Test Case:");
            System.out.println(
                    "Needs to be false (no percolation): " + testPercolation.percolates());
            testPercolation.open(4, 4);
            testPercolation.open(5, 4);
            testPercolation.open(6, 4);
            testPercolation.open(7, 4);
            testPercolation.open(8, 4);
            testPercolation.open(8, 1);
            testPercolation.open(9, 4);
            testPercolation.open(9, 7);
            testPercolation.open(10, 4);
            testPercolation.open(10, 5);
            testPercolation.open(10, 7);
            testPercolation.printGrid();
            System.out.println();
            System.out.println("Needs to be true (percolation): " + testPercolation.percolates());
            System.out.println();

            System.out.println(
                    "Number of Open Sites needs to equal 13: "
                            + testPercolation.numberOfOpenSites());
            System.out.println("Is 0,1 open (needs to be false)? " + testPercolation.isOpen(1, 2));
            System.out.println("Is 0,3 open (needs to be true)? " + testPercolation.isOpen(1, 4));
            System.out.println("Is 0,3 full (needs to be true)? " + testPercolation.isFull(1, 4));
            System.out.println("Is 8,6 full (needs to be false)? " + testPercolation.isFull(9, 7));
        }
    }
}
