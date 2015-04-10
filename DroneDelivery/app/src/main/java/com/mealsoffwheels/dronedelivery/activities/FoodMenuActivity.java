package com.mealsoffwheels.dronedelivery.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.ItemDatabase;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Activity defining lists of items the user can order.
 *
 * @author Eric Kosovec
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
     * @param savedInstanceState Holds any data that was saved upon the user
     *                           closing the app temporarily.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(113, 24, 140)));

        if (items == null) {
            items = new ArrayList<>();
        } else {
            items.clear();
        }

        // Defines the drawer containing the items the user may choose.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, items));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Performed when an item on the drawer is clicked.
             *
             * @param parent   - The Adapter items are in.
             * @param view     - The View the Adapter is in.
             * @param position - The position on the Adapter that was clicked.
             * @param id       - ID of the item clicked, usually is the position.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toItemPage((String) parent.getItemAtPosition(position));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            /**
             * Performed when the drawer is closed.
             *
             * @param view - View of the drawer.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                getSupportActionBar().setTitle("Menu");
                // Title of drawer.
                title = "Everything";
                invalidateOptionsMenu();
            }

            /**
             * Performed when the drawer is opened.
             *
             * @param view - View of the drawer.
             */
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                getSupportActionBar().setTitle(title);

                if (title.equals("Everything")) {
                    changeListAndOpenDrawer((char) 0);
                }

                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        ImageButton combos = (ImageButton) findViewById(R.id.CombosButton);
        ImageButton tacos = (ImageButton) findViewById(R.id.TacosButton);
        ImageButton burritos = (ImageButton) findViewById(R.id.BurritosButton);
        ImageButton specialties = (ImageButton) findViewById(R.id.SpecialtiesButton);
        ImageButton drinks = (ImageButton) findViewById(R.id.DrinksButton);

        combos.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the Button is clicked.
             *
             * @param v - Button's View that was clicked.
             */
            @Override
            public void onClick(View v) {
                changeListAndOpenDrawer((char) 1);
            }
        });

        tacos.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the Button is clicked.
             *
             * @param v - Button's View that was clicked.
             */
            @Override
            public void onClick(View v) {
                changeListAndOpenDrawer((char) 2);
            }
        });

        burritos.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the Button is clicked.
             *
             * @param v - Button's View that was clicked.
             */
            @Override
            public void onClick(View v) {
                changeListAndOpenDrawer((char) 3);
            }
        });

        specialties.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the Button is clicked.
             *
             * @param v - Button's View that was clicked.
             */
            @Override
            public void onClick(View v) {
                changeListAndOpenDrawer((char) 4);
            }
        });

        drinks.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the Button is clicked.
             *
             * @param v - Button's View that was clicked.
             */
            @Override
            public void onClick(View v) {
                changeListAndOpenDrawer((char) 5);
            }
        });

    }

    /**
     * To be called when the drawer is opened with
     * the different cases to change what is displayed.
     *
     * @param function - Defines which items to choose to display.
     */
    private void changeListAndOpenDrawer(char function) {
        items.clear();

        switch (function) {
            // Everything
            case 0:
                Collections.addAll(items, ItemDatabase.combos);
                Collections.addAll(items, ItemDatabase.tacos);
                Collections.addAll(items, ItemDatabase.burritos);
                Collections.addAll(items, ItemDatabase.specialties);
                Collections.addAll(items, ItemDatabase.drinks);
                title = "Everything";
                break;

            // Combos
            case 1:
                Collections.addAll(items, ItemDatabase.combos);
                title = "Combos";
                break;

            // Tacos
            case 2:
                Collections.addAll(items, ItemDatabase.tacos);
                title = "Tacos";
                break;

            // Burritos
            case 3:
                Collections.addAll(items, ItemDatabase.burritos);
                title = "Burritos";
                break;

            // Specialties
            case 4:
                Collections.addAll(items, ItemDatabase.specialties);
                title = "Specialties";
                break;

            // Drinks
            case 5:
                Collections.addAll(items, ItemDatabase.drinks);
                title = "Drinks";
                break;

            default:
                return;
        }

        drawerList.invalidateViews();
        drawerLayout.openDrawer(drawerList);
    }

    /**
     * Called when an ActionButton icon is clicked.
     *
     * @param item - The clicked item.
     * @return - Always returns true.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && !drawerLayout.isDrawerOpen(drawerList)) {
            drawerLayout.openDrawer(drawerList);
        } else if (item.getItemId() == android.R.id.home && drawerLayout.isDrawerOpen(drawerList)) {
            drawerLayout.closeDrawer(drawerList);
        }

        return true;
    }

    /**
     * Called after creating the Activity.
     *
     * @param savedInstanceState - Bundle containing variables
     *                           that were saved.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /**
     * Called whenever the configuration changes.
     *
     * @param newConfig - The configuration that was changed to.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Called when the back button is pressed on the lower task bar.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(drawerList)) {
            drawerLayout.closeDrawer(drawerList);
        }

        // Do default action if drawer is closed.
        else {
            super.onBackPressed();
        }
    }

    /**
     * Navigates to the Item Activity page.
     *
     * @param itemName - Item's name to send to item activity.
     */
    private void toItemPage(String itemName) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra(FoodMenuActivity.class.getName(), itemName);
        startActivity(intent);
    }

}
