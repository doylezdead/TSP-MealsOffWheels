package com.mealsoffwheels.dronedelivery.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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

    /**
     * Called when the Activity gets displayed.
     *
     * @param savedInstanceState - Stores saved variables.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(79, 17, 98)));

        String htmlText =
                "<h2 style=\"text-align: center;\">Meals Off Wheels</h2>\n" +
                        "\n" +
                        "<p>Team Members:</p>\n" +
                        "\n" +
                        "David Mohrhardt, " +
                        "Eric Kosovec, " +
                        "Mitchell Anderson, " +
                        "Ryan Doyle, " +
                        "and Zhen Piao\n" +
                        "\n" +
                        "<h2 style=\"text-align: center;\">Drone Delivery</h2>\n" +
                        "\n" +
                        "<p>Made for Team Software Project, the Drone Delivery application will" +
                        " now allow you to get Taco Bell delivered straight to your door.&nbsp;" +
                        " However, it won&#39;t be a person delivering to you, it will be an autonomous" +
                        " drone.</p>\n";

        ((TextView) findViewById(R.id.AboutText)).setText(Html.fromHtml(htmlText));
    }

}
