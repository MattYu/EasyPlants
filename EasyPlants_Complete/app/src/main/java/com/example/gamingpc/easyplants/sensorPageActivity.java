package com.example.gamingpc.easyplants;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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

public class sensorPageActivity extends AppCompatActivity {

    private static final String TAG = "sensorListActivity";

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
                Log.d("D", "onMenuOpenAnimationStart");
            }

            @Override
            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
                icon1.setText("Set Humidity levels");
                icon2.setText("Graph");
                icon3.setText("Settings");
                Log.d("D", "onMenuOpenAnimationEnd");
            }

            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                icon1.setText("");
                icon2.setText("");
                icon3.setText("");

                Log.d("D", "onMenuCloseAnimationStart");
            }

            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
                menuIcon.setText("menu");
                Log.d("D", "onMenuCloseAnimationEnd");
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
                icon1.setText("");
                icon2.setText("");
                icon3.setText("");
                Log.d("D", "onButtonClickAnimationStart| index: " + index);
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
                    startActivity(optionsIntent);
                }
                Log.d("D", "onButtonClickAnimationEnd| index: " + index);
            }
        });





        //Retrieve humidity value from firebase database
        //TODO change path of child to read from message_list



        DatabaseReference myCurrentRef = FirebaseDatabase.getInstance().getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + sensorID);
        Query query = myCurrentRef.child("SensorData").orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Sets up the shared preference helper function

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Integer humidity = child.child("humidity_value").getValue(Integer.class);
                    Log.d(TAG, Integer.toString(humidity));

                    // Update textview with the humidity
                    displayHumidity.setText(Integer.toString(humidity) + "%");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_page);
        createNotificationChannel();

        mAuth = FirebaseAuth.getInstance();

        //TODO Create Notification Call when humidity value is under the threshold
        //TODO Change value on Firebase to enable pump

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
