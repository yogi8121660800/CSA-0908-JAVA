class ScannerStation extends Thread {
    private final int scripts;

    ScannerStation(String name, int scripts) {
        super(name);
        this.scripts = scripts;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= scripts; i++) {
                Thread.sleep(100);
            }
            System.out.println(getName() + " scanned " + scripts + " scripts.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(getName() + " was interrupted.");
        }
    }
}

class RunnableStation implements Runnable {
    private final String stationName;
    private final int scripts;

    RunnableStation(String stationName, int scripts) {
        this.stationName = stationName;
        this.scripts = scripts;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(300);
            System.out.println(stationName + " scanned " + scripts + " scripts.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Q4_ThreadLifecycle {
    public static void main(String[] args) throws InterruptedException {
        Thread[] stations = {
            new ScannerStation("Station-1", 10),
            new ScannerStation("Station-2", 12),
            new ScannerStation("Station-3", 8),
            new ScannerStation("Station-4", 15)
        };

        for (Thread station : stations) {
            station.start();
            System.out.println(station.getName() + " alive: " + station.isAlive());
        }

        for (Thread station : stations) {
            station.join();
        }

        System.out.println("All Thread-based stations completed.");

        Thread runnableThread =
            new Thread(new RunnableStation("Runnable-Station", 20));
        runnableThread.start();
        runnableThread.join();

        System.out.println("All scanning completed.");
    }
}
