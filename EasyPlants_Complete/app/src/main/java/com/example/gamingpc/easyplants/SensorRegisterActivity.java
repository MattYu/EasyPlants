package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myhexaville.login.LoginActivity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class SensorRegisterActivity extends AppCompatActivity {

    Button save;
    EditText plantName;
    EditText sensorID;
    private FirebaseAuth mAuth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_register);
        mAuth = FirebaseAuth.getInstance();



        save =findViewById(R.id.button_save);
        plantName = findViewById(R.id.text_register_plantname);
        sensorID = findViewById(R.id.text_register_sensorid);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = plantName.getText().toString();
                String newSensor = sensorID.getText().toString();
                myRef = database.getReference("sensorFolder/");

                if (name.length() == 0 || newSensor.length() == 0){
                    Toast.makeText(SensorRegisterActivity.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                }
                else {
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            if (dataSnapshot.child(newSensor).exists()) {
                                String claimed_or_notClaimed = dataSnapshot.child(newSensor).getValue(String.class);
                                DatabaseReference currentRef = database.getReference("sensorFolder/" + newSensor);
                                currentRef.setValue("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + newSensor + "/SensorData");
                                if (claimed_or_notClaimed.contains(mAuth.getCurrentUser().getUid()) && claimed_or_notClaimed.contains(newSensor)) {
                                    Toast.makeText(SensorRegisterActivity.this, "You already have this sensor... please reset your sensor to pair this sensor", Toast.LENGTH_LONG).show();
                                    DatabaseReference myRef6 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + newSensor + "/Deleted");
                                    myRef6.setValue(0);
                                    DatabaseReference myRef7 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + newSensor + "/PlantName");
                                    myRef7.setValue(name);
                                    finish();
                                }
                                else {


                                    DatabaseReference tempRef = database.getReference("sensorFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/");
                                    tempRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Sets up the shared preference helper function

                                            if (!dataSnapshot.child(newSensor).exists()) {
                                                DatabaseReference myRef2 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + newSensor + "/MinThreshold");
                                                myRef2.setValue(0);

                                                DatabaseReference myRef3 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + newSensor + "/MaxThreshold");
                                                myRef3.setValue(100);

                                                DatabaseReference myRef0 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + newSensor + "/AutoWaterOn");
                                                myRef0.setValue(1);

                                                DatabaseReference myRef5 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + newSensor + "/EnableReading");
                                                myRef5.setValue(1);

                                                DatabaseReference myRef6 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + newSensor + "/Deleted");
                                                myRef6.setValue(0);

                                                DatabaseReference myRef7 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + newSensor + "/PlantName");
                                                myRef7.setValue(name);

                                                DatabaseReference myRef4 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + newSensor + "/SensorData/-LQv5Qq2f0pUQ2K9TC26/");
                                                Map<String, Object> mp = new HashMap<>();
                                                mp.put("humidity_value", 0);
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
                                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                                mp.put("Time", (sdf.format(timestamp)));
                                                myRef4.setValue(mp);

                                                DatabaseReference myRef9 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + newSensor + "/AlertTime");
                                                myRef9.setValue(sdf.format(timestamp));

                                                if (!"null".equals(claimed_or_notClaimed)) {
                                                    Toast.makeText(SensorRegisterActivity.this, "Saved! The key appears to be already claimed... please reset your sensor to pair this sensor", Toast.LENGTH_LONG).show();
                                                    DatabaseReference myRef8 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + newSensor + "/EnableReading");
                                                    myRef8.setValue(0);
                                                }
                                                else {
                                                    Toast.makeText(SensorRegisterActivity.this, "Successfully registed! Please connect your sensor to the internet to finish pairing", Toast.LENGTH_LONG).show();
                                                }
                                                finish();
                                            } else {
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(SensorRegisterActivity.this, "The sensor key does not appear to exist", Toast.LENGTH_LONG).show();
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                        }
                    });
                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
