package com.example.jas10022.parkingapp;

public class ParkingLocation {
    private Coordinate location;
    private double rating;
    private int num_ratings = 0;

    public ParkingLocation(Coordinate location, int rating, int num_ratings) {
        this.location = location;
        this.rating = rating;
        this.num_ratings = num_ratings;
    }

    public Coordinate getLocation() {
        return this.location;
    }

    public void addRating(int rating) {
        if (this.num_ratings == 0) {
            this.rating = rating;
        } else {
            this.rating  = (this.rating * num_ratings + rating) / num_ratings;
        }
        this.num_ratings +=1;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setNumRatings(int num_ratings) {
        this.num_ratings = num_ratings;
    }

    public double getRating() {
        return this.rating;
    }

    public int getNumRatings() {
        return this.num_ratings;
    }
}
