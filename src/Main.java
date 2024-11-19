import Events.*;
import Vars.*;
import Objects.*;
import Datatypes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private static PriorityQueue<EventType> mainQueue = new PriorityQueue<>();
    private static LinkedList<Truck> crossingQueue = new LinkedList<>();

    public static <TruckEven> void main(String[] args) throws FileNotFoundException {
        Queue<TrainBlock> trainSchedule = initTrain();

        // Truck Spawn Event Creation
        final int numTrucks = Config.PACKAGES / Truck.capacity;
        for (int i = 0; i < numTrucks; i++) {
            TruckSpawnEvent truckSpawnEvent = new TruckSpawnEvent(i, i*Truck.interval);
            mainQueue.offer(truckSpawnEvent);
        }

        // TODO Train Start/End Event creation

        // EVENT LOOP
        while (Config.PACKAGES > 0) {
            // Truck Spawning
            if (mainQueue.peek() instanceof TruckSpawnEvent) {
                TruckSpawnEvent truckSpawn = (TruckSpawnEvent) mainQueue.poll();
                System.out.printf("Truck %d has been created at %d\n", truckSpawn.getTruckID(), truckSpawn.getTimestamp());
                mainQueue.offer(truckSpawn.spawnTruckEvent());

            // Truck State calculations
            } else if (mainQueue.peek() instanceof TruckEvent) {
                // Change to next state
                TruckEvent truckEvent = (TruckEvent) mainQueue.poll();
                nextEvent(truckEvent);
            }

        }
    }

    public static void nextEvent(TruckEvent truckEvent ) {
        Truck truck = truckEvent.getTruck();
        // State Logics
        switch (truck.getCurrentState()){
            // Calculations when truck at crossing
            case TRUCK_START:
                System.out.printf("Truck %d at crossing at minute %d\n", truck.getId(), truckEvent.getTimestamp());
                truck.setCurrentState(Truck.truckState.TRUCK_AT_CROSSING);
                if (GlobalVars.tracksOccupied || !crossingQueue.isEmpty() ) {
                    crossingQueue.add(truck);
                } else {
                    nextEvent(truckEvent); // RECURSION!!!!
                }
                break;
            // Calculations when truck passes tracks
            case TRUCK_AT_CROSSING:
                System.out.printf("Truck %d crossing tracks at minute %d\n", truck.getId(), truckEvent.getTimestamp());
                truck.setCurrentState(Truck.truckState.TRUCK_CROSS);
                truckEvent.addTimestamp(Config.SECOND_HALF_DISTANCE / Truck.speed);
                mainQueue.offer(truckEvent);
                break;
            // Calculation when truck ends.
            case TRUCK_CROSS:
                System.out.printf("Truck %d at destination at minute %d\n", truck.getId(), truckEvent.getTimestamp());
                truck.setCurrentState(Truck.truckState.TRUCK_END);
                Config.PACKAGES -= Truck.capacity;
                // TODO End of trip stats/logging
                break;
        }
    }

    private static Queue<TrainBlock> initTrain() throws FileNotFoundException {
        Queue<TrainBlock> trainSchedule;
        File file = new File(Config.TRAIN_SCHEDULE_FILE);
        trainSchedule = new ArrayDeque<>();
        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                TrainBlock block = new TrainBlock(scanner.nextInt(), scanner.nextInt());
                trainSchedule.add(block);
            }
        } else {
            throw new FileNotFoundException("Train Schedule file not found! (train_schedule.txt in run directory)");
        }

        return trainSchedule;
    }

}
