package cs2030.simulator;

import cs2030.util.*;
import java.util.Optional;

public class EventStub implements Event {
    private final Customer customer;
    private final double eventT;
    private final String eventName;

    public EventStub(Customer customer, double eventT) {
        this.customer = customer;
        this.eventT = eventT;
        this.eventName = "EventStub";
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

    @Override
    public Statistics update(Statistics statistics) {
        return statistics;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        return Pair.of(Optional.empty(), shop);
    }

    @Override
    public String toString() {
        return String.format("%.3f", this.eventT);
    }
}