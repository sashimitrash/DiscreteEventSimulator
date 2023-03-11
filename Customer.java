package cs2030.simulator;

import java.util.function.Supplier;
import cs2030.util.*;

public class Customer {
    private final int id;
    private final double arrivalT;
    private final Lazy<Double> serviceT;

    public Customer(int id, double arrivalT) {
        this.id = id;
        this.arrivalT = arrivalT;
        this.serviceT = new Lazy<Double>(() -> 1.0);
    }

     public Customer(int id, double arrivalT, Lazy<Double> serviceTime) {
        this.id = id;
        this.arrivalT = arrivalT;
        this.serviceT = serviceTime;
    }

    public int getID() {
        return this.id;
    }

    public double getServiceT() {
        return this.serviceT.get();
    }

    public double getArrivalT() {
        return this.arrivalT;
    }

    public boolean aBeforeOtherCust(Customer other) {
        return this.getID() < other.getID();
    }

    public boolean equals(Customer other) {
        return this.getID() == other.getID();
    }

    @Override
    public String toString() {
        return String.format("%d", this.id);
    }
}