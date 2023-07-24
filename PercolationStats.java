/* *****************************************************************************
 *  Name:              Nicholas French
 *  Created On:        21/7/2023
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private int trialsSize; // Number of Trials
    private double[] completedTrials; // Number of Opened Sites, per trial

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0)
            throw new IllegalArgumentException("PercolationStats N size must be greater than 0.");

        if (trials <= 0)
            throw new IllegalArgumentException(
                    "PercolationStats Trials size must be greater than 0.");

        trialsSize = trials;
        completedTrials = new double[trialsSize];

        // System.out.print("Starting Trials with N = " + n + ", N-by-N grid size = " + Math.pow(n, 2)
        //                          + ", Trials = "
        //                          + trialsSize + " ...");
        for (int i = 0; i < trialsSize; i++) {
            Percolation p = new Percolation(n);

            while (!p.percolates()) {
                int x = StdRandom.uniformInt(0, n);
                int y = StdRandom.uniformInt(0, n);
                p.open(x + 1, y + 1); // Convert to match external API index calls starting at 1-n.
            }

            completedTrials[i] = p.numberOfOpenSites() / Math.pow(n, 2);
        }
        // System.out.print("..finished.\n");
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(completedTrials);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(completedTrials);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (
                mean() - (
                        (CONFIDENCE_95 * (stddev()))
                                / (Math.sqrt(trialsSize)))
        );
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (
                mean() + (
                        (CONFIDENCE_95 * (stddev()))
                                / (Math.sqrt(trialsSize))
                )
        );
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]),
                                                   Integer.parseInt(args[1]));

        System.out.println("mean\t\t\t\t\t= " + ps.mean());
        System.out.println("stddev\t\t\t\t\t= " + ps.stddev());
        System.out.println(
                "95% confidence interval\t= [" + ps.confidenceLo() + ", " + ps.confidenceHi()
                        + "]");

    }
}