package cs2030.simulator;

import java.util.Optional;
import cs2030.util.*;

public class Arrive implements Event {
    private final Customer customer;
    private final double eventT;
    private final String eventName;
    
    public Arrive(Customer customer, double eventT) {
        this.customer = customer;
        this.eventT = eventT;
        this.eventName = "Arrive";
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
        Optional<Event> next = Optional.empty();
        if (shop.hasAvail()) {
            for (Server server : shop.getServerIDs()) {
                if (server.canTakeServe() && (this.eventT >= server.getDoneT())) {
                    next = Optional.of(new Serve(this.customer, this.eventT, server));
                    return Pair.of(next, shop);
                }
            }
        }
        if (shop.hasWait()) {
            for (Server server : shop.getServerIDs()) { 
                if ((server.canTakeWait() && !server.isAtRest()) || server.atRestButCanWait()) {
                    next = Optional.of(new Wait(this.customer, this.eventT, server)); 
                    return Pair.of(next, shop);
                }
            }
        }
        if (shop.hasAvail()) {
            for (Server server : shop.getServerIDs()) {
                if (server.canTakeServe() && (this.eventT < server.getDoneT())) {
                    next = Optional.of(new Wait(this.customer, this.eventT, server));
                    return Pair.of(next, shop);
                }
            }   
        }
        return Pair.of(Optional.of(new Leave(this.customer, this.eventT)), shop);
    }

    @Override
    public String toString() {
        return String.format("%.3f %d arrives", this.getEventT(), this.customer.getID());
    }
}