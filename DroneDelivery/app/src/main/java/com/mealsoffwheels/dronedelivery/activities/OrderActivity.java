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

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.Foods;

import java.util.ArrayList;

public class OrderActivity extends ActionBarActivity {

    private ArrayList<String> order;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

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

        SharedPreferences prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.orders", Context.MODE_PRIVATE);

        int end = prefs.getInt("Last", 0);

        // There exist orders.
        if (end > 0) {
            for (int i = 1; i <= end; ++i) {
                String foodName = prefs.getString(Integer.toString(i), "");
                int quantity = prefs.getInt(Integer.toString(i) + "quantity", -1);

                // Was not removed from the order.
                if (!foodName.equals("") && quantity > 0) {
                    order.add(foodName + ((quantity == 1) ? "" : " (" + quantity + ")"));
                }

            }
        }

        list.setAdapter(new ArrayAdapter<>(this, R.layout.list_view, order));

        Button clearButton = (Button) findViewById(R.id.deleteAll);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearOrderList();
            }
        });
    }

    private void clearOrderList() {
        SharedPreferences prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.orders", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        int end = prefs.getInt("Last", 0);

        if (end > 0) {
            for (int i = 1; i <= end; ++i) {
                editor.remove(Integer.toString(i));
                editor.remove(Integer.toString(i) + "quantity");
                editor.commit();
            }

            editor.putInt("Last", 0);
            editor.commit();
        }

        Handler handle = new Handler();
        handle.post(new UpdateUI());
    }

    private void dataToBuyPage() {
        if (order == null) {
            return;
        }

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(OrderActivity.class.getName(), order.toArray());
        startActivity(intent);
    }

    private void toMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private class UpdateUI implements Runnable {
        @Override
        public void run() {
            order.clear();
            list.invalidateViews();
        }
    }

}
