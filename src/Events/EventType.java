package Events;

/**
 * Abstract class that defines an event type that can sit in the main event queue
 */
public abstract class EventType  implements Comparable<EventType> {
    private double timeStamp;

    /**
     * Event constructor
     * @param timeStamp time stamp in which the event should do its end logic/switch to new event
     */
    public EventType(double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getTimestamp() {
        return timeStamp;
    }

    /**
     * Manually set the timestamp of an event
     * @param timestamp timestamp to set it to
     */
    public void setTimestamp(double timestamp) {
        this.timeStamp = timestamp;
    }

    /**
     * Add time to a timestamp, usually when recalculating next event state timestamp
     * @param timestamp timestamp to set it to
     */
    public void addTimestamp(double timestamp) {
        this.timeStamp += timestamp;
    }

    @Override
    public int compareTo(EventType o) {
        return Double.compare(this.getTimestamp(), o.getTimestamp());
    }
}
