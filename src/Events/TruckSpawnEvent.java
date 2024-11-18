package Events;

import Objects.Truck;

public class TruckSpawnEvent extends EventType{


    private int TruckID;

    public TruckSpawnEvent(int ID, int timeStamp) {
        super(timeStamp);
        TruckID = ID;
    }

    public TruckEvent spawnTruckEvent(){
        Truck tempTruck = new Truck(TruckID);
        return new TruckEvent(tempTruck, getTimestamp());
    }

    public int getTruckID() {
        return TruckID;
    }
}
