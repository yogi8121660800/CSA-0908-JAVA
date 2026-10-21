
import java.util.Scanner;

public class SmartParkingAnalysis {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("CAMPUS SMART-PARKING SYSTEM");
        System.out.println("---------------------------");

        System.out.print("Enter vehicle registration number: ");
        String registrationNumber = sc.nextLine().trim();

        System.out.print("Enter vehicle type (Car/TwoWheeler/ElectricCar): ");
        String vehicleType = sc.nextLine().trim();

        System.out.print("Enter parking duration in hours: ");

        if (!sc.hasNextInt()) {
            System.out.println("Invalid parking duration.");
            sc.close();
            return;
        }

        int hours = sc.nextInt();

        if (registrationNumber.isEmpty()) {
            System.out.println("Error: Registration number is required.");
        } else if (hours <= 0) {
            System.out.println("Error: Duration must be greater than zero.");
        } else if (!vehicleType.equalsIgnoreCase("Car")
                && !vehicleType.equalsIgnoreCase("TwoWheeler")
                && !vehicleType.equalsIgnoreCase("ElectricCar")) {
            System.out.println("Error: Invalid vehicle type.");
        } else {
            System.out.println("\n--- Vehicle Details ---");
            System.out.println("Registration Number: " + registrationNumber);
            System.out.println("Vehicle Type: " + vehicleType);
            System.out.println("Parking Duration: " + hours + " hour(s)");
            System.out.println("Input validation: Successful");
            System.out.println(
                "Note: Slot allocation and fee calculation are implemented in later tasks."
            );
        }

        sc.close();
    }
}
