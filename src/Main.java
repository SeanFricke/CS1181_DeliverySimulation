import Events.TruckEvent;
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

    public static void main(String[] args) throws FileNotFoundException {
        PriorityQueue<TruckEvent> mainQueue = new PriorityQueue<>();
        Queue<TrainBlock> trainSchedule = initTrain(Config.TRAIN_SCHEDULE_FILE);

        // Truck Spawn Event Creation
        final int numTrucks = Config.PACKAGES / Truck.capacity;
        Truck truck;
        for (int i = 0; i < numTrucks; i++) {

        }



        int globalTime = 0;
        while (Config.PACKAGES > 0) {
            // TODO Refactor truck spawning using TruckSpawnEvents
//            if  (truckCounter < numTrucks) {
//                truck = new Truck(truckCounter);
//                globalTime = truckCounter * Truck.interval;
//                System.out.printf("Truck %d has been created at %d\n", truckCounter, globalTime);
//                TruckEvent event = new TruckEvent(truck, globalTime);
//                mainQueue.add(event);
//                truckCounter++;
//            }

            // Event check & recalculate
            if (mainQueue.peek().getTimestamp() <= globalTime + Truck.interval) {
                TruckEvent nextEvent = mainQueue.poll();
                globalTime = nextEvent.getTimestamp();
                nextEvent.nextEvent();
                if (nextEvent.getTruck().getCurrentState() == Truck.truckState.TRUCK_END) {
                    Config.PACKAGES -= Truck.capacity;
                } else {
                    mainQueue.offer(nextEvent);
                }

            }

        }
    }

    private static Queue<TrainBlock> initTrain(String scheduleFile) throws FileNotFoundException {
        Queue<TrainBlock> trainSchedule;
        File file = new File(scheduleFile);
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
