# Delivery Simulation
## Synopsis
Simulation to find optimal number of trucks vs drones.
There will be a train running between start and end.
Shortest delivery time is optimization value.
Trucks and drones do not need to be considered for their return to start. Every 'delivery object' will make a single trip.

## Measurements
### Environment
- 1,500 packages/day
- 30,000 units total distance
  - 3,000 units from start to tracks
  - 27,000 units from tracks to end
### Trucks
- 10 pkg capacity
- 30 units/min speed
- Interval every 15 minutes
### Drones
- 1 pkg capacity
- 500 units/min speed
- Interval every 3 minutes
## Ratio Variability
- Variable called `PERCENT_BY_DRONE`
- percent number of packages that will be delivered by drone (not count)
- round up when dividing into count of drones/trucks
## Truck Calculations
- Discrete event simulation must be used
- Events to be used:
  - `TRUCK_START` 
    - Starts at 0.0, and intervals afterward
    - Simple mathematical distance calc to determine next event
  - `TRUCK_AT_CROSSING`
    - If no train blocking crossing, immediately go to next event
    - If train is blocking crossing, wait, and put in FIFO queue
    - If line is already present, add to queue
    - 1 Minute intervals for trucks to leave queue
    - 1 Minute delay after train leaves to start moving queue
  - `TRUCK_CROSS`
    - Event happens after truck crosses track (leaves queue or equivalent)
    - Simple mathematical distance calc to determine next event
  - `TRUCK_END`
    - Event happens when truck reaches end.
    - Record statistics and time at this point
## Drone Calculations
- Calulate single drone trip time, and total drone trip time
- Drone starts at 0.0
- Simple mathematical distance calc to determine end time
## Event sorting
- Must be done with a priority queue
- Priority queue will have an event object inside it
- Event objects will have a reference to the truck/drones object and a minute 'timestamp' of next event
- Once a truck has a TRUCK_AT_CROSSING event, if there is a TRAIN_STOPPED event, wait until it is done