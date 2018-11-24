package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.gamingpc.easyplants.Models.SensorData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class graphActivity extends AppCompatActivity {

    final String TAG = "GraphActivity";
    List<SensorData> GraphData = new ArrayList<>();

    String sensorID;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Enable the back button to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getPassedData() {
        Intent received = getIntent();
        sensorID = received.getStringExtra("sensorID");

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected void onSetup(){

        getPassedData();

        String path = "UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + sensorID + "/SensorData";

        DatabaseReference dbRef = database.getReference(path);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                fillGraphData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    protected void fillGraphData(DataSnapshot sensorData){



    }

}
