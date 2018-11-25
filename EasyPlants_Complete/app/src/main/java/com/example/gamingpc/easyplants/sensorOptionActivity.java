package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sensorOptionActivity extends AppCompatActivity {

    final static String TAG = "sensorOptionActivity";

    // Initialize the UI elements
    private Button delete;
    private Button save;
    Switch waterSwitch;
    private boolean switchState;    // Tracks whether or not the switch is active or not
    private EditText changeName;
    private String displayedName = "Plant Name";
    private String sensorID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;

    void setup() {
        // Initialize the auto water switch and set it to water is stored in the firebase
        /*waterSwitch = findViewById(R.id.switch_autoWater);
        DatabaseReference waterRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/" + sensorID + "/AutoWaterOn");
        waterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Called when the data is read or changed in firebase
                displayedName = dataSnapshot.getValue(String.class);
                if (dataSnapshot.getValue(Boolean.class)) {
                    // Set default switch value to on
                    waterSwitch.setChecked(true);
                } else {
                    waterSwitch.setChecked(false);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read auto-water value.", error.toException());
            }
        });


        // Initialize text view and get the plant name value to fill it in
        changeName = findViewById(R.id.text_changeName);
        DatabaseReference nameRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/" + sensorID + "/PlantName");
        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Called when the data is read or changed in firebase
                displayedName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read name value.", error.toException());
            }
        });
        changeName.setText(displayedName);
        */

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
                nameRef.setValue(changeName.getText().toString());
                // Set auto water value in firebase
                switchState = waterSwitch.isChecked();
                if (switchState) {
                    waterRef.setValue(1);
                } else {
                    waterRef.setValue(0);
                }
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
}
