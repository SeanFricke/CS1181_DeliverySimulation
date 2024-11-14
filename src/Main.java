import Objects.*;

import java.util.PriorityQueue;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<QueueEvent<Deliverer>> mainQueue = new PriorityQueue<>();
        Random rand = new Random();

        Truck testTruck;
        for (int i = 0; i < 10; i++) {
                testTruck = new Truck(i);
                QueueEvent<Deliverer> event = new QueueEvent<>(testTruck, rand.nextInt(200));
                System.out.println(event);
                mainQueue.add(event);
        }

        System.out.println("--- Pop Test ---");
        while(!mainQueue.isEmpty()) {
            System.out.println(mainQueue.poll());
        }
    }
}
