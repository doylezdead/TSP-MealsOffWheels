package com.mealsoffwheels.dronedelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.FoodNames;

import java.util.ArrayList;


public class OrderActivity extends ActionBarActivity {

    private ArrayList<String> order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button buyButton = (Button) findViewById(R.id.BuyButtonOrderPage);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataToBuyPage();
            }
        });

        ListView list = (ListView) findViewById(R.id.CurrentOrderList);

        if (order == null) {
            order = new ArrayList<>();
        }

        else {
            order.clear();
        }

        for (int i = 1; i <= FoodNames.numberOfNames(); ++i) {
            order.add(FoodNames.getFoodName(i));
        }

        list.setAdapter(new ArrayAdapter<>(this, R.layout.list_view, order));
    }

    private void dataToBuyPage() {
        if (order == null) {
            return;
        }

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(OrderActivity.class.getName(), order.toArray());
        startActivity(intent);
    }

}
