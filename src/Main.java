import Events.*;
import Vars.*;
import Objects.*;
import Datatypes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private static PriorityQueue<EventType> mainQueue = new PriorityQueue<>();
    private static LinkedList<TruckEvent> crossingQueue = new LinkedList<>();

    public static <TruckEven> void main(String[] args) throws FileNotFoundException {
        ArrayList<TrainBlock> trainSchedule = initTrain();

        // Truck Spawn Event creation
        final int numTrucks = Config.PACKAGES / Truck.capacity;
        for (int i = 0; i < numTrucks; i++) {
            TruckSpawnEvent truckSpawnEvent = new TruckSpawnEvent(i, i*Truck.interval);
            mainQueue.offer(truckSpawnEvent);
        }

        // TODO Train Start/End Event creation
        // Train Events creation
        for (TrainBlock trainBlock : trainSchedule) {
            mainQueue.add(new TrainEvent(trainBlock.blockStart, false));
            mainQueue.add(new TrainEvent(trainBlock.blockStart + trainBlock.blockLength, true));
        }

        // EVENT LOOP
        while (Config.PACKAGES > 0) {

            // Train Blocking
            if (mainQueue.peek() instanceof TrainEvent) {
                TrainEvent event = (TrainEvent) mainQueue.poll();
                if (event.isEndEvent()) {
                    System.out.printf("Train is leaving at %d\n", event.getTimestamp());
                    if (!crossingQueue.isEmpty()) {
                        int popTime = event.getTimestamp() + 1;
                        mainQueue.add(new TruckQueueEvent(popTime));
                    }
                    GlobalVars.tracksOccupied = false;
                } else {
                    System.out.printf("Train has arrived at %d\n", event.getTimestamp());
                    GlobalVars.tracksOccupied = true;
                }

            // Truck Spawning
            } else if (mainQueue.peek() instanceof TruckSpawnEvent) {
                TruckSpawnEvent event = (TruckSpawnEvent) mainQueue.poll();
                System.out.printf("Truck %d has been created at %d\n", event.getTruckID(), event.getTimestamp());
                mainQueue.offer(event.spawnTruckEvent());

            // Truck State calculations
            } else if (mainQueue.peek() instanceof TruckEvent) {
                // Change to next state
                TruckEvent event = (TruckEvent) mainQueue.poll();
                nextEvent(event);

            // Truck Queue pops
            } else if (mainQueue.peek() instanceof TruckQueueEvent) {
                TruckQueueEvent event = (TruckQueueEvent) mainQueue.poll();
                if(!GlobalVars.tracksOccupied) {
                    TruckEvent truckEvent = crossingQueue.pop();
                    truckEvent.setTimestamp(event.getTimestamp());
                    nextEvent(truckEvent);
                    mainQueue.offer(truckEvent);
                    if (!crossingQueue.isEmpty()) {
                        event.addTimestamp(1);
                        mainQueue.offer(event);
                    }

                }
            }

        }
    }

    public static void nextEvent(TruckEvent truckEvent ) {
        Truck truck = truckEvent.getTruck();
        // State Logics
        switch (truck.getCurrentState()){
            // Calculations when truck at crossing
            case TRUCK_START:
                System.out.printf("Truck %d at tracks at minute %d\n", truck.getId(), truckEvent.getTimestamp());
                truck.setCurrentState(Truck.truckState.TRUCK_AT_CROSSING);
                if (GlobalVars.tracksOccupied || !crossingQueue.isEmpty() ) {
                    System.out.printf("Truck %d is waiting at tracks\n", truck.getId());
                    crossingQueue.add(truckEvent);
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

    private static ArrayList<TrainBlock> initTrain() throws FileNotFoundException {
        ArrayList<TrainBlock> trainSchedule;
        File file = new File(Config.TRAIN_SCHEDULE_FILE);
        trainSchedule = new ArrayList<>();
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
