package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gamingpc.easyplants.Database.SharedPreferenceHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class sensorPageActivity extends AppCompatActivity {

    private static final String TAG = "sensorListActivity";

    // Initialize the UI elements
    private TextView displayPlantName;
    private TextView displaySensorID;
    private Button toThreshold;
    private Button toGraph;
    private Button toOptions;
    private TextView displayHumidity;

    // Holds the values passed from sensorListActivity
    private String sensorID;
    private String plantName;
    private Integer humidityValue;



    // Grabs the data that was passed from the previous intent
    private void getPassedData() {
        Intent received = getIntent();
        sensorID = received.getStringExtra("sensorID");
        plantName = received.getStringExtra("plantName");

    }

    //Grabs humidity


    // Sets up all the ui and data elements for the activity
    private void setup() {

        // Connect the text views
        displayPlantName = findViewById(R.id.text_sensorPageName);
        displaySensorID = findViewById(R.id.text_sensorPageID);
        displayHumidity = findViewById(R.id.text_humidityReading);
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



        //TODO change path of child to read from User Folder
        //Retrieve the last humidity value from updated from Firebase database message_list
        DatabaseReference humidityValueRef = FirebaseDatabase.getInstance().getReference();
        Query query = humidityValueRef.child("message_list").orderByKey().limitToLast(1);

        //keep track of humidity value in Firebase
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Sets up the shared preference helper function
                SharedPreferenceHelper sp = new SharedPreferenceHelper(getApplicationContext());

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.d(TAG, child.getKey());
                    Log.d(TAG, child.child("humidity_value").getValue().toString());
                    humidityValue = child.child("humidity_value").getValue(Integer.class);

                }

                // Update textview with the humidity
                displayHumidity.setText(Integer.toString(humidityValue) + "%");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error while accessing firebase HumidityTest database");
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
