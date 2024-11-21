package Events;

public class TruckSpawnEvent extends EventType{


    private int TruckID;

    public TruckSpawnEvent(int ID, double timeStamp) {
        super(timeStamp);
        TruckID = ID;
    }

    public TruckEvent spawnTruckEvent(){
        return new TruckEvent(getTimestamp(), TruckID);
    }

    public int getTruckID() {
        return TruckID;
    }
}
