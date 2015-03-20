package com.mealsoffwheels.dronedelivery.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.FoodNames;

import java.util.ArrayList;


public class OrderActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ListView list = (ListView) findViewById(R.id.CurrentOrderList);

        ArrayList<String> order = new ArrayList<>();

        for (int i = 1; i <= FoodNames.numberOfNames(); ++i) {
            order.add(FoodNames.getFoodName(i));
        }

        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, order));

    }

}
