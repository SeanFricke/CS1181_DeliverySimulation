package Vars;

import Events.TruckEvent;
import Objects.Drone;

/**
 * Static Vars that can be calculated right away or with the config file. These should not be changed.
 */
public class StaticVars {

    // Private for calculation simplicity
    private static final int DRONE_PACKAGES = (int) Math.ceil(Config.getPACKAGES() * (Config.PERCENT_BY_DRONE/100.0));
    private static final int TRUCK_PACKAGES = Config.getPACKAGES() - DRONE_PACKAGES;

    // Global and public, but immutable. For all files to use
    public static final int
            TOTAL_DISTANCE = Config.FIRST_HALF_DISTANCE + Config.SECOND_HALF_DISTANCE,
            NUM_TRUCKS = (int) Math.ceil(TRUCK_PACKAGES / (double) TruckEvent.capacity),
            NUM_DRONES = (int) Math.ceil(DRONE_PACKAGES / (double) Drone.capacity);

    // UNIX Terminal color codes (only for beautification, not functionality)
    public static final String
            RED_OUT = "\u001B[31m",
            RESET_OUT = "\u001B[0m";

    // Global, public AND mutable, but it is a simple boolean with no constraints
    public static boolean tracksOccupied = false;

    // Drone Package getter
    public static int getDronePackages() {
        return DRONE_PACKAGES;
    }
}
