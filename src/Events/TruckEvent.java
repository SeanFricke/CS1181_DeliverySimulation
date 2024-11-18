package Events;

import Objects.Truck;
import GlobalVars.Config;


public class TruckEvent extends EventType{
    private final Truck truck;

    public TruckEvent(Truck truck, int constructTimestamp) {
        super(constructTimestamp + Config.FIRST_HALF_DISTANCE / Truck.speed);
        this.truck = truck;
    }

    public Truck getTruck() {
        return truck;
    }

    public void nextEvent() {
        switch (truck.getCurrentState()){
            // Calculations when truck at crossing
            case TRUCK_START:
                System.out.printf("Truck %d at crossing at minute %d\n", truck.getId(), getTimestamp());
                truck.setCurrentState(Truck.truckState.TRUCK_AT_CROSSING);
                // TODO Train track logic
                addTimestamp(90);
                break;
            // Calculations when truck passes tracks
            case TRUCK_AT_CROSSING:
                System.out.printf("Truck %d crossing tracks at minute %d\n", truck.getId(), getTimestamp());
                truck.setCurrentState(Truck.truckState.TRUCK_CROSS);
                addTimestamp(Config.SECOND_HALF_DISTANCE / Truck.speed);
                break;
            // Calculation when truck ends.
            case TRUCK_CROSS:
                System.out.printf("Truck %d at destination at minute %d\n", truck.getId(), getTimestamp());
                truck.setCurrentState(Truck.truckState.TRUCK_END);
                // TODO End of trip stats/logging
                break;
        }
    }

    @Override
    public String toString() {
        return "TruckEvent [deliverer=" + truck.toString() + ", timestamp=" + getTimestamp() + "]";
    }
}
