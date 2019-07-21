package com.example.jas10022.parkingapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;


import android.location.LocationListener;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapCircle;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.SupportMapFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements LocationListener{

    //This is the refrence of the Firebase Stoarage
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LocationManager locationManager;
    LocationListener locationListener;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public static Map map;
    public static double currentLatitude;
    public static double currentLongitude;

    private FusedLocationProviderClient fusedLocationClient;
    SupportMapFragment mapFragment;
    int width;
    int height;

    public static HashMap<Coordinate, ParkingLocation> dataMapGlobal;
    public static HeatmapOverlay heatMap;
    public static KDTree parkingCoordinates;

    boolean click = true;
    private PopupWindow currentWindow;
    public ImageButton currentLocation;
    public FloatingActionButton goToDirections;
    public ImageButton toggleHeatmap;
    private GeoCoordinate currentMarker;

    ImageButton resetData;
    ImageButton creatNewParking;
    private RelativeLayout mainLayout;
    EditText searchDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        // initialize the Map Fragment and
        // retrieve the map that is associated to the fragment
        currentLocation = findViewById(R.id.current_location);
        goToDirections = findViewById(R.id.go_button);
        resetData = findViewById(R.id.reset_data);
        creatNewParking = findViewById(R.id.add_Parking_structure);
        searchDestination = findViewById(R.id.search_for_destination);
        creatNewParking.setVisibility(View.VISIBLE);
        goToDirections.hide();

        toggleHeatmap = findViewById(R.id.Toggle_Heatmap);

        //mainLayout.setBackground(getResources().getDrawable(R.drawable.background));

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  }, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        searchDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!click){
                    currentWindow.dismiss();
                    click = true;
                    goToDirections.hide();
                    creatNewParking.setVisibility(View.VISIBLE);
                    resetData.setVisibility(View.VISIBLE);
                    currentLocation.setVisibility(View.VISIBLE);
                    searchDestination.setInputType(InputType.TYPE_CLASS_TEXT);
                    searchDestination.requestFocus();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(searchDestination, InputMethodManager.SHOW_FORCED);
                }
            }
        });

        creatNewParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this is where you can create a new parking strucutre
            }
        });

        toggleHeatmap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                heatMap.toggle();

                if(!heatMap.on){
                    toggleHeatmap.setImageDrawable(getResources().getDrawable(R.drawable.my_fire));
                }else{
                    toggleHeatmap.setImageDrawable(getResources().getDrawable(R.drawable.ic_ice_blue));
                }
            }
        });

        resetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomizeData();
            }
        });

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                    ActivityCompat.requestPermissions( getParent(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  }, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                }

                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();

                        map.setCenter(new GeoCoordinate(currentLatitude , currentLongitude, 0.0), Map.Animation.LINEAR);
                        map.setZoomLevel(0);
                    }
                });

            }
        });

        goToDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Directions(new Coordinate(currentMarker.getLatitude(),currentMarker.getLongitude()));
            }
        });

        //jas
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
                    List<String> schemes = map.getMapSchemes(); //Make map no traffic
                    map.setMapScheme(schemes.get(4));

                    map.setCenter(new GeoCoordinate(currentLatitude , currentLongitude, 0.0), Map.Animation.LINEAR);
                    map.setZoomLevel(0);


                    //this part of the code is accessing the database and pulling all the parking garanges around the user
                    db.collection("Ratings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            //this is accessing each parking garage document
                            DataGenerator dg = new DataGenerator(queryDocumentSnapshots);
                            dataMapGlobal = dg.getDataMap();
                            parkingCoordinates = dg.getParkingCoordinates();
                            heatMap = new HeatmapOverlay();
                            Coordinate current = new Coordinate(currentLatitude, currentLongitude);

                            try{
                                Image my_location = new Image();
                                my_location.setImageResource(R.drawable.my_loc);
                                MapMarker customMarker = new MapMarker(new GeoCoordinate(current.getLatitude(), current.getLongitude(),0.0),my_location);
                                map.addMapObject(customMarker);

                                Image park_location = new Image();
                                park_location.setImageResource(R.drawable.my_park_loc);

                                for(Coordinate c : dataMapGlobal.keySet()) {
                                    if(current.withinRadius(c, 1000)){
                                        map.addMapObject(new MapMarker(new GeoCoordinate(c.getLatitude(), c.getLongitude(), 0.0), park_location));
                                    }

                                }
                            }catch(Exception e){

                            }
                            mapFragment.getMapGesture().addOnGestureListener(new MapGesture.OnGestureListener.OnGestureListenerAdapter() {
                                @Override
                                public boolean onTapEvent(PointF p) {
                                    ArrayList<ViewObject> viewObjectList = (ArrayList<ViewObject>) map.getSelectedObjects(p);
                                    if(click) {

                                        for (ViewObject viewObject : viewObjectList) {
                                            if (viewObject.getBaseType() == ViewObject.Type.USER_OBJECT) {
                                                MapObject mapObject = (MapObject) viewObject;
                                                if (mapObject.getType() == MapObject.Type.MARKER) {

                                                    MapMarker selectedMarker = ((MapMarker) mapObject);
                                                    currentMarker = selectedMarker.getCoordinate();
                                                    map.setCenter(currentMarker, Map.Animation.LINEAR);
                                                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()));
                                                    ParkingLocation pl = dataMapGlobal.get(new Coordinate(currentMarker));


                                                    if (pl != null) {
                                                        currentWindow = newMarkerEventPopUp((int) Math.round(pl.getRating()), currentMarker);
                                                            currentWindow.showAtLocation(new LinearLayout(getBaseContext()), Gravity.BOTTOM, width / 50, height / 30);
                                                            //popUp.update(50, 50, 300, 80);
                                                        goToDirections.show();
                                                        currentLocation.setVisibility(View.GONE);
                                                        resetData.setVisibility(View.INVISIBLE);
                                                        creatNewParking.setVisibility(View.INVISIBLE);
                                                            click = false;

                                                        System.out.println("selected location: " + currentMarker.getLatitude() + " : " + currentMarker.getLongitude());
                                                    }
                                                }
                                            }
                                        }
                                    }else{
                                        currentWindow.dismiss();
                                        click = true;
                                        goToDirections.hide();
                                        creatNewParking.setVisibility(View.VISIBLE);
                                        resetData.setVisibility(View.VISIBLE);
                                        currentLocation.setVisibility(View.VISIBLE);
                                    }
                                    return false;

                                }

                                @Override
                                public boolean onLongPressEvent(PointF p) {
                                    return false;
                                }
                            });
                        }
                    });




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

    public PopupWindow newMarkerEventPopUp(int rating, final GeoCoordinate location){

        final PopupWindow eventPopUp = new PopupWindow(MainActivity.this);
        LinearLayout eventLayout = new LinearLayout(MainActivity.this);
        eventPopUp.setHeight(height/4);
        eventPopUp.setWidth( width);
        eventPopUp.setAnimationStyle(R.style.PopupAnimation);
        eventPopUp.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));

        eventLayout.setGravity(Gravity.CENTER);
        eventLayout.setMinimumWidth(2 * width / 3);
        eventLayout.setOrientation(LinearLayout.HORIZONTAL);
        eventLayout.setMinimumHeight((height / 3) + (height / 12));
        eventLayout.setBackgroundResource(R.drawable.dark_opaque);

        //change layout
        eventLayout.addView(getLayoutInflater().inflate(R.layout.spot, null));
        eventPopUp.setContentView(eventLayout);

        //establish the Views
        final RatingBar ratingBar = eventPopUp.getContentView().findViewById(R.id.ratingBar);
        final Button parkedButton = eventPopUp.getContentView().findViewById(R.id.Parked_Button);
        final Button ratingButton = eventPopUp.getContentView().findViewById(R.id.rating_button);
        final TextView reviewNum = eventPopUp.getContentView().findViewById(R.id.TextRatingNumber);
        final TextView reviewScore = eventPopUp.getContentView().findViewById(R.id.ratingNumber);

        ratingBar.setNumStars(5);
        ratingBar.setRating(rating);
        ratingBar.setEnabled(false);
        ParkingLocation pl = dataMapGlobal.get(new Coordinate(location));
        reviewNum.setText("Number of Ratings: " + pl.getNumRatings());
        reviewScore.setText("Review Score: " + pl.getRating());

        System.out.println("number of reviews: " + pl.getNumRatings());
        System.out.println("rating value: " + pl.getRating());

        parkedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                parkedButton.setVisibility(View.INVISIBLE);
                ratingButton.setVisibility(View.VISIBLE);
                reviewNum.setVisibility(View.INVISIBLE);
                reviewScore.setVisibility(View.INVISIBLE);
                ratingBar.setNumStars(5);
                ratingBar.setRating(0);
                ratingBar.setEnabled(true);

                ratingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int rating = Math.round(ratingBar.getRating());


                        Coordinate clicked = new Coordinate(location.getLatitude(), location.getLongitude());
                        ParkingLocation parkedLocation = dataMapGlobal.get(clicked);
                        parkedLocation.addRating(rating);
                        HashMap<String,Object> t = new HashMap<String, Object>();
                        t.put("Rating",parkedLocation.getRating());
                        t.put("Number of Ratings",parkedLocation.getNumRatings());
                        db.collection("Ratings").document(location.getLatitude() + ", " + location.getLongitude()).update(t);

                        ParkingLocation pl = dataMapGlobal.get(clicked);
                        if (pl.getClass() == ParkingSpot.class){
                            //1 car only so set the occupied to true
                            ((ParkingSpot)pl).parkInSpot();
                            HashMap<String,Object> a = new HashMap<String, Object>();
                            t.put("Occupied",false);
                            db.collection("Ratings").document(location.getLatitude() + ", " + location.getLongitude()).update(t);
                        }else if(pl.getClass() == ParkingLot.class){
                            //increment the current cars by 1
                            ((ParkingLot)pl).decrementCapacity();
                            HashMap<String,Object> a = new HashMap<String, Object>();
                            t.put("Current Capacity",((ParkingLot)pl).getCurrentCapacity());
                            db.collection("Ratings").document(location.getLatitude() + ", " + location.getLongitude()).update(t);
                        }

                        reviewNum.setText("Number of Ratings: " + pl.getNumRatings());
                        reviewScore.setText("Review Score: " + pl.getRating());

                        parkedButton.setVisibility(View.VISIBLE);
                        ratingButton.setVisibility(View.INVISIBLE);
                        reviewNum.setVisibility(View.VISIBLE);
                        reviewScore.setVisibility(View.VISIBLE);
                        ratingBar.setEnabled(false);
                        ratingBar.setNumStars(5);
                        ratingBar.setRating((float) pl.getRating());

                    }
                });

                ParkingLocation pl = dataMapGlobal.get(new Coordinate(location));
                if (pl.getClass() == ParkingSpot.class){
                    //1 car only so set the occupied to true
                    ((ParkingSpot)pl).parkInSpot();
                    HashMap<String,Object> t = new HashMap<String, Object>();
                    t.put("Occupied",true);
                    db.collection("Ratings").document(location.getLatitude() + ", " + location.getLongitude()).update(t);
                }else if(pl.getClass() == ParkingLot.class){
                    //increment the current cars by 1
                    ((ParkingLot)pl).incrementCapactiy();
                    HashMap<String,Object> t = new HashMap<String, Object>();
                    t.put("Current Capacity",((ParkingLot)pl).getCurrentCapacity());
                    db.collection("Ratings").document(location.getLatitude() + ", " + location.getLongitude()).update(t);
                }


            }
        });


        return eventPopUp;

    }

    public void randomizeData(){
        Random b = new Random();
        for(Coordinate c : dataMapGlobal.keySet()) {
            ParkingLot pl = (ParkingLot) dataMapGlobal.get(c);
            pl.setNumRatings(b.nextInt(100));
            pl.setRating(b.nextInt(5));
            for (int i = 0; i < 5; i++) {
                if(b.nextBoolean()){
                    pl.incrementCapactiy();
                }
            }
            HashMap<String, Object> t = new HashMap<String, Object>();
            t.put("Current Capacity", pl.getCurrentCapacity());
            t.put("Location", new GeoPoint(pl.getLocation().getLatitude(), pl.getLocation().getLongitude()));
            t.put("Number of Ratings", pl.getNumRatings());
            t.put("Rating", pl.getRating());


            db.collection("Ratings").document(pl.getLocation().toString()).update(t);
            System.out.print("finished");

            //This is how to refresh the page
            Intent intent = getIntent();
            finish();
            startActivity(intent);

        }
    }
}
