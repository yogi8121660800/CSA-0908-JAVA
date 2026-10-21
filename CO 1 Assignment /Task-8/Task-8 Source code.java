
package task;

import java.time.LocalTime;

interface Chargeable {
    double chargingFee(int kWh);
}

abstract class Vehicle {
    private final String regNo;

    Vehicle(String regNo) {
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    protected abstract double ratePerHour();

    public double calculateFee(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Minutes cannot be negative");
        }

        int hours = (minutes + 59) / 60;
        return hours * ratePerHour();
    }

    public double calculateFee(LocalTime in, LocalTime out) {
        int minutes =
                (out.toSecondOfDay() - in.toSecondOfDay()) / 60;

        if (minutes < 0) {
            minutes += 24 * 60;
        }

        return calculateFee(minutes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Vehicle)) {
            return false;
        }

        Vehicle v = (Vehicle) obj;
        return regNo.equals(v.regNo);
    }

    @Override
    public int hashCode() {
        return regNo.hashCode();
    }
}

class TwoWheeler extends Vehicle {
    TwoWheeler(String regNo) {
        super(regNo);
    }

    @Override
    protected double ratePerHour() {
        return 10;
    }
}

class Car extends Vehicle {
    Car(String regNo) {
        super(regNo);
    }

    @Override
    protected double ratePerHour() {
        return 30;
    }
}

class ElectricCar extends Car implements Chargeable {
    ElectricCar(String regNo) {
        super(regNo);
    }

    @Override
    public double chargingFee(int kWh) {
        if (kWh < 0) {
            throw new IllegalArgumentException("kWh cannot be negative");
        }

        return 12 * kWh;
    }
}

public class Task8 {
    public static void main(String[] args) {
        Vehicle[] parked = {
            new TwoWheeler("TN01A1"),
            new Car("TN02B2"),
            new ElectricCar("TN03C3")
        };

        for (Vehicle v : parked) {
            double fee = v.calculateFee(130);

            if (v instanceof Chargeable) {
                Chargeable c = (Chargeable) v;
                fee += c.chargingFee(10);
            }

            System.out.println(v.getClass().getSimpleName() + " "
                    + v.getRegNo() + " -> Rs " + fee);
        }

        System.out.println(
                new Car("TN02B2").equals(new ElectricCar("TN02B2")));

        System.out.println("Overloaded method fee: Rs "
                + new Car("TN04D4").calculateFee(
                    LocalTime.of(9, 0),
                    LocalTime.of(11, 10)));
    }
}