package Events;

import Objects.Truck;
import Vars.*;

import java.util.LinkedList;


public class TruckEvent extends EventType{
    private final Truck truck;

    public TruckEvent(Truck truck, int constructTimestamp) {
        super(constructTimestamp + Config.FIRST_HALF_DISTANCE / Truck.speed);
        this.truck = truck;
    }

    public Truck getTruck() {
        return truck;
    }

    @Override
    public String toString() {
        return "TruckEvent [deliverer=" + truck.toString() + ", timestamp=" + getTimestamp() + "]";
    }
}
