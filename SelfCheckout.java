package cs2030.simulator;

import cs2030.util.*;
import java.util.function.Supplier;

public class SelfCheckout extends Server {
    private static final Supplier<Double> defaultRestT = () -> 0.00;

    public SelfCheckout(int ID, int qmax) {
        super(ID, qmax);
    }

    public SelfCheckout(int ID, boolean status, ImList<Customer> customers, double doneT, int qmax) {
        super(ID, status, customers, doneT, qmax, defaultRestT, false);
    }

    @Override
    public String identify() {
        return "SelfCheckout";
    }

    @Override
    public boolean canTakeServe() {
        return this.getStatus();
    }

    @Override
    public boolean canTakeWait() {
        return false;
    }

    @Override
    public SelfCheckout addToBackOfQ(Customer customer) {
        return new SelfCheckout(this.getID(), false, this.getCust().add(customer), this.getDoneT(), this.getQMax());
    }

    @Override
    public SelfCheckout addToQ(Customer customer, double eventT) {
        return new SelfCheckout(this.getID(), false, this.getCust().add(customer), customer.getServiceT() + eventT, this.getQMax());
    }

    @Override
    public SelfCheckout free(double time) {
        return new SelfCheckout(this.getID(), true, ImList.of(), time, this.getQMax());
    }
    
    @Override
    public SelfCheckout clearing() {
        return new SelfCheckout(this.getID(), true, ImList.of(), this.getDoneT(), this.getQMax());
    }
    
    @Override
    public SelfCheckout doneServing(double time) {
        if (this.haveNextInLine()) {
            Customer newServing = this.getCust().remove(0).second().get(0);
            return new SelfCheckout(this.getID(), false, this.getCust().remove(0).second(), newServing.getServiceT() + time, this.getQMax());
        } else if (this.getCust().size() == 0) {
            return new SelfCheckout(this.getID(), true, ImList.of(), time, this.getQMax()); 
        } else {
            return new SelfCheckout(this.getID(), true, this.getCust().remove(0).second(), time, this.getQMax()); 
        }
    }

    @Override
    public SelfCheckout remove(Customer remove) {
        ImList<Customer> newList = ImList.of();
        for (Customer customer : this.getCust()) {
            if (!remove.equals(customer)) {
                newList = newList.add(customer);
            }
        }
        return new SelfCheckout(this.getID(), this.getStatus(), newList, this.getDoneT(), this.getQMax());
    }
    
    @Override
    public SelfCheckout updateDoneT(Double time) {
        return new SelfCheckout(this.getID(), this.getStatus(), this.getCust(), this.getDoneT() + time, this.getQMax());
    }

    @Override
    public SelfCheckout updateServicingT(Double time) {
        return new SelfCheckout(this.getID(), this.getStatus(), this.getCust(), this.getCust().get(0).getServiceT() + time, this.getQMax());
    }

    @Override
    public String toString() {
        return String.format("self-check %d", this.getID());
    }
}