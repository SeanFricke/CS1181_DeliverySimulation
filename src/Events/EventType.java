package Events;

public abstract class EventType {
    private int timeStamp;

    public EventType(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getTimestamp() {
        return timeStamp;
    }

    public void setTimestamp(int timestamp) {
        this.timeStamp = timeStamp;
    }

    public void addTimestamp(int time) {
        this.timeStamp += time;
    }
}
