package cs2030.simulator;

import cs2030.util.*;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.function.Supplier;

public class Simulate5 {
    private final PQ<Event> pq;
    private final Shop shop;

    public Simulate5(int n, List<Pair<Double, Supplier<Double>>> inputTs) {
        PQ<Event> pq = new PQ<Event>(new EventComparator());
        for (int i = 0; i < inputTs.size(); i++) {
            double arrivalT = inputTs.get(i).first();
            Supplier<Double> serviceT = inputTs.get(i).second();
            Lazy<Double> serviceTCache = Lazy.of(serviceT);
            pq = pq.add(new Arrive(new Customer(i+1, arrivalT, serviceTCache), arrivalT));
        }
        this.pq = pq;
        ImList<Server> temp = ImList.of();
        for (int j = 1; j < n+1; j++) {
            temp = temp.add(new Server(j));
        }
        this.shop = new Shop(temp);
    }

    public String run() {
        String string = this.pq.poll().first().toString();
        Shop newShop = this.pq.poll().first().execute(this.shop).second();
        PQ<Event> updatedP = this.pq.poll().second();
        PQ<Event> pqpq = this.pq.poll().first().execute(newShop).first().map(x -> updatedP.add(x)).orElse(updatedP);
        Statistics stats = this.pq.poll().first().execute(newShop).first().map(x -> x.update(new Statistics())).orElse(new Statistics());
        while (!pqpq.isEmpty()) {
            if (!pqpq.poll().first().getEventName().equals("WaitingInLine") && !pqpq.poll().first().getEventName().equals("WaitingForServer")) {
                string += String.format("\n%s", pqpq.poll().first().toString());
            }
            newShop = pqpq.poll().first().execute(newShop).second();
            Statistics statistics = stats;
            stats = pqpq.poll().first().execute(newShop).first().map(x -> x.update(statistics)).orElse(statistics);
            PQ<Event> updatedPQ = pqpq.poll().second();
            pqpq = pqpq.poll().first().execute(newShop).first().map(x -> updatedPQ.add(x)).orElse(updatedPQ);
            
        }
        return String.format("%s\n%s", string, stats.toString());
    }

    @Override
    public String toString() {
        return String.format("Queue: %s; Shop: %s", //
            this.pq.toString(), this.shop.toString());
    }
}