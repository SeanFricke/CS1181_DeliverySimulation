package Events;

/**
 * Event to pop trucks off the crossing queue after a train leaves
 */
public class TruckQueueEvent extends EventType {
    /**
     * TruckQueueEvent Constructor
     * @param timeStamp timestamp that the next truckEvent should be pulled off the queue
     */
    public TruckQueueEvent(double timeStamp) {
        super(timeStamp);
    }
}
