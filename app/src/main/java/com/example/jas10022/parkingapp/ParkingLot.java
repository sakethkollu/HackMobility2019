package com.example.jas10022.parkingapp;

import java.util.Set;

public class ParkingLot extends ParkingLocation {

    private Coordinate location;
    private long maxCapacity;
    private long currentCapacity;

    public ParkingLot(Coordinate location, long maxCapacity, long currentCapacity) {
        super(location);
        this.location = this.getLocation();
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
    }

    public ParkingLot(Coordinate location, double rating, int num_ratings, long maxCapacity, long currentCapacity) {
        super(location, rating, num_ratings);
        this.location = this.getLocation();
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
    }

    public long getCurrentCapacity() {
        return currentCapacity;
    }

    public void incrementCapactiy() {
        this.currentCapacity++;
    }

    public void decrementCapacity(){
        this.currentCapacity--;
    }
    @Override
    public int hashCode() {
        return this.getLocation().hashCode();
    }
}
