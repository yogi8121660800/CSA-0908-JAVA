public class Q6_TokenCounter {
    private final int[] buffer = new int[5];
    private int count = 0;
    private int in = 0;
    private int out = 0;

    public synchronized void put(int token) throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }

        buffer[in] = token;
        in = (in + 1) % buffer.length;
        count++;

        notifyAll();
    }

    public synchronized int take() throws InterruptedException {
        while (count == 0) {
            wait();
        }

        int token = buffer[out];
        out = (out + 1) % buffer.length;
        count--;

        notifyAll();
        return token;
    }

    public static void main(String[] args) throws InterruptedException {
        Q6_TokenCounter counter = new Q6_TokenCounter();
        int[] collected = new int[2];

        Thread cook = new Thread(() -> {
            try {
                for (int token = 1; token <= 20; token++) {
                    counter.put(token);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread[] students = new Thread[2];
        for (int i = 0; i < students.length; i++) {
            final int id = i;
            students[i] = new Thread(() -> {
                try {
                    for (int k = 0; k < 10; k++) {
                        collected[id] += counter.take();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        cook.start();
        for (Thread student : students) {
            student.start();
        }

        cook.join();
        for (Thread student : students) {
            student.join();
        }

        System.out.println("Sum of tokens collected: "
            + (collected[0] + collected[1]) + " (expected 210)");
    }
}
