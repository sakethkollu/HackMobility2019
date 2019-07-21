package com.example.jas10022.parkingapp;

import java.util.Set;

public class ParkingLot extends ParkingLocation {

    private int maxCapacity;
    private int currentCapacity;

    public ParkingLot(Coordinate location, double rating, int num_ratings, int maxCapacity, int currentCapacity) {
        super(location, rating, num_ratings);
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
    }

    @Override
    public int hashCode() {
        return this.getLocation().hashCode();
    }
}
