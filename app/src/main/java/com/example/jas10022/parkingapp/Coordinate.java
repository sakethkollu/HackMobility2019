package com.example.jas10022.parkingapp;

public class Coordinate {
    private double latitude;
    private double longitude;

    public Coordinate (double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static double L2Norm(Coordinate a, Coordinate b) {
        return Math.sqrt(Math.pow((a.getLongitude() - b.getLongitude()), 2)  + Math.pow((a.getLatitude() - b.getLatitude()), 2));
    }

    @Override
    public boolean equals(Object obj) {
        Coordinate other = (Coordinate) obj;
        return this.latitude == other.latitude && this.longitude == other.longitude;
    }


    @Override
    public String toString() {
        return getLatitude() + ", " + getLongitude();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
