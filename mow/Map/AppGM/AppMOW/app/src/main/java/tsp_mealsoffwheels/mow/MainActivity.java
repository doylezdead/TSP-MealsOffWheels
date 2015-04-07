package tsp_mealsoffwheels.mow;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import static tsp_mealsoffwheels.mow.MarkerAnimation.*;

public class MainActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private PolylineOptions path;
    private LatLng droneHome = new LatLng(47.11240, -88.58705);
    private LatLng thisHome;
    private LatLng deviceLocation;
    private Marker mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        // Just adding a generic marker
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        droneHome, 12.0f)
        );

        thisHome = getLatLngFromAddress("1304 College Avenue, Houghton MI 49931", getApplicationContext());

        mMap.addMarker(new MarkerOptions().position(
                        thisHome).title("Dave's Home")
        );

        deviceLocation = getDeviceLocation();

        mMap.addMarker(new MarkerOptions().position(droneHome).title("Taco Bell!!!"));
        mark = mMap.addMarker(new MarkerOptions().position(droneHome).title("Drone"));

        path = new PolylineOptions().geodesic(true)
                .add(droneHome)                             //  Taco Bell on Razorback
                .add(thisHome)
                .color(Color.argb(1000,128,0,128));

        mMap.addPolyline(path);

        //Log.d("BEARING", "should be 069.28 and is " + getBearing(droneHome, thisHome) + " so error = " + (getBearing(droneHome, thisHome) - 069.28));
        Log.d("DISTANCE", "The distance between two points on the map is");
        getLatLngFromAddress("4820 Westchester Commons, Traverse City MI 49684", getApplicationContext());

        //LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if(mMap != null) {
                    animateMarkerAtoB(mark, thisHome, new LatLngInterpolator.Spherical());
                }
            }
        });
    }

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

    public LatLng getDeviceLocation() {
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
            mMap.addMarker(new MarkerOptions().position(curLoc).title("Device Location"));
        }
        else
            Log.d("DEVICE_LOCATION_FAILURE", "Failed to retrieve the device location");
        // End device location block
        return curLoc;
    }
}
