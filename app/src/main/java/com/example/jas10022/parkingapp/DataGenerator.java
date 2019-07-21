package com.example.jas10022.parkingapp;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;


public class DataGenerator {

    private HashMap<Coordinate, ParkingLocation> dataMap;
    private QuerySnapshot queryDocumentSnapshots;
    private KDTree parkingCoordinates;

    public DataGenerator(QuerySnapshot queryDocumentSnapshots) {
        this.queryDocumentSnapshots = queryDocumentSnapshots;
        generateData();
    }

    private void generateData() {

        HashMap<Coordinate, ParkingLocation> dm = new HashMap<>();

        for (DocumentSnapshot document : this.queryDocumentSnapshots.getDocuments()) {
            Coordinate location;
            long currentCapacity;
            long maxCapacity;
            long numberOfRatings;
            boolean occupied;
            double rating;

            location = new Coordinate((GeoPoint) document.get("Location"));
            currentCapacity = (long) document.get("Current Capacity");
            maxCapacity = Long.parseLong(document.get("Max Capacity").toString());


            rating = (double) document.get("Rating");
            numberOfRatings = (long) document.get("Number of Ratings");
            occupied = (boolean) document.get("Occupied");

            if(maxCapacity == -1) {
                dm.put(location, new ParkingSpot(location, rating, numberOfRatings, occupied));
            }else{
                dm.put(location, new ParkingLot(location, rating, numberOfRatings, maxCapacity, currentCapacity));
            }
        }

        this.dataMap = dm;
        this.parkingCoordinates = new KDTree(new ArrayList<>(this.dataMap.keySet()));

    }

    public HashMap<Coordinate, ParkingLocation> getDataMap() {
        return this.dataMap;
    }

    public KDTree getParkingCoordinates() {
        return this.parkingCoordinates;
    }

}

