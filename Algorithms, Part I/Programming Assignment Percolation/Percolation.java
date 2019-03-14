
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {

    private final int size;
    private WeightedQuickUnionUF uF;
    private int[][]arr;
    private int openSite;

    public Percolation(int n) {
        if(n <= 0) throw new IllegalArgumentException();
        size = n;
        arr =  new int[n][n];
        openSite = 0;

        //block of index n*n is connected to the first row and n*n+1 to the last row 
        uF = new WeightedQuickUnionUF(n*n + 2);

        //connect the first row to n*n
        for(int i = 0; i<n; ++i) {
            uF.union(n*n, i);
        }

        //connect the last row to n*n + 1
        for(int i = n*n-1; i>n*n-6; --i) {
            uF.union((n*n)+1, i);
        }
    }

    public void open(int row, int col) {
        //convert to indices starting with 0
        row = row - 1;
        col = col - 1;

        //check if they are valid
        if(validate(row) || validate(col))  throw new IllegalArgumentException();

        //open the site
        arr[row][col] = 1;
        ++openSite;

        //create paths of adjacent paths are open 
        if(!validate(row-1) && arr[row-1][col] == 1) uF.union(translate(row, col), translate(row-1, col));
        if(!validate(row+1)  && arr[row+1][col] == 1) uF.union(translate(row, col), translate(row+1, col));
        if(!validate(col-1) && arr[row][col-1] == 1) uF.union(translate(row, col), translate(row, col-1));
        if(!validate(col+1) && arr[row][col+1] == 1) uF.union(translate(row, col), translate(row, col+1));
    }

    public boolean isOpen(int row, int col) {
        //convert to indices starting with 0
        row = row - 1;
        col = col - 1;

        //check if they are valid
        if(validate(row) || validate(col))  throw new IllegalArgumentException();

        return (arr[row][col] == 1);

    }

    public boolean isFull(int row, int col) {
        //convert to indices starting with 0
        row = row - 1;
        col = col - 1;

        //check if they are valid
        if(validate(row) || validate(col))  throw new IllegalArgumentException();

        return (arr[row][col] == 1 && uF.connected((size*size), translate(row, col)));
    }

    public int numberOfOpenSites() {
        return openSite;
    }

    public boolean percolates() {
        return uF.connected((size*size),(size*size+1));
    }


    private boolean validate(int x) {
        return (x<0 || (x>size-1));
    }

    private int translate(int row, int col) {
        return (row*(size) + col);
    }

    public static void main(String[] args) {
        int[] input = StdIn.readAllInts();
        Percolation p = new Percolation(input[0]);

        for(int i = 1; i<input.length-1; i+=2){
            p.open(input[i], input[i+1]);
        }

        if(p.percolates())StdOut.println("Percolates");
        else StdOut.println("Does not percolate");

        StdOut.println(p.numberOfOpenSites());
    }

    
}