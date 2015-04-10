package com.mealsoffwheels.dronedelivery.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.ItemDatabase;

import java.util.ArrayList;

/**
 * Activity that displays the user's order.
 *
 * @author Eric Kosovec
 */
public class OrderActivity extends ActionBarActivity {

    private ArrayList<String> order;
    private ListView list;

    /**
     * Called when the Activity gets displayed.
     * Sets up Buttons and List display.
     *
     * @param savedInstanceState - Stores saved variables.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Current Orders");

        Button cancelButton = (Button) findViewById(R.id.CancelButtonOrderPage);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMainMenu();
            }
        });

        Button buyButton = (Button) findViewById(R.id.BuyButtonOrderPage);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataToBuyPage();
            }
        });

        list = (ListView) findViewById(R.id.CurrentOrderList);

        if (order == null) {
            order = new ArrayList<>();
        } else {
            order.clear();
        }

        list.invalidateViews();

        ((TextView) findViewById(R.id.TotalViewOrders)).setText("Total: $" + String.format("%.2f", computePrice()));

        SharedPreferences prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.values", Context.MODE_PRIVATE);

        int end = prefs.getInt("Last", 0);

        // Gets all the food names and quantities from SharedPreferences.
        for (int i = 1; i <= end; ++i) {
            String foodName = prefs.getString(Integer.toString(i), "");
            int quantity = prefs.getInt(Integer.toString(i) + "quantity", 0);

            // Was not removed from the order.
            if (!foodName.equals("") && quantity > 0) {
                order.add(foodName + ((quantity == 1) ? "" : " (" + quantity + ")"));
            }
        }

        list.setAdapter(new ArrayAdapter<>(this, R.layout.list_view, order));

        Button clearButton = (Button) findViewById(R.id.deleteAll);
        clearButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the Button is clicked.
             * Clears the order list.
             *
             * @param v - Button's View that was clicked.
             */
            @Override
            public void onClick(View v) {
                clearOrderList();
            }
        });
    }

    /**
     * Clears the order list of the names and quantities.
     */
    private void clearOrderList() {
        SharedPreferences prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.values", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        int end = prefs.getInt("Last", 0);

        for (int i = 1; i <= end; ++i) {
            editor.remove(Integer.toString(i));
            editor.remove(Integer.toString(i) + "quantity");
            editor.commit();
        }

        editor.putInt("Last", 0);
        editor.commit();

        ((TextView) findViewById(R.id.TotalViewOrders)).setText("Total: $0.00");

        new Handler().post(new UpdateUI());
    }

    /**
     * Navigates to the Payment Activity.
     */
    private void dataToBuyPage() {
        if (order == null) {
            return;
        }

        SharedPreferences prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.values", Context.MODE_PRIVATE);

        // No order to buy
        if (prefs.getInt("Last", -1) <= 0) {
            return;
        }

        // Compute weight and price
        int weight = computeWeight();
        double price = computePrice();

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(OrderActivity.class.getName() + "weight", weight);
        intent.putExtra(OrderActivity.class.getName() + "cost", price);

        startActivity(intent);
    }

    /**
     * Computes the weight in grams of the order.
     *
     * @return - Weight of order in grams.
     */
    private int computeWeight() {
        int weightSum = 0;

        SharedPreferences prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.values", Context.MODE_PRIVATE);

        int end = prefs.getInt("Last", 0);
        String foodName = "";
        int quantity = 0;

        for (int i = 1; i <= end; ++i) {
            foodName = prefs.getString(Integer.toString(i), "");
            quantity = prefs.getInt(Integer.toString(i) + "quantity", 0);

            // Not a deleted item.
            if (quantity > 0 && !foodName.equals("")) {
                weightSum += (ItemDatabase.getData(foodName).weight * quantity);
                weightSum += (ItemDatabase.getData("Sauce").weight * quantity);

                // Combo case, add drink size weight.
                if (foodName.contains("Combo")) {
                    weightSum += (ItemDatabase.getData("XL").weight * quantity);
                }
            }
        }

        return weightSum;
    }

    /**
     * Computes the price of the current order.
     *
     * @return - Price of current order.
     */
    private double computePrice() {
        double priceSum = 0.;

        SharedPreferences prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.values", Context.MODE_PRIVATE);

        int end = prefs.getInt("Last", 0);
        String foodName = "";
        int quantity = 0;

        for (int i = 1; i <= end; ++i) {
            foodName = prefs.getString(Integer.toString(i), "");
            quantity = prefs.getInt(Integer.toString(i) + "quantity", 0);

            // Not a deleted item.
            if (quantity > 0 && !foodName.equals("")) {
                priceSum += (ItemDatabase.getData(foodName).price * quantity);

                // Combo case, add drink size price.
                if (foodName.contains("Combo")) {
                    priceSum += (ItemDatabase.getData("XL").price * quantity);
                }
            }
        }

        return priceSum;
    }

    /**
     * Navigates back to the Main Activity.
     */
    private void toMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Clears the order list view and refreshes it
     * on a different thread.
     */
    private class UpdateUI implements Runnable {
        /**
         * Runs to clear the list view.
         */
        @Override
        public void run() {
            order.clear();
            findViewById(R.id.TotalViewOrders).invalidate();
            list.invalidateViews();
        }
    }

}
