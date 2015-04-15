package com.mealsoffwheels.dronedelivery.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.map.HaversineDistance;
import com.mealsoffwheels.dronedelivery.map.LatLngInterpolator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.mealsoffwheels.dronedelivery.map.MarkerAnimation.animateMarkerAtoB;

/**
 * @author David C. Mohrhardt
 */
public class DroneFinder extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private PolylineOptions path;
    private List<LatLng> latlngPath;
    private LatLng droneHome = new LatLng(47.11240, -88.58705);     // Taco Bell at Razorback Dr.
    private LatLng thisHome;
    private LatLng deviceLocation;
    private Marker mark;

    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private Location userLocation = null;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_finder);

        prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.values", Context.MODE_PRIVATE);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // No address given.
        if (prefs.getInt("Address Given", 0) == 0) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });

            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            thisHome = new LatLng(latitude, longitude);
        }

        // Address was given.
        else {
            // Construct given address into one string.
            String address = "";
            address += prefs.getString("Street Address", "") + " " + prefs.getString("City", "") +
                    " " + prefs.getString("Zip Code", "");
            thisHome = getLatLngFromAddress(address, getApplicationContext());
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        droneHome, 12.0f)
        );

        mMap.addMarker(new MarkerOptions().position(thisHome).title("Destination"));
        mMap.addMarker(new MarkerOptions().position(droneHome).title("Taco Bell"));

        mark = mMap.addMarker(new MarkerOptions().position(droneHome).title("Drone"));

        if (prefs.getInt("orderID", -1) != -1) {
            path = new PolylineOptions().geodesic(true)
                    .add(droneHome)                             //  Taco Bell on Razorback
                    .add(thisHome)
                    .color(Color.argb(1000, 128, 0, 128));

            mMap.addPolyline(path);
            latlngPath = path.getPoints();

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    if (mMap != null) {
                        animateMarkerAtoB(mark, thisHome, new LatLngInterpolator.Spherical());
                        new Handler().postDelayed(new SimulateOrderComplete(),
                                (int)new HaversineDistance(mark.getPosition(), thisHome).getTime());
                    }
                }
            });
        }
    }

    private class SimulateOrderComplete implements Runnable {
        @Override
        public void run() {
            clearOrder();
        }
    }

    private void clearOrder() {
        int end = prefs.getInt("Last", 0);
        SharedPreferences.Editor editor = prefs.edit();

        for (int i = 1; i <= end; ++i) {
            editor.remove(Integer.toString(i));
            editor.remove(Integer.toString(i) + "quantity");
            editor.commit();
        }

        // Reset last legit order location.
        editor.putInt("Last", 0);
        editor.remove("orderID");
        editor.putInt("Address Given", 0);
        editor.remove("Street Address");
        editor.remove("Zip Code");
        editor.remove("City");
        editor.commit();
    }

    /**
     * Written by David C. Mohrhardt
     *
     * @param from
     * @param to
     * @return
     */
    private double getBearing(LatLng from, LatLng to) {
        double theta;

        double startLat = Math.toRadians(from.latitude);
        double startLon = Math.toRadians(from.longitude);
        double endLat = Math.toRadians(to.latitude);
        double endLon = Math.toRadians(to.longitude);

        double y = Math.sin(endLon - startLon) * Math.cos(endLat);
        double x = Math.cos(startLat) * Math.sin(endLat) -
                Math.sin(startLat) * Math.cos(endLat) * Math.cos(endLon - startLon);

        theta = Math.atan2(y, x);
        theta = Math.toDegrees(theta);

        return theta;
    }

    /**
     * getLatLongFromAddress()
     * Written by: David C Mohrhardt
     * <p/>
     * This method is designed to perform reverse Geocoding to get GPS coordinates from an address
     * string and return the latitude and longitude of the address.
     *
     * @param addr    The string containing the address to be decoded.
     * @param context The context of the application
     * @return dest     Returns the LatLng object containing the coordinates to the destination.
     */
    public LatLng getLatLngFromAddress(String addr, Context context) {
        Log.d("DEVICELOCAL = ", "" + Locale.getDefault());

        LatLng dest = null;

        Geocoder gc = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gc.getFromLocationName(addr, 5);
            if (addresses.size() > 0) {
                double lat = addresses.get(0).getLatitude();
                Log.d("GEOCODE_LAT = ", "" + lat);
                double lng = addresses.get(0).getLongitude();
                Log.d("GEOCODE_LNG = ", "" + lng);
                dest = new LatLng(lat, lng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }

    /**
     * Get the LatLng path from point A to point B on the map and pass that back to the main
     * program.
     *
     * @return latlngPath The list of LatLng points that makes up the shortest distance path.
     */
    public List<LatLng> getLatLngPath() {
        return latlngPath;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
