package com.simulation;

public class Person {
    private final int destination;

    public Person(int destination) {
        this.destination = destination;
    }

    public int getDestFloor() {
        return destination;
    }

    @Override
    public String toString() {
        return "Person{" +
                "destination=" + destination +
                '}';
    }
}
