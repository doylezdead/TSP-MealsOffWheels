package com.mealsoffwheels.dronedelivery.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.FoodNames;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
            itemName.setText(FoodNames.names[itemNumber]);
        }

        else {
            itemName.setText("Item Page");
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

        try {
            /*String csvLine = "0," + itemNumber + '\n';
            FileOutputStream outputStream = openFileOutput(MainActivity.ORDER_LIST_NAME, Context.MODE_PRIVATE);
            outputStream.write("Hello, world\n".getBytes());
            outputStream.write(csvLine.getBytes());
            outputStream.flush();
            outputStream.close();*/

            FileInputStream in = openFileInput(MainActivity.ORDER_LIST_NAME);
            byte[] buff = new byte[2046];
            int max = in.read(buff);
            String str = "";
            for (int i = 0; i < max; ++i) {
                str += (char)buff[i];
            }
            System.out.print(str);
            in.close();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toMenuPage() {
        startActivity(new Intent(this, FoodMenuActivity.class));
    }
}
