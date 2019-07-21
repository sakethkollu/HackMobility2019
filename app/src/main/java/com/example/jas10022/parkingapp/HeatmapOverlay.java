package com.example.jas10022.parkingapp;

import com.here.android.mpa.mapping.Map;

public class HeatmapOverlay {
    public HeatmapOverlay(Map map){
        this.addCircles(map);
    }

    private void addCircles(Map map){
        for (Coordinate c : MainActivity.dataMapGlobal.keySet()){
            map.addMapObject(MainActivity.dataMapGlobal.get(c).heatmapLocation());
        }
    }
}
