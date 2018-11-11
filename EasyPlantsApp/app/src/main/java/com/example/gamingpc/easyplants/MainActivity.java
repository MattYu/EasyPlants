package com.example.gamingpc.easyplants;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";

    // Set up the UI elements
    Button toSensorList;
    Button toCamera;
    TextView humidityDisplay;

    // Initializes the UI elements for the main activity
    private void setup() {
        createNotificationChannel();
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

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        //notificationCall();

        //Retrieve humidity value from firebase database
        DatabaseReference humidityValue = FirebaseDatabase.getInstance().getReference().child("message_list").child("humidity_value");

        humidityValue.addValueEventListener(new ValueEventListener() {
           //addValueEventListener
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer humidity = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, humidity.toString());

                //checks if value is inside the threshold (40)
                if(humidity < 40){
                    Log.d(TAG,"send notification");
                    notificationCall();
                }
                //else if (sensorData.getHumidityValue() > sensorData.getSensorThresholdMax()){
                //ToDo Send notification to stop watering
                //}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error while accessing firebase HumidityTest database");
            }
        });

    }

    public void notificationCall(){

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this,"1")
                .setSmallIcon(R.drawable.plant_test)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("Notification from EasyPlant")
                .setContentText("Water your plant!");
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


}

