
class Vehicle {
    protected String registrationNumber;
    protected String type;

    public Vehicle(String registrationNumber, String type) {
        this.registrationNumber = registrationNumber;
        this.type = type;
    }

    public void displayDetails() {
        System.out.println("Registration Number: " + registrationNumber);
        System.out.println("Vehicle Type: " + type);
    }
}

class Car extends Vehicle {
    public Car(String registrationNumber) {
        super(registrationNumber, "Car");
    }
}

class TwoWheeler extends Vehicle {
    public TwoWheeler(String registrationNumber) {
        super(registrationNumber, "Two-Wheeler");
    }
}

class ElectricCar extends Vehicle {
    private int batteryCapacity;

    public ElectricCar(String registrationNumber, int batteryCapacity) {
        super(registrationNumber, "Electric Car");
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Battery Capacity: " + batteryCapacity + " kWh");
    }
}

class ParkingSlot {
    private int slotNumber;
    private Vehicle vehicle;

    public ParkingSlot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.vehicle = null;
    }

    public boolean parkVehicle(Vehicle v) {
        if (vehicle == null) {
            vehicle = v;
            return true;
        }
        return false;
    }

    public void releaseVehicle() {
        vehicle = null;
    }

    public void displaySlot() {
        System.out.println("Slot " + slotNumber + ": "
                + (vehicle == null ? "Available" : "Occupied"));
    }
}

class ParkingFloor {
    private int floorNumber;
    private ParkingSlot[] slots;

    public ParkingFloor(int floorNumber, int numberOfSlots) {
        this.floorNumber = floorNumber;
        slots = new ParkingSlot[numberOfSlots];

        for (int i = 0; i < numberOfSlots; i++) {
            slots[i] = new ParkingSlot(i + 1);
        }
    }

    public void displaySlots() {
        System.out.println("Floor: " + floorNumber);

        for (ParkingSlot slot : slots) {
            slot.displaySlot();
        }
    }
}

class ParkingFacility {
    private String facilityName;
    private ParkingFloor[] floors;

    public ParkingFacility(String facilityName, int numberOfFloors,
                           int slotsPerFloor) {
        this.facilityName = facilityName;
        floors = new ParkingFloor[numberOfFloors];

        for (int i = 0; i < numberOfFloors; i++) {
            floors[i] = new ParkingFloor(i + 1, slotsPerFloor);
        }
    }

    public void displayFacility() {
        System.out.println("Facility: " + facilityName);

        for (ParkingFloor floor : floors) {
            floor.displaySlots();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Vehicle car = new Car("TN01AB1234");
        Vehicle bike = new TwoWheeler("TN02CD5678");
        Vehicle ev = new ElectricCar("TN03EF9012", 60);

        System.out.println("--- Vehicle Details ---");
        car.displayDetails();
        System.out.println();

        bike.displayDetails();
        System.out.println();

        ev.displayDetails();

        ParkingFacility facility =
                new ParkingFacility("Campus Parking", 2, 3);

        System.out.println("\n--- Parking Facility ---");
        facility.displayFacility();
    }
}
