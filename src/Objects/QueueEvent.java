package Objects;

import java.io.Serializable;

public class QueueEvent<T extends Deliverer> implements Comparable<QueueEvent<T>> {
    private final T deliverer;
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

    public void nextEvent() {
        if (deliverer.getClass() == Truck.class) {
            switch (((Truck) deliverer).getCurrentState()){
                case TRUCK_START:
                    System.out.printf("Truck %d at crossing at minute %d\n", deliverer.getId(), timestamp);
                    ((Truck) deliverer).setCurrentState(Truck.truckState.TRUCK_AT_CROSSING);
                    break;
                case TRUCK_AT_CROSSING:
                    System.out.printf("Truck %d crossing tracks at minute %d\n", deliverer.getId(), timestamp);
                    // TODO Train track logic
                    ((Truck) deliverer).setCurrentState(Truck.truckState.TRUCK_CROSS);
                    timestamp += 900;
                    break;
                case TRUCK_CROSS:
                    System.out.printf("Truck %d at destination at minute %d\n", deliverer.getId(), timestamp);
                    ((Truck) deliverer).setCurrentState(Truck.truckState.TRUCK_END);
                    // TODO End of trip stats/logging
                    break;
            }
        }
    }

    @Override
    public int compareTo(QueueEvent<T> o) {
        return Integer.compare(this.getTimestamp(), o.getTimestamp());
    }

    @Override
    public String toString() {
        return "QueueEvent [deliverer=" + deliverer.toString() + ", timestamp=" + timestamp + "]";
    }
}
