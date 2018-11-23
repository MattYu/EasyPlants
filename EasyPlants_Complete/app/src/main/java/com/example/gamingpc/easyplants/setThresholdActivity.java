package com.example.gamingpc.easyplants;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        String message = temp.getThresholdMin() + " - " + temp.getThresholdMax() + "%";
        currentMinMax.setText(message);
        seekBarMin.setProgress(temp.getThresholdMin());
        seekBarMax.setProgress(temp.getThresholdMax());


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferenceHelper helperSave = new SharedPreferenceHelper(setThresholdActivity.this);
                helperSave.saveFromUserThreshold(new UserThreshold(seekBarMax.getProgress(), seekBarMin.getProgress()));
                Toast.makeText(getApplicationContext(), "New Threshold Set", Toast.LENGTH_SHORT).show();

                //startActivity(new Intent(setThresholdActivity.this, LoginActivity.class));
            }
        });

        recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Intent to cameraActivity");
                startActivity(new Intent(setThresholdActivity.this, VisionActivity.class));
            }
        });




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_threshold);

        // Allow the action bar back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        sensorID = intent.getStringExtra("sensorID");

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

        Intent intent = getIntent();
        if (intent.hasExtra("Min")) {

            seekBarMin.setProgress(Integer.parseInt(intent.getStringExtra("Min")));

        }
        if (intent.hasExtra("Max")) {

            seekBarMax.setProgress(Integer.parseInt(intent.getStringExtra("Max")));

        }

        sensorID = intent.getStringExtra("sensorID");

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
