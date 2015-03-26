package com.mealsoffwheels.dronedelivery.activities;

import android.content.Intent;
import android.os.Bundle;
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

        ListView list = (ListView) findViewById(R.id.CurrentOrderList);

        if (order == null) {
            order = new ArrayList<>();
        }

        else {
            order.clear();
        }

        for (int i = 0; i < 6; ++i) {
            int rand = (int)(Math.random() * Foods.names.length);
            order.add(Foods.names[rand]);
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

    private void toMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
