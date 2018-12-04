package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamingpc.easyplants.Database.SharedPreferenceHelper;
import com.example.gamingpc.easyplants.Models.UserThreshold;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.john.waveview.WaveView;
import com.myhexaville.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class setThresholdActivity extends AppCompatActivity {

    private static final String TAG = "setThresholdActivity";

    FirebaseAuth mAuth;
    // Set up UI elements
    Button save;
    Button recommendation;
    TextView currentMinMax; // Displays the current set threshold
    TextView currentMessage;

    String sensorID;

    SeekBar seekBarMax;
    SeekBar seekBarMin;
    WaveView waveView;

    Integer initialMinThres = 0;
    Integer initialMaxThres = 100;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    // Used to store the threshold valu

    // Initializes the UI elements
    private void setup() {

        // The text fields
        mAuth = FirebaseAuth.getInstance();
        currentMinMax = findViewById(R.id.text_currentMinMax);
        currentMessage = findViewById(R.id.text_currentThresh);

        seekBarMax = (SeekBar) findViewById(R.id.seek_bar);
        waveView = (WaveView) findViewById(R.id.wave_view);
        seekBarMin = (SeekBar) findViewById(R.id.seek_bar2);

        Intent intent = getIntent();
        sensorID = intent.getStringExtra("sensorID");



        DatabaseReference myRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/" + sensorID + "/MinThreshold");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()) {
                    initialMinThres= dataSnapshot.getValue(Integer.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        myRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/" + sensorID + "/MaxThreshold");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()) {
                    initialMaxThres = dataSnapshot.getValue(Integer.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        seekBarMax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBarMin.getProgress() > seekBarMax.getProgress()) {
                    Toast.makeText(setThresholdActivity.this, "Oh no! Water level can't be set lower than the min", Toast.LENGTH_SHORT).show();
                    seekBarMin.setProgress(seekBarMax.getProgress());
                }
                waveView.setProgress(progress);
                String message = Integer.toString(seekBarMin.getProgress()) + " - " + Integer.toString(seekBarMax.getProgress()) + "%";
                currentMinMax.setText(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarMin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBarMin.getProgress() > seekBarMax.getProgress()) {
                    Toast.makeText(setThresholdActivity.this, "Oh no! Water level can't be set higher than the max", Toast.LENGTH_SHORT).show();
                    seekBarMax.setProgress(seekBarMin.getProgress());
                }
                waveView.setProgress(seekBarMax.getProgress()-(int)10/(progress+1));
                String message = Integer.toString(seekBarMin.getProgress()) + " - " + Integer.toString(seekBarMax.getProgress()) + "%";
                currentMinMax.setText(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        currentMinMax.bringToFront();
        currentMessage.bringToFront();

        // Save button and its listener
        save = findViewById(R.id.button_save);
        recommendation = findViewById(R.id.buttonRecommendation);

        SharedPreferenceHelper helperLoad = new SharedPreferenceHelper(setThresholdActivity.this);
        UserThreshold temp = helperLoad.getUserthreshold();

        Handler handler = new Handler();
        Toast.makeText(getApplicationContext(), "Loading your data...", Toast.LENGTH_SHORT).show();

        handler.postDelayed(new Runnable() {
            public void run() {
                String message = Integer.toString(initialMaxThres) + " - " + Integer.toString(initialMaxThres) + "%";
                currentMinMax.setText(message);
                seekBarMin.setProgress(initialMinThres);
                seekBarMax.setProgress(initialMaxThres);
            }
        }, 1000);




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //SharedPreferenceHelper helperSave = new SharedPreferenceHelper(setThresholdActivity.this);
                //helperSave.saveFromUserThreshold(new UserThreshold(seekBarMax.getProgress(), seekBarMin.getProgress()));

                DatabaseReference currentRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/" + sensorID);
                DatabaseReference minRef = currentRef.child("MinThreshold");
                DatabaseReference maxRef = currentRef.child("MaxThreshold");

                minRef.setValue(seekBarMin.getProgress());
                maxRef.setValue(seekBarMax.getProgress());
                Toast.makeText(getApplicationContext(), "New Threshold Set", Toast.LENGTH_SHORT).show();

                //startActivity(new Intent(setThresholdActivity.this, LoginActivity.class));
            }
        });

        recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Intent to cameraActivity");
                Intent visionIntent = new Intent(setThresholdActivity.this, VisionActivity.class);
                visionIntent.putExtra("sensorID", sensorID);
                startActivity(visionIntent);
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

    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        DatabaseReference myRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/" + sensorID + "/MinThreshold");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                initialMinThres= dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        myRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/" + sensorID + "/MaxThreshold");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()) {
                    initialMaxThres = dataSnapshot.getValue(Integer.class);
                }
                else{
                    Toast.makeText(setThresholdActivity.this, "Oh no! We couldn't download the data on time", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


            seekBarMin.setProgress(initialMinThres);
            seekBarMax.setProgress(initialMaxThres);


    }

    /*
    // Sends input threshold data to firebase
    private void toFirebase(String lower, String upper) {

        DatabaseReference refMin = FirebaseDatabase.getInstance().getReference().child("ThresholdValues").child("min");
        DatabaseReference refMax = FirebaseDatabase.getInstance().getReference().child("ThresholdValues").child("max");
        refMin.setValue(lower);
        refMax.setValue(upper);
    }
    */
}
