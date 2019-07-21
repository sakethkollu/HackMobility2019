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


public class DataGenerator
{
    public static HashSet<GeoPoint> gps = new HashSet<GeoPoint>();
    HashMap<Coordinate, ParkingLocation> mp = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DataGenerator() {

        db.collection("Ratings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){

                    Coordinate location = new Coordinate((GeoPoint) document.get("Location"));
                    long currentCapacity = (long) document.get("Current Capacity");
                    long maxCapacity = Long.parseLong((String) document.get("Max Capacity"));
                    int numberOfRatings = (int) document.get("Number Of Ratings");
                    boolean occupied = (boolean) document.get("Occupied");
                    int rating = (int) document.get("Rating");

                }
            }
        });


    }
}
