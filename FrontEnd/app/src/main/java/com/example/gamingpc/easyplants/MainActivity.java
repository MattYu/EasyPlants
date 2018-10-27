package com.example.gamingpc.easyplants;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";

    // Set up the UI elements
    Button setThreshold;
    Button setSchedule;
    TextView humidityDisplay;

    // Initializes the UI elements for the main activity
    private void setup() {

        // Initialize the set Threshold button and allow it to open the dialog
        setThreshold = findViewById(R.id.button_thresh);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
    }
}
