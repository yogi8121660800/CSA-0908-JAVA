import java.util.*;

public class InventoryMapBenchmark {
    static volatile int sink;

    static Map<String, Integer> makeMap(Map<String, Integer> map, int n) {
        for (int i = 0; i < n; i++) map.put(String.format("MED%07d", i), 100 + i);
        return map;
    }

    static long lookupTime(Map<String, Integer> map, int n, int lookups) {
        long start = System.nanoTime();
        for (int i = 0; i < lookups; i++) {
            Integer value = map.get(String.format("MED%07d", i % n));
            if (value != null) sink += value;
        }
        return System.nanoTime() - start;
    }

    static long iterationTime(Map<String, Integer> map) {
        long start = System.nanoTime();
        for (Map.Entry<String, Integer> e : map.entrySet()) sink += e.getValue();
        return System.nanoTime() - start;
    }

    static long sortedReportTime(Map<String, Integer> map) {
        long start = System.nanoTime();
        List<String> codes = new ArrayList<>(map.keySet());
        Collections.sort(codes);
        if (codes.size() > 0) sink += map.get(codes.get(0));
        return System.nanoTime() - start;
    }

    static void run(int n) {
        Map<String,Integer> hash = makeMap(new HashMap<>(), n);
        Map<String,Integer> tree = makeMap(new TreeMap<>(), n);
        Map<String,Integer> linked = makeMap(new LinkedHashMap<>(), n);
        int lookups = 100000;

        System.out.println("\\nInventory size: " + n);
        System.out.printf("100,000 get: HashMap %.3f ms | TreeMap %.3f ms | LinkedHashMap %.3f ms%n",
                lookupTime(hash,n,lookups)/1e6, lookupTime(tree,n,lookups)/1e6,
                lookupTime(linked,n,lookups)/1e6);
        System.out.printf("Iteration:   HashMap %.3f ms | TreeMap %.3f ms | LinkedHashMap %.3f ms%n",
                iterationTime(hash)/1e6, iterationTime(tree)/1e6, iterationTime(linked)/1e6);
        System.out.printf("Sorted report (copy + sort): HashMap %.3f ms | LinkedHashMap %.3f ms%n",
                sortedReportTime(hash)/1e6, sortedReportTime(linked)/1e6);
        System.out.println("TreeMap first code (already sorted): " + tree.keySet().iterator().next());
        System.out.println("LinkedHashMap first inserted code: " + linked.keySet().iterator().next());
    }

    public static void main(String[] args) {
        run(1000);
        run(10000);
        run(50000);
    }
}
