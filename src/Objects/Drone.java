package Objects;

public class Drone extends Deliverer{
    public Drone(int id) {
        super(id, 1, 500, 3);
    }

    @Override
    public String toString() {
        return "Drone " + super.toString();
    }
}
