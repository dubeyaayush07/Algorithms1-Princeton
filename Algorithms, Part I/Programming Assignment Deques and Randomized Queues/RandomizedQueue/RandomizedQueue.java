import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;


public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int n;
    private int first;
    private int last;

    public RandomizedQueue() {
        q = (Item[]) new Object[2];
        n = 0;
        first = 0;
        last = 0;
    }
    
    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    
    public void enqueue(Item item) {
        if(item == null) throw new IllegalArgumentException();
        if( n == q.length) resize(2*q.length);
        q[last++] = item;
        if(last == q.length) last = 0;
        n++;
    }
    
    public Item dequeue() {
        if(isEmpty()) throw new NoSuchElementException("Queue underflow");
        int toDelete = getRandom();
        
        //swap the toDelete with the first element
        Item temp = q[toDelete];
        q[toDelete] = q[first];
        q[first] = temp;
        
        //remove the first element from the queue
        Item item = q[first];
        q[first] = null;
        n--;
        first++;
        if(first == q.length) first = 0;
        
        if(n > 0 && n == q.length/4) resize(q.length/2);
        return item;
        
    }

    public Item sample() {
        return q[getRandom()];
    }

    public Iterator<Item> iterator() {
        return new ArrayIterator();
    } 

    private class ArrayIterator implements Iterator<Item> {
        private int i;
        Item[] temp;

        public ArrayIterator() {
            i = 0 ;
            temp = (Item[]) new Object[n];

            for (int i = 0; i < n; ++i) {
                temp[i] = q[(first + i) % q.length];
            }
            StdRandom.shuffle(temp);
        }

        public boolean hasNext() { return i<n; }
        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = temp[i];
            i++;
            return item;
        }

    }

    
    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; ++i) {
            temp[i] = q[(first + i) % q.length];
        }
        q = temp;
        first = 0;
        last = n;
    }

    private int getRandom() {
        return (first + StdRandom.uniform(n) ) % q.length;
    }

}