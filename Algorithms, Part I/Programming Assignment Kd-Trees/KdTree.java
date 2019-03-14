import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;


public class KdTree {
    private Node root;
    private int n;

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;
        private boolean isVertical;

        public Node() { }
        public Node(Point2D point, boolean v) {
            p = point;
            isVertical = v;
        }
        public Node(Point2D point, RectHV r, boolean v) {
            p = point;
            rect = r;
            isVertical = v;
        }

    }

    public KdTree() {
        n = 0;
        root = null;
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        if (contains(p)) return;

        if (root == null) root = new Node(p, new RectHV(0, 0, 1, 1), true);
        else root = insert(root, p, true);

        ++n;
    }

    private Node insert(Node node, Point2D pt, boolean isVertical) {
        if (node == null) return new Node(pt, isVertical);
        
        int cmp = 0;
        if (pt.compareTo(node.p) == 0) cmp = 0;
        else if (isVertical) {
            if (pt.x() < node.p.x()) cmp = -1;
            else cmp = 1;
        }
        else {
            if (pt.y() < node.p.y()) cmp = -1;
            else cmp = 1;
        }

        if  (cmp < 0) {
            node.lb  = insert(node.lb, pt, !isVertical);
            if (node.lb.rect == null) {
                if (isVertical) node.lb.rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax());
                else node.lb.rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y());
            }
        }
        else if (cmp > 0) {
            node.rt = insert(node.rt, pt, !isVertical);
            if (node.rt.rect == null) {
                if (isVertical) node.rt.rect = new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
                else node.rt.rect = new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax());
            }
        }
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        return find(root, p);
    }

    private boolean find(Node node, Point2D pt) {
        if (node == null) return false;
        int cmp = 0;

        if (pt.compareTo(node.p) == 0) cmp = 0;
        else if (node.isVertical) {
            if (pt.x() < node.p.x()) cmp = -1;
            else cmp = 1;
        }
        else {
            if (pt.y() < node.p.y()) cmp = -1;
            else cmp = 1;
        }
        
        if (cmp < 0) return find(node.lb, pt);
        else if (cmp > 0) return find(node.rt, pt);
        else return true;
    }

    public void draw() {
        // draw points and line
        traverse(root);        
    }

    private void traverse(Node node) {
        if (node == null) return;

        // draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();

        // draw splitting lines
        if (node.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }

        traverse(node.lb);
        traverse(node.rt);
        
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.IllegalArgumentException();
        Stack<Point2D> points = new Stack<Point2D>();
        range(root, rect, points);
        return points;
    }

    private void range(Node node, RectHV rect, Stack<Point2D> points) {
        if (node == null || !rect.intersects(node.rect)) return;
        if (rect.contains(node.p)) points.push(node.p);

        range(node.lb, rect, points);
        range(node.rt, rect, points);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        Node result = new Node();
        search(root, p, result, Double.POSITIVE_INFINITY);
        return result.p;
    }

    private void search(Node node, Point2D pt, Node result, double dist) {
        if (node == null || dist < node.rect.distanceTo(pt)) return;
        
        if (dist > node.p.distanceSquaredTo(pt)) {
            dist = node.p.distanceSquaredTo(pt);
            result.p = node.p;
        }
        
        // decide which tree to go down first
        boolean isLb = false;
        if (node.isVertical) {
            if (pt.x() < node.p.x()) isLb = true;
            else isLb = false;
        }
        else {
            if (pt.y() < node.p.y()) isLb = true;
            else isLb = false;
        }
        
        if (isLb) {
            search(node.lb, pt, result, dist);
            search(node.rt, pt, result, dist);

        } else {
            search(node.rt, pt, result, dist);
            search(node.lb, pt, result, dist);
        }

    }

}