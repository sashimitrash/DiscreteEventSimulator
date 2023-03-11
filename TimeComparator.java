package cs2030.simulator;

import java.util.Comparator;

public class TimeComparator implements Comparator<Server> {
    @Override
    public int compare(Server a, Server b) {
        return (a.getDoneT() < b.getDoneT()) ? -1 : 1;
    }
}