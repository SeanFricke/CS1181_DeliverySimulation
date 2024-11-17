package Objects;

import Datatypes.TrainBlock;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Train {
    private Queue<TrainBlock> trainSchedule;
    public Train() throws FileNotFoundException {
        File file = new File("train_schedule.txt");
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
    }

    public Queue<TrainBlock> getTrainSchedule() {
        return trainSchedule;
    }


}
