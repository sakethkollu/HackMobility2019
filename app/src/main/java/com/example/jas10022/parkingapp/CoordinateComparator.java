package com.example.jas10022.parkingapp;

public class CoordinateComparator {
    public static int compare(int depth, com.example.jas10022.parkingapp.Coordinate curr, com.example.jas10022.parkingapp.Coordinate goal) {
        if (depth % 2 == 0) {
            return Double.compare(curr.getLatitude(), goal.getLatitude());
        }
        return -1 * Double.compare(curr.getLongitude(), goal.getLongitude());
    }
}