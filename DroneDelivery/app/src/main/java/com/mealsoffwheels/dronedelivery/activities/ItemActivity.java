package com.mealsoffwheels.dronedelivery.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.Foods;

import java.io.FileInputStream;

public class ItemActivity extends ActionBarActivity {

    private int itemNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        itemNumber = -1;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();

        TextView itemName = (TextView) findViewById(R.id.ItemName);

        if (intent != null) {
            itemNumber = intent.getIntExtra(FoodMenuActivity.class.getName(), 0);
            itemName.setText(Foods.combos[itemNumber]);
        }

        else {
            itemName.setText("Item Page");
        }

        if (itemNumber == 0) {
            ImageView imageView = (ImageView) findViewById(R.id.FoodImage);
            imageView.setBackgroundResource(Foods.foodImage[0]);
        }

        Button addToOrderButton = (Button) findViewById(R.id.AddToOrderButton);
        addToOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToOrder();
            }
        });

        Button cancelButton = (Button) findViewById(R.id.BackButtonItemPage);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMenuPage();
            }
        });
    }

    private void addToOrder() {
        if (itemNumber == -1) {
            return;
        }

        SharedPreferences prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.orders", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        // Points to last order place.
        int end = prefs.getInt("Last", 0);

        ++end;

        editor.putInt("Last", end);
        editor.commit();

        editor.putInt(Integer.toString(end), itemNumber);
        editor.commit();

        startActivity(new Intent(this, OrderActivity.class));
        finish();
    }

    private void toMenuPage() {
        startActivity(new Intent(this, FoodMenuActivity.class));
        finish();
    }
}
