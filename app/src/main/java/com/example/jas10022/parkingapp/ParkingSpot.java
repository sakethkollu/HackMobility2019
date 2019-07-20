package com.example.jas10022.parkingapp;

public class ParkingSpot extends ParkingLocation {

    private boolean occupied = false;

    public ParkingSpot(Coordinate location, int rating, int num_ratings) {
        super(location, rating, num_ratings);
    }

    public void parkInSpot() {
        this.occupied = true;
    }

    public void releaseSpot() {
        this.occupied = false;
    }

    public boolean isOccupied() {
        return occupied;
    }
}
