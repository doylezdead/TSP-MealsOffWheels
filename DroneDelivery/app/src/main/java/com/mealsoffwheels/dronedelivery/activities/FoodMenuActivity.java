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

import com.mealsoffwheels.dronedelivery.R;

import java.util.ArrayList;


public class FoodMenuActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        GridView gridView = (GridView) findViewById(R.id.gridView);

        ArrayList<String> foods = new ArrayList<>();

        foods.add("#1");
        foods.add("#2");
        foods.add("#3");
        foods.add("#4");
        foods.add("#5");
        foods.add("#6");
        foods.add("#7");
        foods.add("#8");
        foods.add("#9");
        foods.add("#10");

        ListAdapter list = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foods);

        gridView.setAdapter(list);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
