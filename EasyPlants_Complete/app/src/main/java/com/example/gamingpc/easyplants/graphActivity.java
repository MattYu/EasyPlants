package com.example.gamingpc.easyplants;

import android.app.Fragment;
import android.content.Intent;
import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;

import com.example.gamingpc.easyplants.Models.SensorData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.view.LineChartView;

public class graphActivity extends AppCompatActivity {

    final String TAG = "GraphActivity";
    List<SensorData> totalSensorData = new ArrayList<>();
    List<SensorData> graphData = new ArrayList<>();

    List<List<SensorData>> GraphDataYear = new ArrayList<List<SensorData>>();

    String sensorID;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    LineChartView graph = (LineChartView) findViewById(R.id.graph);
    Spinner spinner = (Spinner) findViewById(R.id.yearSelector);
    Button refreshGraphButton = (Button) findViewById(R.id.refreshGraphButton);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

        // Enable the back button to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class PlaceholderFragment extends Fragment {

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
                fillTotalData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    protected void fillTotalData(DataSnapshot sensorData){

    for(DataSnapshot entry : sensorData.getChildren()){
        
    }

    }

}
