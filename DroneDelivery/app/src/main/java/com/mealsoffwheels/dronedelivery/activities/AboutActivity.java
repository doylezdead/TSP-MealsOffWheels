package com.mealsoffwheels.dronedelivery.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.widget.TextView;

import com.mealsoffwheels.dronedelivery.R;

/**
 * About Activity that gives a summary of the application's
 * creation and purpose.
 *
 * @author Eric Kosovec
 */
public class AboutActivity extends ActionBarActivity {

    private String htmlText = "";

    /**
     * Called when the Activity gets displayed.
     *
     * @param savedInstanceState - Stores saved variables.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView aboutText = (TextView) findViewById(R.id.AboutText);

        // htmlText will always be at the bottom of the class to reduce
        // clutter.
        aboutText.setText(Html.fromHtml(htmlText));
    }

}
