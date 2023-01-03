# Strategy pattern example

```java
// Strategy interface
public interface VehicleStrategy {
    void transport();
}

// Concrete strategy 1
public class BicycleStrategy implements VehicleStrategy {
    @Override
    public void transport() {
        System.out.println("Biking to the airport....kill me..");
    }
}

// Concrete strategy 2
public class BusStrategy implements VehicleStrategy {
    @Override
    public void transport() {
        System.out.println("Using bus to the airport, not too bad!");
    }
}

// Concrete strategy 3
public class TaxiStrategy implements VehicleStrategy {

    @Override
    public void transport() {
        System.out.println("Using taxi to the airport, comfy and nice!");
    }
}

// Client
public class Client {

    private final static double BUS_BUDGET = 5.4;
    private final static double TAXI_BUDGET = 15.5;

    public static void main(String[] args) {
        // Assume we validate the args and args[0]
        Double budget = Double.parseDouble(args[0]);

        VehicleStrategy vehicleStrategy;

        if (budget.compareTo(BUS_BUDGET) < 0) {
            vehicleStrategy = new BicycleStrategy();
        } else if (budget.compareTo(TAXI_BUDGET) < 0) {
            vehicleStrategy = new BusStrategy();
        } else {
            vehicleStrategy = new TaxiStrategy();
        }

        // Goto airport
        vehicleStrategy.transport();
    }
}

```