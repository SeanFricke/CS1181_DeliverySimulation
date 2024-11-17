package Objects;

public class QueueEvent implements Comparable<QueueEvent> {
    private final Truck truck;
    private int timestamp;

    public QueueEvent(Truck truck, int timestamp) {
        this.truck = truck;
        this.timestamp = timestamp;
    }
    public Truck getTruck() {
        return truck;
    }
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void nextEvent() {
        switch (truck.getCurrentState()){
            // Calculations when truck at crossing
            case TRUCK_START:
                System.out.printf("Truck %d at crossing at minute %d\n", truck.getId(), timestamp);
                truck.setCurrentState(Truck.truckState.TRUCK_AT_CROSSING);
                // TODO Train track logic
                timestamp += 50;
                break;
            // Calculations when truck passes tracks
            case TRUCK_AT_CROSSING:
                System.out.printf("Truck %d crossing tracks at minute %d\n", truck.getId(), timestamp);
                truck.setCurrentState(Truck.truckState.TRUCK_CROSS);
                timestamp += 900;
                break;
            // Calculation when truck ends.
            case TRUCK_CROSS:
                System.out.printf("Truck %d at destination at minute %d\n", truck.getId(), timestamp);
                truck.setCurrentState(Truck.truckState.TRUCK_END);
                // TODO End of trip stats/logging
                break;
        }
    }

    @Override
    public int compareTo(QueueEvent o) {
        return Integer.compare(this.getTimestamp(), o.getTimestamp());
    }

    @Override
    public String toString() {
        return "QueueEvent [deliverer=" + truck.toString() + ", timestamp=" + timestamp + "]";
    }
}
