import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private ListNode<Item> listFront = null;
    private ListNode<Item> listBack = null;
    private int numItems = 0;

    public static void main(String[] args) {
        // Sanity test.
        Deque<Integer> d = new Deque<>();
        assert d.isEmpty();
        d.addFirst(2);
        d.addLast(3);
        d.addFirst(1);
        assert d.removeLast() == 3;
    }

    public boolean isEmpty() {
        return numItems == 0;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("item cannot be null");

        ListNode<Item> newNode = new ListNode<Item>(item);
        newNode.setNext(listFront);
        if (listFront != null)
            listFront.prev = newNode;
        listFront = newNode;
        numItems++;
        if (numItems == 1)
            listBack = newNode;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("item cannot be null");

        ListNode<Item> newNode = new ListNode<Item>(item);
        newNode.setPrev(listBack);
        if (listBack != null)
            listBack.next = newNode;
        listBack = newNode;
        numItems++;
        if (numItems == 1)
            listFront = newNode;
    }

    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty");

        Item item = listBack.value;
        listBack = listBack.prev;
        if (listBack != null)
            listBack.next = null;
        numItems--;
        if (numItems == 0)
            listFront = null;
        return item;
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty");

        Item item = listFront.value;
        listFront = listFront.next;
        if (listFront != null)
            listFront.prev = null;
        numItems--;
        if (numItems == 0)
            listBack = null;
        return item;
    }

    public int size() {
        return numItems;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator<Item>(listFront);
    }

    private class ListNode<Item> {
        private final Item value;
        private ListNode<Item> prev = null;
        private ListNode<Item> next = null;

        public ListNode(Item value) {
            this.value = value;
        }

        public ListNode<Item> getPrev() {
            return prev;
        }

        public void setPrev(ListNode<Item> prev) {
            this.prev = prev;
        }

        public ListNode<Item> getNext() {
            return next;
        }

        public void setNext(ListNode<Item> next) {
            this.next = next;
        }
    }

    private class DequeIterator<Item> implements Iterator<Item> {
        private ListNode<Item> cursor;

        public DequeIterator(ListNode<Item> start) {
            cursor = start;
        }

        public boolean hasNext() {
            return cursor != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = cursor.value;
            cursor = cursor.next;
            return item;
        }
    }
}
