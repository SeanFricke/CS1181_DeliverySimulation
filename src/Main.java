import Events.*;
import Vars.*;
import Datatypes.*;
import Objects.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// TODO Resources included Adrian, ANSI CODE table, and System.getProperty()

/**
 * Truck and drone package delivery simulation with a train obstacle.<br>
 * All base variables for the simulation can be edited through the 'Config' class as well as 'TruckEvent' and 'Drone'.
 * @author Sean Fricke
 * @since 24/11/20
 * @references
 * <ul>
 *     <li>Adrian Abbey (Senior Student as guidance)</li>
 *     <li>
 *         <a href="https://gist.github.com/JBlond/2fea43a3049b38287e5e9cefc87b2124">ANSI Code table for terminal color output</a>
 *     </li>
 *     <li>
 *         <a href="https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html">Java Docs for OS type gathering (for the color as well)</a>
 *     </li>
 * </ul>
 */
public class Main {
    // Collection Inits (Event queue, crossing queue, and list of ended trucks)
    private static PriorityQueue<EventType> mainQueue = new PriorityQueue<>();
    private static LinkedList<TruckEvent> crossingQueue = new LinkedList<>();
    private static ArrayList<TruckEvent> endArray = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<TrainBlock> trainSchedule = initTrain(); // Init train schedule

        printScenarioStats(); // Print starting stats

        Config.subPackages(StaticVars.getDronePackages()); // Preemptively subtract drone packages to only focus on trucks now

        /* Truck Spawn event creation, adding every truck start as an event to the main queue
           (not starting the trucks themselves yet) */
        for (int i = 0; i < StaticVars.NUM_TRUCKS; i++) {
            TruckSpawnEvent truckSpawnEvent = new TruckSpawnEvent(i, i * TruckEvent.interval);
            mainQueue.offer(truckSpawnEvent);
        }

        // Train schedule event creation. For every track block, create a start and end event pair.
        for (TrainBlock trainBlock : trainSchedule) {
            mainQueue.add(new TrainEvent(trainBlock.blockStart, false));
            mainQueue.add(new TrainEvent(trainBlock.blockStart + trainBlock.blockLength, true));
        }
        printTrainSchedule(trainSchedule); // Print train schedule

        // EVENT LOOP
        while (Config.getPACKAGES() > 0) {

            // Train Blocking
            if (mainQueue.peek() instanceof TrainEvent) {
                TrainEvent event = (TrainEvent) mainQueue.poll(); // Since it is a Train event, typecast it officially

                // If train is leaving
                if (event.isEndEvent()) {

                    // Log the event
                    if(System.getProperty("os.name").equals("Linux")) { // If on Linux, print in COLOR
                        System.out.printf("%s%.1f: TRAIN leaves crossing%s\n",
                                StaticVars.RED_OUT,
                                event.getTimestamp(),
                                StaticVars.RESET_OUT);
                    } else{
                        System.out.printf("%.1f: TRAIN leaves crossing\n", event.getTimestamp());
                    }

                    // If there is a queue to unload, start a Truck queue unload event with a +1 time delta
                    if (!crossingQueue.isEmpty()) {
                        double popTime = event.getTimestamp() + 1;
                        mainQueue.add(new TruckQueueEvent(popTime));
                    }
                    StaticVars.tracksOccupied = false; // Set the occupied var to false, as it is leaving

                // If train is arriving
                } else {

                    // Log the event
                    if(System.getProperty("os.name").equals("Linux")) { // If on Linux, print in COLOR
                        System.out.printf("%s%.1f: TRAIN arrives at crossing%s\n",
                                StaticVars.RED_OUT,
                                event.getTimestamp(),
                                StaticVars.RESET_OUT);
                    } else{
                        System.out.printf("%.1f: TRAIN arrives at crossing\n", event.getTimestamp());
                    }

                    StaticVars.tracksOccupied = true; // Set occupied var to true, as it is blocking the tracks now
                }

            // TruckEvent Spawning
            } else if (mainQueue.peek() instanceof TruckSpawnEvent) {
                TruckSpawnEvent event = (TruckSpawnEvent) mainQueue.poll(); // Typecast now that we know it is correct type
                System.out.printf("%.1f: Truck #%d begins journey\n", event.getTimestamp(), event.getTruckID()); // Log
                mainQueue.offer(event.spawnTruckEvent()); // Use spawnTruckEvent method to start its TruckEvent, put in main queue

            // Truck State calculations
            } else if (mainQueue.peek() instanceof TruckEvent) {
                // Change to next state
                TruckEvent event = (TruckEvent) mainQueue.poll(); // Typecast now that we know it is a TruckEvent
                nextEvent(event); // Switch to next event state and do the appropriate logic

            // Truck Queue pops
            } else if (mainQueue.peek() instanceof TruckQueueEvent) {
                TruckQueueEvent event = (TruckQueueEvent) mainQueue.poll(); // Typecasting

                // If tracks are not occupied by the time the truck attempts to cross
                if(!StaticVars.tracksOccupied) {
                    TruckEvent truckEvent = crossingQueue.poll(); // poll off the first TruckEvent

                    // Set truck event to time of crossing, switch to next event, and recalculate based on that time
                    truckEvent.setTimestamp(event.getTimestamp());
                    nextEvent(truckEvent);
                    mainQueue.offer(truckEvent); // Put truck event back onto the main event queue

                    // If there is still more to remove in a second
                    if (!crossingQueue.isEmpty()) {
                        event.addTimestamp(1);  // Add a +1 time delta
                        mainQueue.offer(event); // Put the event back on for the next truckEvent to cross
                    }

                }
                /* Nothing here, but this signifies that if there is a train again, once the truck goes to cross,
                * then we essentially throw away the TruckQueueEvent, because another one will be created again once
                * the current train leaves, and we don't want more than one of these events because that would
                * possibly try to cross 2+ trucks in an invalid timespan from then on*/
            }

        }

