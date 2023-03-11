package cs2030.simulator;

import cs2030.util.*;
import java.util.function.Supplier;

public class Server {
    private final int serverID;
    private final boolean status;
    private final ImList<Customer> customers;
    private static final int initialMaxWait = 1;
    private final double doneT;
    private final int qmax;
    private final Supplier<Double> restTimes;
    private final boolean atRest;

    public Server(int serverID) {
        this.serverID = serverID;
        this.status = true;
        this.customers = ImList.of();
        this.doneT = 0.00;
        this.qmax = initialMaxWait;
        this.restTimes = () -> 0.00;
        this.atRest = false;
    }

    public Server(int serverID, int qmax) {
        this.serverID = serverID;
        this.status = true;
        this.customers = ImList.of();
        this.doneT = 0.00;
        this.qmax = qmax;
        this.restTimes = () -> 0.00;
        this.atRest = false;
    }

    public Server(int serverID, int qmax, Supplier<Double> restTimes) {
        this.serverID = serverID;
        this.status = true;
        this.customers = ImList.of();
        this.doneT = 0.00;
        this.qmax = qmax;
        this.restTimes = restTimes;
        this.atRest = false;
    }

    public Server(int serverID, boolean status, ImList<Customer> customers, double doneT, int qmax, Supplier<Double> restTimes) {
        this.serverID = serverID;
        this.status = status;
        this.customers = customers;
        this.doneT = doneT;
        this.qmax = qmax;
        this.restTimes = restTimes;
        this.atRest = false;
    }

    public Server(int serverID, boolean status, ImList<Customer> customers, double doneT, int qmax, Supplier<Double> restTimes, boolean atRest) {
        this.serverID = serverID;
        this.status = status;
        this.customers = customers;
        this.doneT = doneT;
        this.qmax = qmax;
        this.restTimes = restTimes;
        this.atRest = atRest;
    }

    public double getDoneT() {
        return this.doneT;
    }

    public ImList<Customer> getCust() {
        return this.customers; 
    }

    public boolean haveNextInLine() {
        return this.customers.size() > 1 && this.customers.size() < this.qmax + 1;
    }

    public boolean haveServing() {
        return this.customers.size() > 0;
    }

    public Customer getServing() {
        return this.customers.get(0);
    }
    
    public Customer getNextInLine() {
        return this.customers.get(1);
    }

    public boolean canWait() {
        return this.customers.size() < this.qmax + 1 ? true : false;
    }

    public boolean getStatus() {
        return this.status;
    }

    public int getQMax() {
        return this.qmax;
    }

    public Supplier<Double> getRestT() {
        return this.restTimes;
    }

    public String identify() {
        return "Human";
    }

    public boolean canTakeWait() {
        return (!this.getStatus() && this.canWait()) ? true : false;
    }

    public boolean canTakeServe() {
        return (this.getStatus() && this.canWait()) ? true : false;
    }

    public boolean atRestButCanWait() {
        return (this.atRest && !this.getStatus() && this.customers.size() < this.qmax) ? true : false;
    }

    public boolean isAtRest() {
        return this.atRest;
    }

    public int getID() {
        return this.serverID;
    }

    public Server atRest() {
        return new Server(this.serverID, this.status, this.customers,  this.doneT, this.qmax, this.restTimes, true);
    }

    public Server notAtRest() {
        return new Server(this.serverID, true, this.customers,  this.doneT, this.qmax, this.restTimes, false);
    }

    public Server addToBackOfQ(Customer customer) {
        return new Server(this.serverID, false, this.customers.add(customer), this.doneT, this.qmax, this.restTimes, this.atRest);
    }

    public Server addToQ(Customer customer, double eventT) {
        return new Server(this.serverID, false, this.customers.add(customer), customer.getServiceT() + eventT, this.qmax, this.restTimes, this.atRest);
    }

    public Server free(double time) {
        return new Server(this.serverID, true, ImList.of(), time, this.qmax, this.restTimes, false);
    }
    
    public Server clearing() { 
        return new Server(this.serverID, false, ImList.of(), this.doneT, this.qmax, this.restTimes, this.atRest);
    }
    
    public Server doneServing(double time) {
        if (this.haveNextInLine()) {
            Customer newServing = this.customers.remove(0).second().get(0);
            return new Server(this.serverID, false, this.customers.remove(0).second(), newServing.getServiceT() + time, this.qmax, this.restTimes, this.atRest);
        } else if (this.customers.size() == 0) {
            return new Server(this.serverID, true, ImList.of(), time, this.qmax, this.restTimes, this.atRest); 
        } else {
            return new Server(this.serverID, true, this.customers.remove(0).second(), time, this.qmax, this.restTimes, this.atRest); 
        }
    }

    public Server checking(Customer customer) {
        return new Server(this.serverID, false, this.customers.set(0, customer), this.doneT + customer.getServiceT(), this.qmax, this.restTimes, this.atRest);
    }

    public Server remove(Customer remove) {
        ImList<Customer> newList = ImList.of();
        for (Customer customer : this.customers) {
            if (!remove.equals(customer)) {
                newList = newList.add(customer);
            }
        }
        return new Server(this.serverID, this.status, newList, this.doneT, this.qmax, this.restTimes, this.atRest);
    }
    
    public Server updateDoneT(Double time) {
        return new Server(this.serverID, this.status, this.customers, this.doneT + time, this.qmax, this.restTimes, this.atRest);
    }

    public Server updateServicingT(Double time) {
        return new Server(this.serverID, this.status, this.customers, this.customers.get(0).getServiceT() + time, this.qmax, this.restTimes, this.atRest);
    }

    public boolean equals(Server other) {
        return this.getID() == other.getID();
    }

    @Override
    public String toString() {
        return String.format("%d", this.serverID);
    }
}