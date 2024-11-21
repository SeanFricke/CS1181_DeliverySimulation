package Events;

/**
 * Event to spawn a TruckEvent at a certain point
 */
public class TruckSpawnEvent extends EventType{
    private final int TruckID; // ID to pass to the truckEvent once it is created

    /**
     * TruckSpawnEvent constructor
     * @param ID ID of truck
     * @param timeStamp time that the truckEvent should be constructed
     */
    public TruckSpawnEvent(int ID, double timeStamp) {
        super(timeStamp);
        TruckID = ID;
    }

    /**
     * Spawn a truckEvent from this event without any other arguments.
     * @return The spawned truckEvent
     */
    public TruckEvent spawnTruckEvent(){
        return new TruckEvent(getTimestamp(), TruckID);
    }

    // Getter
    public int getTruckID() {
        return TruckID;
    }
}
