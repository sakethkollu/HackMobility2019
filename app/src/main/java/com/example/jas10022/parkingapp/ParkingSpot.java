package com.example.jas10022.parkingapp;

public class ParkingSpot extends ParkingLocation {

    private boolean occupied;

    public ParkingSpot(Coordinate location, double rating, int num_ratings, boolean occupied) {
        super(location, rating, num_ratings);
        this.occupied = occupied;
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
