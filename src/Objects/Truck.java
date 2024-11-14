package Objects;

public class Truck extends Deliverer{
    private enum truckState {
        TRUCK_START,
        TRUCK_AT_CROSSING,
        TRUCK_CROSS,
        TRUCK_END
    }

    private truckState currentEvent;

    public Truck(int id) {
        super(id, 10, 30, 15);
        currentEvent = truckState.TRUCK_START;
    }

    public truckState getCurrentState() {
        return currentEvent;
    }

    @Override
    public String toString() {
        return "Truck " + super.toString();
    }
}
