
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
    private final SET<Point2D> set;

    public PointSET() {
        set = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        return set.contains(p);
    }

    public int size() {
        return set.size();
    }

    public void draw() {
        for (Point2D p: set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.IllegalArgumentException();
        Stack<Point2D> points = new Stack<Point2D>();

        for (Point2D p: set) {
            if (rect.contains(p)) points.push(p);
        }

        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();

        double min = Double.POSITIVE_INFINITY;
        Point2D near = null;

        for (Point2D pt: set) {
            double dist = p.distanceSquaredTo(pt);
            if (dist < min) {
                near = pt;
                min = dist;
            }
        }
        return near;
    }

    public static void main(String[] args) {
        PointSET p = new PointSET();
        p.insert(new Point2D(0.3, 0.6));
        p.insert(new Point2D(0.7, 0.2));
        System.out.println(p.nearest(new Point2D(0.8, 0.1)));
    }
}

