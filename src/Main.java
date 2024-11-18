import Events.*;
import GlobalVars.Config;
import Objects.*;
import Datatypes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Main {

    public static <TruckEven> void main(String[] args) throws FileNotFoundException {
        PriorityQueue<EventType> mainQueue = new PriorityQueue<>();
        Queue<TrainBlock> trainSchedule = initTrain();

        // Truck Spawn Event Creation
        final int numTrucks = Config.PACKAGES / Truck.capacity;
        for (int i = 0; i < numTrucks; i++) {
            TruckSpawnEvent truckSpawnEvent = new TruckSpawnEvent(i, i*Truck.interval);
            mainQueue.offer(truckSpawnEvent);
        }

        while (Config.PACKAGES > 0) {
            // Truck Spawning
            if (mainQueue.peek() instanceof TruckSpawnEvent) {
                TruckSpawnEvent truckSpawn = (TruckSpawnEvent) mainQueue.poll();
                System.out.printf("Truck %d has been created at %d\n", truckSpawn.getTruckID(), truckSpawn.getTimestamp());
                mainQueue.offer(truckSpawn.spawnTruckEvent());

            // Truck State calculations
            } else if (mainQueue.peek() instanceof TruckEvent) {

                TruckEvent truckEvent = (TruckEvent) mainQueue.poll();
                truckEvent.nextEvent();
                if (truckEvent.getTruck().getCurrentState() == Truck.truckState.TRUCK_END) {
                    Config.PACKAGES -= Truck.capacity;
                } else {
                    mainQueue.offer(truckEvent);
                }

            }

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
