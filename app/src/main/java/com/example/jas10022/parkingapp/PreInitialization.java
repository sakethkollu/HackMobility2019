package com.example.jas10022.parkingapp;

import java.util.HashMap;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import android.location.LocationListener;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.SupportMapFragment;

import java.util.HashMap;
import java.util.HashSet;


public class PreInitialization {
    public static HashSet<GeoPoint> gps = new HashSet<GeoPoint>();
    Map map = MainActivity.map;
    HashMap<Coordinate, ParkingLocation> mp = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PreInitialization() {

        db.collection("Ratings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){

                    try {
                        //System.out.println(document.getId() + "=>" + document.getData());
                        GeoPoint location = (GeoPoint) document.get("Location");

                        Double rating = (double) document.get("Rating");
                        Integer numOfRatings = (int) document.get("Number of Ratings");


                        long maxSpots = Long.parseLong((String) document.get("Max Capacity"));
                        int current_capacity = (int) document.get("Current Capacity");

                        boolean occupied = (boolean) document.get("Occupied");

                        Coordinate loc = new Coordinate(location.getLatitude(), location.getLongitude());

                        if(maxSpots == - 1){
                            mp.put(loc, new ParkingSpot(loc, rating, numOfRatings, occupied));
                        }else{
                            mp.put(loc, new ParkingLot(loc, rating, numOfRatings, maxSpots, current_capacity));
                        }

                    }catch( NullPointerException n){


                        try{
                            GeoPoint location = (GeoPoint) document.get("Location");
                            long maxSpots = Long.parseLong((String) document.get("Max Capacity"));

                            long current_capacity = (long) document.get("Current Capacity");
                            boolean occupied = (boolean) document.get("Occupied");

                            Coordinate loc = new Coordinate(location.getLatitude(), location.getLongitude());
                            if(maxSpots == -1){
                                mp.put(loc, new ParkingSpot(loc, false));
                            }else{
                                mp.put(loc, new ParkingLot(loc, maxSpots, 0));
                            }

                        }catch(Exception e) {
                            GeoPoint location = (GeoPoint) document.get("Location");
                            long maxSpots = -1;

                            Coordinate loc = new Coordinate(location.getLatitude(), location.getLongitude());
                            if(maxSpots == -1){
                                mp.put(loc, new ParkingSpot(loc, false));
                            }else{
                                mp.put(loc, new ParkingLot(loc, maxSpots, 0));
                            }

                            gps.add(location);
                            System.out.println("added a point: "  + loc);
                        }


                    }


                }
            }
        });


    }
}
