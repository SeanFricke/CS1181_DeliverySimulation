import Objects.*;

import java.util.PriorityQueue;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int PACKAGES = 1_000;
        final int TOTAL_DISTANCE = 30_000;


        final int numTrucks = PACKAGES / Truck.capacity;

        PriorityQueue<QueueEvent> mainQueue = new PriorityQueue<>();
        int globalTime = 0;

        Truck truck;
        int truckCounter = 0;
        while (PACKAGES > 0) {
            if  (truckCounter < numTrucks) {
                truck = new Truck(truckCounter);
                globalTime = truckCounter * Truck.interval;
                System.out.printf("Truck %d has been created at %d\n", truckCounter, globalTime);
                QueueEvent event = new QueueEvent(truck, globalTime);
                mainQueue.add(event);
                truckCounter++;
            }

            if (mainQueue.peek().getTimestamp() <= globalTime + Truck.interval) {
                QueueEvent nextEvent = mainQueue.poll();
                globalTime = nextEvent.getTimestamp();
                nextEvent.nextEvent();
                if (nextEvent.getTruck().getCurrentState() == Truck.truckState.TRUCK_END) {
                    PACKAGES -= Truck.capacity;
                    System.out.printf("Packages left: %d\n", PACKAGES);
                } else {
                    mainQueue.offer(nextEvent);
                }

            }

        }
    }
}
