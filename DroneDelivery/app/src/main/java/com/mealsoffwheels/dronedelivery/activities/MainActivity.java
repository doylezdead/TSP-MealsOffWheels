package com.mealsoffwheels.dronedelivery.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.Payload;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Activity class defining the main menu
 * the user first sees when the application is
 * opened.
 *
 * @author Eric Kosovec
 */
public class MainActivity extends ActionBarActivity {

    // Defines cases of which Activity to navigate to.
    private final char FOOD_MENU_NUM = 0;
    private final char ORDER_NUM = 1;
    private final char DRONE_STATUS_NUM = 2;
    private final char ABOUT_NUM = 3;

    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private Location userLocation = null;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button menuButton = (Button) findViewById(R.id.MenuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(FOOD_MENU_NUM);
            }
        });

        Button oButton = (Button) findViewById(R.id.OrdersButton);
        oButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(ORDER_NUM);
            }
        });

        Button dButton = (Button) findViewById(R.id.StatusButton);
        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(DRONE_STATUS_NUM);
            }
        });

        Button aButton = (Button) findViewById(R.id.AboutButton);
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(ABOUT_NUM);
            }
        });

        // Check if last is defined, if not, define it.
        prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.values", Context.MODE_PRIVATE);

        editor = prefs.edit();

        // If last order position is not defined, define it.
        if (prefs.getInt("Last", -1) == -1) {
            editor.putInt("Last", 0);
            editor.commit();
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new FindLocation();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        new GetGPS().execute();
    }

    /**
     * Navigates to a new Activity based on the given
     * number.
     *
     * @param act - The number defining the Activity
     *            to go to, as defined above.
     */
    private void toNewActivity(char act) {
        switch (act) {
            case FOOD_MENU_NUM:
                startActivity(new Intent(this, FoodMenuActivity.class));
                break;
            case ORDER_NUM:
                startActivity(new Intent(this, OrderActivity.class));
                break;
            case DRONE_STATUS_NUM:
                startActivity(new Intent(this, DroneFinder.class));
                break;
            case ABOUT_NUM:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

    /**
     * Class to listen to changes in GPS location.
     */
    private class FindLocation implements LocationListener {

        /**
         * Called when the user's location is changed.
         *
         * @param location - The user's location.
         */
        public void onLocationChanged(Location location) {
            if (location != null) {
                userLocation = location;
                locationManager.removeUpdates(locationListener);
            }
        }

        /**
         * Unimplemented.
         *
         * @param provider - Unused.
         * @param status   - Unused.
         * @param extras   - Unused.
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        /**
         * Unimplemented.
         *
         * @param provider - Unused.
         */
        public void onProviderEnabled(String provider) {
        }

        /**
         * Unimplemented.
         *
         * @param provider - Unused.
         */
        public void onProviderDisabled(String provider) {
        }
    }

    /**
     * Thread task to wait for a GPS location and
     * send it to the server in order to get a
     * store ID.
     */
    private class GetGPS extends AsyncTask<Void, Void, Void> {
        private final int PORT = 25565;
        private final String HOST = "doyle.pw";

        /**
         * Performed when execute is called on
         * the GetGPS class. Waits for the
         * GPS location to be discovered, then
         * sends the location to the server and
         * parses the response payload.
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

            SharedPreferences prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.values", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            // Store the coordinates in the SharedPreferences file.
            editor.putFloat("Longitude", (float) userLocation.getLongitude());
            editor.putFloat("Latitude", (float) userLocation.getLatitude());
            editor.commit();

            Payload payload = new Payload(
                    0,
                    0,
                    userLocation.getLatitude(),
                    userLocation.getLongitude(),
                    "",
                    ""
            );

            try {
                Socket socket = new Socket(HOST, PORT);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                oos.writeObject(payload);
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                Payload receivedPayload = null;

                // Keep trying to get payload back from server.
                while (receivedPayload == null) {
                    receivedPayload = (Payload) ois.readObject();

                    if (receivedPayload == null) {
                        SystemClock.sleep(1000);
                    }
                }

                oos.close();
                ois.close();
                socket.close();

                boolean error = false;
                if (receivedPayload.opcode != 0 || receivedPayload.value < 0) {
                    error = true;
                }

                editor.putInt("storeID", receivedPayload.value);
                editor.commit();

                if (error) {
                    System.err.print("Failed to get store ID.");
                    // TODO ERROR MESSAGE
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
