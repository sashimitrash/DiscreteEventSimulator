package cs2030.simulator;

import java.util.Optional;
import cs2030.util.*;

public class Done implements Event {
    private final Customer customer;
    private final double eventT;
    private final Server server;
    private final String eventName;
    
    public Done(Customer customer, double eventT, Server server) {
        this.customer = customer;
        this.eventT = eventT;
        this.server = server;
        this.eventName = "Done";
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
        Shop updatedShop = shop;
        if (updatedServer.identify().equals("SelfCheckout")) {
            if (updatedServer.getID() != shop.getFirstSelfCheckoutID()) {
                for (Server server : updatedShop.getServerIDs()) {
                    if (server.getID() == shop.getFirstSelfCheckoutID()) { 
                        server = server.remove(this.customer);
                        updatedShop = updatedShop.updateShop(server);
                        break;
                    }
                }
            }
        }
        updatedServer = updatedServer.doneServing(this.eventT);
        Optional<Event> next = Optional.empty();
        if (updatedServer.identify().equals("Human")) {
            double restTime = updatedServer.getRestT().get();
            if (restTime != 0.0) {
                updatedShop = updatedShop.updateShop(updatedServer.updateDoneT(restTime).atRest());
            } else {
                updatedShop = updatedShop.updateShop(updatedServer.notAtRest());
            }
            return Pair.of(next, updatedShop);
        }
        updatedShop = updatedShop.updateShop(updatedServer);
        return Pair.of(next, updatedShop);
    }

    @Override
    public String toString() {
        return String.format("%.3f %d done serving by %s", this.getEventT(), this.customer.getID(), this.server.toString());
    }
}