package com.example.gamingpc.easyplants;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";

    // Set up the UI elements
    Button setThreshold;
    Button setSchedule;
    TextView humidityDisplay;

    // Initializes the UI elements for the main activity
    private void setup() {

        // Initialize the set Threshold button and allow it to open the activity
        setThreshold = findViewById(R.id.button_thresh);
        setThreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Intent to setThresholdActivity");
                startActivity(new Intent(MainActivity.this, setThresholdActivity.class));
            }
        });

        // Initialize the setSchedule button and allow it to open the activity
        setSchedule = findViewById(R.id.button_schedule);
        setSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Intent to setScheduleActivitry");
                startActivity(new Intent(MainActivity.this, setScheduleActivity.class));
            }
        });

        // Initialize the textView and display the appropriate value
        humidityDisplay = findViewById(R.id.text_humidityReading);
        // TODO connect to actual humidity reading from arduino

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
    }

}
