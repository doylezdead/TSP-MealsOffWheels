package com.mealsoffwheels.dronedelivery.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by David on 4/01/2015.
 */
public class HaversineDistance {

    private final double droneSpeed = 0.0134112;    // In meters per millisecond
    private double latA, latB, lngA, lngB;
    private final double r = 6371000.00;

    public HaversineDistance(LatLng start, LatLng finish) {
        latA = start.latitude;
        lngA = start.longitude;
        latB = start.latitude;
        lngB = start.longitude;
    }

    public double getDistance() {
        double dLat = Math.toRadians(latB - latA);
        double dLng = Math.toRadians(lngB - lngA);
        latA = Math.toRadians(latA);
        latB = Math.toRadians(latB);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.cos(latA) * Math.cos(latB) *
                Math.pow(Math.sin(dLng / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(a - 1));
        double d = r * c;
        return d;
    }

    public float getTime() {
        return (float) (getDistance() / droneSpeed);
    }
}
