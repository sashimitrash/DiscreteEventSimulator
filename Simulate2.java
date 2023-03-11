package cs2030.simulator;

import cs2030.util.*;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class Simulate2 {
    private final PQ<Event> pq;
    private final Shop shop;

    public Simulate2(int n, List<Double> arrivalTs) {
        PQ<Event> pq = new PQ<Event>(new EventComparator());
        for (int i = 0; i < arrivalTs.size(); i++) {
            double timing = arrivalTs.get(i);
            pq = pq.add(new EventStub(new Customer(i+1, timing), timing));
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
        PQ<Event> pqpq = this.pq.poll().second();
        while (!pqpq.isEmpty()) {
            string += String.format("\n%s", pqpq.poll().first().toString());
            pqpq = pqpq.poll().second();
        }
        return String.format("%s\n%s", string, "-- End of Simulation --");
    }

    @Override
    public String toString() {
        return String.format("Queue: %s; Shop: %s", //
            this.pq.toString(), this.shop.toString());
    }
}