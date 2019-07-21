package com.example.jas10022.parkingapp;

import com.google.firebase.firestore.GeoPoint;
import com.here.android.mpa.common.GeoCoordinate;

public class Coordinate {
    private double latitude;
    private double longitude;

    public Coordinate (GeoPoint geoPoint) {
        this(geoPoint.getLatitude(), geoPoint.getLongitude());
    }

    public Coordinate (GeoCoordinate geoCoor) {
        this(geoCoor.getLatitude(), geoCoor.getLongitude());
    }

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

    public boolean withinRadius(Coordinate other, double radius_in_meters) {

            double lat1 = this.latitude;
            double lon1 = this.longitude;

            double lat2 = other.getLatitude();
            double lon2 = other.getLongitude();

            double R = 6378.137; // Radius of earth in KM
            double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
            double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                            Math.sin(dLon/2) * Math.sin(dLon/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double d = R * c;
            return (d * 1000) < radius_in_meters; // meters
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
