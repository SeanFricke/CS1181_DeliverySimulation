package Objects;

public class Truck extends Deliverer{
    public static final int capacity = 10, speed = 30, interval = 15;

    public enum truckState {
        TRUCK_START,
        TRUCK_AT_CROSSING,
        TRUCK_CROSS,
        TRUCK_END
    }

    private truckState currentEvent;

    public Truck(int id) {
        super(id,capacity,speed,interval);
        currentEvent = truckState.TRUCK_START;
    }

    public truckState getCurrentState() {

        return currentEvent;
    }

    public void setCurrentState(truckState currentEvent) {
        this.currentEvent = currentEvent;
    }

    @Override
    public String toString() {
        return "Truck " + super.toString() + ", currentEvent: " + currentEvent + "]";
    }
}
