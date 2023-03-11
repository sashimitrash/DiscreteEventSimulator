package cs2030.simulator;

import cs2030.util.*;
import java.util.Optional;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.function.Supplier;

public class Simulate8 {
    private final PQ<Event> pq;
    private final Shop shop;

    public Simulate8(int n, int numOfSelfChecks, List<Pair<Double, Supplier<Double>>> inputTs, int qmax, Supplier<Double> restTimes) {
        PQ<Event> pq = new PQ<Event>(new EventComparator());
        for (int i = 0; i < inputTs.size(); i++) {
            double arrivalT = inputTs.get(i).first();
            Supplier<Double> serviceT = inputTs.get(i).second();
            Lazy<Double> serviceTCache = Lazy.of(serviceT);
            pq = pq.add(new Arrive(new Customer(i + 1, arrivalT, serviceTCache), arrivalT));
        }
        this.pq = pq;
        ImList<Server> temp = ImList.of();
        for (int j = 1; j < n+1; j++) {
            temp = temp.add(new Server(j, qmax, restTimes));
        }
        if (numOfSelfChecks > 0) {
            temp = temp.add(new SelfCheckout(n+1, qmax));
            for (int k = n+2; k < numOfSelfChecks + n + 1; k++) {
                temp = temp.add(new SelfCheckout(k, 0));
            }
        }
        this.shop = new Shop(temp, n, numOfSelfChecks);
    }

    public String run() {
        String string = this.pq.poll().first().toString();
        Shop newShop = this.pq.poll().first().execute(this.shop).second();
        PQ<Event> updatedP = this.pq.poll().second();
        Pair<Optional<Event>, Shop> firstExecuted = this.pq.poll().first().execute(newShop);
        PQ<Event> pqpq = firstExecuted.first().map(x -> updatedP.add(x)).orElse(updatedP);
        Statistics stats = firstExecuted.first().map(x -> x.update(new Statistics())).orElse(new Statistics());
        while (!pqpq.isEmpty()) {
            if (!pqpq.poll().first().getEventName().equals("WaitingInLine") && !pqpq.poll().first().getEventName().equals("WaitingForServer")) {
                string += String.format("\n%s", pqpq.poll().first().toString());
            }
            Pair<Optional<Event>, Shop> executed = pqpq.poll().first().execute(newShop);
            newShop = executed.second();
            Statistics statistics = stats;
            Optional<Event> event = executed.first();
            stats = event.map(x -> x.update(statistics)).orElse(statistics);
            PQ<Event> updatedPQ = pqpq.poll().second();
            pqpq = event.map(x -> updatedPQ.add(x)).orElse(updatedPQ);
        }
        return String.format("%s\n%s", string, stats.toString());
    }

    @Override
    public String toString() {
        return String.format("Queue: %s; Shop: %s", //
            this.pq.toString(), this.shop.toString());
    }
}