        // Begin end stats
        System.out.print("""
                
                Stats
                -----
                """);
        double tempTruckTotal = printTruckTotals(endArray); // Truck Stats
        double tempDroneTotal = printDronesStats(); // Drone Stats
        printGrossTotal(tempTruckTotal, tempDroneTotal); // Total stats

    }

    /**
     * Method that seamlessly switches a truckEvent to its next state in the process and does its associated logic.
     * @param truckEvent The truckEvent to switch to the next state
     */
    public static void nextEvent(TruckEvent truckEvent ) {
        switch (truckEvent.getCurrentState()){
            // Calculations when truck reaches crossing
            case TRUCK_START:
                truckEvent.setCurrentState(TruckEvent.truckState.TRUCK_AT_CROSSING); // Go to next state

                // If we cannot immediately cross tracks, get in FIFO queue
                if (StaticVars.tracksOccupied || !crossingQueue.isEmpty() ) {
                    System.out.printf("%.1f: Truck #%d waits at crossing\n", truckEvent.getTimestamp(), truckEvent.getId()); // Log
                    crossingQueue.add(truckEvent); // Add to FIFO queue
                } else {
                    nextEvent(truckEvent); // RECURSION!!!! (Immediately skip to next event if clear to cross right away)
                }
                break;

            // Calculations when truck passes the tracks
            case TRUCK_AT_CROSSING:
                System.out.printf("%.1f: Truck #%d crosses crossing\n", truckEvent.getTimestamp(), truckEvent.getId()); // Log
                truckEvent.setCurrentState(TruckEvent.truckState.TRUCK_CROSS); // Go to next state
                truckEvent.addTimestamp((double) Config.SECOND_HALF_DISTANCE / TruckEvent.speed); // Set timestamp to time of destination
                mainQueue.offer(truckEvent); // put back in main event queue
                break;
            // Calculation when truck reaches destination
            case TRUCK_CROSS:
                System.out.printf("%.1f: Truck #%d completes journey\n", truckEvent.getTimestamp(), truckEvent.getId()); // Log
                truckEvent.setCurrentState(TruckEvent.truckState.TRUCK_END); // Go to last state
                Config.subPackages(TruckEvent.capacity); // Subtract the carried packages, as they have been successfully delivered
                endArray.add(truckEvent); // Put on ended array and wait there
                break;
        }
    }

    // Stat Outputs

    /**
     * Print Truck stats to terminal after processing them
     * @param finishedTrucks List of all the finished truckEvents to gather their individual data. Should be the total sim's truck count
     * @return Total time for Gross Total logic
     */
    public static double printTruckTotals(ArrayList<TruckEvent> finishedTrucks) {
        // Stat var inits
        double totalTime = 0;
        double avgTime = 0;

        // For each truckEvent in the list of finished ones
        for (int i = 0; i < finishedTrucks.size(); i++) {
            TruckEvent truckEvent = finishedTrucks.get(i); // Get the associated TruckEvent from the index
            double travelTime = truckEvent.getTimestamp() - truckEvent.getConstructTime(); // Find time difference (travel time)
            System.out.printf("Truck #%d total trip time: %.1f minutes\n", truckEvent.getId(), travelTime); // Log
            avgTime += travelTime; // Add all travel times up for avg

            // On last truck, get the timestamp as that is the last one to finish
            if (i == finishedTrucks.size() - 1) {
                totalTime = truckEvent.getTimestamp();
            }
        }
        avgTime /= finishedTrucks.size(); // Divide the sum to get the avg
        System.out.printf(
                """
                
                Truck AVG trip time: %.1f minutes
                Truck Total time: %.1f minutes
                
                """,
                avgTime, totalTime);
        return totalTime;
    }

    /**
     * Print the start and end of the train's blocks in a nice list
     * @param trainSchedule Train schedule block array to use in list
     */
    public static void printTrainSchedule(ArrayList<TrainBlock> trainSchedule) {
        System.out.print(
                """
                Train Schedule
                --------------
                """);

        // For every block, print the start, and the end (Start + Length)
        for (TrainBlock block : trainSchedule) {
            System.out.printf("%d-%d\n", block.blockStart, block.blockStart + block.blockLength);
        }
        System.out.println(); // Blank ending line
    }

    /**
     * Print start of Scenario Stats, such as drone ratio, packages count, number of drones, and number of trucks
     */
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
    }

    /**
     * Print Drone stats to terminal after processing them
     * @return Total time for Gross Total logic
     */
    public static double printDronesStats(){
        // Stat var inits
        double tripTime = (double) StaticVars.TOTAL_DISTANCE / Drone.speed; // Trip time from distance/speed
        double totalTime = tripTime + StaticVars.NUM_DRONES * Drone.interval; // As like with trucks, total will be the end of last drone journey
        System.out.printf(
                """
                Drone trip time: %.1f minutes
                Drone total time: %.1f minutes
                
                """,
                tripTime, totalTime);
        return totalTime;
    }

    /**
     * Take both drone and truck totals and print gross total as the larger one
     * @param truckTotal End time of all trucks
     * @param droneTotal End time of all drones
     */
    public static void printGrossTotal(double truckTotal, double droneTotal) {
        double grossTotal = Math.max(truckTotal, droneTotal); // Find the larger end time
        System.out.printf("Total time: %.1f minutes", grossTotal); // Log it
    }

    // Data inits

    /**
     * Init method to read train schedule file and process it into arrays of train blocks
     * @return An array of train blocks, each with a start/length pair
     * @throws FileNotFoundException File cannot be found at the needed location
     */
    private static ArrayList<TrainBlock> initTrain() throws FileNotFoundException {
        ArrayList<TrainBlock> trainSchedule = new ArrayList<>(); // Init block array
        File file = new File(Config.TRAIN_SCHEDULE_FILE); // Pull the file data at the given file location

        // If file is found
        if (file.exists()) {
            // Turn each given Int in a line into the start/length pair of a TrainBlock datatype
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                TrainBlock block = new TrainBlock(scanner.nextInt(), scanner.nextInt());
                trainSchedule.add(block); // Add it to schedule array
            }
        } else {
            // Error catching
            throw new FileNotFoundException("Train Schedule file not found! (" + Config.TRAIN_SCHEDULE_FILE + " not found in run directory)");
        }

        return trainSchedule;
    }

}
