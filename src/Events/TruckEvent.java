package Events;

import Vars.*;

public class TruckEvent extends EventType{
    public static final int capacity = 10, speed = 30, interval = 15;

    public final double constructTime;
    private final int id;
    private truckState currentEvent;

    public enum truckState {
        TRUCK_START,
        TRUCK_AT_CROSSING,
        TRUCK_CROSS,
        TRUCK_END
    }

    public TruckEvent(double constructTimestamp, int id) {
        super(constructTimestamp + (double) Config.FIRST_HALF_DISTANCE / speed);
        this.id = id;
        currentEvent = truckState.TRUCK_START;
        constructTime = constructTimestamp;
    }

    public int getId() {
        return id;
    }

    public double getConstructTime() {
        return constructTime;
    }

    public void setCurrentState(truckState currentEvent) {
        this.currentEvent = currentEvent;
    }

    public truckState getCurrentState() {
        return currentEvent;
    }

}
