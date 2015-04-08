package com.mealsoffwheels.dronedelivery.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.mealsoffwheels.dronedelivery.R;

/**
 * Activity defining the place the user can see the drone location,
 * status, and estimated arrival time. Also, displays the Google
 * Map API.
 *
 * @author Eric Kosovec
 */
public class DroneStatusActivity extends ActionBarActivity {

    /**
     * Called when the Activity gets displayed.
     *
     * @param savedInstanceState - Stores saved variables.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_status);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

}
