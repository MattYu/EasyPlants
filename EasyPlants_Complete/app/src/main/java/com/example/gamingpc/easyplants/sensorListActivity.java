package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class sensorListActivity extends AppCompatActivity {

    private static final String TAG = "sensorListActivity";

    // UI elements
    private ListView sensorList;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;


    // Test arrays for UI
    String[] PLANTS = {"Aloe Vera", "Rose Bush", "Poppy"};
    String[] SENSOR_ID = {"1", "2", "3"};

    // Called to set up all the UI elements
    private void setup() {

        sensorList = findViewById(R.id.list_sensors);
        CustomAdapter customAdapter = new CustomAdapter();
        sensorList.setAdapter(customAdapter);

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                new Task().execute("");
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
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);

        // Enable the back button to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setup();
    }

    protected void onStart() {
        super.onStart();

        setup();
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
            return PLANTS.length;
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

            names.setText(PLANTS[i]);
            id.setText("Sensor: " + SENSOR_ID[i]);
            image.setImageResource(R.drawable.plant_test);

            return view;
        }
    }
}
