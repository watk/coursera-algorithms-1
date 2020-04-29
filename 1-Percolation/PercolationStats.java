import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // Performs independent trials on an n-by-n grid.
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1)
            throw new IllegalArgumentException("n or trials < 1");

        double[] results = new double[trials];
        for (int trial = 0; trial < trials; trial++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            results[trial] = (double) p.numberOfOpenSites() / n / n;
        }

        // Eagerly calculate the stats for simplicity.
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
        final double halfInterval = 1.96 * stddev / Math.sqrt(results.length);
        confidenceLo = mean - halfInterval;
        confidenceHi = mean + halfInterval;
    }

    // The sample mean of the percolation threshold.
    public double mean() {
        return mean;
    }

    // The sample standard deviation of the percolation threshold.
    public double stddev() {
        return stddev;
    }

    // The low endpoint of a 95% confidence interval.
    public double confidenceLo() {
        return confidenceLo;
    }

    // The high endpoint of a 95% confidence interval.
    public double confidenceHi() {
        return confidenceHi;
    }


    public static void main(String[] args) {
        if (args.length < 2)
            throw new IllegalArgumentException("Two arguments required");
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]),
                                                      Integer.parseInt(args[1]));
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " +
                                   stats.confidenceHi() + "]");
    }
}
