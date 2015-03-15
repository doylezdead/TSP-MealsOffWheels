package com.mealsoffwheels.dronedelivery;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class ItemActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Button cancelButton = (Button) findViewById(R.id.cancelButtonItemPage);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMenuPage();
            }
        });
    }

    private void toOrderPage() {

    }

    private void toPaymentPage() {

    }

    private void toMenuPage() {
        startActivity(new Intent(this, FoodMenuActivity.class));
    }

}
