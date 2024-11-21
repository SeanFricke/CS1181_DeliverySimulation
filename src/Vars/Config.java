package Vars;

public class Config {
    // ENVIRONMENT
    public static final int FIRST_HALF_DISTANCE = 3_000;
    public static final int SECOND_HALF_DISTANCE = 27_000;


    // SCENARIO
    private static int PACKAGES = 1_500;
    public static final int PERCENT_BY_DRONE = 25;
    public static final String TRAIN_SCHEDULE_FILE = "train_schedule.txt";

    // Getters and setters for packages
    public static void subPackages(int packages) {
        PACKAGES -= packages;
    }
    public static int getPACKAGES() {
        return PACKAGES;
    }
}
