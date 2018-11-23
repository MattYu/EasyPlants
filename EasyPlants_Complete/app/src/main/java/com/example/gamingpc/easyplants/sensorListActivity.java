package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myhexaville.login.LoginActivity;
import com.google.firebase.database.Query;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class sensorListActivity extends AppCompatActivity {

    private static final String TAG = "sensorListActivity";

    // UI elements
    private ListView sensorList;
    FirebaseAuth mAuth;

    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    String userFolderDir;


    // Test arrays for UI
    List<String> plantName = new ArrayList<>();
    List<String> currentHumidityValue = new ArrayList<>();
    List<String> minHumidityThreshold = new ArrayList<>();
    List<String> deleted = new ArrayList<>();
    List<String> enableReading = new ArrayList<>();
    List<String> sensorId = new ArrayList<>();
    List<String> enabledSensorId = new ArrayList<>();

    Map<String, Boolean> uniqueMp = new HashMap<>();


    String[] PLANTS = {"Aloe Vera", "Rose Bush", "Poppy"};
    String[] SENSOR_ID = {"1", "2", "3"};

    // Called to set up all the UI elements
    private void setup() {

        sensorList = findViewById(R.id.list_sensors);

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                new Task().execute("");
            }
        });

        DatabaseReference myRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                setSensorList(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        sensorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Retrieve the sensor id of the list item the user clicked on
                TextView sensorID = view.findViewById(R.id.text_sensorID);
                TextView plantName = view.findViewById(R.id.text_plantName);
                String clickedID = sensorID.getText().toString();
                String clickedName = plantName.getText().toString();

                // Intent to the sensor page
                Intent intent = new Intent(sensorListActivity.this, sensorPageActivity.class);
                // Pass the sensor ID and plant name to the sensor page
                intent.putExtra("sensorID", clickedID);
                intent.putExtra("plantName", clickedName);
                startActivity(intent);
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                CustomAdapter customAdapter = new CustomAdapter();
                sensorList.setAdapter(customAdapter);
            }
        }, 1000);

    }

    private void setSensorList(DataSnapshot plantData) {

        //reset HashMap and Arrays
        //iterate through each user, ignoring their UID
        for (DataSnapshot entry : plantData.getChildren()) {
            int deleted = 1;
            try {
                deleted = entry.child("Deleted").getValue(Integer.class);
            } catch (Exception e) {
                continue;
            }
            if (deleted == 0 && !uniqueMp.containsKey(entry.getKey())){
                plantName.add((String) entry.child("PlantName").getValue());
                minHumidityThreshold.add(Integer.toString(entry.child("MinThreshold").getValue(Integer.class)));
                int sensorEnabled = entry.child("EnableReading").getValue(Integer.class);
                if (sensorEnabled == 1){
                    enableReading.add("Enabled");
                }
                else{
                    enableReading.add("Disabled");
                }
                DatabaseReference myCurrentRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + entry.getKey());
                Query query = myCurrentRef.child("SensorData").orderByKey().limitToLast(1);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Sets up the shared preference helper function

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.child("humidity_value").getValue() == null){
                                currentHumidityValue.add("N/A");
                            }
                            else{
                                currentHumidityValue.add(Integer.toString(child.child("humidity_value").getValue(Integer.class)));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "Error while accessing firebase HumidityTest database");
                    }
                });
            }
            uniqueMp.put(entry.getKey(), true);

        }


            //Get phone field and append to list
            //String plantName = (String) entry.child("PlantName").getValue();

            //this.plant.add(plantName);
            //this.minThreshold.put(plantName, Long.toString((long) entry.child("Minimum Humidity %").getValue()));
            //this.maxThreshold.put(plantName, Long.toString((long) entry.child("Maximum Humidity %").getValue()));

        return;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sensor_list);

        // Enable the back button to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setup();
    }

    protected void onStart() {
        super.onStart();

        setup();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private class Task extends AsyncTask<String, Void, String> {
      //  ...
      protected String doInBackground(String... params) {
          long totalSize = 0;
          return "";
      }

        @Override protected void onPostExecute(String result) {
            // Call setRefreshing(false) when the list has been refreshed.
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute("");
        }
    }

    // Class used to populate the custom adapter - with the methods that java requires for some reason
    class CustomAdapter extends BaseAdapter {

        // Temporary until Firebase data is accessed
        @Override
        public int getCount() {
            return plantName.size()-1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.sensor_list_view_custom, null);

            TextView names = view.findViewById(R.id.text_plantName);
            TextView id = view.findViewById(R.id.text_sensorID);
            ImageView image = view.findViewById(R.id.view_plantImage);


            names.setText(plantName.toArray(new String[0])[i]);
            id.setText("Current humidity: " + currentHumidityValue.toArray(new String[0])[i] + "\nCurrent minimum humidity: " + minHumidityThreshold.toArray(new String[0])[i] + "\nStatus: " + enableReading.toArray(new String[0])[i]);
            image.setImageResource(R.drawable.plant_test);


            return view;
        }
    }
}
