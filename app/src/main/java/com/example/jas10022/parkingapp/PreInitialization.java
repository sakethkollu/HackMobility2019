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

public class PreInitialization {
    HashMap<Coordinate, ParkingLocation> map = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PreInitialization() {

        db.collection("Ratings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){

                    System.out.println(document.getId() + "=>" + document.getData());
                    GeoPoint location = (GeoPoint) document.get("Location");

                    double ratings = (double)document.get("Rating");
                    int numOfRatings = (int)document.get("Number of Ratings");


                    int maxSpots = Integer.parseInt((String) document.get("Max Capacity"));
                    int current_capacity = (int) document.get("Current Capacity");

                    boolean occupied = (boolean) document.get("Occupied");

                    Coordinate loc = new Coordinate(location.getLatitude(),location.getLongitude());

                    if(maxSpots == - 1){
                        map.put(loc, new ParkingSpot(loc, ratings, numOfRatings, occupied));
                    }else{
                        map.put(loc, new ParkingLot(loc, ratings, numOfRatings, maxSpots, current_capacity));
                    }

                }
            }
        });


    }
}
