package com.mealsoffwheels.dronedelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mealsoffwheels.dronedelivery.R;


public class PaymentActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();

        TextView text = (TextView) findViewById(R.id.TotalView);

        if (intent != null) {
            double total = intent.getDoubleExtra(OrderActivity.class.getName() + "cost", -1);
            int weight = intent.getIntExtra(OrderActivity.class.getName() + "weight", -1);

            if (total == -1 || weight == -1) {
                errorCase();
            }

            else {
                text.setText("Total: $" + total); // TODO: Need to truncate to 2 decimal places.
            }
        }

        else {
            errorCase();
        }

        final Button accept = (Button) findViewById(R.id.AcceptButton);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptAction();
            }
        });

        Button decline = (Button) findViewById(R.id.DeclineButton);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineAction();
            }
        });
    }

    private void acceptAction() {
        // TODO: GET DATA FROM FIELDS
        // SEND TO SERVER
        // IF ANYTHING WRONG IN TEXT FIELDS,
        // HAVE TOAST MESSAGE POP UP
    }

    private void declineAction() {
        startActivity(new Intent(this, OrderActivity.class));
        finish();
    }

    private void errorCase() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
