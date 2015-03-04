package com.mealsoffwheels.dronedelivery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends ActionBarActivity {

    private TextView msg;
    private ObjectOutputStream oos;
    private Socket skt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide Action bar
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.hide();*/

        // Gets references to layout objects.
        Button sendButton = (Button) findViewById(R.id.SendDataButton);
        msg = (TextView) findViewById(R.id.textView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Run connect to server and data send in background
                SendData sd = new SendData();
                sd.execute();
            }
        });
    }

    // <Do in background type, onProgressUpdate type, onPostExecute type>
    private class SendData extends AsyncTask<Void, Void, Void> {
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
    }

    @Override
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
