import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int n;
    private Node first;
    private Node last;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    public boolean isEmpty() {
        return (n == 0);
    }

    public int size() {
        return n;
    }

    public void addFirst(Item item) {
        if(item == null) throw new java.lang.IllegalArgumentException();
        Node oldFirst = first;

        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;

        if(isEmpty()) last = first;
        else oldFirst.prev = first;
        n++;

    } 

    public void addLast(Item item) {
        if(item == null) throw new java.lang.IllegalArgumentException();
        Node oldLast = last;

        last = new Node();
        last.item = item;
        last.prev = oldLast;
        last.next = null;

        if (isEmpty()) first = last;
        else oldLast.next = last;
        n++;
    }

    public Item removeFirst() {
        if(isEmpty()) throw new NoSuchElementException("The deque is already empty");
        Item item = first.item;
        first = first.next;
        n--;

        if(isEmpty()) last = null;
        else first.prev = null;

        return item;
    }

    public Item removeLast() {
        if(isEmpty()) throw new NoSuchElementException("The deque is already empty");
        Item item = last.item;
        last = last.prev;
        n--;

        if(isEmpty()) first = null;
        else  last.next = null;

        return item;

    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() { return current != null; }
        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if(!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<Integer> group = new Deque<>();
        for(int i = 0; i<5;++i){
            group.addLast(i);
        }

        group.addFirst(1);
        group.addLast(5);
        group.removeFirst();
        

        for(int i : group){
            System.out.println(i + "\n");
        }
        
    }


}