package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myhexaville.login.LoginActivity;

public class sensorOptionActivity extends AppCompatActivity {

    final static String TAG = "sensorOptionActivity";

    // Initialize the UI elements
    private Button delete;
    private Button save;
    Switch waterSwitch;
    private boolean switchState;    // Tracks whether or not the switch is active or not
    private EditText changeName;
    private String displayedName;
    private String sensorID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;

    void setup() {
        Intent received = getIntent();
        sensorID = received.getStringExtra("sensorID");
        displayedName = received.getStringExtra("plantName");

        // Initialize the auto water switch and set it to water is stored in the firebase
        waterSwitch = findViewById(R.id.switch_autoWater);
        DatabaseReference initialWaterRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/" + sensorID + "/AutoWaterOn");
        initialWaterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer waterValue = dataSnapshot.getValue(Integer.class);
                if(waterValue == 0) {
                    waterSwitch.setChecked(false);
                } else {
                    waterSwitch.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to retrieve current auto water value");
            }
        });

        // Fills in the edit text with the current plant name
        changeName = findViewById(R.id.text_changeName);
        changeName.setText(displayedName);



        delete = findViewById(R.id.button_deleteData);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent received = getIntent();
                sensorID = received.getStringExtra("sensorID");

                // Sets the deleted value to one to make sure it no longer shows up in the list
                DatabaseReference currentRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/" + sensorID + "/Deleted");
                currentRef.setValue(1);
                finish();
            }
        });

        save = findViewById(R.id.button_saveOptions);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent received = getIntent();
                sensorID = received.getStringExtra("sensorID");
                //TODO
                // Saves the plant name and auto water value to the user in firebase
                DatabaseReference nameRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/" + sensorID + "/PlantName");
                DatabaseReference waterRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/" + sensorID + "/AutoWaterOn");

                // Set the new name in firebase
                if(changeName.getText().toString().length() < 1) {
                    Toast.makeText(sensorOptionActivity.this, "Name must be at least one character - Name not changed", Toast.LENGTH_SHORT).show();
                } else {
                    nameRef.setValue(changeName.getText().toString());
                }

                // Set auto water value in firebase
                switchState = waterSwitch.isChecked();
                if (switchState) {
                    waterRef.setValue(1);
                } else {
                    waterRef.setValue(0);
                }
                Toast.makeText(sensorOptionActivity.this, "New Data Saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_option);

        // Enable the back button to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
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


