import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Q3_SalesReport {
    public static void main(String[] args) {
        List<Path> inputFiles = Arrays.asList(
            Path.of("store1.txt"),
            Path.of("store2.txt"),
            Path.of("store3.txt")
        );
        Path outputFile = Path.of("daily_report.txt");
        double total = 0.0;

        // try-with-resources closes each reader and the writer automatically.
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            writer.write("DAILY SALES REPORT");
            writer.newLine();

            for (Path file : inputFiles) {
                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    double storeTotal = 0.0;

                    while ((line = reader.readLine()) != null) {
                        storeTotal += Double.parseDouble(line.trim());
                    }

                    total += storeTotal;
                    writer.write(file.getFileName() + " total: " + storeTotal);
                    writer.newLine();
                } catch (IOException | NumberFormatException e) {
                    writer.write(file.getFileName() + " failed: " + e.getMessage());
                    writer.newLine();
                }
            }

            writer.write("Grand total: " + total);
            writer.newLine();
            System.out.println("Report generated: " + outputFile.toAbsolutePath());
            System.out.println("Grand total: " + total);
        } catch (IOException e) {
            System.out.println("Could not create report: " + e.getMessage());
        }
    }
}
