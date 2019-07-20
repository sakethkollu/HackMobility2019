package com.example.jas10022.parkingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.SupportMapFragment;

public class MainActivity extends AppCompatActivity {

    //This is the refrence of the Firebase Stoarage
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        // initialize the Map Fragment and
        // retrieve the map that is associated to the fragment
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(
                    OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // now the map is ready to be used
                    final Map map = mapFragment.getMap();

                    //create a geoCordinate based off the long and latitude
                    //this is how you cna create a new Map Marker in a specified location
                    //map.addMapObject(new MapMarker(new GeoCoordinate(49.163, -123.137766, 10)));


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


                    // ...
                } else {
                    System.out.println("ERROR: Cannot initialize SupportMapFragment: " + error);
                }
            }
        });
    }
}
