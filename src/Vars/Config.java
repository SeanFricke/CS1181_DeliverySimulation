package Vars;

/**
 * Configurable variables to set up a scenario for the simulation
 */
public class Config {
    // ENVIRONMENT
    public static final int FIRST_HALF_DISTANCE = 3_000;
    public static final int SECOND_HALF_DISTANCE = 27_000;


    // SCENARIO
    private static int PACKAGES = 1_500;
    public static final int PERCENT_BY_DRONE = 25;
    public static final String TRAIN_SCHEDULE_FILE = "train_schedule.txt";

    // Getters and setters for packages (no need to mess with these)

    /**
     * Subtract a given amount of packages from the package 'pile'
     * @param packages number of packages to subtract
     */
    public static void subPackages(int packages) {
        PACKAGES -= packages;
    }
    public static int getPACKAGES() {
        return PACKAGES;
    }
}
