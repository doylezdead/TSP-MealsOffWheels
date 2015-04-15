package com.mealsoffwheels.dronedelivery.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by David on 3/25/2015.
 */
public class MarkerAnimation {
    public static void animateMarkerAtoB(final Marker marker, final LatLng finishLoc, final LatLngInterpolator latLngInterpolator) {
        final LatLng startPos = marker.getPosition();
        final Handler handles = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationMS = 30000;      // This will be changed to update with the drone speed over time.

        handles.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // This is for calculating the elapsed time in ms used by the interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationMS;
                v = interpolator.getInterpolation(t);

                marker.setPosition(latLngInterpolator.interpolate(v, startPos, finishLoc));

                if (t < 1) {
                    handles.postDelayed(this, 16);
                }
            }
        });
    }
}
