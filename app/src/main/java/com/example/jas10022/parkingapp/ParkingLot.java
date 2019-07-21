package com.example.jas10022.parkingapp;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.mapping.MapCircle;

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
    public double occupency(){
        if (maxCapacity == 0 || currentCapacity == maxCapacity){
            return 1.0;
        }
        else{
            return 1.0 - Double.valueOf(currentCapacity) /maxCapacity;
        }
    }

    private double getArea(){
        if (maxCapacity == 1){
            return 2500;
        }
        return 30000*Math.log(maxCapacity)/Math.log(Math.exp(1));
    }

    private double getRadius(){
        double area = this.getArea();
        if (area > 0) {
            return Math.sqrt(area);
        }
        else{
            return 50;
        }
    }

    @Override
    public MapCircle heatmapLocation(){
        double lat = this.location.getLatitude();
        double lng = this.location.getLongitude();
        GeoCoordinate coord = new GeoCoordinate(lat, lng);
        MapCircle position = new MapCircle(this.getRadius(), coord);
        //int alpha = 0x60; //Three circles on top of one another makes full
        int colour;
        if (this.occupency() >= .66){
            colour = 0x5500ff00;
        }
        else if (this.occupency() >= .34){
            colour = 0x55ffff00;
        }
        else{
            colour = 0x55ff0000;
        }
        /*
        int red = (int) (0xff * (1.0 - this.occupency())); //red ratio is equal to percentage full
        int green = (int) (0xff * this.occupency()); //green ratio is equal to percent empty
        int colour = alpha * 0x1000000 + red * 0x10000 + green*0x100; //convert to ARGB bullshit for colour
        */

        position.setFillColor(colour);
        return position;
    }





    @Override
    public int hashCode() {
        return this.getLocation().hashCode();
    }
}
