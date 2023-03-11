package cs2030.simulator;

import java.util.Optional;
import cs2030.util.*;

public class Leave implements Event {
    private final Customer customer;
    private final double eventT;
    private final String eventName;
    
    public Leave(Customer customer, double eventT) {
        this.customer = customer;
        this.eventT = eventT;
        this.eventName = "Leave";
    }

    @Override
    public boolean aBeforeb(Event b) {
        return this.getEventT() < b.getEventT();
    }

    @Override
    public boolean aAfterb(Event b) {
        return this.getEventT() > b.getEventT();
    }    

    @Override
    public String getEventName() {
        return this.eventName;
    }

    @Override
    public double getEventT() {
        return this.eventT;
    }

    @Override
    public Customer getCust() {
        return this.customer;
    }

    public Statistics update(Statistics statistics) {
        return statistics.incLeft();
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        Optional<Event> next = Optional.empty();
        return Pair.of(next, shop);
    }

    @Override
    public String toString() {
        return String.format("%.3f %d leaves", this.getEventT(), this.customer.getID());
    }
}