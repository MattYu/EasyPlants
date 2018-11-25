package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sensorOptionActivity extends AppCompatActivity {

    // Initialize the UI elements

    Button delete;
    private String sensorID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_option);

        delete = findViewById(R.id.button_deleteData);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent received = getIntent();
                sensorID = received.getStringExtra("sensorID");
                //TODO
                DatabaseReference currentRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/" + sensorID);
                finish();
            }
        });
        // Enable the back button to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
