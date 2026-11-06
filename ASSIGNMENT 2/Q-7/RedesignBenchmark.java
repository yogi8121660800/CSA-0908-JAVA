import java.util.*;

public class RedesignBenchmark {
    static volatile long sink;

    // Original design: ArrayList used as a queue; remove(0) shifts remaining elements.
    static long arrayListQueue(int n) {
        List<Integer> queue = new ArrayList<>();
        for (int i = 0; i < n; i++) queue.add(i);
        long start = System.nanoTime();
        while (!queue.isEmpty()) sink += queue.remove(0);
        return System.nanoTime() - start;
    }

    // Redesigned: ArrayDeque is designed for efficient operations at both ends.
    static long arrayDequeQueue(int n) {
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) queue.addLast(i);
        long start = System.nanoTime();
        while (!queue.isEmpty()) sink += queue.removeFirst();
        return System.nanoTime() - start;
    }

    static void run(int n) {
        // Alternate order in your own repeated trials to reduce order bias.
        long original = arrayListQueue(n);
        long redesigned = arrayDequeQueue(n);
        System.out.printf("n=%d | ArrayList front-removal %.3f ms | ArrayDeque %.3f ms%n",
                n, original / 1e6, redesigned / 1e6);
    }

    public static void main(String[] args) {
        System.out.println("Q7 redesign: queue workload with frequent front removals");
        run(5000);
        run(20000);
        run(50000);
        System.out.println("\\nDiagnosis: ArrayList remove(0) shifts all remaining elements, causing repeated work.");
        System.out.println("Objective: reduce queue-removal work from O(n) per removal to O(1) amortized.");
        System.out.println("Redesign: use ArrayDeque addLast() and removeFirst().");
        System.out.println("Trade-off: ArrayDeque is not a general indexed playlist; it is suitable for end-based queue operations.");
    }
}
