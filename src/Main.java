import Events.*;
import Vars.*;
import Datatypes.*;
import Objects.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// TODO Resources included Adrian, ANSI CODE table, and System.getProperty()
public class Main {
    private static PriorityQueue<EventType> mainQueue = new PriorityQueue<>();
    private static LinkedList<TruckEvent> crossingQueue = new LinkedList<>();
    private static ArrayList<TruckEvent> endArray = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<TrainBlock> trainSchedule = initTrain();
        printScenarioStats();

        Config.subPackages(StaticVars.DRONE_PACKAGES);

        // Truck Spawn event creation
        for (int i = 0; i < StaticVars.NUM_TRUCKS; i++) {
            TruckSpawnEvent truckSpawnEvent = new TruckSpawnEvent(i, i*TruckEvent.interval);
            mainQueue.offer(truckSpawnEvent);
        }

        // Train schedule event creation and print
        for (TrainBlock trainBlock : trainSchedule) {
            mainQueue.add(new TrainEvent(trainBlock.blockStart, false));
            mainQueue.add(new TrainEvent(trainBlock.blockStart + trainBlock.blockLength, true));
        }
        printTrainSchedule(trainSchedule);

        // EVENT LOOP
        while (Config.getPACKAGES() > 0) {

            // Train Blocking
            if (mainQueue.peek() instanceof TrainEvent) {
                TrainEvent event = (TrainEvent) mainQueue.poll();
                if (event.isEndEvent()) {
                    if(System.getProperty("os.name").equals("Linux")) {
                        System.out.printf("%s%.1f: TRAIN leaves crossing%s\n",
                                StaticVars.RED_OUT,
                                event.getTimestamp(),
                                StaticVars.RESET_OUT);
                    } else{
                        System.out.printf("%.1f: TRAIN leaves crossing\n", event.getTimestamp());
                    }

                    if (!crossingQueue.isEmpty()) {
                        double popTime = event.getTimestamp() + 1;
                        mainQueue.add(new TruckQueueEvent(popTime));
                    }
                    StaticVars.tracksOccupied = false;
                } else {
                    if(System.getProperty("os.name").equals("Linux")) {
                        System.out.printf("%s%.1f: TRAIN arrives at crossing%s\n",
                                StaticVars.RED_OUT,
                                event.getTimestamp(),
                                StaticVars.RESET_OUT);
                    } else{
                        System.out.printf("%.1f: TRAIN arrives at crossing\n", event.getTimestamp());
                    }

                    StaticVars.tracksOccupied = true;
                }

            // Truck Spawning
            } else if (mainQueue.peek() instanceof TruckSpawnEvent) {
                TruckSpawnEvent event = (TruckSpawnEvent) mainQueue.poll();
                System.out.printf("%.1f: Truck #%d begins journey\n", event.getTimestamp(), event.getTruckID());
                mainQueue.offer(event.spawnTruckEvent());

            // Truck State calculations
            } else if (mainQueue.peek() instanceof TruckEvent) {
                // Change to next state
                TruckEvent event = (TruckEvent) mainQueue.poll();
                nextEvent(event);

            // Truck Queue pops
            } else if (mainQueue.peek() instanceof TruckQueueEvent) {
                TruckQueueEvent event = (TruckQueueEvent) mainQueue.poll();
                if(!StaticVars.tracksOccupied) {
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
        System.out.print("""
                
                Stats
                -----
                """);
        double tempTruckTotal = printTruckTotals(endArray);
        double tempDroneTotal = printDronesStats();
        printGrossTotal(tempTruckTotal, tempDroneTotal);

    }

    public static void nextEvent(TruckEvent truckEvent ) {
        // State Logics
        switch (truckEvent.getCurrentState()){
            // Calculations when truck at crossing
            case TRUCK_START:
                truckEvent.setCurrentState(TruckEvent.truckState.TRUCK_AT_CROSSING);
                if (StaticVars.tracksOccupied || !crossingQueue.isEmpty() ) {
                    System.out.printf("%.1f: Truck #%d waits at crossing\n", truckEvent.getTimestamp(), truckEvent.getId());
                    crossingQueue.add(truckEvent);
                } else {
                    nextEvent(truckEvent); // RECURSION!!!!
                }
                break;
            // Calculations when truck passes tracks
            case TRUCK_AT_CROSSING:
                System.out.printf("%.1f: Truck #%d crosses crossing\n", truckEvent.getTimestamp(), truckEvent.getId());
                truckEvent.setCurrentState(TruckEvent.truckState.TRUCK_CROSS);
                truckEvent.addTimestamp((double) Config.SECOND_HALF_DISTANCE / TruckEvent.speed);
                mainQueue.offer(truckEvent);
                break;
            // Calculation when truck ends.
            case TRUCK_CROSS:
                System.out.printf("%.1f: Truck #%d completes journey\n", truckEvent.getTimestamp(), truckEvent.getId());
                truckEvent.setCurrentState(TruckEvent.truckState.TRUCK_END);
                Config.subPackages(TruckEvent.capacity);
                endArray.add(truckEvent);
                break;
        }
    }

    // Stat outputs
    public static double printTruckTotals(ArrayList<TruckEvent> finishedTrucks) {
        double totalTime = 0;
        double avgTime = 0;
        for (int i = 0; i < finishedTrucks.size(); i++) {
            TruckEvent truckEvent = finishedTrucks.get(i);
            double travelTime = truckEvent.getTimestamp() - truckEvent.getConstructTime();
            System.out.printf("Truck #%d total trip time: %.1f minutes\n", truckEvent.getId(), travelTime);
            avgTime += truckEvent.getTimestamp();
            if (i == finishedTrucks.size() - 1) {
                totalTime = truckEvent.getTimestamp();
            }
        }
        avgTime /= finishedTrucks.size();
        System.out.printf(
                """
                
                Truck AVG trip time: %.1f minutes
                Truck Total time: %.1f minutes
                
                """,
                avgTime, totalTime);
        return totalTime;
    }

    public static void printTrainSchedule(ArrayList<TrainBlock> trainSchedule) {
        System.out.print(
                """
                Train Schedule
                --------------
                """);
        for (TrainBlock block : trainSchedule) {
            System.out.printf("%d-%d\n", block.blockStart, block.blockStart + block.blockLength);
        }
        System.out.println();
    }

    public static void printScenarioStats(){
        System.out.printf(
                """
                With %d%% drones and %d packages,
                There will be:
                -%d drones
                -%d trucks
                """,
                Config.PERCENT_BY_DRONE, Config.getPACKAGES(),
                StaticVars.NUM_DRONES, StaticVars.NUM_TRUCKS);
        System.out.println();
    }

    public static double printDronesStats(){
        double tripTime = (double) StaticVars.TOTAL_DISTANCE / Drone.speed;
        double totalTime = tripTime + StaticVars.NUM_DRONES * Drone.interval;
        System.out.printf(
                """
                
                Drone trip time: %.1f minutes
                Drone total time: %.1f minutes
                
                """,
                tripTime, totalTime);
        return totalTime;
    }

    public static void printGrossTotal(double truckTotal, double droneTotal) {
        double grossTotal = Math.max(truckTotal, droneTotal);
        System.out.printf("Total time: %.1f minutes", grossTotal);
    }

    // Data inits
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
