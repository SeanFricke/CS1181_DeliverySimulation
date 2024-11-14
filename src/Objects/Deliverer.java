package Objects;

public abstract class Deliverer {
    private final int id, capacity, speed, interval;

    public Deliverer(int id, int capacity, int speed, int interval) {
        this.id = id;
        this.capacity = capacity;
        this.speed = speed;
        this.interval = interval;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSpeed() {
        return speed;
    }

    public int getInterval() {
        return interval;
    }

    @Override
    public String toString() {
        return "[id=" + id + ", capacity=" + capacity + ", speed=" + speed + ", interval=" + interval;
    }
}
