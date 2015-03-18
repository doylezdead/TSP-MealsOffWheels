package com.mealsoffwheels.dronedelivery.common;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connect extends AsyncTask<Void, Void, Void> {
    private final int PORT;
    private final String HOST;

    private Payload sendPayload;
    private Payload receivePayload;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket skt;
    private boolean run;
    private TextView changeText;

    public Connect(int port, String host, Payload data, TextView changeText) {
        PORT = port; //25565
        HOST = host; // doyle.pw
        sendPayload = data;
        receivePayload = null;
        oos = null;
        ois = null;
        run = false;
        this.changeText = changeText;
    }

    public String getHost() {
        return HOST;
    }

    public int getPort() {
        return PORT;
    }

    public Socket getSocket() {
        return skt;
    }

    public void setSocket(Socket skt) {
        this.skt = skt;
    }

    public void setSendPayload(Payload data) {
        sendPayload = data;
    }

    public boolean isDataReceived() {
        return receivePayload != null;
    }

    public Payload getReceivePayload() {
        return receivePayload;
    }

    public void close() {
        try {
            if (skt != null) {
                skt.close();
            }

            if (oos != null) {
                oos.flush();
                oos.close();
            }

            if (ois != null) {
                ois.close();
            }

            skt = null;
            oos = null;
            ois = null;
            receivePayload = null;
            sendPayload = null;
            run = false;
        }

        catch (IOException e) {

        }
    }

    public void stopWaitingForData() {
        run = false;
    }

    @Override
    protected Void doInBackground(Void... arg) {
        // No data given to send.
        if (sendPayload == null) {
            return null;
        }

        // Show no data has been received yet.
        receivePayload = null;

        try {
            // Socket not created already
            if (skt == null) {
                // Get the address of the host.
                InetAddress address = InetAddress.getByName(HOST);

                skt = new Socket(address, PORT);

                // Indicate a new ObjectOutputStream is needed.
                oos = null;
            }

            if (oos == null) {
                oos = new ObjectOutputStream(skt.getOutputStream());
            }

            // Send the payload.
            oos.writeObject(sendPayload);
            oos.flush(); // Make sure all data was sent.

            if (ois == null) {
                ois = new ObjectInputStream(skt.getInputStream());
            }

            run = true;

            while (run) {
                // Try for Payload from server.
                receivePayload = (Payload) ois.readObject();

                // Haven't gotten response yet.
                // TODO: Add in feature of quitting after a minute or so.
                if (receivePayload == null) {
                    SystemClock.sleep(500); // Sleep to reduce power needed from phone for busy wait.
                }

                // Got response.
                else {
                    System.out.println("return value is " + receivePayload.value);
                    break;
                }
            }

            run = false;
            sendPayload = null;

            // TEMPORARY
            if (receivePayload != null && changeText != null) {
                changeText.setText(receivePayload.value + " " + receivePayload.xcoord + " " + receivePayload.ycoord + " " + receivePayload.name);
                changeText.invalidate(); // Sets it up to refresh the textview
            }
        }

        catch (ClassNotFoundException | IOException ex) {
            ex.printStackTrace();
            System.out.println("fail");
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
