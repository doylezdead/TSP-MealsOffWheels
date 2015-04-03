package com.mealsoffwheels.dronedelivery.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.Foods;

import java.io.FileInputStream;

public class ItemActivity extends ActionBarActivity {

    private String itemName;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        itemName = "";

        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();

        if (intent != null) {
            itemName = intent.getStringExtra(FoodMenuActivity.class.getName());
            actionBar.setTitle(itemName);
        }

        editText = (EditText) findViewById(R.id.Quantity);

        Button addToOrderButton = (Button) findViewById(R.id.AddToOrderButton);
        addToOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToOrder();
            }
        });
    }

    private void addToOrder() {
        if (itemName == null || itemName.equals("") || editText == null) {
            return;
        }

        SharedPreferences prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.orders", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        boolean found = false;

        // Points to last order place.
        int end = prefs.getInt("Last", 0);

        for (int i = 1; i <= end; ++i) {
            String foodName = prefs.getString(Integer.toString(i), "");
            int quantity = prefs.getInt(Integer.toString(i) + "quantity", -1);

            if (foodName.equals(itemName)) {
                editor.putInt(foodName + "quantity", quantity + Integer.parseInt(editText.getText().toString()));
                editor.commit();
                System.out.println("Found with " + Integer.parseInt(editText.getText().toString()));
                System.out.println("New value is " + prefs.getInt(foodName + "quantity", -1));
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Not found");
            ++end;

            editor.putInt("Last", end);
            editor.commit();

            String endName = Integer.toString(end);

            editor.putString(endName, itemName);
            editor.commit();
            editor.putInt(endName + "quantity", Integer.parseInt(editText.getText().toString()));
            editor.commit();
        }

        startActivity(new Intent(this, OrderActivity.class));
        finish();
    }

    private void toMenuPage() {
        startActivity(new Intent(this, FoodMenuActivity.class));
        finish();
    }
}
