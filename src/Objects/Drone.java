package Objects;

public class Drone extends Deliverer{
    public static final int capacity = 500, speed = 1, interval = 3;

    public Drone(int id) {
        super(id, capacity, speed, interval);
    }

    @Override
    public String toString() {
        return "Drone " + super.toString() + "]";
    }
}
