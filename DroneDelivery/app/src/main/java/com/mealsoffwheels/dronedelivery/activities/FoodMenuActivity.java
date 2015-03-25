package com.mealsoffwheels.dronedelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.FoodNames;

import java.util.ArrayList;


public class FoodMenuActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        ListView listView = (ListView) findViewById(R.id.MenuList);

        ArrayList<String> foods = new ArrayList<>();

        for (int i = 0; i < FoodNames.names.length; ++i) {
            foods.add("#" + (i + 1) + ((i + 1) < 10 ? "    " : "  ") + FoodNames.names[i]);
        }

        ListAdapter list = new ArrayAdapter<>(this, R.layout.list_view, foods);

        listView.setAdapter(list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toItemPage(position);
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMenuPage();
            }
        });
    }

    private void toMenuPage() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void toItemPage(int item) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra(FoodMenuActivity.class.getName(), item);
        startActivity(intent);
    }

}
