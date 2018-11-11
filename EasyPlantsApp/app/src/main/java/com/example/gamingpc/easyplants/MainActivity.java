package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";

    // Set up the UI elements
    Button toSensorList;
    Button toCamera;
    TextView humidityDisplay;

    // Initializes the UI elements for the main activity
    private void setup() {

        // Initialize the setSchedule button and allow it to open the activity
        toSensorList = findViewById(R.id.button_sensor);
        toSensorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Intent to sensorListActivity");
                startActivity(new Intent(MainActivity.this, sensorListActivity.class));
            }
        });

        /*
        // Initialize the button that go to the camera activity
        toCamera = findViewById(R.id.button_camera);
        toCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Intent to cameraActivity");
                startActivity(new Intent(MainActivity.this, VisionActivity.class));
            }
        });
        */



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
