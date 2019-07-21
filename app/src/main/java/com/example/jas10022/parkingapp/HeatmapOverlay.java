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

    public HeatmapOverlay(){
        saved = new HashSet<>();
        this.addCircles(MainActivity.map);
    }

    private void addCircles(Map map){
        Coordinate current =  new Coordinate(MainActivity.currentLatitude, MainActivity.currentLongitude);
        MapCircle circle;
        for (Coordinate c : MainActivity.dataMapGlobal.keySet()){
            // Check an area a bit larger than selected locations
            if (current.withinRadius(c, 1500)) {
                circle = MainActivity.dataMapGlobal.get(c).heatmapLocation();
                saved.add(circle);

                map.addMapObject(circle);
            }
        }
    }

    public void clearHeatmap(){
        MainActivity.map.removeMapObjects(new ArrayList<MapObject>(this.saved));
    }
}
