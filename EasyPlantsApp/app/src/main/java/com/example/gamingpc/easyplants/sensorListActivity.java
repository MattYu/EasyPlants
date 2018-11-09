package com.example.gamingpc.easyplants;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class sensorListActivity extends AppCompatActivity {

    private static final String TAG = "sensorListActivity";

    // UI elements
    private ListView sensorList;

    // Called to set up all the UI elements
    private void setup() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);

        // Enable the back button to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setup();
    }
}
