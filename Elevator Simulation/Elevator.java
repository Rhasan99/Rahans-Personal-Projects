package com.simulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.simulation.ElevatorSimulation.MAX_PEOPLE;

public class Elevator {
    private final int id;
    private int currentFloor;
    private Direction direction;
    private final List<Integer> stops;
    private final List<Person> passengers;

    public Elevator(int id, int currentFloor, Direction direction) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.direction = direction;
        this.stops = new ArrayList<>();
        this.passengers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void addStop(int floor) {
        if (!stops.contains(floor)) {
            stops.add(floor);
        }
    }

    public void removeStops() {
        stops.removeIf(floor -> floor == currentFloor);

    }

    public boolean canAddStop(int floor) {
        return direction == Direction.UP && floor > currentFloor ||
                direction == Direction.DOWN && floor < currentFloor ||
                direction == Direction.UP && stops.stream().allMatch(stop -> stop > currentFloor) ||
                direction == Direction.DOWN && stops.stream().allMatch(stop -> stop < currentFloor);
    }

    public boolean isIdle() {
        return stops.isEmpty() && passengers.isEmpty();
    }

    public boolean isFull() {
        return passengers.size() >= MAX_PEOPLE; // static final MAX_PEOPLE = 10;
    }

    public boolean addPassenger(Person person) {
        if (isFull()) {
            return false;
        } else {
            passengers.add(person);
            return true;
        }
    }

    public List<Person> removePassengers() {
        List<Person> exitingPassengers = new ArrayList<>();
        Iterator<Person> iterator = passengers.iterator();
        while (iterator.hasNext()) {
            Person passenger = iterator.next();
            if (passenger.getDestFloor() == currentFloor) {
                exitingPassengers.add(passenger);
                iterator.remove();
            }
        }
        return exitingPassengers;
    }

    public boolean hasStops() {
        return !stops.isEmpty();
    }

    public int getNextStop() {
        return stops.get(0);
    }

    public void move() {
        currentFloor += direction == Direction.UP ? 1 : -1;
    }

    public void reverseDirection() {
        direction = direction == Direction.UP ? Direction.DOWN : Direction.UP;
    }

    @Override
    public String toString() {
        return "Elevator " + id +
                " (Floor " + currentFloor + ", Direction " + direction +
                ", Stops " + stops + ", Passengers " + passengers.size() + ")";
    }
}