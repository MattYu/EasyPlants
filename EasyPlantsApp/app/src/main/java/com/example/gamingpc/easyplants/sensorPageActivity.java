package com.example.gamingpc.easyplants;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class sensorPageActivity extends AppCompatActivity {

    private static final String TAG = "sensorListActivity";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_page);

        // Enable the back button to the sensorListActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
