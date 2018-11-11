package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class sensorPageActivity extends AppCompatActivity {

    private static final String TAG = "sensorListActivity";

    // Initialize the UI elements
    private TextView displayPlantName;
    private TextView displaySensorID;
    private Button toThreshold;
    private Button toGraph;
    private Button toOptions;

    // Holds the values passed from sensorListActivity
    private String sensorID;
    private String plantName;

    // Grabs the data that was passed from the previous intent
    private void getPassedData() {
        Intent received = getIntent();
        sensorID = received.getStringExtra("sensorID");
        plantName = received.getStringExtra("plantName");
    }

    // Sets up all the ui and data elements for the activity
    private void setup() {

        // Connect the text views
        displayPlantName = findViewById(R.id.text_sensorPageName);
        displaySensorID = findViewById(R.id.text_sensorPageID);
        // Fill the text views with the sensor id and plant name
        displayPlantName.setText(plantName);
        displaySensorID.setText(sensorID);

        // Button to go to setTresholdActivity
        toThreshold = findViewById(R.id.button_toThresh);
        toThreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent threshIntent = new Intent(sensorPageActivity.this, setThresholdActivity.class);
                threshIntent.putExtra("id", sensorID);
                startActivity(threshIntent);
            }
        });

        // Button to go to graphActivity
        toGraph = findViewById(R.id.button_toGraph);
        toGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent graphIntent = new Intent(sensorPageActivity.this, graphActivity.class);
                graphIntent.putExtra("id", sensorID);
                startActivity(graphIntent);
            }
        });

        // Button to go to sensorOptionsActivity
        toOptions = findViewById(R.id.button_toOptions);
        toOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent optionsIntent = new Intent(sensorPageActivity.this, sensorOptionActivity.class);
                optionsIntent.putExtra("id", sensorID);
                startActivity(optionsIntent);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_page);

        // Enable the back button to the sensorListActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getPassedData();
        setup();

    }

    protected void onStart() {
        super.onStart();
        setup();
    }

    protected void onResume() {
        super.onResume();
        setup();
    }

    protected void onRestart() {
        super.onRestart();
        setup();
    }
}
