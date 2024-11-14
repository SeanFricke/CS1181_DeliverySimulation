package Objects;

import java.io.Serializable;

public class QueueEvent<T extends Deliverer> implements Comparable<QueueEvent<T>> {
    private T deliverer;
    private int timestamp;

    public QueueEvent(T deliverer, int timestamp) {
        this.deliverer = deliverer;
        this.timestamp = timestamp;
    }
    public T getDeliverer() {
        return deliverer;
    }
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(QueueEvent<T> o) {
        return Integer.compare(this.getTimestamp(), o.getTimestamp()) * -1;
    }
}
