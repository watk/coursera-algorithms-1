import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        // Use Reservoir Sampling so that it is only necessary to keep k items.
        RandomizedQueue<String> q = new RandomizedQueue<>();
        int itemNum = 0;
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (q.size() < k) {
                q.enqueue(item);
            } else {
                int r = StdRandom.uniform(itemNum + 1);
                if (r < k) {
                    q.dequeue();
                    q.enqueue(item);
                }
            }
            itemNum++;
        }

        for (String s : q)
            System.out.println(s);
    }
}
