package com.mealsoffwheels.dronedelivery.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.Payload;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class PaymentActivity extends ActionBarActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private int weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();

        TextView text = (TextView) findViewById(R.id.TotalView);

        if (intent != null) {
            double total = intent.getDoubleExtra(OrderActivity.class.getName() + "cost", -1);
            weight = intent.getIntExtra(OrderActivity.class.getName() + "weight", -1);

            if (total == -1 || weight == -1) {
                errorCase();
            }

            else {
                text.setText("Total: $" + String.format("%.2f", total));
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
        EditText name = (EditText) findViewById(R.id.UsersName);
        EditText phone = (EditText) findViewById(R.id.PhoneNumber);
        EditText email = (EditText) findViewById(R.id.Email);

        String userName = name.getText().toString();
        String phoneNumber = phone.getText().toString();
        String emailAddress = email.getText().toString();

        if (userName.length() <= 1) {
            // TODO ERROR
            return;
        }

        String contact = "";
        boolean phoneGood = true;
        boolean emailGood = true;

        // Probably not formatted correctly. Parse later
        if (phoneNumber.length() > 14 || phoneNumber.length() < 10) {
            phoneGood = false;
        }

        // Parse later
        if (emailAddress.length() == 0) {
            emailGood = false;
        }

        if (!phoneGood && !emailGood) {
            return;
        }

        if (!phoneGood) {
            contact = emailAddress;
        }

        else {
            contact = phoneNumber;
        }

        prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.orders", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.commit();

        int storeID = prefs.getInt("storeID", -1);

        // TODO: IF -1, GET STOREID

        SendOrder sendOrder = new SendOrder(userName, contact, storeID, weight);
        sendOrder.execute(); // Block until done?

        startActivity(new Intent(this, DroneStatusActivity.class));
        finish();
    }

    private void declineAction() {
        startActivity(new Intent(this, OrderActivity.class));
        finish();
    }

    private void errorCase() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private class SendOrder extends AsyncTask<Void, Void, Void> {
        private final int PORT = 25565;
        private final String HOST = "doyle.pw";

        private String userName;
        private String contact;
        private int storeID;
        private int weight;

        public SendOrder(String userName, String contact, int storeID, int weight) {
            this.userName = userName;
            this.contact = contact;
            this.storeID = storeID;
            this.weight = weight;
        }

        @Override
        protected Void doInBackground(Void... arg) {
            Payload payload = new Payload(storeID, 0., 0., userName, contact);
            payload.opcode = 1;
            payload.weight = weight;

            try {
                Socket socket = new Socket(HOST, PORT);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                oos.writeObject(payload);

                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                Payload receivedPayload = null;

                while (receivedPayload == null) {
                    receivedPayload = (Payload) ois.readObject();

                    if (receivedPayload == null) {
                        SystemClock.sleep(1000);
                    }
                }

                oos.close();
                ois.close();
                socket.close();

                if (receivedPayload.value < 0 || receivedPayload.opcode != 1) {
                    //Toast toast = new Toast(this);
                    return null;
                }

                SharedPreferences prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.orders", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putInt("orderID", receivedPayload.value);
                editor.commit();
            }

            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
