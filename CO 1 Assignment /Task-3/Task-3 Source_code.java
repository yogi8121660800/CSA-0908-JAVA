
class Vehicle {
    String registrationNumber;
    String type;

    Vehicle(String registrationNumber, String type) {
        this.registrationNumber = registrationNumber;
        this.type = type;
    }
}

class ParkingSlot {
    private int slotNumber;
    private Vehicle vehicle;

    ParkingSlot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.vehicle = null;
    }

    boolean isAvailable() {
        return vehicle == null;
    }

    boolean parkVehicle(Vehicle v) {
        if (isAvailable()) {
            vehicle = v;
            return true;
        }
        return false;
    }

    boolean releaseVehicle() {
        if (!isAvailable()) {
            vehicle = null;
            return true;
        }
        return false;
    }

    void displaySlot() {
        if (isAvailable()) {
            System.out.println("Slot " + slotNumber + ": Available");
        } else {
            System.out.println("Slot " + slotNumber + ": Occupied by "
                    + vehicle.registrationNumber);
        }
    }
}

class ParkingFacility {
    private ParkingSlot[] slots;

    ParkingFacility(int numberOfSlots) {
        slots = new ParkingSlot[numberOfSlots];

        for (int i = 0; i < numberOfSlots; i++) {
            slots[i] = new ParkingSlot(i + 1);
        }
    }

    boolean parkVehicle(Vehicle v) {
        for (ParkingSlot slot : slots) {
            if (slot.parkVehicle(v)) {
                System.out.println("Vehicle " + v.registrationNumber
                        + " parked successfully.");
                return true;
            }
        }

        System.out.println("Parking is full. No slot available.");
        return false;
    }

    void displayAvailability() {
        System.out.println("\n--- Parking Slot Status ---");

        for (ParkingSlot slot : slots) {
            slot.displaySlot();
        }
    }

    boolean releaseSlot(int slotNumber) {
        if (slotNumber < 1 || slotNumber > slots.length) {
            System.out.println("Invalid slot number.");
            return false;
        }

        if (slots[slotNumber - 1].releaseVehicle()) {
            System.out.println("Slot " + slotNumber
                    + " released successfully.");
            return true;
        }

        System.out.println("Slot " + slotNumber + " is already empty.");
        return false;
    }
}

public class Main {
    public static void main(String[] args) {
        ParkingFacility facility = new ParkingFacility(3);

        Vehicle car = new Vehicle("TN01AB1234", "Car");
        Vehicle bike = new Vehicle("TN02CD5678", "Two-Wheeler");
        Vehicle ev = new Vehicle("TN03EF9012", "Electric Car");
        Vehicle extraCar = new Vehicle("TN04GH3456", "Car");

        System.out.println("--- Campus Smart-Parking System ---");

        facility.parkVehicle(car);
        facility.parkVehicle(bike);
        facility.parkVehicle(ev);

        facility.displayAvailability();

        System.out.println("\n--- Attempt to Park Another Vehicle ---");
        facility.parkVehicle(extraCar);

        System.out.println("\n--- Vehicle Exit ---");
        facility.releaseSlot(2);

        facility.displayAvailability();
    }
}
