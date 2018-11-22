package com.example.gamingpc.easyplants;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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


        //Retrieve humidity value from firebase database
        //TODO change path of child to read from message_list
        DatabaseReference humidityValue = FirebaseDatabase.getInstance().getReference().child("HumidityTest").child("humidity_value");

        //keep track of humidity value in Firebase
        humidityValue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Sets up the shared preference helper function
                SharedPreferenceHelper sp = new SharedPreferenceHelper(getApplicationContext());

                Integer humidity = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, Integer.toString(humidity));

                // Update textview with the humidity
                displayHumidity.setText(Integer.toString(humidity) + "%");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error while accessing firebase HumidityTest database");
            }
        });



    }

    public void notificationCall(String message){

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this,"1")
                .setSmallIcon(R.drawable.plant_test)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("Notification from EasyPlant")
                .setContentText(message);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notificationBuilder.build());
        Log.d(TAG,"NotificationCall");

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notify EasyPlant";
            String description = "Notification received from EasyPlant";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_page);
        createNotificationChannel();

        //TODO Create Notification Call when humidity value is under the threshold
        //TODO Change value on Firebase to enable pump

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
