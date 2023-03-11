package cs2030.simulator;

import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
    @Override
    public int compare(Event a, Event b) {
        return a.aBeforeb(b) ? -1 : (a.aAfterb(b) ? 1 : (a.getCust().aBeforeOtherCust(b.getCust()) ? -1 : 1));
    }
}