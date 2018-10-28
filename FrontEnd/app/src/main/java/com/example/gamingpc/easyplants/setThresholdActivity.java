package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class setThresholdActivity extends AppCompatActivity {

    private static final String TAG = "setThresholdActivity";

    // Set up UI elements
    Button save;
    EditText lowerThresh;   // User input of lower humidity bound
    EditText upperThresh;   // User input of upper humidity bound
    TextView currentMinMax; // Displays the current set threshold

    // Initializes the UI elements
    private void setup() {
        // The text fields
        lowerThresh = findViewById(R.id.text_newMin);
        upperThresh = findViewById(R.id.text_newMax);
        currentMinMax = findViewById(R.id.text_currentMinMax);
        // Save button and its listener
        save = findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO save new threshold values
                startActivity(new Intent(setThresholdActivity.this, MainActivity.class));
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_threshold);

        // Allow the action bar back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setup();
    }
}
