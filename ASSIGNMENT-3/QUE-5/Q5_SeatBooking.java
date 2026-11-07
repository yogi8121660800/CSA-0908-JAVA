public class Q5_SeatBooking {
    private final boolean[] booked;
    private int bookingsMade = 0;

    Q5_SeatBooking(int seats) {
        booked = new boolean[seats];
    }

    // The synchronized keyword makes check-and-book one atomic operation.
    public synchronized void book(int seat) {
        if (!booked[seat]) {
            booked[seat] = true;
            bookingsMade++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Q5_SeatBooking screen = new Q5_SeatBooking(10);
        Thread[] users = new Thread[100];

        for (int i = 0; i < users.length; i++) {
            final int seat = i % 10;
            users[i] = new Thread(() -> screen.book(seat));
            users[i].start();
        }

        for (Thread t : users) {
            t.join();
        }

        System.out.println("Seats sold: " + screen.bookingsMade);
    }
}
