
class ParkingTicket {
    private static int counter = 1000;

    private int ticketNumber;
    private String vehicleNo;

    ParkingTicket(String vehicleNo) {
        this.vehicleNo = vehicleNo;
        ticketNumber = ++counter;
    }

    void display() {
        System.out.println("Ticket Number: " + ticketNumber);
        System.out.println("Vehicle Number: " + vehicleNo);
    }

    static void displayTotalTickets() {
        System.out.println("Total Tickets Issued: " + (counter - 1000));
    }
}

public class Task5 {
    public static void main(String[] args) {
        ParkingTicket t1 = new ParkingTicket("TN01AB1111");
        ParkingTicket t2 = new ParkingTicket("TN02CD2222");
        ParkingTicket t3 = new ParkingTicket("TN03EF3333");

        t1.display();
        t2.display();
        t3.display();

        ParkingTicket.displayTotalTickets();
    }
}