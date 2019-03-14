import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;



public class BruteCollinearPoints {

    private Point[] pointList;
    private LineSegment[] lines;
    private int n;

    public BruteCollinearPoints(Point[] points) {
        // if the array  is null
        if (points == null) throw new  java.lang.IllegalArgumentException();
        
        // check for null Points
        for (Point p : points) {
            if(p == null) throw new  java.lang.IllegalArgumentException();
        }

        //sort for checking duplicates and also for adding points in the correct order
        MergeX.sort(points);

        // check for duplicate points
        for (int i = 1; i<points.length; ++i) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                throw new  java.lang.IllegalArgumentException();
            }
        }

        // initialize state
        pointList = points;
        lines = new LineSegment[2];
        n = 0;

        // find pairs of 4 collinear points
        for (int p = 0; p<pointList.length; ++p) {
            for (int q = p + 1; q<pointList.length; ++q) {
                for (int r = q + 1; r<pointList.length; ++r) {
                    for (int s = r + 1; s<pointList.length; ++s){
                        if (checkCollinearity(pointList[p], pointList[q], pointList[r], pointList[s])) {
                            push(new LineSegment(pointList[p], pointList[s]));
                        }
                    }
                }
            }
        }


    }

    public int numberOfSegments() {
        return n;
    }

    public LineSegment[] segments() {
        resize(n);
        return lines;
    }

    private boolean checkCollinearity(Point p, Point q, Point r, Point s) {
        return (p.slopeTo(q) == p.slopeTo(r) && p.slopeTo(s) ==  p.slopeTo(r));
    }

    private void resize(int capacity) {
        LineSegment[] temp = new LineSegment[capacity];

        for (int i = 0; i < n; i++) {
            temp[i] = lines[i];
        }
        lines = temp;
    }

    private void push(LineSegment line) {
        if (n == lines.length) resize(2*lines.length);
        lines[n++] = line;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}