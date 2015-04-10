package com.mealsoffwheels.dronedelivery.activities;

import android.content.*;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.*;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.Payload;

import java.io.*;
import java.net.Socket;

/**
 * Defines an Activity for payment from the user for
 * their order, then tells the server the needed info
 * to get the order delivered to the user.
 *
 * @author Eric Kosovec
 */
public class PaymentActivity extends ActionBarActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private int weight;

    /**
     * Called when the Activity gets displayed.
     *
     * Gets the cost and weight of the order to
     * display and send to server, respectively.
     *
     * @param savedInstanceState - Stores saved variables.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(113, 24, 140)));

        prefs = getSharedPreferences("com.mealsoffwheels.dronedelivery.values", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.commit();

        Intent intent = getIntent();

        if (intent != null) {
            double total = intent.getDoubleExtra(OrderActivity.class.getName() + "cost", -1);
            weight = intent.getIntExtra(OrderActivity.class.getName() + "weight", -1);

            // On error, just go back to main menu to avoid them getting a free order.
            if (total == -1 || weight == -1) {
                errorCase();
            } else {
                // Display the cost to the user.
                ((TextView) findViewById(R.id.TotalView)).setText("Total: $" + String.format("%.2f", total));
            }
        } else {
            errorCase();
        }

        Button accept = (Button) findViewById(R.id.AcceptButton);
        accept.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when Button is clicked.
             *
             * Calls the method to parse and gather user
             * data to send to the server.
             *
             * @param v - Button's view.
             */
            @Override
            public void onClick(View v) {
                acceptAction();
            }
        });

        Button decline = (Button) findViewById(R.id.DeclineButton);
        decline.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when Button is clicked.
             *
             * Calls method to navigate back to the order page.
             *
             * @param v - Button's view.
             */
            @Override
            public void onClick(View v) {
                declineAction();
            }
        });
    }

    /**
     * Parses user input and sends the data to the server to
     * get the user their order delivered.
     */
    private void acceptAction() {
        EditText name = (EditText) findViewById(R.id.UsersName);
        EditText phone = (EditText) findViewById(R.id.PhoneNumber);
        EditText email = (EditText) findViewById(R.id.Email);
        EditText streetAddr = (EditText) findViewById(R.id.StreetAddress);
        EditText city = (EditText) findViewById(R.id.City);
        EditText zip = (EditText) findViewById(R.id.ZipCode);

        String userName = name.getText().toString();
        String phoneNumber = phone.getText().toString();
        String emailAddress = email.getText().toString();

        // Initially indicate no address was given.
        editor.putInt("Address Given", 0);
        editor.commit();

        // No username specified.
        if (userName.length() <= 1) {
            return;
        }

        String contact;
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

        // Gave neither phone # nor e-mail.
        if (!phoneGood && !emailGood) {
            return;
        }

        // Gave one or the other, so set one as the contact.
        if (!phoneGood) {
            contact = emailAddress;
        } else {
            contact = phoneNumber;
        }

        int storeID = prefs.getInt("storeID", -1);

        // Could not get the store ID.
        if (storeID == -1) {
            return;
        }

        SendOrder sendOrder = new SendOrder(userName, contact, storeID, weight);

        try {
            sendOrder.execute().get(); // Blocks until done.

            // Can't determine where they live, so use GPS coordinates.
            if (streetAddr.length() <= 0 && zip.length() <= 0) {
                editor.putInt("Address Given", 0);
                editor.commit();
            }

            // Where they live was given.
            else {
                editor.putInt("Address Given", 1);
                editor.putString("Street Address", streetAddr.getText().toString().trim());
                editor.putString("Zip Code", zip.getText().toString().trim());
                editor.putString("City", city.getText().toString().trim());
                editor.commit();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, DroneFinder.class));
        finish();
    }

    /**
     * Navigates back to the order page on a decline of paying.
     */
    private void declineAction() {
        startActivity(new Intent(this, OrderActivity.class));
        finish();
    }

    /**
     * Goes back to the main menu in the case of an error.
     */
    private void errorCase() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * Sends the needed order info and contact info to the
     * server.
     */
    private class SendOrder extends AsyncTask<Void, Void, Void> {
        private final int PORT = 25565;
        private final String HOST = "doyle.pw";

        private String userName;
        private String contact;
        private int storeID;
        private int weight;

        /**
         * Takes in the data needed to send to the
         * server.
         *
         * @param userName - User's full name.
         * @param contact - Given e-mail or phone number.
         * @param storeID - Store ID of the store to send the order to.
         * @param weight - Total weight in grams of the order.
         */
        public SendOrder(String userName, String contact, int storeID, int weight) {
            this.userName = userName;
            this.contact = contact;
            this.storeID = storeID;
            this.weight = weight;
        }

        /**
         * Runs whenever execute method is called on the SendOrder class.
         *
         * Sets up a connection to the server, sends data, then waits for
         * a response and acts accordingly to the server response.
         *
         * @param arg - Unused.
         *
         * @return - Always null.
         */
        @Override
        protected Void doInBackground(Void... arg) {
            // Data to send.
            Payload payload = new Payload(
                    storeID,
                    1,
                    prefs.getFloat("Longitude", 0f),
                    prefs.getFloat("Latitude", 0f),
                    userName,
                    contact
            );

            payload.weight = weight;

            try {
                // Get connection to server.
                Socket socket = new Socket(HOST, PORT);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                // Send payload to server.
                oos.writeObject(payload);

                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                Payload receivedPayload = null;

                // Keep trying for a response from the server.
                while (receivedPayload == null) {
                    receivedPayload = (Payload) ois.readObject();

                    if (receivedPayload == null) {
                        SystemClock.sleep(1000);
                    }
                }

                oos.close();
                ois.close();
                socket.close();

                // Parse received payload from server. If neither case holds,
                // must be an error.
                if (receivedPayload.value < 0 || receivedPayload.opcode != 1) {
                    errorCase();
                    editor.remove("Address Given");
                    editor.commit();
                    return null;
                }

                // Store the order ID to get later.
                editor.putInt("orderID", receivedPayload.value);
                editor.commit();

                // Remove all orders from the list.
                clearOrderList();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                errorCase();
            }

            return null;
        }

        /**
         * Clears the order list in the case of a
         * successful purchase.
         */
        private void clearOrderList() {
            int end = prefs.getInt("Last", 0);

            for (int i = 1; i <= end; ++i) {
                editor.remove(Integer.toString(i));
                editor.remove(Integer.toString(i) + "quantity");
                editor.commit();
            }

            // Reset last legit order location.
            editor.putInt("Last", 0);
            editor.commit();
        }
    }
}
