package cs2030.simulator;

import java.util.Optional;
import cs2030.util.*;

public class Wait implements Event {
    private final Customer customer;
    private final double eventT;
    private final Server server;
    private final String eventName;
    
    public Wait(Customer customer, double eventT, Server server) {
        this.customer = customer;
        this.eventT = eventT;
        this.server = server;
        this.eventName = "Wait";
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
        Server updatedServer = shop.updateServer(this.server);
        updatedServer = updatedServer.addToBackOfQ(this.customer);
        Shop updatedShop = shop.updateShop(updatedServer);
        if (updatedServer.identify().equals("SelfCheckout")) { 
            Server newServer = shop.getSelfCheckout().get(0);
            Optional<Event> next = Optional.of(new WaitingInLine(this.customer, newServer.getDoneT(), newServer));
            return Pair.of(next, updatedShop);
        } else {
            Optional<Event> next = Optional.of(new WaitingInLine(this.customer, updatedServer.getDoneT(), updatedServer));
            return Pair.of(next, updatedShop);   
        }
    }

    @Override
    public String toString() {
        return String.format("%.3f %d waits at %s", this.getEventT(), this.customer.getID(), this.server.toString());
    }
}