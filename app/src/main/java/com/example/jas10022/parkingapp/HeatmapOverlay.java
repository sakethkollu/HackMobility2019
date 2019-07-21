package com.example.jas10022.parkingapp;

import com.here.android.mpa.mapping.Map;

public class HeatmapOverlay {
    public HeatmapOverlay(Map map){
        this.addCircles(map);
    }

    private void addCircles(Map map){
        Coordinate current =  new Coordinate(MainActivity.currentLatitude, MainActivity.currentLongitude);
        for (Coordinate c : MainActivity.dataMapGlobal.keySet()){
            // Check an area a bit larger than selected locations
            if (current.withinRadius(c, 1500)) {
                map.addMapObject(MainActivity.dataMapGlobal.get(c).heatmapLocation());
            }
        }
    }
}
