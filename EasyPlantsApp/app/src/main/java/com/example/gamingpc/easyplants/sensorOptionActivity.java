package com.example.gamingpc.easyplants;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class sensorOptionActivity extends AppCompatActivity {

    // Initialize the UI elements


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_option);

        // Enable the back button to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
