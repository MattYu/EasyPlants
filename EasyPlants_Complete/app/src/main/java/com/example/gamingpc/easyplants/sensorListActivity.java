package com.example.gamingpc.easyplants;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;

public class sensorListActivity extends AppCompatActivity {

    private static final String TAG = "sensorListActivity";

    // UI elements
    private ListView sensorList;
    FirebaseAuth mAuth;

    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    String userFolderDir;

    Button addsensor;

    // Test arrays for UI
    List<String> plantName = new ArrayList<>();
    List<String> currentHumidityValue = new ArrayList<>();
    List<String> minHumidityThreshold = new ArrayList<>();
    List<String> deleted = new ArrayList<>();
    List<String> enableReading = new ArrayList<>();
    List<String> sensorId = new ArrayList<>();
    List<String> enabledSensorId = new ArrayList<>();

    Map<String, Boolean> uniqueMp = new HashMap<>();


    // Called to set up all the UI elements
    private void setup() {

        sensorList = findViewById(R.id.list_sensors);
        addsensor = findViewById(R.id.button_addSensor);
        addsensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Intent to sensorListActivity");
                startActivity(new Intent(sensorListActivity.this, SensorRegisterActivity.class));
            }
        });


        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                plantName = new ArrayList<>();
                currentHumidityValue = new ArrayList<>();
                minHumidityThreshold = new ArrayList<>();
                deleted = new ArrayList<>();
                enableReading = new ArrayList<>();
                sensorId = new ArrayList<>();
                enabledSensorId = new ArrayList<>();
                uniqueMp = new HashMap<>();


                refreshData();
                Handler handler = new Handler();
                Toast.makeText(getApplicationContext(), "Loading your data...", Toast.LENGTH_SHORT).show();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        //mWaveSwipeRefreshLayout.setEnabled(false);
                        CustomAdapter customAdapter = new CustomAdapter();
                        sensorList.setAdapter(customAdapter);
                    }
                }, 3500);
                new Task().execute("");
                //mWaveSwipeRefreshLayout.setRefreshing(true);
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
            if (entry.child("Deleted").exists()) {
                deleted = entry.child("Deleted").getValue(Integer.class);
            }
            if (deleted == 0 && !uniqueMp.containsKey(entry.getKey())){
                sensorId.add(entry.getKey());
                if ( entry.child("PlantName").exists()) {
                    plantName.add((String) entry.child("PlantName").getValue());
                }
                else{
                    plantName.add("Plant Name Undefined");
                }
                if (entry.child("MinThreshold").exists()) {
                    minHumidityThreshold.add(Integer.toString(entry.child("MinThreshold").getValue(Integer.class)));
                }
                else{
                    minHumidityThreshold.add("0");
                }

                if (entry.child("EnableReading").exists()) {
                    int sensorEnabled = entry.child("EnableReading").getValue(Integer.class);
                    if (sensorEnabled == 1) {
                        enableReading.add("Enabled");
                    } else {
                        enableReading.add("Disabled");
                    }
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
                        Integer humidity = 0;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.child("humidity_value").exists()) {
                                humidity = child.child("humidity_value").getValue(Integer.class);
                                currentHumidityValue.add(Integer.toString(child.child("humidity_value").getValue(Integer.class)));
                            }
                            else{
                                currentHumidityValue.add("Humidity Unavailable");
                            }
                        }

                        //Compare humidity value with threshold to send a notification to the user only after two minutes
                        if(entry.child("PlantName").exists() && entry.child("Deleted").getValue(Integer.class) == 0 && entry.child("EnableReading").getValue(Integer.class) == 1){
                            if(humidity < entry.child("MinThreshold").getValue(Integer.class)){

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                String nowtime = timestamp.toString();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(timestamp.getTime());  //calendar has now value of time
                                String oldTime = entry.child("AlertTime").getValue(String.class);

                                try {

                                    Date parseNewDate = sdf.parse(nowtime);
                                    Date parsedDate = sdf.parse(oldTime);
                                    Date newDate = new Date(parsedDate.getTime() + 12 * 3600* 1000);
                                    Timestamp oldTimestamp = new Timestamp(parsedDate.getTime());
                                    if(parseNewDate.getTime() > newDate.getTime() ){
                                        calendar.add(Calendar.MINUTE, 2);
                                        timestamp = new Timestamp(calendar.getTimeInMillis());
                                        //set the new time for the Alert
                                        DatabaseReference myRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + entry.getKey() + "/AlertTime");
                                        myRef.setValue(sdf.format(timestamp));
                                        //send the notification
                                        notificationCall("Plant " + entry.child("PlantName").getValue() + " needs watering");
                                    }
                                } catch(Exception e) { //this generic but you can control another types of exception
                                    // look the origin of excption
                                }




                            }
                            if(humidity > entry.child("MaxThreshold").getValue(Integer.class)){
                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(timestamp.getTime());  //calendar has now value of time
                                String oldTime = entry.child("AlertTime").getValue(String.class);

                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                                    Date parsedDate = sdf.parse(oldTime);
                                    Timestamp oldTimestamp = new Timestamp(parsedDate.getTime());
                                    if(timestamp.after(oldTimestamp)){
                                        calendar.add(Calendar.MINUTE, 2);
                                        timestamp = new Timestamp(calendar.getTimeInMillis());
                                        //set the new time for the Alert
                                        DatabaseReference myRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + entry.getKey() + "/AlertTime");
                                        myRef.setValue(sdf.format(timestamp));
                                        //send the notification
                                        notificationCall("Plant " + entry.child("PlantName").getValue() + " has too much water");
                                    }
                                } catch(Exception e) { //this generic but you can control nother types of exception
                                    // look the origin of excption
                                }
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

        return;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sensor_list);

        createNotificationChannel();
        // Enable the back button to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setup();

    }

    private void notificationCall(String message){

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this,"1")
                .setSmallIcon(R.drawable.logo_circle)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("Notification from EasyPlant")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVisibility(VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notificationBuilder.build());
        Log.d(TAG,"NotificationCall");

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notify EasyPlant";
            String description = "Notification received from EasyPlant";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    protected void refreshData(){
        Log.w(TAG, "Refresh Data");
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
    }

    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        setup();

    }

    private class Task extends AsyncTask<String, Void, String> {
      //  ...
      protected String doInBackground(String... params) {
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
            return sensorId.size();
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
            TextView info = view.findViewById(R.id.text_sensorInfo);
            ImageView image = view.findViewById(R.id.view_plantImage);

            if (plantName.size() != 0 & sensorId.size()!=0 && currentHumidityValue.size() !=0 && minHumidityThreshold.size() !=0 && minHumidityThreshold.size()!=0 && enableReading.size()!=0) {
            names.setText(plantName.toArray(new String[0])[i]);
            id.setText(sensorId.toArray(new String[0])[i]);

                info.setText("\nCurrent humidity: " + currentHumidityValue.toArray(new String[0])[i] + "\nCurrent minimum humidity: " + minHumidityThreshold.toArray(new String[0])[i] + "\nStatus: " + enableReading.toArray(new String[0])[i]);
                image.setImageResource(R.drawable.mainplanticon);
            }
            else{
                Toast.makeText(sensorListActivity.this, "We couldn't fetch the data in time. Please verify your network connection", Toast.LENGTH_LONG).show();
            }

            notifyDataSetChanged();
            return view;
        }
    }
}
