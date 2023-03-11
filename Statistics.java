package cs2030.simulator;

class Statistics {
    private final double waitingT;
    private final int noServed;
    private final int noLeft;

    public Statistics() {
        this.waitingT = 0;
        this.noServed = 0;
        this.noLeft = 0;
    }

    private Statistics(double waitingT, int noServed, int noLeft) {
        this.waitingT = waitingT;
        this.noServed = noServed;
        this.noLeft = noLeft;
    }

    public Statistics incServed() {
        return new Statistics(this.waitingT, this.noServed + 1, this.noLeft);
    }

    public Statistics incWaitingT(double time) {
        return new Statistics(this.waitingT + time, this.noServed, this.noLeft);
    }

    public Statistics incLeft() {
        return new Statistics(this.waitingT, this.noServed, this.noLeft + 1);
    }

    public String toString() {
        return String.format("[%.3f %d %d]", (waitingT / noServed), noServed, noLeft);
    }
}