package com.example.jas10022.parkingapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import android.location.LocationListener;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.SupportMapFragment;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener{

    //This is the refrence of the Firebase Stoarage
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LocationManager locationManager;
    LocationListener locationListener;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    Map map;
    double currentLatitude;
    double currentLongitude;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        // initialize the Map Fragment and
        // retrieve the map that is associated to the fragment

        //jas

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  }, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
            }
        });


        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(
                    OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // now the map is ready to be used
                    map = mapFragment.getMap();
                    List<String> schemes = map.getMapSchemes();
                    map.setMapScheme(schemes.get(2));

                    map.setCenter(new GeoCoordinate(currentLatitude , currentLongitude, 0.0), Map.Animation.NONE);
                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 4);

                    //create a geoCordinate based off the long and latitude
                    //this is how you cna create a new Map Marker in a specified location
                    //map.addMapObject(new MapMarker(new GeoCoordinate(49.163, -123.137766, 10)));

                    //This is how to get from the realt time database

//                    myRef.child("0").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            // This method is called once with the initial value and again
//                            // whenever data at this location is updated.
//                            Double longitude = Double.parseDouble(dataSnapshot.child("Longitude").getValue().toString());
//                            Double latitude =  Double.parseDouble(dataSnapshot.child("Latitude").getValue().toString());
//
//                            map.addMapObject(new MapMarker(new GeoCoordinate(latitude, longitude, 10)));
//
//                            Log.d("MainActivity", "Value is: " + longitude);
//                            Log.d("MainActivity", "Value is: " + latitude);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError error) {
//                            // Failed to read value
//                            Log.w("MainActivity", "Failed to read value.", error.toException());
//                        }
//                    });
//
//                    //jas's code- this is how you get it from the firestore database
//                    db.collection("Ratings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){
//
//                                System.out.println(document.getId() + "=>" + document.getData());
//                                double Ratings = (double)document.get("Rating");
//                                int numOfRatings = (int)document.get("Number of Ratings");
//                                GeoPoint location = (GeoPoint) document.get("Location");
//                                Coordinate loc = new Coordinate(location.getLatitude(),location.getLongitude());
//                                ParkingLocation parkingLocation = new ParkingLocation(loc, Ratings,numOfRatings);
//
//
//                                //this is where you want to populate the local hash map
//
//                            }
//                        }
//                    });

                    myRef.child("0").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            Double longitude = Double.parseDouble(dataSnapshot.child("Longitude").getValue().toString());
                            Double latitude =  Double.parseDouble(dataSnapshot.child("Latitude").getValue().toString());

                            map.addMapObject(new MapMarker(new GeoCoordinate(latitude, longitude, 10)));

                            Log.d("MainActivity", "Value is: " + longitude);
                            Log.d("MainActivity", "Value is: " + latitude);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("MainActivity", "Failed to read value.", error.toException());
                        }
                    });

                    //jas's code- this is how you get it from the firestore database
                    /*
                    db.collection("Ratings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){

                                System.out.println(document.getId() + "=>" + document.getData());
                                double Ratings = (double)document.get("Rating");
                                int numOfRatings = (int)document.get("Number of Ratings");
                                GeoPoint location = (GeoPoint) document.get("Location");
                                Coordinate loc = new Coordinate(location.getLatitude(),location.getLongitude());
                                ParkingLocation parkingLocation = new ParkingLocation(loc, Ratings,numOfRatings);

                                //this is where you want to populate the local hash map

                            }
                        }
                    });*/


                    /*
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++){
>>>>>>> Stashed changes

                                DataSnapshot currentdocumnet = dataSnapshot.child(Integer.toString(i));
                                String parkingSpots = currentdocumnet.child("NumberOfSigns").getValue().toString();
                                String longitude = currentdocumnet.child("Longitude").getValue().toString();
                                String latitude =  currentdocumnet.child("Latitude").getValue().toString();

                                HashMap<String, Object> t = new HashMap<String, Object>();

                                t.put("Occupied", false);
                                t.put("Max Capacity", parkingSpots);
                                t.put("Current Capacity", 0);

                                db.collection("Ratings").document(latitude + ", " + longitude).update(t);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/


                    /* This is how you can retrive a value
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String value = dataSnapshot.getValue(String.class);
                            Log.d("MainActivity", "Value is: " + value);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("MainActivity", "Failed to read value.", error.toException());
                        }
                    });*/

                    /* This is how you can set the value of a specific doc in the realtime database
                    mDatabase.child("users").child(userId).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            // ...
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            // ...
                        }
                    });

                     */

                    /* This is how you retrieve data from the firestore database
                    db.collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
                     */



                    // ...

                PreInitialization pre = new PreInitialization();

                } else {
                    System.out.println("ERROR: Cannot initialize SupportMapFragment: " + error);
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        System.out.println("Current Location: " + currentLatitude + " : " + currentLongitude);

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}
