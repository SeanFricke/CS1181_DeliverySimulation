package Events;

import Vars.*;

/**
 * Event that simulates a truck moving to destination, and can sit on the main event queue
 */
public class TruckEvent extends EventType {
    // Change these to affect truck behavior
    public static final int capacity = 10, speed = 30, interval = 15;

    // Var inits
    public final double constructTime;
    private final int id;
    private truckState currentEvent;

    // Enum for the truck states
    public enum truckState {
        TRUCK_START,
        TRUCK_AT_CROSSING,
        TRUCK_CROSS,
        TRUCK_END
    }

    /**
     * TruckEvent constructor
     * @param constructTimestamp time at which the TruckEvent was constructed
     * @param id id of truck
     */
    public TruckEvent(double constructTimestamp, int id) {
        // Set inital timestamp to the time to get to the crossing, offset by the construction time
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

    /**
     * Manually set this TruckEvent to a state
     * @param currentEvent State to set this TruckEvent to
     */
    public void setCurrentState(truckState currentEvent) {
        this.currentEvent = currentEvent;
    }

    public truckState getCurrentState() {
        return currentEvent;
    }

}
