import java.util.Locale;

public class TicketBenchmark {
    private static final int WARMUPS = 2;
    private static final int RUNS = 5;

    static String buildWithPlus(int fields, int tickets) {
        String result = "";
        for (int t = 0; t < tickets; t++) {
            String ticket = "";
            for (int f = 0; f < fields; f++) {
                ticket = ticket + "FIELD" + f + "=" + (t + f) + ";";
            }
            result = ticket; // retain the last ticket; avoids storing all tickets
        }
        return result;
    }

    static String buildWithBuilder(int fields, int tickets) {
        String result = "";
        for (int t = 0; t < tickets; t++) {
            StringBuilder ticket = new StringBuilder();
            for (int f = 0; f < fields; f++) {
                ticket.append("FIELD").append(f).append("=")
                      .append(t + f).append(";");
            }
            result = ticket.toString();
        }
        return result;
    }

    static String buildWithBuffer(int fields, int tickets) {
        String result = "";
        for (int t = 0; t < tickets; t++) {
            StringBuffer ticket = new StringBuffer();
            for (int f = 0; f < fields; f++) {
                ticket.append("FIELD").append(f).append("=")
                      .append(t + f).append(";");
            }
            result = ticket.toString();
        }
        return result;
    }

    static long measure(int method, int fields, int tickets) {
        long start = System.nanoTime();
        String output;
        if (method == 0) output = buildWithPlus(fields, tickets);
        else if (method == 1) output = buildWithBuilder(fields, tickets);
        else output = buildWithBuffer(fields, tickets);
        long elapsed = System.nanoTime() - start;
        // Consume output so the work remains observable.
        if (output.length() == -1) System.out.println(output);
        return elapsed;
    }

    static void benchmark(String workload, int fields, int tickets) {
        System.out.println("\\n" + workload + " (" + tickets + " tickets x " + fields + " fields)");
        String[] names = {"String +", "StringBuilder", "StringBuffer"};
        for (int method = 0; method < 3; method++) {
            for (int i = 0; i < WARMUPS; i++) measure(method, fields, tickets);
            long total = 0;
            for (int i = 0; i < RUNS; i++) total += measure(method, fields, tickets);
            System.out.printf(Locale.US, "%-15s average: %.3f ms%n",
                    names[method], total / (double) RUNS / 1_000_000.0);
        }
    }

    public static void main(String[] args) {
        benchmark("Workload 1: short ticket", 5, 1);
        benchmark("Workload 2: high load", 50, 10000);
        benchmark("Workload 3: many fields", 200, 1000);

        System.out.println("\\nPer-ticket recommendation: StringBuilder for local construction.");
        System.out.println("Shared log recommendation: synchronize access or use a thread-safe logging design; "
                + "StringBuffer synchronizes individual operations but does not make a multi-step log transaction atomic.");
    }
}
