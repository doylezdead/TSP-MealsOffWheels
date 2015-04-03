package com.mealsoffwheels.dronedelivery.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.Foods;
import com.mealsoffwheels.dronedelivery.common.Payload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends ActionBarActivity {

    private final char FOOD_MENU_NUM = 0;
    private final char ORDER_NUM = 1;
    private final char DRONE_STATUS_NUM = 2;
    private final char ABOUT_NUM = 3;

    private int longitude = Integer.MIN_VALUE;
    private int latitude = Integer.MIN_VALUE;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private Location userLocation = null;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide Action bar
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
        Button dButton = (Button) findViewById(R.id.DroneStatusButton);
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

        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.MenuButtonLayout);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(FOOD_MENU_NUM);
            }
        });

        LinearLayout ordersLayout = (LinearLayout) findViewById(R.id.OrdersButtonLayout);
        ordersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(ORDER_NUM);
            }
        });

        LinearLayout droneStatusLayout = (LinearLayout) findViewById(R.id.DroneStatusLayout);
        droneStatusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(DRONE_STATUS_NUM);
            }
        });

        LinearLayout aboutLayout = (LinearLayout) findViewById(R.id.AboutLayout);
        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(ABOUT_NUM);
            }
        });

        prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.orders", Context.MODE_PRIVATE);

        editor = prefs.edit();

        // Indicate no current orders, if there are none
        if (prefs.getInt("Last", -1) == -1) {
            editor.putInt("Last", 0);
            editor.commit();
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new FindLocation();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

       // new GetGPS().execute();
    }

    private class FindLocation implements LocationListener {

        public void onLocationChanged(Location location) {
            if (location == null) {
                return;
            }

            else {
                userLocation = location;
                locationManager.removeUpdates(locationListener); // also remove if away from app
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    }

    // Convert to some other thread?
    private class GetGPS extends AsyncTask<Void, Void, Void> {

        private final int PORT = 25565;
        private final String HOST = "doyle.pw";

        @Override
        protected Void doInBackground(Void... arg) {

            long totalTime = 0;

            // Wait to get GPS location.
            while (userLocation == null) {
                SystemClock.sleep(1000);
                totalTime += 1000;

                // Keep trying for 2 minutes.
                if (totalTime >= 120000) {
                    return null;
                }
            }

            if (editor != null) {
                editor.putFloat("Longitude", (float)userLocation.getLongitude());
                editor.commit();
                editor.putFloat("Latitude", (float) userLocation.getLatitude());
                editor.commit();
            }

            Payload payload = new Payload(
                    0,
                    userLocation.getLongitude(),
                    userLocation.getLatitude(),
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

                totalTime = 0;

                while (receivedPayload == null) {
                    receivedPayload = (Payload) ois.readObject();

                    if (receivedPayload == null) {
                        SystemClock.sleep(1000);
                        totalTime += 1000;

                        if (totalTime >= 180000) {
                            return null; // didn't receive in 3 minutes. Up time maybe?
                        }
                    }
                }

                boolean error = false;
                if (receivedPayload.opcode != 0 || receivedPayload.value < 0) {
                    error = true;
                }

                editor.putInt("StoreID", receivedPayload.value);
                editor.commit();

                if (error) {
                    // give message
                }
            }

            catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {

        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }


    private void toNewActivity(char act) {
        switch (act) {
            case FOOD_MENU_NUM:
                startActivity(new Intent(this, FoodMenuActivity.class));
                break;
            case ORDER_NUM:
                startActivity(new Intent(this, OrderActivity.class));
                break;
            case DRONE_STATUS_NUM:
                startActivity(new Intent(this, DroneStatusActivity.class));
                break;
            case ABOUT_NUM:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

}
