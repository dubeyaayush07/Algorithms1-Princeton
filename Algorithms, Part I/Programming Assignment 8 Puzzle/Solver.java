import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;


public class Solver {
    private final int movesCount;
    private final Node last;

    private class Node implements Comparable<Node> {
        private final Board board;
        private final Node pred;
        private final int step;
        private final int priority;

        public Node(Board b, Node p, int s) {
            board = b;
            pred = p;
            step = s;
            priority = s  + board.manhattan();
        }

        public int compareTo(Node that) {
            return this.priority - that.priority;
        }         
    }

    public Solver(Board initial) {
        if (initial == null) throw new java.lang.IllegalArgumentException();

        // intialize the priority queues for initail and its
        MinPQ<Node> original = new MinPQ<Node>();
        MinPQ<Node> complement = new MinPQ<Node>();

        Node n1 = new Node(initial, null, 0);
        Node n2 = new Node(initial.twin(), null, 0);

        original.insert(n1);
        complement.insert(n2);

        n1 = proceed(original);
        n2 = proceed(complement);

        // alternating back and forth between search nodes in each of the two game trees
        while (!n1.board.isGoal() && !n2.board.isGoal()) {
            n1 = proceed(original);
            n2 = proceed(complement);
        }

        if (n2.board.isGoal()) {
            last = null;
            movesCount = -1;
        }
        else {
            movesCount = n1.step;
            last = n1;
        }

    }

    public boolean isSolvable() {
        return movesCount != -1;
    }

    public int moves() {
        return movesCount;
    }

    public Iterable<Board> solution() {
        if (last == null) return null;
        Stack<Board> boards = new Stack<Board>();
        Node temp = last;

        while (temp != null) {
            boards.push(temp.board);
            temp = temp.pred;
        }
        
        return boards;
    }

    private Node proceed(MinPQ<Node> q) {
        Node node = q.delMin();
        int m = node.step + 1;
        Iterable<Board> it = node.board.neighbors();

        // add neighbors and prevent repeatation
        for (Board b: it) {
            if (node.pred == null || !(b.equals(node.pred.board))) {
                q.insert(new Node(b, node, m));
            }
        }
        return node;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}