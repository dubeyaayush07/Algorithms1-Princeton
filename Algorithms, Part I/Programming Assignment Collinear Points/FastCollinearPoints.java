import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;

public class FastCollinearPoints {
    private Point[] pointList;
    private LineSegment[] lines;
    private int n;

    public FastCollinearPoints(Point[] points) {
        // if the array  is null
        if (points == null) throw new  java.lang.IllegalArgumentException();
        
        // check for null Points
        for (Point p : points) {
            if(p == null) throw new  java.lang.IllegalArgumentException();
        }


        // check for duplicate points
        for (int i = 0; i<points.length; ++i) {
            for (int j = i + 1; j<points.length; ++j) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new  java.lang.IllegalArgumentException();
                }
            }
        }

        // initialize state
        pointList = points;
        lines = new LineSegment[2];
        n = 0;

        computeSegments();
    }

    public int numberOfSegments() {
        return n;
    }

    public LineSegment[] segments() {
        resize(n);
        return lines;
    }

    private void computeSegments() {
        // Comparator<Point> c = Point.BY_ORDER;

        Point[] temp =  new Point[pointList.length];
        for (int i = 0; i < pointList.length; ++i) temp[i] = pointList[i];

        // iterate through each point in the array and push lines which the point is part of
        for (int p = 0; p < pointList.length; ++p) {
            MergeX.sort(temp, pointList[p].slopeOrder());
            Point basis = temp[0];
            int start = 0, end = 0;

            for (int i = 2; i < temp.length; ++i) {
                // compute points with same slope with basis
                if (basis.slopeTo(temp[i - 1]) == basis.slopeTo(temp[i])) {
                    Point max = temp[0], min = temp[0];
                    start = i - 1;
                    if (max.compareTo(temp[i-1]) < 0) max = temp[i-1];
                    if (min.compareTo(temp[i-1]) > 0) min = temp[i-1];

                    while (i < temp.length && basis.slopeTo(temp[i - 1]) == basis.slopeTo(temp[i])) {
                        if (max.compareTo(temp[i]) < 0) max = temp[i];
                        if (min.compareTo(temp[i]) > 0) min = temp[i];
                        ++i;
                    }

                    // if line consists of more than 3 collinear points including basis
                    if (i - start > 2){
                        LineSegment line = new LineSegment(min, max);
                        if(!compare(line))push(line);
                    }
                }
            }
        }
    }

    


    private boolean compare(LineSegment line) {
        for(int i = 0; i < n; ++i) {
            if(lines[i].toString().equals(line.toString())) return true;
        }
        return false;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }


}