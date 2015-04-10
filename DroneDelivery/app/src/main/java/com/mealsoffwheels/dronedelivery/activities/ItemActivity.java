package com.mealsoffwheels.dronedelivery.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.ItemDatabase;

/**
 * Activity defining a page to see data about a food
 * that may be bought.
 *
 * @author Eric Kosovec
 */
public class ItemActivity extends ActionBarActivity {

    private String itemName;
    private EditText editText;

    /**
     * Called when the Activity gets displayed.
     * Sets up food Image and price.
     *
     * @param savedInstanceState - Stores saved variables.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        itemName = "";

        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();

        // Get the item that was selected.
        if (intent != null) {
            itemName = intent.getStringExtra(FoodMenuActivity.class.getName());
            actionBar.setTitle(itemName);

            ImageView imageView = (ImageView) findViewById(R.id.FoodImage);
            imageView.setImageResource(ItemDatabase.getData(itemName).image);
        }

        editText = (EditText) findViewById(R.id.Quantity);

        Button addToOrderButton = (Button) findViewById(R.id.AddToOrderButton);
        addToOrderButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the Button is clicked.
             * Calls method to add the item to the
             * order and start the order activity.
             *
             * @param v - Button's View that was clicked.
             */
            @Override
            public void onClick(View v) {
                addToOrder();
            }
        });
    }

    /**
     * Adds the item to the order list and starts the order Activity.
     */
    private void addToOrder() {
        if (itemName == null || itemName.equals("") || editText == null) {
            return;
        }

        // Get what amount wanted and correct the amount to the max and min allowed.
        int requestedAmount = Integer.parseInt(editText.getText().toString());

        if (requestedAmount <= 0) {
            requestedAmount = 1;
        } else if (requestedAmount > 50) {
            requestedAmount = 50;
        } else {
            requestedAmount = Integer.parseInt(editText.getText().toString());
        }

        SharedPreferences prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.values",
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        // Points to last order place.
        int end = prefs.getInt("Last", 0);

        String foodName = "";
        boolean found = false;
        int quantity = 0;
        int i;

        // Try to find if the item has already been ordered.
        for (i = 1; i <= end; ++i) {
            foodName = prefs.getString(Integer.toString(i), "");
            quantity = prefs.getInt(Integer.toString(i) + "quantity", 0);

            if (foodName.equals(itemName)) {
                found = true;
                break;
            }
        }

        // Update the quantity if found.
        if (found) {
            editor.putInt(Integer.toString(i) + "quantity",
                    quantity + requestedAmount);
            editor.commit();
        }

        // Update the end pointer and add the new order item.
        else {
            ++end;

            editor.putInt("Last", end);
            editor.commit();

            String endName = Integer.toString(end);

            editor.putString(endName, itemName);
            editor.commit();

            editor.putInt(endName + "quantity", requestedAmount);
            editor.commit();
        }

        startActivity(new Intent(this, OrderActivity.class));
        finish();
    }
}
