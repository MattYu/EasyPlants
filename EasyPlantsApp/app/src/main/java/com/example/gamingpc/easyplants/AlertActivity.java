package com.example.gamingpc.easyplants;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.gamingpc.easyplants.Models.SensorData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AlertActivity extends AppCompatActivity {


    private DatabaseReference reference;
    private Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        reference = FirebaseDatabase.getInstance().getReference("message_list");
        //order by date
        reference.orderByChild("date").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                SensorData sensorData = dataSnapshot.getValue(SensorData.class);

                //checks if value is inside the threshold
                if(sensorData.getHumidityValue() < sensorData.getSensorThresholdMin()){
                    //ToDo Send notification to water the plant
                }
                else if (sensorData.getHumidityValue() > sensorData.getSensorThresholdMax()){
                    //ToDo Send notification to stop watering
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                SensorData sensorData = dataSnapshot.getValue(SensorData.class);

                //checks if value is inside the threshold
                if(sensorData.getHumidityValue() < sensorData.getSensorThresholdMin()){
                    //ToDo Send notification to water the plant
                }
                else if (sensorData.getHumidityValue() > sensorData.getSensorThresholdMax()){
                    //ToDo Send notification to stop watering
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                
            }

        });
    }





    // Read from the database
    //myRef.addValueEventListener(new ValueEventListener() {
    //@Override
    //   public void onDataChange(DataSnapshot dataSnapshot) {
    //      // This method is called once with the initial value and again
            // whenever data at this location is updated.
    //        String value = dataSnapshot.getValue(String.class);
    //        Log.d(TAG, "Value is: " + value);
    //    }

    //   @Override
    //    public void onCancelled(DatabaseError error) {
    //        // Failed to read value
    //        Log.w(TAG, "Failed to read value.", error.toException());
    //    }
    //});
}
