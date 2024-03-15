package com.simulation;

import java.util.*;
import java.util.stream.Collectors;

public class ElevatorSimulation {
    static final int MAX_PEOPLE = 10;
    public final int numElevators;
    public final int numFloors;
    public final List<Elevator> elevators;
    public final List<List<Person>> waitingLists;
    public final Random random;

    public ElevatorSimulation(int numElevators, int numFloors) {
        this.numElevators = numElevators;
        this.numFloors = numFloors;
        this.random = new Random();

        // Initialize elevators
        elevators = new ArrayList<>();
        for (int i = 0; i < numElevators; i++) {
            elevators.add(new Elevator(i, 1, Direction.UP));
        }

        // Initialize waiting lists
        waitingLists = new ArrayList<>();
        for (int i = 0; i < numFloors; i++) {
            waitingLists.add(new ArrayList<>());
        }
    }

    public void runSimulation(int numIterations) {
        for (int i = 0; i < numIterations; i++) {
            // Generate new arrivals for new people who want to access the elevator
            generateNewArrivals();

            // Handle elevator requests
            for (int floor = 0; floor < numFloors; floor++) {
                List<Person> people = waitingLists.get(floor);
                if (!people.isEmpty()) {
                    Direction direction = getDirectionForFloor(floor);
                    List<Elevator> candidateElevators = getCandidateElevators(floor, direction);
                    if (!candidateElevators.isEmpty()) {
                        Elevator elevator = getBestElevator(candidateElevators, floor);
                        elevator.addStop(floor);
                        people.forEach(elevator::addPassenger);
                        people.clear();
                    }
                }
            }

            System.out.println("\n\nIteration " + (i + 1) + ":----------------------------------------------------------------");

            // Move elevators
            for (Elevator elevator : elevators) {
                if (elevator.getDirection() == Direction.UP) {
                    if (elevator.getCurrentFloor() < numFloors) {
                        elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
                    } else {
                        elevator.reverseDirection();
                    }
                } else {
                    if (elevator.getCurrentFloor() > 1) {
                        elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
                    } else {
                        elevator.reverseDirection();
                    }
                }
                elevator.removeStops();
                System.out.println("Remove from Elevator " + elevator.getId() + " (" + elevator.removePassengers() + ")");
//                int size = elevator.removePassengers().size();
//                if(size > 0) {
//                    System.out.println("Removing " + size + " person(s) from Elevator " + elevator.id );
//                }
            }

            // Print elevator status
            for (Elevator elevator : elevators) {
                System.out.println("Elevator " + elevator.getId() + ": " + elevator.toString());
            }
        }
    }

    private void generateNewArrivals() {
        for (int floor = 1; floor < numFloors; floor++) {
            // People arriving at other floors
            int numArrivals = getPoisson(1.0 / 6);
            for (int j = 0; j < numArrivals; j++) {
                int destFloor = getDestinationFloor(floor);
                waitingLists.get(floor).add(new Person(destFloor));
            }
        }
    }

    /**
     * Generates a random integer that follows the Poisson distribution with a mean of lambda.
     * The Poisson distribution models the number of occurrences of an event in a fixed interval of time or space, given the average rate of occurrence.
     * This method uses the exponential distribution and inverse transform sampling to generate the random value.
     *
     * @param lambda the mean of the Poisson distribution
     * @return a random integer that follows the Poisson distribution with a mean of lambda
     */
    private int getPoisson(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= random.nextDouble();
        } while
        (p > L);

        return k - 1;
    }

    private int getDestinationFloor(int currentFloor) {
        if (currentFloor == 1) {
            if (random.nextDouble() < 0.2) {
                // Go to cafeteria on floor 7 (20% of the time)
                return 7;
            } else {
                // Go to a random floor (except 1)
                int destFloor;
                destFloor = random.nextInt(numFloors - 1) + 2;
                return destFloor;
            }
        } else {
            // Go to a random floor
            int destFloor;
            do {
                destFloor = random.nextInt(numFloors - 1) + 1;
            } while (destFloor == currentFloor);
            return destFloor;
        }
    }

    private Direction getDirectionForFloor(int floor) {
        if (floor == 1) {
            return Direction.UP;
        } else if (floor == numFloors) {
            return Direction.DOWN;
        } else {
            return random.nextDouble() < 0.5 ? Direction.UP : Direction.DOWN;
        }
    }

    private List<Elevator> getCandidateElevators(int floor, Direction direction) {
        return elevators.stream()
                .filter(elevator -> elevator.getDirection() == direction)
                .filter(elevator -> elevator.canAddStop(floor))
                .collect(Collectors.toList());
    }

    private Elevator getBestElevator(List<Elevator> candidateElevators, int floor) {
        // Choose the closest elevator among the candidates
        int minDistance = Integer.MAX_VALUE;
        Elevator bestElevator = null;
        for (Elevator elevator : candidateElevators) {
            int distance = Math.abs(elevator.getCurrentFloor() - floor);
            if (distance < minDistance) {
                minDistance = distance;
                bestElevator = elevator;
            }
        }
        return bestElevator;
    }

    public static void main(String[] args) {
        ElevatorSimulation simulation = new ElevatorSimulation(5, 10);
        simulation.runSimulation(100);
    }
}
