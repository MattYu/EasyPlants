package com.example.gamingpc.easyplants;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myhexaville.login.LoginActivity;

import android.graphics.Color;
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

        //Initialize the LogOut button which signs the user out
        logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Deleting user token");
                mAuth.signOut();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });



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

