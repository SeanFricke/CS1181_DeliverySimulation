package Events;

/**
 * TrainEvent for when the train arrives or leaves
 */
public class TrainEvent extends EventType{
    private final boolean isEndEvent;

    /**
     * TrainEvent constructor
     * @param timeStamp Timestamp at which the train arrives/leaves
     * @param isEndEvent True if train leaves, False if it arrives
     */
    public TrainEvent(double timeStamp, boolean isEndEvent) {
        super(timeStamp);
        this.isEndEvent = isEndEvent;
    }

    public boolean isEndEvent() {
        return isEndEvent;
    }

}
