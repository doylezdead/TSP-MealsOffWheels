package com.mealsoffwheels.dronedelivery.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mealsoffwheels.dronedelivery.R;
import com.mealsoffwheels.dronedelivery.common.Connect;
import com.mealsoffwheels.dronedelivery.common.Payload;


public class AboutActivity extends ActionBarActivity {

    private Connect cc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button sendData = (Button) findViewById(R.id.SendDataButton);
        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientSend();
            }
        });
    }

    private void clientSend() {
        if (cc != null) {
            cc.close();
        }

        cc = new Connect(25565, "doyle.pw", new Payload(2, 3.5, 6.5, "Eric Kosovec"));
        cc.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (cc != null) {
            cc.close();
        }
    }


}
