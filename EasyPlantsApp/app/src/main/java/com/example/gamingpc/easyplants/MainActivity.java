package com.example.gamingpc.easyplants;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gamingpc.easyplants.Database.FirebaseHelper;
import com.example.gamingpc.easyplants.Database.SharedPreferenceHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";

    // Set up the UI elements
    Button toSensorList;
    TextView humidityDisplay;

    // Used to access firebase functions
    FirebaseHelper fb;

    //stores the humidityValue from firebase
    Integer humidityValue;


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


        // Initialize the textView
        humidityDisplay = findViewById(R.id.text_humidityReading);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();

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
                humidityDisplay.setText(Integer.toString(humidityValue) + "%");

                //notify user if humidity is under or over a certain threshold

                if(humidityValue < sp.getLowerThresh()){
                    notificationCall("Water your plant!");
                }
                else if (humidityValue > sp.getUpperThresh()){
                    notificationCall("Your plant has too much water.");
                }
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


}

