import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class ResultTask implements Callable<String> {
    private final Path file;

    ResultTask(Path file) {
        this.file = file;
    }

    @Override
    public String call() throws Exception {
        if (!Files.exists(file)) {
            throw new IOException("File is missing");
        }

        String content = Files.readString(file).trim();
        if (content.isEmpty()) {
            throw new IOException("File is empty or corrupt");
        }

        String[] marks = content.split("\\s+");
        int total = 0;
        for (String mark : marks) {
            int value = Integer.parseInt(mark);
            if (value < 0 || value > 100) {
                throw new IllegalArgumentException("Invalid mark: " + value);
            }
            total += value;
        }

        return file.getFileName() + " -> Total marks: " + total;
    }
}

public class Q7_ConcurrentResultProcessor {
    public static void main(String[] args) {
        // Supply score-file paths as command-line arguments.
        List<Path> files = new ArrayList<>();
        for (String arg : args) {
            files.add(Path.of(arg));
        }

        if (files.isEmpty()) {
            System.out.println("Usage: java Q7_ConcurrentResultProcessor file1.txt file2.txt ...");
            return;
        }

        ExecutorService pool = Executors.newFixedThreadPool(4);
        ConcurrentLinkedQueue<String> successes = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<String> failures = new ConcurrentLinkedQueue<>();
        List<Future<String>> futures = new ArrayList<>();

        try {
            for (Path file : files) {
                futures.add(pool.submit(new ResultTask(file)));
            }

            for (int i = 0; i < futures.size(); i++) {
                try {
                    successes.add(futures.get(i).get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    failures.add(files.get(i) + " -> Processing interrupted");
                    break;
                } catch (java.util.concurrent.ExecutionException e) {
                    failures.add(files.get(i) + " -> " + e.getCause().getMessage());
                }
            }
        } finally {
            pool.shutdown();
        }

        System.out.println("Successful files: " + successes.size());
        for (String result : successes) {
            System.out.println(result);
        }

        System.out.println("Failed files: " + failures.size());
        for (String failure : failures) {
            System.out.println(failure);
        }
    }
}
