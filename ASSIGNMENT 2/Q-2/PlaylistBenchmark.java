import java.util.*;

public class PlaylistBenchmark {
    static volatile long sink;

    static long timeAddEnd(List<Integer> list, int n) {
        long s = System.nanoTime();
        for (int i = 0; i < n; i++) list.add(i);
        return System.nanoTime() - s;
    }

    static long timeFrontInsert(List<Integer> list, int n) {
        long s = System.nanoTime();
        for (int i = 0; i < n; i++) list.add(0, i);
        return System.nanoTime() - s;
    }

    static long timeMiddleInsert(List<Integer> list, int n) {
        long s = System.nanoTime();
        for (int i = 0; i < n; i++) list.add(list.size() / 2, i);
        return System.nanoTime() - s;
    }

    static long timeFrontRemove(List<Integer> list) {
        long s = System.nanoTime();
        while (!list.isEmpty()) sink += list.remove(0);
        return System.nanoTime() - s;
    }

    static long timeMiddleRemove(List<Integer> list) {
        long s = System.nanoTime();
        while (!list.isEmpty()) sink += list.remove(list.size() / 2);
        return System.nanoTime() - s;
    }

    static long timeRandomAccess(List<Integer> list, int repeats) {
        long s = System.nanoTime();
        for (int i = 0; i < repeats; i++) sink += list.get((i * 31) % list.size());
        return System.nanoTime() - s;
    }

    static long timeIteration(List<Integer> list) {
        long s = System.nanoTime();
        for (int value : list) sink += value;
        return System.nanoTime() - s;
    }

    static void runForSize(int n) {
        System.out.println("\\n--- Playlist size: " + n + " ---");
        List<Integer> a = new ArrayList<>();
        List<Integer> l = new LinkedList<>();
        System.out.printf("Add end       ArrayList %.3f ms | LinkedList %.3f ms%n",
                timeAddEnd(a, n)/1e6, timeAddEnd(l, n)/1e6);

        a = new ArrayList<>(); l = new LinkedList<>();
        System.out.printf("Insert front  ArrayList %.3f ms | LinkedList %.3f ms%n",
                timeFrontInsert(a, n)/1e6, timeFrontInsert(l, n)/1e6);

        a = new ArrayList<>(); l = new LinkedList<>();
        System.out.printf("Insert middle ArrayList %.3f ms | LinkedList %.3f ms%n",
                timeMiddleInsert(a, n)/1e6, timeMiddleInsert(l, n)/1e6);

        a = new ArrayList<>(); l = new LinkedList<>();
        for (int i=0; i<n; i++) { a.add(i); l.add(i); }
        System.out.printf("Get k-th      ArrayList %.3f ms | LinkedList %.3f ms%n",
                timeRandomAccess(a, 2000)/1e6, timeRandomAccess(l, 2000)/1e6);

        System.out.printf("Iterate       ArrayList %.3f ms | LinkedList %.3f ms%n",
                timeIteration(a)/1e6, timeIteration(l)/1e6);

        a = new ArrayList<>(); l = new LinkedList<>();
        for (int i=0; i<n; i++) { a.add(i); l.add(i); }
        System.out.printf("Remove front  ArrayList %.3f ms | LinkedList %.3f ms%n",
                timeFrontRemove(a)/1e6, timeFrontRemove(l)/1e6);

        a = new ArrayList<>(); l = new LinkedList<>();
        for (int i=0; i<n; i++) { a.add(i); l.add(i); }
        System.out.printf("Remove middle ArrayList %.3f ms | LinkedList %.3f ms%n",
                timeMiddleRemove(a)/1e6, timeMiddleRemove(l)/1e6);
    }

    public static void main(String[] args) {
        runForSize(1000);
        runForSize(5000);
        runForSize(10000);

        Deque<String> upNext = new ArrayDeque<>();
        upNext.addFirst("Urgent song");
        upNext.addLast("Normal song");
        System.out.println("\\nArrayDeque Up Next: " + upNext);
        System.out.println("Remove next: " + upNext.removeFirst());
    }
}
