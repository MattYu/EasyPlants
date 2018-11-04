package com.example.gamingpc.easyplants;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class setScheduleActivity extends AppCompatActivity {

    private static final String TAG = "setScheduleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);

        // Activates the action bar back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
