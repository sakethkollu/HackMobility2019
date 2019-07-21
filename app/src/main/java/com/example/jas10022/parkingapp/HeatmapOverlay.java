package com.example.jas10022.parkingapp;

import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapCircle;
import com.here.android.mpa.mapping.MapObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class HeatmapOverlay {
    private HashSet<MapCircle> saved;
    public boolean on;

    public HeatmapOverlay(){
        this.saved = new HashSet<>();
        this.addCircles();
    }

    private void addCircles(){
        this.on = true;
        //Coordinate current =  new Coordinate(MainActivity.currentLatitude, MainActivity.currentLongitude);
        Coordinate current = new Coordinate(37.7876, -122.3966); //Hardcode for debugging
        MapCircle circle;
        for (Coordinate c : MainActivity.dataMapGlobal.keySet()){
            // Check an area a bit larger than selected locations
            if (current.withinRadius(c, 1500)) {
                circle = MainActivity.dataMapGlobal.get(c).heatmapLocation();

                this.saved.add(circle);
                MainActivity.map.addMapObject(circle);
            }
        }
    }

    private void clearHeatmap(){
        this.on = false;
        MainActivity.map.removeMapObjects(new ArrayList<MapObject>(this.saved));
        this.saved.clear();
    }

    public void toggle(){
        if (this.on){
            this.clearHeatmap();
        }
        else{
            this.addCircles();
        }
    }
}
