import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items = (Item[]) new Object[1];
    private int numEnqueued = 0;

    public static void main(String[] args) {
        // Sanity test.
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        q.enqueue(1);
        assert q.size() == 1;
        assert !q.isEmpty();
        assert q.dequeue() == 1;
        assert q.isEmpty();
        q.enqueue(1);
        q.enqueue(2);
        assert q.dequeue() + q.dequeue() == 3;
    }

    // Enqueue an item.
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("item cannot be null");

        // Resize the array if necessary.
        if (numEnqueued == items.length)
            items = Arrays.copyOf(items, items.length * 2);

        items[numEnqueued++] = item;
    }

    // Returns the number of enqueued items.
    public int size() {
        return numEnqueued;
    }

    // Returns whether the queue is empty.
    public boolean isEmpty() {
        return numEnqueued == 0;
    }

    // Remove and return a random item.
    public Item dequeue() {
        if (numEnqueued == 0)
            throw new NoSuchElementException("RandomizedQueue is empty");

        int index = StdRandom.uniform(numEnqueued);
        Item item = items[index];
        items[index] = items[numEnqueued - 1];
        items[numEnqueued - 1] = null;
        numEnqueued--;

        if (numEnqueued < items.length / 2)
            items = Arrays.copyOf(items, items.length / 2);

        return item;
    }

    // Return a random item (but do not remove it).
    public Item sample() {
        if (numEnqueued == 0)
            throw new NoSuchElementException("RandomizedQueue is empty");

        return items[StdRandom.uniform(numEnqueued)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator<Item>(this);
    }

    private class RandomizedQueueIterator<Item> implements Iterator<Item> {
        RandomizedQueue<Item> q;
        int[] indices;
        int nextIndex = 0;

        public RandomizedQueueIterator(RandomizedQueue<Item> queue) {
            q = queue;
            indices = StdRandom.permutation(q.numEnqueued);
        }

        public boolean hasNext() {
            return nextIndex < indices.length;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return q.items[indices[nextIndex++]];
        }
    }
}
