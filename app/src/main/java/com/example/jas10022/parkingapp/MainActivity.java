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
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.SupportMapFragment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener{

    //This is the refrence of the Firebase Stoarage
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LocationManager locationManager;
    LocationListener locationListener;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public static Map map;
    double currentLatitude;
    double currentLongitude;
    private FusedLocationProviderClient fusedLocationClient;

    public static HashMap<Coordinate, ParkingLocation> dataMapGlobal;

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

                    //this part of the code is accessing the database and pulling all the parking garanges around the user
                    db.collection("Ratings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            //this is accessing each parking garage document
                            DataGenerator dg = new DataGenerator(queryDocumentSnapshots);
                            dataMapGlobal = dg.getDataMap();
                            for(Coordinate c : dataMapGlobal.keySet()) {
                                map.addMapObject(new MapMarker(new GeoCoordinate(c.getLatitude(), c.getLongitude(), 5)));
                            }
                            MapGesture.OnGestureListener listener =
                                    new MapGesture.OnGestureListener.OnGestureListenerAdapter() {
                                        @Override
                                        public boolean onMapObjectsSelected(List<ViewObject> objects) {
                                            for (ViewObject viewObj : objects) {
                                                if (viewObj.getBaseType() == ViewObject.Type.USER_OBJECT) {
                                                    if (((MapObject) viewObj).getType() == MapObject.Type.MARKER) {

                                                        GeoCoordinate selectedPoint = ((MapMarker) viewObj).getCoordinate();

                                                        System.out.println("Selected location is:  " + selectedPoint.getLatitude() + " : " + selectedPoint.getLongitude());

                                                    }
                                                }
                                            }
                                            return false;
                                        }
                                    };
                        }
                    });


                    List<String> schemes = map.getMapSchemes();
                    map.setMapScheme(schemes.get(2));

                    map.setCenter(new GeoCoordinate(currentLatitude , currentLongitude, 0.0), Map.Animation.NONE);
                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()));

                    //create a geoCordinate based off the long and latitude
                    //this is how you cna create a new Map Marker in a specified location
                    //map.addMapObject(new MapMarker(new GeoCoordinate(49.163, -123.137766, 10)));u

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
