package Events;

public abstract class EventType  implements Comparable<EventType> {
    private double timeStamp;

    public EventType(double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getTimestamp() {
        return timeStamp;
    }

    public void setTimestamp(double timestamp) {
        this.timeStamp = timestamp;
    }

    public void addTimestamp(double time) {
        this.timeStamp += time;
    }

    @Override
    public int compareTo(EventType o) {
        return Double.compare(this.getTimestamp(), o.getTimestamp());
    }
}
