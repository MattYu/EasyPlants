/*

This is the sensor page. User are lead to this page by clicking on a sensor on the senorListPage
It contains real-time humidity data and 2 options: Menu option, leading to sensorOptionActivity, and set threshold option, leading to setThresholdActivity
This page has a firebase and auth controller to listen to activity changes specific to the user

 */


package com.example.gamingpc.easyplants;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamingpc.easyplants.Database.SharedPreferenceHelper;
import com.example.gamingpc.easyplants.Models.UserThreshold;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.myhexaville.login.LoginActivity;
import com.ramotion.circlemenu.CircleMenuView;

import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;

public class sensorPageActivity extends AppCompatActivity {

    private static final String TAG = "SensorPageActivity";

    // Initialize the UI elements
    private TextView displayPlantName;
    private TextView displaySensorID;
    private TextView displayHumidity;
    private MenuInflater menu;
    private TextView menuIcon;
    private TextView icon1;
    private TextView icon2;
    private TextView icon3;

    FirebaseAuth mAuth;

    // Holds the values passed from sensorListActivity
    private String sensorID;
    private String plantName;

    //Stores the current humidity and threshold
    private Integer humidity;
    private Integer minThresh = 0;
    private Integer maxThresh = 100;


    // Grabs the data that was passed from the previous intent
    private void getPassedData() {
        Intent received = getIntent();
        sensorID = received.getStringExtra("sensorID");
        plantName = received.getStringExtra("plantName");

    }


    // Sets up all the ui and data elements for the activity
    private void setup() {

        // Connect the text views
        menuIcon = findViewById(R.id.sensor_text_menu);
        icon1 = findViewById(R.id.sensor_text_icon1);
        icon2 = findViewById(R.id.sensor_text_icon2);
        icon3 = findViewById(R.id.sensor_text_icon3);

        displayPlantName = findViewById(R.id.text_sensorPageName);
        displaySensorID = findViewById(R.id.text_sensorPageID);
        displayHumidity = findViewById(R.id.text_humidityReading);
        // Fill the text views with the sensor id and plant name
        displayPlantName.setText(plantName);
        displaySensorID.setText(sensorID);

        icon1.setText("");
        icon2.setText("");
        icon3.setText("");

        final CircleMenuView menu = (CircleMenuView) findViewById(R.id.circle_menu);
        menu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                menuIcon.setText("");
                Log.d(TAG, "onMenuOpenAnimationStart");
            }

            @Override
            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
                icon1.setText("Set Humidity levels");
                icon2.setText("Graph");
                icon3.setText("Settings");
                Log.d(TAG, "onMenuOpenAnimationEnd");
            }

            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                icon1.setText("");
                icon2.setText("");
                icon3.setText("");

                Log.d(TAG, "onMenuCloseAnimationStart");
            }

            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
                menuIcon.setText("menu");
                Log.d(TAG, "onMenuCloseAnimationEnd");
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
                icon1.setText("");
                icon2.setText("");
                icon3.setText("");
                Log.d(TAG, "onButtonClickAnimationStart| index: " + index);
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                if (index == 0){
                    Intent threshIntent = new Intent(sensorPageActivity.this, setThresholdActivity.class);
                    threshIntent.putExtra("sensorID", sensorID);
                    startActivity(threshIntent);
                }
                else if (index == 1){
                    Intent graphIntent = new Intent(sensorPageActivity.this, graphActivity.class);
                    graphIntent.putExtra("sensorID", sensorID);
                    startActivity(graphIntent);
                }
                else if (index ==2){
                    Intent optionsIntent = new Intent(sensorPageActivity.this, sensorOptionActivity.class);
                    optionsIntent.putExtra("sensorID", sensorID);
                    optionsIntent.putExtra("plantName", plantName);
                    startActivity(optionsIntent);
                }
                Log.d(TAG, "onButtonClickAnimationEnd| index: " + index);
            }
        });


        //Retrieve humidity value from firebase database

        DatabaseReference myCurrentRef = FirebaseDatabase.getInstance().getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + sensorID);
        Query query = myCurrentRef.child("SensorData").orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.child("humidity_value").exists()) {
                        humidity = child.child("humidity_value").getValue(Integer.class);
                        Log.d(TAG, Integer.toString(humidity));

                        // Update textview with the humidity
                        displayHumidity.setText(Integer.toString(humidity) + "%");

                    }
                    else{
                        Toast.makeText(sensorPageActivity.this, "Oh no! We couldn't fetch the current humidity", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error while accessing firebase HumidityTest database");
            }
        });

        //Retrieve Max Threshold value from
        Query queryMaxThresh = myCurrentRef.child("MaxThreshold");
        queryMaxThresh.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxThresh = dataSnapshot.getValue(Integer.class);
                Log.d(TAG,"Max Threshold is " + Integer.toString(maxThresh));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error while accessing firebase Maximum Threshold value");
            }
        });

        //Retrieve Min Threshold value from
        Query queryMinThresh = myCurrentRef.child("MinThreshold");
        queryMinThresh.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                minThresh = dataSnapshot.getValue(Integer.class);
                Log.d(TAG,"Min Threshold is " + Integer.toString(minThresh));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error while accessing firebase Minimum Threshold value");
            }
        });

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_page);

        mAuth = FirebaseAuth.getInstance();


        // Enable the back button to the sensorListActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getPassedData();
        setup();

    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
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
