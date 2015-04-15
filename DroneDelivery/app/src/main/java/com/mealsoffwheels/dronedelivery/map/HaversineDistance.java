package com.mealsoffwheels.dronedelivery.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by David on 4/01/2015.
 */
public class HaversineDistance {

    private final double droneSpeed = 0.0134112;    // In meters per millisecond
    private double latA, latB, lngA, lngB;
    private final double r = 6372800;

    public HaversineDistance(LatLng start, LatLng finish) {
        latA = start.latitude;
        lngA = start.longitude;
        latB = finish.latitude;
        lngB = finish.longitude;
    }

    public double getDistance() {
        double dLat = Math.toRadians(latB - latA);
        double dLng = Math.toRadians(lngB - lngA);
        latA = Math.toRadians(latA);
        latB = Math.toRadians(latB);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.cos(latA) * Math.cos(latB) *
                Math.pow(Math.sin(dLng / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double d = r * c;
        return d;
    }

    public float getTime() {
        return (float) (getDistance() / droneSpeed);
    }
}
