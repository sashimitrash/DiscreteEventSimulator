package cs2030.simulator;

import cs2030.util.*;
import java.util.PriorityQueue;
import java.util.Comparator;

public class PQ<T> {
    private final PriorityQueue<T> pq;
    private final Comparator<? super T> comp;

    public PQ(Comparator<? super T> comparator) {
        this.pq = new PriorityQueue<T>(comparator);
        this.comp = comparator;
    }

    private PQ(Comparator<? super T> comparator, PriorityQueue<T> pq) {
        this.pq = pq;
        this.comp = comparator;
    }

    public PQ<T> add(T item) {
        PriorityQueue<T> newPQ = new PriorityQueue<T>(this.comp);
        this.pq.forEach(x -> newPQ.add(x));
        newPQ.add(item);
        return new PQ<T>(this.comp, newPQ);
    }

    public Pair<T, PQ<T>> poll() {
        PQ<T> newPQ = new PQ<T>(this.comp);
        for (T thing : this.pq) {
            newPQ = newPQ.add(thing);
        }
        T removed = newPQ.pq.poll();
        return Pair.of(removed, newPQ);
    }

    public PriorityQueue<T> getPQ() {
        return this.pq;
    }

    public boolean isEmpty() {
        return this.pq.isEmpty();
    }

    @Override
    public String toString() {
        return this.pq.toString();
    }
} 