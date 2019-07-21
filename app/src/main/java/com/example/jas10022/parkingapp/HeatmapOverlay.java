package com.example.jas10022.parkingapp;


import com.here.android.mpa.mapping.MapCircle;
import com.here.android.mpa.mapping.MapObject;

import java.util.ArrayList;


public class HeatmapOverlay {
    private ArrayList<MapObject> saved;
    public boolean on;

    public HeatmapOverlay() {
        this.saved = new ArrayList<>();
        Coordinate current =  new Coordinate(MainActivity.currentLatitude, MainActivity.currentLongitude);
        MapCircle circle;
        for (Coordinate c : MainActivity.dataMapGlobal.keySet()){
            // Check an area a bit larger than selected locations
            if (current.withinRadius(c, 1500)) {
                circle = MainActivity.dataMapGlobal.get(c).heatmapLocation();
                this.saved.add(circle);
                MainActivity.map.addMapObject(circle);
            }
        }

        this.on = true;

    }

    private void addCircles(){
        for(MapObject mo : this.saved) {
            MainActivity.map.addMapObject(mo);
        }
    }

    private void clearHeatmap(){
        for(MapObject mo : this.saved) {
            MainActivity.map.removeMapObject(mo);
        }

    }

    public void toggle(){
        if (this.on){
            this.clearHeatmap();
        }
        else{
            this.addCircles();
            this.on = true;
        }
    }
}
