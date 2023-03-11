package cs2030.simulator;

import java.util.Optional;
import cs2030.util.*;

public class Serve implements Event {
    private final Customer customer;
    private final double eventT;
    private final Server server;
    private final String eventName;
    
    public Serve(Customer customer, double eventT, Server server) {
        this.customer = customer;
        this.eventT = eventT;
        this.server = server;
        this.eventName = "Serve";
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
        return statistics.incWaitingT(this.eventT - this.customer.getArrivalT()).incServed();
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        Server updatedServer = shop.updateServer(this.server);
        if (!updatedServer.haveServing()) {
            updatedServer = updatedServer.addToQ(this.customer, this.eventT);
        } else if (updatedServer.isAtRest()) {
            updatedServer = updatedServer.updateServicingT(this.eventT).notAtRest();
        }
        Shop updatedShop = shop.updateShop(updatedServer);
        Optional<Event> next = Optional.of(new Done(this.customer, updatedServer.getDoneT(), updatedServer));
        return Pair.of(next, updatedShop);
    }

    @Override
    public String toString() {
        return String.format("%.3f %d serves by %s", this.eventT, this.customer.getID(), this.server.toString());
    }
}