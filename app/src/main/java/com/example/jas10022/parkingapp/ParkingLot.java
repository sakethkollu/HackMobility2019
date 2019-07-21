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

    @Override
    public MapCircle heatmapLocation(){
        double lat = this.location.getLatitude();
        double lng = this.location.getLongitude();
        GeoCoordinate coord = new GeoCoordinate(lat, lng);
        MapCircle position = new MapCircle(150, coord);
        int alpha = 0xff / 3;
        int red = (int) (0xff * (1.0 - this.occupency()));
        int green = (int) (0xff * this.occupency());
        int colour = alpha * 0x1000000 + red * 0x10000 + green*0x100;


        position.setFillColor(colour);
        return position;
    }





    @Override
    public int hashCode() {
        return this.getLocation().hashCode();
    }
}
