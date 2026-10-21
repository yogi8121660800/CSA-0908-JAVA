
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

    @Override
    public void displayDetails() {
        System.out.println("--- Car Details ---");
        super.displayDetails();
    }
}

class TwoWheeler extends Vehicle {
    public TwoWheeler(String registrationNumber) {
        super(registrationNumber, "Two-Wheeler");
    }

    @Override
    public void displayDetails() {
        System.out.println("--- Two-Wheeler Details ---");
        super.displayDetails();
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
        System.out.println("--- Electric Car Details ---");
        super.displayDetails();
        System.out.println("Battery Capacity: " + batteryCapacity + " kWh");
    }
}

public class Main {
    public static void main(String[] args) {
        Vehicle v1 = new Car("TN01AB1234");
        Vehicle v2 = new TwoWheeler("TN02CD5678");
        Vehicle v3 = new ElectricCar("TN03EF9012", 60);

        v1.displayDetails();
        System.out.println();

        v2.displayDetails();
        System.out.println();

        v3.displayDetails();
    }
}
