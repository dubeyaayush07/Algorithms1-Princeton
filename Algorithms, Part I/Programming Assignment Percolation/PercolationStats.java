import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private final int tests;
    private final double[] probs;

    public PercolationStats(int size, int trials) {
        if(size <= 0 || trials <= 0) throw new IllegalArgumentException();
        //intialize instance variables
            tests = trials;
            probs = new double[trials];

        //do the required number of tests
        for(int i = 0; i<tests;++i) {
            Percolation simulation =  new Percolation(size);
            //open sites until the system percolates
            while(!simulation.percolates()) {
                //get random rows and column
                int row = StdRandom.uniform(1, size+1);
                int col = StdRandom.uniform(1, size+1);

                simulation.open(row, col);
            }

            //save the probability in the probs array
            int openSites = simulation.numberOfOpenSites();
            double total = size*size;
            double prob = openSites/total;
            probs[i] = prob;
        }

    }

    public double mean() {
        return StdStats.mean(probs);
    }

    public double stddev() {
        return StdStats.stddev(probs);
    }

    public double confidenceLo() {
        double mean = StdStats.mean(probs);
        return (mean - (1.96/Math.sqrt(tests)));
    }

    public double confidenceHi() {
        double mean = StdStats.mean(probs);
        return (mean + (1.96/Math.sqrt(tests)));
    }



    public static void main(String[] args)  {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats stat = new PercolationStats(n, t);
        StdOut.println("mean                    = " + stat.mean());
        StdOut.println("stddev                  = " + stat.stddev());
        StdOut.println("95% confidence interval = " + "[" + stat.confidenceLo() + ", " + stat.confidenceHi() + "]" );
    }
}
