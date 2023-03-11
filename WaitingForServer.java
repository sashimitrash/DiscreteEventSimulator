package cs2030.simulator;

import java.util.Optional;
import cs2030.util.*;

public class WaitingForServer implements Event {
    private final Customer customer;
    private final double eventT;
    private final Server server;
    private final String eventName;
    
    public WaitingForServer(Customer customer, double eventT, Server server) {
        this.customer = customer;
        this.eventT = eventT;
        this.server = server;
        this.eventName = "WaitingForServer";
    }

    @Override
    public boolean aBeforeb(Event b) {
        return this.getEventT() < b.getEventT();
    }

    @Override
    public boolean aAfterb(Event b) {
        return this.getEventT() > b.getEventT();
    }

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
        Server updatedServer = shop.updateServer(this.server);
        Shop updatedShop = shop;
        if (this.eventT >= updatedServer.getDoneT()) {
            Server newServer = updatedServer;
            if (updatedServer.isAtRest()) {
                updatedServer = updatedServer.updateDoneT(this.eventT);
            }  
            updatedShop = shop.updateShop(updatedServer);
            Optional<Event> next = Optional.of(new Serve(this.customer, this.eventT, updatedServer));
            return Pair.of(next, updatedShop);
        } else {
            Optional<Event> next = Optional.of(new WaitingForServer(this.customer, updatedServer.getDoneT(), updatedServer));
            return Pair.of(next, shop);
        }   
    }
}
