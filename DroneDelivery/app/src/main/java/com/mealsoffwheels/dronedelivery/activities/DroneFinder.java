package com.mealsoffwheels.dronedelivery.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.Payload;
import com.mealsoffwheels.dronedelivery.map.LatLngInterpolator;

import static com.mealsoffwheels.dronedelivery.map.MarkerAnimation.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    /**
     * Thread task to wait for a GPS location.
     */
    private class GetGPS extends AsyncTask<Void, Void, Void> {

        /**
         * Performed when execute is called on
         * the GetGPS class. Waits for the
         * GPS location to be discovered.
         *
         * @param arg - Unused.
         * @return - Always returns null.
         */
        @Override
        protected Void doInBackground(Void... arg) {
            // Wait to get GPS location.
            while (userLocation == null) {
                SystemClock.sleep(1000);
            }

            return null;
        }
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
                public void onLocationChanged(Location location) { }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }

                @Override
                public void onProviderEnabled(String provider) { }

                @Override
                public void onProviderDisabled(String provider) { }
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

        path = new PolylineOptions().geodesic(true)
                .add(droneHome)                             //  Taco Bell on Razorback
                .add(thisHome)
                .color(Color.argb(1000, 128, 0, 128));

        mMap.addPolyline(path);
        latlngPath = path.getPoints();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if(mMap != null) {
                    animateMarkerAtoB(mark, thisHome, new LatLngInterpolator.Spherical());
                }
            }
        });
    }

<<<<<<< HEAD
=======
    /**
     * Written by David C. Mohrhardt
     * @param from
     * @param to
     * @return
     */
    private double getBearing(LatLng from, LatLng to) {
        double theta;

        double startLat = Math.toRadians( from.latitude );
        double startLon = Math.toRadians( from.longitude);
        double endLat = Math.toRadians( to.latitude );
        double endLon = Math.toRadians( to.longitude );

        double y = Math.sin(endLon - startLon) * Math.cos(endLat);
        double x = Math.cos(startLat) * Math.sin(endLat) -
                Math.sin(startLat) * Math.cos(endLat) * Math.cos(endLon - startLon);

        theta = Math.atan2(y, x);
        theta = Math.toDegrees( theta );

        return theta;
    }

>>>>>>> origin/dev-eric
    /**
     * getLatLongFromAddress()
     * Written by: David C Mohrhardt
     *
     * This method is designed to perform reverse Geocoding to get GPS coordinates from an address
     * string and return the latitude and longitude of the address.
     *
     * @param addr      The string containing the address to be decoded.
     * @param context   The context of the application
     *
     * @return dest     Returns the LatLng object containing the coordinates to the destination.
     */
    public LatLng getLatLngFromAddress(String addr, Context context) {
        Log.d("DEVICELOCAL = ", "" + Locale.getDefault());

        LatLng dest = null;

        Geocoder gc = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gc.getFromLocationName(addr, 5);
            if(addresses.size() > 0) {
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

    /*public LatLng getDeviceLocation() {
        // Here is where we will start to add the Devices location as a variable
        LatLng curLoc = null;
        mMap.setMyLocationEnabled(true);    // Enable Location layer
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); // Get LocationManager from the System Service
        Criteria criteria = new Criteria(); // Create a criteria object to retrieve best provider
        String provider = locationManager.getBestProvider(criteria, true);  // Retrieve the name of the best provider
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);   // Get current location
        if(myLocation != null) {
            Log.d("CUR_LAT", "" + myLocation.getLatitude());
            Log.d("CUR_LNG", "" + myLocation.getLongitude());
            curLoc = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());    // Create a LatLng object of the devices current location.
        }
        else
            Log.d("DEVICE_LOCATION_FAILURE", "Failed to retrieve the device location");
        // End device location block
        return curLoc;
    }*/

    /**
     * Get the LatLng path from point A to point B on the map and pass that back to the main
     * program.
     *
     * @return latlngPath The list of LatLng points that makes up the shortest distance path.
     */
    public List<LatLng> getLatLngPath() {
        return latlngPath;
    }

}
