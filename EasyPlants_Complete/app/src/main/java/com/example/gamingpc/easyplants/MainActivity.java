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

import com.example.gamingpc.easyplants.Database.FirebaseHelper;
import com.example.gamingpc.easyplants.Database.SharedPreferenceHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myhexaville.login.LoginActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.gelitenight.waveview.library.WaveView;



public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private static final String TAG = "Main Activity";



    //Wave
    private WaveHelper mWaveHelper;

    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 10;

    // Set up the UI elements
    Button toSensorList;
    Button logOut;
    TextView humidityDisplay;

    // Used to access firebase functions
    FirebaseHelper fb;


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

        logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "deleting user token");
                mAuth.signOut();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        /*
        // Initialize the button that go to the camera activity
        toCamera = findViewById(R.id.button_camera);
        toCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Intent to cameraActivity");
                startActivity(new Intent(LoginActivity.this, VisionActivity.class));
            }
        });
        */

        // Initialize the textView
        humidityDisplay = findViewById(R.id.text_humidityReading);


    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        setup();


        final WaveView waveView = (WaveView) findViewById(R.id.wave);
        waveView.setBorder(mBorderWidth, mBorderColor);
        waveView.setShapeType(WaveView.ShapeType.CIRCLE);
        waveView.setWaveColor(
                Color.parseColor("#40b7d28d"),
                Color.parseColor("#80b7d28d"));
        mBorderColor = Color.parseColor("#B0b7d28d");
        waveView.setBorder(mBorderWidth, mBorderColor);
        mBorderWidth = 0;
        waveView.setBorder(mBorderWidth, mBorderColor);


        mWaveHelper = new WaveHelper(waveView);


        /*
        //Retrieve humidity value from firebase database
        //TODO change path of child to read from message_list
        DatabaseReference humidityValue = FirebaseDatabase.getInstance().getReference().child("HumidityTest").child("humidity_value");
        //Query query = humidityValue.orderBy("name", Direction.DESCENDING).limit(3);


        //keep track of humidity value in Firebase
        humidityValue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Sets up the shared preference helper function
                SharedPreferenceHelper sp = new SharedPreferenceHelper(getApplicationContext());

                Integer humidity = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, Integer.toString(humidity));

                // Update textview with the humidity
                humidityDisplay.setText(Integer.toString(humidity) + "%");

                //notify user if humidity is under or over a certain threshold
                //TODO retrieve threshold from firebase
                if(humidity < sp.getLowerThresh()){
                    notificationCall("Water your plant!");
                }
                else if (humidity > sp.getUpperThresh()){
                    notificationCall("Your plant has too much water.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error while accessing firebase HumidityTest database");
            }
        });*/

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
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.start();
    }



}

