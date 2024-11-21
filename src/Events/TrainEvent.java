package Events;

public class TrainEvent extends EventType{
    private final boolean isEndEvent;
    public TrainEvent(double timeStamp, boolean isEndEvent) {
        super(timeStamp);
        this.isEndEvent = isEndEvent;
    }

    public boolean isEndEvent() {
        return isEndEvent;
    }

}
