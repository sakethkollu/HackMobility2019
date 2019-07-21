package com.example.jas10022.parkingapp;

import android.graphics.Color;

import com.here.android.mpa.mapping.MapCircle;
import com.here.android.mpa.common.GeoCoordinate;
import java.util.List;

public class ParkingLocation {
    private Coordinate location;
    private double rating = -1;
    private int num_ratings = 0;

    public ParkingLocation(Coordinate location) {
        this.location = location;
    }

    public ParkingLocation(Coordinate location, double rating, int num_ratings) {
        this.location = location;
        this.rating = rating;
        this.num_ratings = num_ratings;
    }

    public Coordinate getLocation() {
        return this.location;
    }

    public void addRating(int rating) {
        if (this.num_ratings == 0 || this.rating == -1) {
            this.rating = rating;
        } else {
            this.rating  = (this.rating * num_ratings + rating) / (num_ratings + 1);
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

    public MapCircle heatmapLocation(){
        double lat = this.location.getLatitude();
        double lng = this.location.getLongitude();
        GeoCoordinate coord = new GeoCoordinate(lat, lng);
        MapCircle position = new MapCircle(50, coord);
        position.setFillColor(0x55ff0000); //IF NO DATA, MAKE RED
        return position;
    }




    @Override
    public String toString() {
        return "Parking Location @ " + this.location.toString() + " Rating: " + this.rating + " x " + this.num_ratings;
    }
}
