package cs2030.simulator;

import java.util.List;
import java.util.Optional;
import cs2030.util.*;

public class Shop {
    private final ImList<Server> serverIDs;
    private final int numOfSelfChecks;
    private final int numOfHuman;

    public Shop(List<Server> serverList) {
        this.serverIDs = ImList.<Server>of(serverList);
        this.numOfSelfChecks = 0;
        this.numOfHuman = this.serverIDs.size();
    }

    public Shop(ImList<Server> updated) {
        this.serverIDs = updated;
        this.numOfSelfChecks = 0;
        this.numOfHuman = updated.size();
    }

    public Shop(ImList<Server> updated, int n, int numOfSelfChecks) {
        this.serverIDs = updated;
        this.numOfSelfChecks = numOfSelfChecks;
        this.numOfHuman = n;
    }

    public ImList<Server> getServerIDs() {
        return this.serverIDs;
    }

    public ImList<Server> getSelfCheckout() {
        ImList<Server> newList = ImList.of();
        for (Server server : this.serverIDs) {
            if (server.identify().equals("SelfCheckout")) {
                newList = newList.add(server);
            }
        }
        return newList.sort(new TimeComparator());
    }

    public boolean hasAvail() {
        for (Server newServer : this.serverIDs) {
            if (newServer.canTakeServe()) {
                return true;
            } 
        }
        return false;
    }

    public boolean hasWait() {
        for (Server newServer : this.serverIDs) { 
            if (newServer.canTakeWait()) {
                return true;
            } 
        }
        return false;
    }

    public boolean hasAvailSelfCheckout() {
        for (Server server : this.serverIDs) {
            if (server.identify().equals("SelfCheckout") && server.canTakeServe()) {
                return true;
            }
        }
        return false;
    }

    public int getFirstSelfCheckoutID() {
        return this.numOfHuman + 1;
    }

    public Shop updateShop(Server updatedServer) {
        ImList<Server> newList = ImList.of();
        for (Server server : this.serverIDs) {
            if (updatedServer.getID() == server.getID()) {
                newList = newList.add(updatedServer);
            } else {
                newList = newList.add(server);
            }
        }
        return new Shop(newList);
    }

    public Server updateServer(Server server) {
        for (Server newServer : this.serverIDs) {
            if (server.equals(newServer)) {
                return newServer;
            }
        }
        return server;
    }

    @Override
    public String toString() {
        return this.serverIDs.toString();
    }
}