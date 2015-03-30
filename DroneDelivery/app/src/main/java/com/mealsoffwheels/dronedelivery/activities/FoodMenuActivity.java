package com.mealsoffwheels.dronedelivery.activities;

import android.content.*;
import android.content.res.*;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.Foods;

import java.util.*;

/**
 * Activity defining lists of items the user can order.
 */
public class FoodMenuActivity extends ActionBarActivity {
    private ArrayList<String> items;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String title = "Everything";

    /**
     * Method called by System when orientation is changed, or the activity
     * comes into view.
     *
     * @param savedInstanceState
     *      Holds any data that was saved upon the user
     *      closing the app temporarily.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        items = new ArrayList<>();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, items));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toItemPage(position); // for now
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Menu");
                title = "Everything";
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                getSupportActionBar().setTitle(title);

                if (title.equals("Everything")) {
                    changeListAndOpenDrawer((char)0);
                }

                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        Button combos = (Button) findViewById(R.id.CombosButton);
        Button tacos = (Button) findViewById(R.id.TacosButton);
        Button burritos = (Button) findViewById(R.id.BurritosButton);
        Button specialties = (Button) findViewById(R.id.SpecialtiesButton);
        Button drinks = (Button) findViewById(R.id.DrinksButton);

        combos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               changeListAndOpenDrawer((char)1);
            }
        });

        tacos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeListAndOpenDrawer((char)2);
            }
        });

        burritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeListAndOpenDrawer((char)3);
            }
        });

        specialties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeListAndOpenDrawer((char)4);
            }
        });

        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeListAndOpenDrawer((char)5);
            }
        });
    }

    private void changeListAndOpenDrawer(char function) {
        items.clear();

        switch (function) {
            // Everything
            case 0:
                Collections.addAll(items, Foods.combos);
                Collections.addAll(items, Foods.tacos);
                Collections.addAll(items, Foods.burritos);
                Collections.addAll(items, Foods.specialties);
                Collections.addAll(items, Foods.drinks);
                title = "Everything";
                break;

            // Combos
            case 1:
                Collections.addAll(items, Foods.combos);
                title = "Combos";
                break;

            // Tacos
            case 2:
                Collections.addAll(items, Foods.tacos);
                title = "Tacos";
                break;

            // Burritos
            case 3:
                Collections.addAll(items, Foods.burritos);
                title = "Burritos";
                break;

            // Specialties
            case 4:
                Collections.addAll(items, Foods.specialties);
                title = "Specialties";
                break;

            // Drinks
            case 5:
                Collections.addAll(items, Foods.drinks);
                title = "Drinks";
                break;

            default:
                return;
        }

        drawerList.invalidateViews();

        drawerLayout.openDrawer(drawerList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && !drawerLayout.isDrawerOpen(drawerList)) {
            drawerLayout.openDrawer(drawerList);
        }

        else if (item.getItemId() == android.R.id.home && drawerLayout.isDrawerOpen(drawerList)) {
            drawerLayout.closeDrawer(drawerList);
        }

        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(drawerList)) {
            drawerLayout.closeDrawer(drawerList);
        }

        else {
            super.onBackPressed();
        }
    }

    private void toItemPage(int item) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra(FoodMenuActivity.class.getName(), item);
        startActivity(intent);
    }

}
