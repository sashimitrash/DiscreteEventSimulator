package cs2030.simulator;

import java.util.Optional;
import cs2030.util.Pair;

public interface Event {
    Pair<Optional<Event>, Shop> execute(Shop shop);
    
    boolean aBeforeb(Event b);

    boolean aAfterb(Event b);

    double getEventT();

    String getEventName();

    Customer getCust();

    Statistics update(Statistics statistics);
}