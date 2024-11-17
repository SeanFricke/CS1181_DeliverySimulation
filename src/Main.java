import Objects.*;

import java.util.PriorityQueue;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int PACKAGES = 1000;

        final int numTrucks = PACKAGES / 10;

        PriorityQueue<QueueEvent> mainQueue = new PriorityQueue<>();
        int globalTime = 0;

        Truck truck;
        for (int i = 0; i < 10; i++) {
            globalTime = 15 * i;
            truck = new Truck(i);
            System.out.printf("Truck %d has been created at %d\n", i, globalTime);
            QueueEvent event = new QueueEvent(truck, globalTime + 100);
            mainQueue.add(event);
            if (mainQueue.peek().getTimestamp() <= globalTime + 15) {
                QueueEvent nextEvent = mainQueue.poll();
                globalTime = nextEvent.getTimestamp();
                if (nextEvent.getTruck().getCurrentState() != Truck.truckState.TRUCK_END) {
                    nextEvent.nextEvent();
                    mainQueue.offer(nextEvent);
                }
            }

        }
    }
}
