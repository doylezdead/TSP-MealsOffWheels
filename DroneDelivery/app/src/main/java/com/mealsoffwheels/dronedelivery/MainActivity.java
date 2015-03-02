package com.mealsoffwheels.dronedelivery;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.net.*;
import java.io.*;


public class MainActivity extends ActionBarActivity {

    private final int PORT = 13337;
    private final String HOST = "doyle.pw";
    private Socket skt = null;
    private ObjectOutputStream oos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //StringBuffer inputBuffer = new StringBuffer();

        if (skt == null) {
            try {
                InetAddress address = InetAddress.getByName(HOST);

                skt = new Socket(address, PORT);

                oos = new ObjectOutputStream(skt.getOutputStream());
                //oos.writeObject();
                //oos.flush();

            } catch (UnknownHostException e) {

            } catch (IOException e) {

            }
        }
    }

    private void sendData() {
        try {
            //oos.writeObject();
            oos.flush();
        }
        catch (IOException e) {

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
