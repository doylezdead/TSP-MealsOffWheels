package com.mealsoffwheels.dronedelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mealsoffwheels.dronedelivery.R;

public class MainActivity extends ActionBarActivity {

    private final char FOOD_MENU_NUM = 0;
    private final char ORDER_NUM = 1;
    private final char DRONE_STATUS_NUM = 2;
    private final char ABOUT_NUM = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button menuButton = (Button) findViewById(R.id.MenuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(FOOD_MENU_NUM);
            }
        });
        Button oButton = (Button) findViewById(R.id.OrdersButton);
        oButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(ORDER_NUM);
            }
        });
        Button dButton = (Button) findViewById(R.id.DroneStatusButton);
        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(DRONE_STATUS_NUM);
            }
        });
        Button aButton = (Button) findViewById(R.id.AboutButton);
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(ABOUT_NUM);
            }
        });

        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.MenuButtonLayout);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(FOOD_MENU_NUM);
            }
        });

        LinearLayout ordersLayout = (LinearLayout) findViewById(R.id.OrdersButtonLayout);
        ordersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(ORDER_NUM);
            }
        });

        LinearLayout droneStatusLayout = (LinearLayout) findViewById(R.id.DroneStatusLayout);
        droneStatusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(DRONE_STATUS_NUM);
            }
        });

        LinearLayout aboutLayout = (LinearLayout) findViewById(R.id.AboutLayout);
        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity(ABOUT_NUM);
            }
        });

    }

    private void toNewActivity(char act) {
        switch (act) {
            case FOOD_MENU_NUM:
                startActivity(new Intent(this, FoodMenuActivity.class));
                break;
            case ORDER_NUM:
                startActivity(new Intent(this, OrderActivity.class));
                break;
            case DRONE_STATUS_NUM:
                startActivity(new Intent(this, DroneStatusActivity.class));
                break;
            case ABOUT_NUM:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

    // Commented-out in case my class doesn't work
    // <Do in background type, onProgressUpdate type, onPostExecute type>
    /*private class SendData extends AsyncTask<Void, Void, Void> {
        private final int PORT = 25565;
        private final String HOST = "doyle.pw";

        @Override
        protected Void doInBackground(Void... arg) {
            try {
                InetAddress address = InetAddress.getByName(HOST);
                skt = new Socket(address, PORT);

                msg.setText("Data sent");
                msg.invalidate(); // Sets up the GUI to refresh

                oos = new ObjectOutputStream(skt.getOutputStream());

                // Send the payload.
                oos.writeObject(new Payload(0, 5.0, 6.2, "Eric Kosovec"));

                oos.flush(); // Make sure all data was sent.
                oos.close(); // Close stream.
                skt.close(); // Close socket.
            } catch (UnknownHostException ex) {
                msg.setText("Unknown host."); // Have pop-up error box on app?
                msg.invalidate();
            } catch (IOException ex) {
                msg.setText("Failed to send data.");
                msg.invalidate();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {

        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }*/

/*    @Override
    public void onPause() {
        super.onPause();
        // disconnect from server and finish writing data???
        try {
            if (oos != null) {
                oos.flush();
                oos.close();
            }

            if (skt != null) {
                skt.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }*/

}
